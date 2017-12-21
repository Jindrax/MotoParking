/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import GUI.GUI;
import Negocio.Cupo;
import Utilidades.Auxi;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Todesser
 */
public class ServidorTCP implements Runnable {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private GUI gui = null;
    public List<clientThread> clientes = null;

    @Override
    public void run() {
        int portNumber = 9235;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("Iniciando servidor TCP en puerto: 9235");
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                (new clientThread(clientSocket, gui, this)).start();
            } catch (IOException e) {
                System.out.println("Error creando el socket");
            }
        }
    }

    public ServidorTCP(GUI gui) {
        this.gui = gui;
        clientes = new ArrayList<>();
    }
    
    public void notifyChanges(List<Cupo> cupos){
        List<CupoJSON> cuposJSON = new ArrayList<>();
        for(Cupo cupo: cupos){
            cuposJSON.add(cupo.toJSON());
        }
        String estado = new Gson().toJson(cuposJSON);
        for(clientThread cliente: clientes){
            cliente.notifyChanges(estado);
        }
    }

}

class clientThread extends Thread {
    
    private Socket clientSocket = null;
    private GUI gui = null;
    private PrintStream outToClient = null;
    

    public clientThread(Socket clientSocket, GUI gui, ServidorTCP servidor) {
        this.clientSocket = clientSocket;
        this.gui = gui;
        servidor.clientes.add(this);
    }

    public void run() {
        int id = clientSocket.getPort();
        System.out.println("Hilo creado con el cliente: " + id);
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new PrintStream(clientSocket.getOutputStream());
            List<CupoJSON> cuposJSON = new ArrayList<>();
            for(Cupo cupo: gui.getCuposActivos()){
                cuposJSON.add(cupo.toJSON());
            }
            String estado = new Gson().toJson(cuposJSON);
            outToClient.println(estado);
            while (true) {
                Gson parser = new Gson();
                String entrada = inFromClient.readLine();
                if (entrada == null || entrada.compareToIgnoreCase("salir")==0) {
                    break;
                }
                RespuestaServidor respuesta = null;
                SolicitudCliente solicitud = parser.fromJson(entrada, SolicitudCliente.class);
                boolean cerrado = false;
                List<CupoJSON> cupos = new ArrayList<>();
                switch((int)solicitud.getTipoSolicitud()){
                    case 0:
                        cupos.add(gui.ingresoDiario(solicitud.getPlaca(), (int)solicitud.getCascos()));
                        respuesta = new RespuestaServidor(1, cupos);
                        break;
                    case 1:
                        cupos.add(gui.prospectoPorRed(solicitud.getConsecutivo()));
                        respuesta = new RespuestaServidor(2, cupos);
                        break;
                    case 2:
                        cupos.add(gui.retiroPorRed(solicitud.getConsecutivo(), solicitud.isImpresion()));
                        respuesta = new RespuestaServidor(3, cupos);
                        break;
                }
                if(cerrado){
                    break;
                }
                outToClient.println(parser.toJson(respuesta));
            }
            inFromClient.close();
            outToClient.close();
            clientSocket.close();
            System.out.println("Cerrando hilo del cliente: " + id);
            this.join();
        } catch (IOException ex) {
            Logger.getLogger(clientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(clientThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            
        }
    }
    
    public void notifyChanges(String estado){
        outToClient.println(estado);
    }
}

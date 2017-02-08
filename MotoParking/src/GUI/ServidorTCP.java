/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Utilidades.Auxi;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
                (new clientThread(clientSocket, gui)).start();
            } catch (IOException e) {
                System.out.println("Error creando el socket");
            }
        }
    }

    public ServidorTCP(GUI gui) {
        this.gui = gui;
    }

}

class clientThread extends Thread {
    
    private Socket clientSocket = null;
    private GUI gui = null;

    public clientThread(Socket clientSocket, GUI gui) {
        this.clientSocket = clientSocket;
        this.gui = gui;
    }

    public void run() {
        int id = clientSocket.getPort();
        System.out.println("Hilo creado con el cliente: " + id);
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream outToClient = new PrintStream(clientSocket.getOutputStream());
            outToClient.println("Bienvenido");
            while (true) {
                String entrada = inFromClient.readLine();
                if (entrada == null || entrada.compareToIgnoreCase("salir")==0) {
                    break;
                }
                entrada = entrada.trim();
                String respuesta = null;
                String[] data = entrada.split(" ");
                data[0] = data[0].trim();
                boolean cerrado = false;
                switch(Auxi.selector(data[0])){
                    case 0:
                        respuesta = "Invalido";
                        break;
                    case 1:
                    case 2:
                    case 3:
                        respuesta = gui.ingresoDiario(data[0].toUpperCase(), Integer.valueOf(data[1].trim()));
                        break;
                    case 4:
                        respuesta = gui.prospectoPorRed(data[0]);
                        if(respuesta.compareToIgnoreCase("Ticket no existente o retirado")==0){
                            break;
                        }
                        outToClient.println(respuesta);
                        outToClient.println("(r)etirar, (i)mprimir, (c)ancelar");
                        String seleccion = inFromClient.readLine();
                        if (seleccion == null) {
                            cerrado = true;
                            break;
                        }
                        switch(seleccion.charAt(0)){
                            case 'r':
                                respuesta = gui.retiroPorRed(data[0], false);
                                break;
                            case 'i':
                                respuesta = gui.retiroPorRed(data[0], true);
                                break;
                            default:
                                respuesta = "Retiro cancelado";
                                break;                                
                        }
                        break;
                    case 5:
                        respuesta = "Cliente Mensual";
                }
                if(cerrado){
                    break;
                }
                outToClient.println(respuesta);
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
}

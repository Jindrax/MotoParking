/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import GUI.GUI;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Todesser
 */
public class ServidorUDP implements Runnable {

    private GUI gui;

    public ServidorUDP(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void run() {
        System.out.println("Iniciando servidor UDP en el puerto: 9235");
        while (true) {
            DatagramSocket serverSocket;
            try {
                int port = 9235;
                serverSocket = new DatagramSocket(port);
                byte[] receiveData = new byte[1024];
                byte[] sendData = new byte[1024];
                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    String sentence = new String(receivePacket.getData());
                    System.out.println("Recibido: " + sentence);
                    String[] data = sentence.split(" ");
                    String respuesta = "";
                    if (data.length < 2) {
                        respuesta = gui.actionDiarioProceso(data[0].trim(), 0, 1);
                    } else {
                        respuesta = gui.actionDiarioProceso(data[0].trim(), Integer.valueOf(data[1].trim()), 1);
                    }
                    System.out.println("Respuesta: " + respuesta);
                    InetAddress IPAddress = receivePacket.getAddress();
                    sendData = respuesta.getBytes();
                    DatagramPacket sendPacket
                            = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                    receiveData = new byte[1024];
                    sendData = new byte[1024];
                }
            } catch (SocketException ex) {
                Logger.getLogger(ServidorUDP.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServidorUDP.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}

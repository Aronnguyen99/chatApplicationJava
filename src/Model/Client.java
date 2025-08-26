/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Ducquy135
 */
public class Client implements Runnable{
    private Socket mySocket;
    private Server chatServer;
    private String id;
    private InputStream input;
    private OutputStream output;

    public Client(Socket mySocket, String id, Server chatServer) {
        this.mySocket = mySocket;
        this.id = id;
        this.chatServer = chatServer;
        try{
            this.input = mySocket.getInputStream();
            this.output = mySocket.getOutputStream();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = input.read(buffer)) != -1) {
                String message = new String(buffer, 0, bytesRead);
                chatServer.broadcastMessage(this.id+ " : "+message);
            }
        }catch(Exception e){
            
        }
    }
    public void sendMessage(String message) {
        try{
            output.write(message.getBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
   }
    
}

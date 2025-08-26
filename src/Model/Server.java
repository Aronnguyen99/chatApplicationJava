/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLServerSocket;

/**
 *
 * @author Ducquy135
 */
public class Server {
    private static final int PORT = 5000;
    private List<Client> clients = new ArrayList<>();
    public void startServer() {
        try{
            //websocket
            ServerSocket serverSocker = new ServerSocket(PORT);
            System.out.println("Server started. Listening on port " + PORT);
            
            //clients connect to server
            while(true) {
                Socket clientSocket = serverSocker.accept();
                System.out.println("New client connected: "+ clientSocket.getInetAddress().getHostAddress());
                
                Client clientHandler = new Client(clientSocket, System.currentTimeMillis() + "", this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void broadcastMessage(String message) {
        for(Client client:clients) {
            client.sendMessage(message);
        }
    }
}

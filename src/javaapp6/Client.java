/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapp6;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import javafx.scene.layout.VBox;

/**
 *
 * @author Nguyen Huy
 */
public class Client {
    public Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    String msgSendTo_name;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (Exception e) {
            closeEverything(socket,bufferedReader, bufferedWriter);
        }  
    }
    public void set_name(String username) throws IOException{
        this.username = username;
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
    }
    public void msgSendTo_name(String name){
        this.msgSendTo_name = name;
        System.out.println("client recieve msgSendTo_name: " + this.msgSendTo_name);
    }
    public void sendMessage(String msgToSend){
//        try {
//            bufferedWriter.write(username);
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//            
//            Scanner sc = new Scanner(System.in);
//            while(socket.isConnected()){
//                String msgToSend = sc.nextLine();
//                bufferedWriter.write(username +":" + msgToSend);
//                bufferedWriter.newLine();
//                 bufferedWriter.flush();
//            }
//        } catch (IOException e) {
//               closeEverything(socket,bufferedReader, bufferedWriter);
//        }
             try{
            bufferedWriter.write(msgToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error sending message to the client");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    
    public void listenForMessage(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroup;
                while(socket.isConnected()){
                    try {
                    msgFromGroup = bufferedReader.readLine();
                    ClientChatController.addLabel(msgFromGroup, vBox );
//                    System.out.println(msgFromGroup);
                    } catch (IOException e) {
                        System.out.println("Error receiving message from client");
                        closeEverything(socket,bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    
     public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    public static void main (String[] args) throws IOException{
//        Scanner sc = new Scanner(System.in);
//        System.out.println("User name:");
//        String username = sc.nextLine();
//        Socket socket = new Socket("localhost",99);
//        Client client = new Client(socket,username);
//        client.listenForMessage();
//        client.sendMessage();
    } 
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */


import Controller.SignupController;
import java.io.IOException;
import java.net.ServerSocket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Ducquy135
 */
public class FXMain extends Application {
    
    @Override
    public void start(Stage stage) throws IOException { 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/login.fxml"));
        Parent root = loader.load();
       
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        launch(args);
//        ServerSocket serverSocket = new ServerSocket(99);
//        Server server = new Server(serverSocket);
//        server.startServer();
    }
}

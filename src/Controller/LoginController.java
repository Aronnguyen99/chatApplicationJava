/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.awt.Desktop;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.bson.Document;
import Model.ConnectDB;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javaapp6.ClientChatController;
/**
 * FXML Controller class
 *
 * @author Ducquy135
 */
public class LoginController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private PasswordField pass;
    @FXML
    private Label labelLogin;
    @FXML
    private CheckBox selectedShowPass;
    @FXML
    private TextField showPass;
    @FXML
    private TextField showPass1;
    @FXML
    private TextField forgot_name;
    @FXML
    private PasswordField newpass;
    @FXML
    private PasswordField confirmpass;
    @FXML
    private Label labelForgot;
    @FXML
    private Button button_login;
    
    private Stage stage;
    
    private Scene scene; 
    
    
    private final MongoClient mongo = new MongoClient("localhost", 27017);
    private final MongoDatabase db = mongo.getDatabase("eProject");
    private final MongoCollection<Document> col= db.getCollection("Userss");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }
//    button_login.setOnAction(event -> {
//            try {
//                checkLogin(new ActionEvent());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    @FXML 
    public void handleKeyPressed(KeyEvent event) throws IOException{
        if(name.getText().isBlank() == true || pass.getText().isBlank() == true){
            labelLogin.setText("Invalid Information");
        }
        else{
            String enteredUsername = name.getText();
            String enterdPassword = pass.getText();
                
            //Query MongoDB
            Document queryUser = new Document("name", enteredUsername)
                    .append("pass", enterdPassword);
            
            FindIterable<Document> cursor = col.find(queryUser);
            if(cursor != null) {
                int i = 0;
                for(Document doc : cursor){
                  i++; 
                }
                if(i!=0) {
                    
                    switchToChatApp(event);
                }
                else{
                    labelLogin.setText("Login Failed");
                }
            }
        }
    }
    
    public void checkLogin(ActionEvent event) throws IOException{
    String enteredUsername = name.getText();
    String enterdPassword = pass.getText();
        if(name.getText().isBlank() == true || pass.getText().isBlank() == true){
            labelLogin.setText("Invalid Information");
        }
        else{
                
            //Query MongoDB
            Document queryUser = new Document("name", enteredUsername)
                    .append("pass", enterdPassword);
            
            FindIterable<Document> cursor = col.find(queryUser);
            if(cursor != null) {
                int i = 0;
                for(Document doc : cursor){
                  i++; 
                }
                if(i!=0) {
                    
                    switchToChatApp(event);
                }
                else{
                    labelLogin.setText("Login Failed");
                }
            }
        }
    }
    public void switchToChatApp(ActionEvent event) throws IOException{
        String username = name.getText();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/chatApplication.fxml"));
        Parent root = loader.load();
        ClientChatController clientChat = loader.getController();
        clientChat.displayName(username);
//        Parent root = FXMLLoader.load(getClass().getResource("../View/chatApplication.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    } 
    public void switchToChatApp(KeyEvent event) throws IOException{
        String username = name.getText();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/chatApplication.fxml"));
        Parent root = loader.load();
        ClientChatController clientChat = loader.getController();
        clientChat.displayName(username);
//        Parent root = FXMLLoader.load(getClass().getResource("../View/chatApplication.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    } 
    public void switchForgotPass(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/forgot.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    public void switchRegis(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/signup.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    public void handleAlready(ActionEvent event) throws IOException{
        switchToLogin(event);
    }
    public void switchToLogin(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    public void switchToLogin(KeyEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    public void showPassword(){
        if(selectedShowPass.isSelected()) {
            showPass.setText(pass.getText());
            showPass.setVisible(true);
            pass.setVisible(false);
        }
        else{
            pass.setText(showPass.getText());
            showPass.setVisible(false);
            pass.setVisible(true);
        }
    }
    public void showPasswordForgot(){
        if(selectedShowPass.isSelected()) {
            showPass.setText(confirmpass.getText());
            showPass1.setText(newpass.getText());
            showPass.setVisible(true);
            showPass1.setVisible(true);
            newpass.setVisible(false);
            confirmpass.setVisible(false);
        }
        else{
            newpass.setText(showPass1.getText());
            confirmpass.setText(showPass.getText());
            showPass.setVisible(false);
            showPass1.setVisible(false);
            newpass.setVisible(true);
            confirmpass.setVisible(true);
        }
    }
    
    public void changePass(ActionEvent event) throws IOException {
        if (forgot_name.getText().isEmpty() || newpass.getText().isEmpty() || confirmpass.getText().isEmpty()) {
            labelForgot.setText("Fill all blank fields");
        } else {
            String enteredforgotName = forgot_name.getText();
            String enterdnewPass = newpass.getText();
            String enterdconfirmPass = confirmpass.getText();

            // Tạo một bộ lọc để kiểm tra sự tồn tại của tên người dùng trong cơ sở dữ liệu
            Document usernameQuery = new Document("name", enteredforgotName);

            // Thực hiện truy vấn để kiểm tra sự tồn tại của tên người dùng
            long count = col.countDocuments(usernameQuery);

            if (count == 0) {
                labelForgot.setText("Username not found");
            } else {
                if (!enterdnewPass.equals(enterdconfirmPass)) {
                    labelForgot.setText("Password not match");
                } 
                else {
                    // Tạo một bản ghi mới để cập nhật mật khẩu
                    Document newPasswordDoc = new Document("$set", new Document("pass", enterdnewPass));

                    // Tạo một bộ lọc để chỉ định bản ghi cần được cập nhật
                    Document filter = new Document("name", enteredforgotName);

                    // Thực hiện cập nhật trong MongoDB
                    UpdateResult updateResult = col.updateOne(filter, newPasswordDoc);

                    // Kiểm tra kết quả của hoạt động cập nhật
                    if (updateResult.getModifiedCount() > 0) {
                        switchToLogin(event); // Chuyển đến giao diện đăng nhập
                    } else {
                        labelForgot.setText("Failed to update password");
                    }
                }
            }
        }
    }
    public void handleChangePass(KeyEvent event) throws IOException {
        if (forgot_name.getText().isEmpty() || newpass.getText().isEmpty() || confirmpass.getText().isEmpty()) {
            labelForgot.setText("Fill all blank fields");
        } else {
            String enteredforgotName = forgot_name.getText();
            String enterdnewPass = newpass.getText();
            String enterdconfirmPass = confirmpass.getText();

            // Tạo một bộ lọc để kiểm tra sự tồn tại của tên người dùng trong cơ sở dữ liệu
            Document usernameQuery = new Document("name", enteredforgotName);

            // Thực hiện truy vấn để kiểm tra sự tồn tại của tên người dùng
            long count = col.countDocuments(usernameQuery);

            if (count == 0) {
                labelForgot.setText("Username not found");
            } else {
                if (!enterdnewPass.equals(enterdconfirmPass)) {
                    labelForgot.setText("Password not match");
                } 
                else {
                    // Tạo một bản ghi mới để cập nhật mật khẩu
                    Document newPasswordDoc = new Document("$set", new Document("pass", enterdnewPass));

                    // Tạo một bộ lọc để chỉ định bản ghi cần được cập nhật
                    Document filter = new Document("name", enteredforgotName);

                    // Thực hiện cập nhật trong MongoDB
                    UpdateResult updateResult = col.updateOne(filter, newPasswordDoc);

                    // Kiểm tra kết quả của hoạt động cập nhật
                    if (updateResult.getModifiedCount() > 0) {
                        switchToLogin(event); // Chuyển đến giao diện đăng nhập
                    } else {
                        labelForgot.setText("Failed to update password");
                    }
                }
            }
        }
    }
}

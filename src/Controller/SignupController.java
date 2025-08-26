package Controller;

import com.mongodb.MongoClient;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.input.KeyEvent;

public class SignupController implements Initializable {

    @FXML
    private TextField name;
    @FXML
    private Label nameErrorLabel;
    @FXML
    private TextField phone;
    @FXML
    private Label phoneErrorLabel;
    @FXML
    private TextField email;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private PasswordField pass;
    @FXML
    private Label passErrorLabel;
    @FXML
    private Label lb_exist;
    @FXML
    private Button button_register;

    private Stage stage;
    private Scene scene;
    private final MongoClient mongo = new MongoClient("localhost", 27017);
    private final MongoDatabase db = mongo.getDatabase("eProject");
    private final MongoCollection<Document> col= db.getCollection("Userss");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Add ChangeListeners to the text fields
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !isValidName(newValue)) {
                nameErrorLabel.setText("Name chỉ chứa chữ cái in thường, in hoa và chữ số");
            } else {
                nameErrorLabel.setText("");
            }
        });

        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !isValidPhoneNumber(newValue)) {
                phoneErrorLabel.setText("Số điện thoại không hợp lệ");
            } else {
                phoneErrorLabel.setText("");
            }
        });

        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !isValidEmail(newValue)) {
                emailErrorLabel.setText("Email không hợp lệ");
            } else {
                emailErrorLabel.setText("");
            }
        });

        pass.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !isValidPassword(newValue)) {
                passErrorLabel.setText("Mật khẩu phải bao gồm chữ cái in thường, in hoa và chữ số");
            } else {
                passErrorLabel.setText("");
            }
        });
//        button_register.setOnAction(event -> {
//        try {
//            handleRegisButton(new ActionEvent());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    });
    }

    @FXML 
    public void handleKeyPressed(KeyEvent event) throws IOException{
        Document userDoc = new Document("name", name.getText())
                .append("email", email.getText())
                .append("phone", phone.getText())
                .append("pass", pass.getText());
        String userName = name.getText();
        Document usernameQuery = new Document("name", userName);
            // Thực hiện truy vấn để kiểm tra sự tồn tại của tên người dùng
        long count = col.countDocuments(usernameQuery);
        System.out.println(count);
        if(count == 0) {
            if(checkInput()) {
            insertDocument(userDoc);
            switchToLogin(event);
            }
        }
        else{
            lb_exist.setText("User Already Exist");
        }
    }
    
    public boolean checkInput(){
        boolean isValid = true;
        if(name.getText().isEmpty()|| !isValidName(name.getText())) {
            name.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            isValid = false;
        }
        else{
            name.setStyle(null);
        }
        if(phone.getText().isEmpty() || !isValidPhoneNumber(phone.getText())) {
            phone.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            isValid = false;
        }
        else{
            phone.setStyle(null);
        }
        if(email.getText().isEmpty() || !isValidEmail(email.getText())) {
            email.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            isValid = false;
        }
        else{
            email.setStyle(null);
        }
        if(pass.getText().isEmpty() || !isValidPassword(pass.getText())) {
            pass.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            isValid = false;
        }
        else{
            pass.setStyle(null);
        }
        return isValid;
    }
    
    @FXML
    public void handleRegisButton(ActionEvent event) throws IOException {
        Document userDoc = new Document("name", name.getText())
                .append("email", email.getText())
                .append("phone", phone.getText())
                .append("pass", pass.getText());
        String userName = name.getText();
        Document usernameQuery = new Document("name", userName);
            // Thực hiện truy vấn để kiểm tra sự tồn tại của tên người dùng
        long count = col.countDocuments(usernameQuery);
        System.out.println(count);
        if(count == 0) {
            if(checkInput()) {
            insertDocument(userDoc);
            switchToLogin(event);
            }
        }
        else{
            lb_exist.setText("User Already Exist");
        }
    }
    @FXML
    public void handleAlready(ActionEvent event) throws IOException{
        switchToLogin(event);
    }
    public void switchToLogin(ActionEvent event) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        
    }
    public void switchToLogin(KeyEvent event) throws IOException {
        System.out.println("Enter");
        Parent root = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void insertDocument(Document document) {
        try (MongoClient mongo = new MongoClient("localhost", 27017)) {
            MongoDatabase db = mongo.getDatabase("eProject");
            MongoCollection<Document> col = db.getCollection("Userss");
            col.insertOne(document);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error");
        }
    }   

    public void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Regex validation methods
    private boolean isValidName(String name) {
        String regex = "^[a-zA-Z0-9]+$";
        return Pattern.matches(regex, name);
    }

    private boolean isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        return Pattern.matches(regex, email);
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        return Pattern.matches(regex, password);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^(0|\\84)\\d{9}$";
        return Pattern.matches(regex, phoneNumber);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javaapp6;


import Model.MongoDBConnection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.bson.Document;
import javafx.scene.input.KeyEvent;
import Controller.LoginController;
import Model.MongoDBConnection;
import Model.UserSearch;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import javafx.scene.control.Label;
import Model.MessageCollectionCreator;
import Model.SendMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.sun.javafx.logging.PlatformLogger.Level;
import java.lang.System.Logger;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.bson.Document;
/**
 * FXML Controller class
 *
 * @author Nguyen Huy
 */
public class ClientChatController implements Initializable {

    @FXML
    private ScrollPane sp_main;
    @FXML
    private ScrollPane scrollpane_chat;
    @FXML
    private  VBox pane_mess;
    @FXML
    private TextField tf_mesage;
    @FXML
    private Button btn_send;
    Client client;
    @FXML
    private Label lb_name;
    @FXML
    private Label lb_title;
    @FXML
    private Button search_button;
    @FXML
    private TextField search_field;
    @FXML
    private VBox print_result;

    private UserSearch userSearch; // Khai báo lớp UserSearch
    
//    @FXML
//    private VBox pane_friend;
    static String color_text_rcv = "#00684a";
    static String fromName;
    static String toName;
    private Stage stage;
    private Scene scene;

    static String catch_msg = "false";
    
    
    private static boolean update_friend = false;

    
    
    
//    private final MongoClient mongo = new MongoClient("localhost", 27017);
    MongoDBConnection connection = new MongoDBConnection("mongodb://localhost:27017", "eProject");
    private final MongoDatabase db = connection.getDatabase();
    MongoCollection<Document> usersCollection = db.getCollection("Userss");
    public MongoCollection<Document> col;
    public FindIterable<Document> cursor;
    /**
     * Initializes the controller class.
     */
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            performSearch(newValue);
        });
        search_button.setOnAction(event -> search());
        scrollpane_chat.vvalueProperty().bind(pane_mess.heightProperty());
        Platform.runLater(() -> tf_mesage.requestFocus());
          try{
            LoginController login = new LoginController();
            client = new Client(new Socket("localhost",99), lb_name.getText());
            System.out.println("Connected to Server");
        }catch(IOException e){
            e.printStackTrace();
        }
        client.listenForMessage(pane_mess);
        listen_msg();
        btn_send.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                try {
                    button_handle(tf_mesage.getText());
                } catch (JsonProcessingException ex) {
                    java.util.logging.Logger.getLogger(ClientChatController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        });
    }
    public void listen_msg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(client.socket.isConnected()){
                    if(update_friend && search_field.getText().isEmpty()){
                        Platform.runLater(() -> {
                            print_result.getChildren().clear();
                            friend_list();
                        });
                        update_friend = false;
                        catch_msg = "catched";
                    }
                    System.out.print("");
                }
            }
        }).start();
    }
    private void performSearch(String searchString) {

        // Kiểm tra nếu chuỗi tìm kiếm là null hoặc rỗng
        if (searchString.isEmpty()) {
            print_result.getChildren().clear();
            friend_list();
            return; // Không làm gì nếu chuỗi tìm kiếm là rỗng
        }

        MongoDBConnection connection = new MongoDBConnection("mongodb://localhost:27017", "eProject");
        MongoCollection<Document> usersCollection = connection.getDatabase().getCollection("Userss");
        userSearch = new UserSearch(usersCollection);

        List<Document> searchResults = userSearch.searchUser(searchString);

        print_result.getChildren().clear();

        if (!searchResults.isEmpty()) {
            System.out.println("Search results for name: " + searchString);
            addResultLabel(searchResults, print_result);
            for (Document result : searchResults) {
                System.out.println(result.toJson());
            }
        } else {
            displayNoResultMessage(); // Hiển thị thông báo khi không có kết quả
        }

        connection.closeConnection(); // Đóng kết nối sau khi sử dụng
    }
    private void displayNoResultMessage() {
        Platform.runLater(() -> {
            print_result.getChildren().clear();
            Label noResultLabel = new Label("Không có kết quả phù hợp");
            
            noResultLabel.setPadding(new Insets(5, 5, 5, 5));
            noResultLabel.setStyle("-fx-text-fill: #00684A; -fx-font-size: 20px; -fx-font-weight: bold; -fx-border-color: #00684A; -fx-border-width: 2px; -fx-alignment: center;");
            print_result.setAlignment(Pos.TOP_CENTER);
            print_result.getChildren().add(noResultLabel);
            print_result.setMargin(noResultLabel, new Insets(210, 0, 0, 0));
        });
    }
    @FXML
    private void search() {
        userSearch = new UserSearch(usersCollection);
        String searchString = search_field.getText().trim();
        List<Document> searchResults = userSearch.searchUser(searchString);

        print_result.getChildren().clear();

        if (!searchResults.isEmpty()) {
            System.out.println("Search results for name: " + searchString);
            addResultLabel(searchResults, print_result);
            for (Document result : searchResults) {
                System.out.println(result.toJson());
            }
        } else {
            System.out.println("No users found with the name: " + searchString);
        }

        connection.closeConnection(); // Đóng kết nối sau khi sử dụng
    }
    public void addResultLabel(List<Document> searchResults, VBox print_result) {
        VBox.setMargin(print_result, new Insets(3, 3, 3, 3));
        Platform.runLater(() -> {
            print_result.getChildren().clear(); // Xóa các thành phần hiển thị cũ
            for (Document result : searchResults) {
                String name = result.getString("name"); // Lấy tên từ Document
                String email = result.getString("email"); // Lấy email từ Document
                if(!name.equals(fromName)){
                    print_result.getChildren().add(friend_gen(name, email));
                }
            }
            print_result.setAlignment(Pos.TOP_CENTER);
        });
    }
    @FXML 
    public void handleKeyPressed(KeyEvent event) throws IOException{
        if(event.getCode() == KeyCode.ENTER) {
            button_handle(tf_mesage.getText());
        }
    }
    
    
    public static void save_to_mongo(String from_user, String to_user, String messageContent){
        if(from_user != null && to_user != null){
            MongoDBConnection connection = new MongoDBConnection("mongodb://localhost:27017", "eProject");
            MongoDatabase database = connection.getDatabase();
            MessageCollectionCreator messageCollectionCreator = new MessageCollectionCreator(database);
            messageCollectionCreator.createMessageCollection(from_user, to_user);

            SendMessage sendMessage = new SendMessage(database);
            sendMessage.sendMessage(from_user, to_user, messageContent);
        }
    }
    public void fetch_from_mongo(String from_user, String to_user){
        MessageCollectionCreator msg_creator = new MessageCollectionCreator(db);
         col= db.getCollection(msg_creator.generateCollectionName(from_user, to_user));
         cursor = col.find();
    }
        
    public HBox display_sendText(String msgToSend){
        HBox hbox =new HBox(); 
        hbox.setAlignment(Pos.TOP_RIGHT);
        hbox.setPadding(new Insets(5,5,5,10));
        hbox.setMaxWidth(300);
        pane_mess.setAlignment(Pos.TOP_RIGHT);

        Text text =new Text(msgToSend);
        TextFlow textFlow = new TextFlow(text);
        text.setStyle("-fx-fill: white;" + "-fx-font-size: 22px");
        textFlow.setStyle( 
                    "-fx-background-color: #00684a;" + 
                    "-fx-background-radius: 20px;" +  
                    "-fx-border-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hbox.getChildren().add(textFlow);
        
        return hbox;
    }
    public static HBox display_recieveText(String msgFromGroup){
        HBox hbox = new HBox();
        HBox hboxSmall = new HBox();
        hboxSmall.setAlignment(Pos.TOP_LEFT);
        hbox.setPadding(new Insets(5,5,5,10));
        hboxSmall.setMaxWidth(300);
        
        Text text = new Text(msgFromGroup);
        TextFlow textFlow = new TextFlow(text);
        text.setStyle("-fx-fill: " + color_text_rcv +";"+
                "-fx-font-size: 22px");
        textFlow.setStyle( "fx-border-color: #00684a;"+
                           "-fx-background-color: white;" +
                           "-fx-background-radius: 20px;" +  
                           "-fx-border-radius: 20px;");  
        textFlow.setPadding(new Insets(5,10,5,10));
        hboxSmall.getChildren().add(textFlow);
        hbox.getChildren().add(hboxSmall);
        return hbox; 
    }
    public void button_handle(String msgToSend) throws JsonProcessingException{
        if(!msgToSend.isEmpty()){
            
            String messageContent = tf_mesage.getText();
            pane_mess.getChildren().add(display_sendText(msgToSend));

            ObjectMapper msg = new ObjectMapper();
            message snd_text = new message();
            snd_text.setFromName(fromName);
            snd_text.setToName(toName);
            snd_text.setContent(msgToSend);
            snd_text.setCol_name(" ");
            snd_text.setDate(new Date());
            String json_msg = msg.writeValueAsString(snd_text);
            
            if(snd_text.getToName() == null){
                System.out.println("to name is empty!");
                lb_title.setText("S E R V E R");
                lb_title.setStyle("-fx-text-fill: RED;");
                color_text_rcv = "RED";
                pane_mess.getChildren().add(display_recieveText("BUT NOBODY CAME"));
                print_result.getChildren().clear();
                friend_list();
            }else{
                save_to_mongo( fromName, toName, messageContent);
                print_result.getChildren().clear();
                friend_list();
                client.sendMessage(json_msg);
            }
            tf_mesage.clear();
        }
    }
    public static void addLabel(String msgFromGroup, VBox vBox){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                ObjectMapper json = new ObjectMapper(); 
                try {
                    message rcv_pakage = json.readValue(msgFromGroup, message.class);
                    System.out.println("send text: from " + rcv_pakage.getFromName() + "--- to: " + rcv_pakage.getToName() + "--- content: " + rcv_pakage.getContent());

                    if(rcv_pakage.getToName().equals(fromName)){
                        update_friend = true;
                        if(rcv_pakage.getFromName().equals(toName)){
                            vBox.getChildren().add(display_recieveText(rcv_pakage.getContent()));
                        }
                    }
                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
            }        
        });
    }
    public void displayName(String username) throws IOException{
        char firstInitial = username.charAt(0);
        this.fromName = username;
        friend_list();
        client.set_name(this.fromName);
        lb_name.setText(Character.toString(firstInitial).toUpperCase());
        System.out.println("THIS IS :" + fromName);
    }
    
    public void friend_list(){
        MongoIterable<String> list = db.listCollectionNames();
        String regexPattern = ".*_" + this.fromName + "|.*_" + this.fromName + "_.*";
        ArrayList<message> list_friends = new ArrayList<>();
        for (String name : list) {
            message friend_one = new message();
            String fstText = "";
            if(name.matches(regexPattern)){
                friend_one.setCol_name(name);
                col= db.getCollection(name);
                cursor = col.find();
                for(Document doc : cursor){
                    friend_one.setDate(doc.getDate("date"));
                    friend_one.setFromName(doc.getString("from"));
                    friend_one.setToName(doc.getString("to"));
                    friend_one.setContent(doc.getString("content"));
                }
                list_friends.add(friend_one);
             }
         }
            Collections.sort(list_friends, new Comparator<message>() {
                public int compare(message o1, message o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });

        for (message list_friend : list_friends) {
            String fstText = new String();
            String user_name = new String();
            if(list_friend.getFromName().equals(fromName)){
                fstText = "You: " + list_friend.getContent();
                user_name = list_friend.getToName();
            }else{
                fstText = "" + list_friend.getFromName() + ": " + list_friend.getContent();
                user_name = list_friend.getFromName();
            }
            print_result.getChildren().add(friend_gen(user_name, fstText));
        }
      }
      
    
    public  StackPane friend_gen(String toName, String fst_msg){
        StackPane sp = new StackPane();
        VBox vbox = new VBox();
        Label lb_name = new Label(toName);
        Label lb_content = new Label(fst_msg);
        Button btn_choose = new Button();
        
        Label friend_name = new Label(toName.substring(0, 1).toUpperCase());
        Pane pane = new Pane();
        Circle circle_name = new Circle();
        
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setStyle("-fx-pref-width: 194 .0px;"
                + "-fx-pref-height: 80.0px;");
        
        sp.setStyle("-fx-pref-width: 315.0px;"
                + "-fx-pref-height: 80.0px;");
        sp.setAlignment(Pos.CENTER_LEFT);
       
        pane.setStyle("-fx-pref-width: 92.0px;"
                + "-fx-pref-height: 92.0px;");
        circle_name.setLayoutX(40);
        circle_name.setLayoutY(39);
        circle_name.setRadius(31);
        circle_name.setStrokeWidth(4);
        circle_name.setStyle("INSIDE");
        circle_name.setStyle("-fx-stroke: #00684a;"
                + "-fx-fill: WHITE;");
        
        friend_name.setAlignment(Pos.CENTER);
        friend_name.setContentDisplay(ContentDisplay.CENTER);
        friend_name.setLayoutX(13);
        friend_name.setLayoutY(13);
        friend_name.setTextAlignment(TextAlignment.CENTER);
        friend_name.setStyle("-fx-pref-width: 53.0px;"
                + "-fx-pref-height: 53.0px;"
                + "-fx-<font-style>: System Bold;"
                + "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
                +"-fx-text-fill: BLACK;");
        
        pane.getChildren().add(circle_name);
        pane.getChildren().add(friend_name);
        pane.setStyle("-fx-border-color:  #00684A;"
                + "-fx-border-width: 0 0 3 0;");
        
        vbox.setMargin(lb_name, new Insets(0, 0, 0, 90));
        vbox.setMargin(lb_content, new Insets(0, 0, 0, 90));
        
        lb_name.setAlignment(Pos.TOP_LEFT);
        lb_name.setStyle("-fx-text-fill: BLACK;"
                + "-fx-font-size: 20px;"
                + "-fx-pref-width: 166.0px;"
                + "-fx-pref-height: 26.0px;"
                + "-fx-<font-style>: Ebrima Bold;");
        lb_content.setAlignment(Pos.CENTER_LEFT);
        lb_content.setStyle("-fx-text-fill: #9da7a7;"
                + "-fx-font-size: 14px;"
                + "-fx-pref-width: 214.0px;"
                + "-fx-pref-height: 26.0px;"
                + "-fx-<font-style>: Ebrima;");
        
        vbox.getChildren().add(lb_name);
        vbox.getChildren().add(lb_content);
        
        btn_choose.setAlignment(Pos.CENTER_RIGHT);
        btn_choose.setStyle("-fx-content-display: CENTER;"
                + "-fx-pref-width: 314.0px;"
                + "-fx-pref-height: 121.0px;"
                + "-fx-opacity: 0.0");
        btn_choose.setOnAction(e -> {
            this.toName = toName;
            lb_title.setStyle("-fx-text-fill:  #00684a;");
            color_text_rcv = "black";
            lb_title.setText(toName);
            client.msgSendTo_name(toName);
            pane_mess.getChildren().clear();
            fetch_from_mongo(fromName, toName);
            int i=0;
            for(Document doc : cursor){
                String msg = doc.getString("content");
                if(doc.getString("from").equals(fromName)){
                    pane_mess.getChildren().add(display_sendText(msg));
                }else{
                    pane_mess.getChildren().add(display_recieveText(msg));
                }
                i++;
            }
            search_field.clear();
            print_result.getChildren().clear();
            friend_list();
            if(i == 0){
                print_result.getChildren().add(friend_gen(toName, ""));
            }
            Platform.runLater(() -> tf_mesage.requestFocus());
        });
        if(lb_name.getText().equals(this.lb_title.getText())){
            pane.setStyle("-fx-background-color: #00684A;");
            lb_name.setStyle("-fx-text-fill: White;"
                + "-fx-font-size: 20px;"
                + "-fx-pref-width: 166.0px;"
                + "-fx-pref-height: 26.0px;"
                + "-fx-<font-style>: Ebrima Bold;");
            lb_content.setStyle("-fx-text-fill: White;"
                + "-fx-font-size: 14px;"
                + "-fx-pref-width: 214.0px;"
                + "-fx-pref-height: 26.0px;"
                + "-fx-<font-style>: Ebrima;");
        }
        sp.getChildren().add(pane);
        sp.getChildren().add(vbox);
        sp.getChildren().add(btn_choose);
        return sp;
        
    }
    public void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
}

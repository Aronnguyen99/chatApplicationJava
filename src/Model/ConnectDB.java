/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 *
 * @author Ducquy135
 */
public class ConnectDB {
    public static void main(String[] args) {
        try{
        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase db = mongo.getDatabase("eProject");
        MongoCollection<Document> col = db.getCollection("Userss");
        
        System.out.println("Connect to database successfully");
        
    }
    catch(Exception e) {
        e.printStackTrace();
    }
    }
}

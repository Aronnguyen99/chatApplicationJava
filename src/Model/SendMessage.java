package Model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import Model.MessageCollectionCreator;
import Model.MongoDBConnection;
import org.bson.Document;
import java.time.LocalDateTime;
import java.util.Arrays;

public class SendMessage {

    private MongoDatabase database;

    public SendMessage(MongoDatabase database) {
        this.database = database;
    }

    public void sendMessage(String fromName, String toName, String messageContent) {
        String collectionName = generateCollectionName(fromName, toName);

        // Check if the collection already exists, if not, create it
        if (!collectionExists(collectionName)) {
            MessageCollectionCreator collectionCreator = new MessageCollectionCreator(database);
            collectionCreator.createMessageCollection(fromName, toName);
        }

        MongoCollection<Document> messageCollection = database.getCollection(collectionName);

        Document messageDocument = new Document();
        messageDocument.append("from", fromName)
                       .append("to", toName)
                       .append("date", LocalDateTime.now())
                       .append("content", messageContent);

        messageCollection.insertOne(messageDocument);

        System.out.println("Message sent from " + fromName + " to " + toName + ": " + messageContent);
    }

    private boolean collectionExists(String collectionName) {
        for (String existingCollection : database.listCollectionNames()) {
            if (existingCollection.equals(collectionName)) {
                return true;
            }
        }
        return false;
    }

    private String generateCollectionName(String name1, String name2) {
        String[] sortedUsernames = {name1, name2};
        Arrays.sort(sortedUsernames);
        return "message_" + sortedUsernames[0] + "_" + sortedUsernames[1];
    }

    public static void main(String[] args) {
        String connectionString = "mongodb://localhost:27017";
        String databaseName = "eProject";

        MongoDBConnection connection = new MongoDBConnection(connectionString, databaseName);
        MongoDatabase database = connection.getDatabase();

        SendMessage sendMessage = new SendMessage(database);

        // Example usage: Send messages between "alice_in_wonderland" and "bob_reggae"
        sendMessage.sendMessage("alice_in_wonderland", "bob_reggae", "Chào ae tôi!");
        

        connection.closeConnection();
    }
}

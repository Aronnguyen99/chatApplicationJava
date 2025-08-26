package Model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

public class MessageCollectionCreator {

    private MongoDatabase database;

    public MessageCollectionCreator(MongoDatabase database) {
        this.database = database;
    }

    public void createMessageCollection(String fromName, String toName) {
        String collectionName = generateCollectionName(fromName, toName);

        // Check if the collection already exists
        if (!collectionExists(collectionName)) {
            MongoCollection<Document> messageCollection = database.getCollection(collectionName);

            // Create indexes for sorting by date
            messageCollection.createIndex(new Document("date", 1));

            System.out.println("Message collection created: " + collectionName);
        } else {
            System.out.println("Message collection already exists: " + collectionName);
        }
    }

    public boolean collectionExists(String collectionName) {
        for (String existingCollection : database.listCollectionNames()) {
            if (existingCollection.equals(collectionName)) {
                return true;
            }
        }
        return false;
    }

    public static String generateCollectionName(String name1, String name2) {
        // Sort usernames to ensure consistent collection names
        String[] sortedUsernames = {name1, name2};
        Arrays.sort(sortedUsernames);
        return "message_" + sortedUsernames[0] + "_" + sortedUsernames[1];
    }

    public static void main(String[] args) {
        String connectionString = "mongodb://localhost:27017";
        String databaseName = "eProject";

        MongoDBConnection connection = new MongoDBConnection(connectionString, databaseName);
        MongoDatabase database = connection.getDatabase();

        MessageCollectionCreator messageCollectionCreator = new MessageCollectionCreator(database);

        // Example usage: Create message collections for pairs of usernames
        messageCollectionCreator.createMessageCollection("alice_in_wonderland", "bob_reggae");
        messageCollectionCreator.createMessageCollection("eva_actress", "charlie_brown");
        messageCollectionCreator.createMessageCollection("diana_prince", "edward_cullen");

        connection.closeConnection();
    }
}

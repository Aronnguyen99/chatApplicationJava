package Model;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserSearch {

    private final MongoCollection<Document> usersCollection;

    public UserSearch(MongoCollection<Document> usersCollection) {
        this.usersCollection = usersCollection;
    }

    public List<Document> searchUser(String searchString) {
        List<Document> resultsByName = searchByName(searchString);
        List<Document> resultsByEmail = searchByEmail(searchString);

        Set<Object> uniqueIds = new HashSet<>();
        List<Document> combinedResults = new ArrayList<>();

        for (Document doc : resultsByName) {
            Object id = doc.get("_id");
            if (!uniqueIds.contains(id)) {
                combinedResults.add(doc);
                uniqueIds.add(id);
            }
        }

        for (Document doc : resultsByEmail) {
            Object id = doc.get("_id");
            if (!uniqueIds.contains(id)) {
                combinedResults.add(doc);
                uniqueIds.add(id);
            }
        }

        return combinedResults;
    }

    public List<Document> searchByName(String name) {
        String regexPattern = ".*" + name + ".*";
        Document query = new Document("name", new Document("$regex", regexPattern));

        // Projection to include only 'name' and 'email' fields
        Document projection = new Document("name", 1).append("email", 1);

        return convertToList(usersCollection.find(query).projection(projection));
    }

    public List<Document> searchByEmail(String email) {
        String regexPattern = ".*" + email + ".*";
        Document query = new Document("email", new Document("$regex", regexPattern));

        // Projection to include only 'name' and 'email' fields
        Document projection = new Document("name", 1).append("email", 1);

        return convertToList(usersCollection.find(query).projection(projection));
    }

    private List<Document> convertToList(FindIterable<Document> documents) {
        List<Document> resultList = new ArrayList<>();
        for (Document doc : documents) {
            resultList.add(doc);
        }
        return resultList;
    }
}
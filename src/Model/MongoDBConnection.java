package Model;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

import java.util.Arrays;

public class MongoDBConnection {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;

    public MongoDBConnection(String connectionString, String databaseName) {
        MongoClientURI uri = new MongoClientURI(connectionString);
        this.mongoClient = new MongoClient(uri);

        // Kiểm tra xem database có sẵn chưa, nếu chưa thì tạo mới
        if (!databaseExists(databaseName)) {
            createDatabaseAndUsersCollection(databaseName);
        } else {
            this.database = mongoClient.getDatabase(databaseName);
            this.usersCollection = database.getCollection("Userss");
            System.out.println("Connected to existing MongoDB: " + databaseName);
        }
    }

    private boolean databaseExists(String databaseName) {
        for (String existingDatabase : mongoClient.listDatabaseNames()) {
            if (existingDatabase.equals(databaseName)) {
                return true;
            }
        }
        return false;
    }

    private void createDatabaseAndUsersCollection(String databaseName) {
        // Tạo mới database và collection "Users" nếu chưa tồn tại
        MongoDatabase newDatabase = mongoClient.getDatabase(databaseName);
        newDatabase.createCollection("Userss");
        System.out.println("Database and Users collection created: " + databaseName);

        // Tạo unique indexes cho username và email
        usersCollection = newDatabase.getCollection("Userss");
        usersCollection.createIndex(new Document("name", 1), new IndexOptions().unique(true));
        usersCollection.createIndex(new Document("email", 1), new IndexOptions().unique(true));

        // Tạo document mẫu để mô tả cấu trúc collection
//        Document userDocument = new Document("name", "John Doe")
//                .append("username", "john_doe")
//                .append("email", "john.doe@example.com")
//                .append("password", "hashedPassword") // Bạn nên lưu mật khẩu đã được hash trong thực tế
//                .append("birthday", "1990-01-01")
//                .append("listfriend", Arrays.asList("friend1", "friend2"))
//                .append("blacklist", Arrays.asList("userBlocked1", "userBlocked2"));

        // Chèn document mẫu vào collection
//        usersCollection.insertOne(userDocument);

        // Gán database và collection cho đối tượng
        this.database = newDatabase;
        this.usersCollection = usersCollection;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Disconnected from MongoDB.");
        }
    }

    private void addDocument(String name, String email, String phone, String pass) {
        // Kiểm tra trùng khóa chính trước khi thêm Document vào collection
        if (usersCollection.countDocuments(new Document("name", name)) == 0) {
            Document userDocument = new Document("name", name)
                    .append("email", email)
                    .append("phone", phone)
                    .append("pass", pass);
            usersCollection.insertOne(userDocument);
            System.out.println("Document for username '" + name + "' inserted successfully.");
        } else {
            System.out.println("Document for username '" + name + "' already exists. Skipped.");
        }
    }

    public static void main(String[] args) {
        String connectionString = "mongodb://localhost:27017";
        String databaseName = "eProject";

        MongoDBConnection connection = new MongoDBConnection(connectionString, databaseName);
        MongoDatabase database = connection.getDatabase();

        connection.addDocument("Quy", "quy@gmail.com",  "0945132002", "Quy1");
        connection.addDocument("Duong", "duong@gmail.com",  "0912312344", "Duong1");
        connection.addDocument("Tien", "tien@gmail.com",  "0912345678", "Tien1");
        connection.addDocument("Huy", "huy@gmail.com",  "0931414151", "Huy1");
        connection.addDocument("Kien", "kien@gmail.com",  "0941352000", "Kien1");
        connection.closeConnection();
    }
}

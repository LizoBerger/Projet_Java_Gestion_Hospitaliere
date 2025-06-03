import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
    private static final String URI = "mongodb+srv://berglizo534:Projet_java_25@cluster0.g6a1a9p.mongodb.net/";
    private static final String DB_NAME = "Database_DrJava";

    public static MongoDatabase getDatabase() {
        MongoClient mongoClient = MongoClients.create(URI);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        return database;
    }
}

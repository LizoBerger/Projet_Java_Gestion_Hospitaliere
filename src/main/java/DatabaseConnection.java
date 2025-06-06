import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    private static MongoDatabase database;

    public static void Connect() {
        Dotenv dotenv = Dotenv.configure()
                .directory("src")
                .filename("data.env")
                .load();
        try {


            String mongoUri = dotenv.get("MONGO_URI");
            MongoClient mongoClient = MongoClients.create(mongoUri);
            database = mongoClient.getDatabase("Database_DrJava");


            System.out.println("Connexion à MongoDB réussie !");
        } catch (Exception e) {
            System.err.println("Erreur de connexion à MongoDB : " + e.getMessage());
        }

    }
    public static MongoDatabase getDatabase(){
        return database;
    }
}

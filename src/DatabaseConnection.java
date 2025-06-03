import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    private static MongoDatabase database;

    static {
        try {
            // Charger les variables d'environnement depuis le fichier .env
            Dotenv dotenv = Dotenv.load();

            // Récupérer l'URI de connexion MongoDB
            String uri = dotenv.get("MONGO_URI");

            // Créer un client MongoDB
            MongoClient mongoClient = MongoClients.create(uri);

            // Connexion à la base de données (remplacer "hopital" si besoin)
            database = mongoClient.getDatabase("hopital");

            System.out.println("Connexion à MongoDB réussie !");
        } catch (Exception e) {
            System.err.println("Erreur de connexion à MongoDB : " + e.getMessage());
        }
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}

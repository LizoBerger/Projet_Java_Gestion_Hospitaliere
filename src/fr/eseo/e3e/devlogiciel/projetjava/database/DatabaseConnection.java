package fr.eseo.e3e.devlogiciel.projetjava.database;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * @class DatabaseConnection
 * @brief Classe utilitaire pour gérer la connexion à la base de données MongoDB.
 *
 * Cette classe permet d'établir une connexion à MongoDB en utilisant
 * une URI récupérée depuis un fichier de configuration (.env).
 */
public class DatabaseConnection {

    /** Instance statique de la base de données MongoDB */
    public static MongoDatabase database;

    /**
     * @brief Initialise la connexion à MongoDB.
     *
     * Charge les variables d'environnement depuis un fichier ".env" situé dans "src",
     * récupère la variable MONGO_URI, puis crée un client MongoDB et se connecte à la base "DrJava".
     *
     * Affiche un message de succès ou d'erreur sur la console.
     */
    public static void Connect() {
        Dotenv dotenv = Dotenv.configure()
                .directory("src")
                .filename("data.env")
                .load();
        try {
            String mongoUri = dotenv.get("MONGO_URI");
            MongoClient mongoClient = MongoClients.create(mongoUri);
            database = mongoClient.getDatabase("DrJava");

            System.out.println("Connexion à MongoDB réussie !");
        } catch (Exception e) {
            System.err.println("Erreur de connexion à MongoDB : " + e.getMessage());
        }
    }

    /**
     * @brief Retourne l'objet MongoDatabase utilisé pour les opérations sur la base.
     *
     * @return Instance MongoDatabase connectée à la base "DrJava".
     */
    public static MongoDatabase getDatabase(){
        return database;
    }
}

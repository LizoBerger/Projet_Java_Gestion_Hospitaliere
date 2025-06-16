package fr.eseo.e3e.devlogiciel.projetjava.users.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;

/**
 * @class AuthService
 * @brief Service d'authentification des utilisateurs.
 *
 * Cette classe fournit des méthodes pour gérer la connexion des utilisateurs
 * via leur email et mot de passe, en interrogeant la base de données MongoDB.
 */
public class AuthService {

    /** Collection MongoDB des utilisateurs */
    public static MongoCollection<Document> users = DatabaseConnection.getDatabase().getCollection("User");
    private final MongoCollection<Document> UserCollection;

    /**
     * @brief Tente d'authentifier un utilisateur avec un email et un mot de passe.
     *
     * @param email L'adresse email de l'utilisateur.
     * @param mdp Le mot de passe sous forme de tableau de caractères.
     * @return true si l'authentification réussit, false sinon.
     *
     * Cette méthode vérifie si l'email existe dans la base,
     * puis compare le mot de passe fourni avec celui enregistré.
     */
    public static boolean seConnecter(String email, char[] mdp) {
        Document user = users.find(Filters.eq("Email", email)).first();

        if (user == null) {
            throw new IllegalArgumentException("Email inconnu");
        }

        String password = new String(mdp);
        if (!user.getString("Password").equals(password)) {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }

        return true;
    }

    public AuthService() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        if (database == null) {
            throw new IllegalStateException("Database not initialized. Call DatabaseConnection.connect() first.");
        }
        this.UserCollection = database.getCollection("users");
    }
}

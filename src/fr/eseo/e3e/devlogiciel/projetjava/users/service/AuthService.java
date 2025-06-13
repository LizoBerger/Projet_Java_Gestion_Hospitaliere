package fr.eseo.e3e.devlogiciel.projetjava.users.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import org.bson.Document;


public class AuthService {
    public static MongoCollection<Document> users = DatabaseConnection.getDatabase().getCollection("User");

    public static boolean seConnecter(String email, char[] mdp) {

        Document user = users.find(Filters.eq("Email", email)).first();

        if (user == null) {
            System.out.println("Email inconnu");
            return false;
        }

        String password = new String(mdp);
        if (!user.getString("Password").equals(password)) {
            System.out.println("Mot de passe incorrect");
            return false;
        }

        return user.getString("Password").equals(password);
    }
}

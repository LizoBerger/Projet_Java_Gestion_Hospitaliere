package fr.eseo.e3e.devlogiciel.projetjava.users.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

public abstract class Utilisateurs {

    ObjectId id;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;

    public static MongoCollection<Document> users = DatabaseConnection.getDatabase().getCollection("User");

    public Utilisateurs(ObjectId id, String nom, String prenom, String email, String mdp) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
    }



    public abstract String getRole();

    public abstract String getNom();

    public abstract String getPrenom();




    public static String getNomComplet(String email) {
        Document user = users.find(Filters.eq("Email", email)).first();

        if (user == null) return null;

        return user.getString("Prénom") + " " + user.getString("Nom");
    }
}

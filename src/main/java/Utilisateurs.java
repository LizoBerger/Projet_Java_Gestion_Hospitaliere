
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class Utilisateurs {

    int id;
    private String Nom;
    private String Prenom;
    private String email;
    private String MDP;

    public static boolean seConnecter(String email, char[] mdp) {
        MongoCollection<Document> users = DatabaseConnection.getDatabase().getCollection("users");
        Document user = users.find(Filters.eq("email", email)).first();

        if (user == null) return false;

        String password = new String(mdp);
        return user.getString("password").equals(password);
    }

    public static String detecterRole(String email) {

        MongoCollection<Document> users = DatabaseConnection.getDatabase().getCollection("users");
        Document user = users.find(Filters.eq("email", email)).first();

        if (user == null) return null;

        return user.getString("role");

    }

    public static String getPrenom(String email) {
        MongoCollection<Document> users = DatabaseConnection.getDatabase().getCollection("users");
        Document user = users.find(Filters.eq("email", email)).first();

        if (user == null) return null;

        return user.getString("prenom");
    }
}

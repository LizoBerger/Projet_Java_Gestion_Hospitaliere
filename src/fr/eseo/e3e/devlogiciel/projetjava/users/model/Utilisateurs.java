package fr.eseo.e3e.devlogiciel.projetjava.users.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * @class Utilisateurs
 * @brief Classe abstraite représentant un utilisateur générique.
 *
 * Cette classe définit les attributs communs à tous les types
 * d'utilisateurs (médecin, patient, etc.) ainsi que les méthodes
 * abstraites devant être implémentées par les sous-classes.
 */
public abstract class Utilisateurs {

    ObjectId id;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;

    /** Collection MongoDB des utilisateurs */
    public static MongoCollection<Document> users = DatabaseConnection.getDatabase().getCollection("User");

    /**
     * @brief Constructeur de la classe Utilisateurs.
     *
     * @param id Identifiant MongoDB unique
     * @param nom Nom de l'utilisateur
     * @param prenom Prénom de l'utilisateur
     * @param email Adresse email
     * @param mdp Mot de passe
     */
    public Utilisateurs(ObjectId id, String nom, String prenom, String email, String mdp) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
    }

    /**
     * @brief Retourne le rôle de l'utilisateur (ex: "Médecin", "Patient").
     * @return Le rôle sous forme de chaîne.
     */
    public abstract String getRole();

    /**
     * @brief Retourne le nom de l'utilisateur.
     * @return Le nom de l'utilisateur.
     */
    public abstract String getNom();

    /**
     * @brief Retourne le prénom de l'utilisateur.
     * @return Le prénom de l'utilisateur.
     */
    public abstract String getPrenom();

    /**
     * @brief Récupère le nom complet (prénom + nom) d'un utilisateur
     *        à partir de son adresse email, en interrogeant la base de données.
     *
     * @param email L'adresse email de l'utilisateur recherché.
     * @return Le nom complet sous forme "Prénom Nom", ou null si non trouvé.
     */
    public static String getNomComplet(String email) {
        Document user = users.find(Filters.eq("Email", email)).first();

        if (user == null) return null;

        return user.getString("Prénom") + " " + user.getString("Nom");
    }
}

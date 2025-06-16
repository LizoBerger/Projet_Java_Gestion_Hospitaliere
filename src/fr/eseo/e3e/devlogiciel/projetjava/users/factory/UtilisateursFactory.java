package fr.eseo.e3e.devlogiciel.projetjava.users.factory;

import fr.eseo.e3e.devlogiciel.projetjava.consultation.service.ConsultationService;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Utilisateurs;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import org.bson.Document;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.eseo.e3e.devlogiciel.projetjava.consultation.service.ParseHoraire.parseHoraires;

/**
 * @class UtilisateursFactory
 * @brief Factory pour créer des instances de {@link Utilisateurs} (Patient ou Médecin)
 *        à partir de la base de données MongoDB.
 *
 * Fournit des méthodes statiques pour récupérer un utilisateur depuis la collection "User"
 * selon l'email ou le nom, et instancie la bonne classe fille (Patient ou Medecin).
 */
public class UtilisateursFactory {

    /** Collection MongoDB "User" */
    private static MongoCollection<Document> users = DatabaseConnection.getDatabase().getCollection("User");

    /**
     * @brief Récupère un utilisateur à partir de son adresse email.
     *
     * @param email L'email de l'utilisateur recherché.
     * @return Une instance de {@link Patient} ou {@link Medecin} si trouvée, sinon null.
     *
     * @details
     * - Interroge la collection MongoDB "User" avec le filtre sur "Email".
     * - Selon le champ "Role" dans la base, instancie un Patient ou un Médecin.
     * - Pour un Médecin, parse les horaires de consultation au format JSON en Map.
     */
    public static Utilisateurs UtilisateurFromEmail(String email) {
        Document doc = users.find(Filters.eq("Email", email)).first();

        if (doc == null) {
            return null;
        }

        String role = doc.getString("Role");

        if ("Patient".equalsIgnoreCase(role)) {
            return new Patient(
                    doc.getObjectId("_id"),
                    doc.getString("Prénom"),
                    doc.getString("Nom"),
                    doc.getString("Email"),
                    doc.getString("Password"),
                    doc.getDate("Date of birth").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    doc.getString("Role")
            );
        } else if ("Médecin".equalsIgnoreCase(role)) {
            String jsonHoraires = doc.getString("horairesConsultation");
            Map<String, List<ConsultationService>> horaires = parseHoraires(jsonHoraires);

            return new Medecin(
                    doc.getObjectId("_id"),
                    doc.getString("Prénom"),
                    doc.getString("Nom"),
                    doc.getString("Email"),
                    doc.getString("Password"),
                    doc.getDate("Date of birth").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    doc.getString("Role"),
                    horaires
            );
        }
        return null;
    }

    /**
     * @brief Récupère un utilisateur à partir de son nom.
     *
     * @param nom Le nom de l'utilisateur recherché.
     * @return Une instance de {@link Patient} ou {@link Medecin} si trouvée, sinon null.
     *
     * @details
     * Fonctionne de façon similaire à {@link #UtilisateurFromEmail(String)} mais filtre sur le nom.
     */
    public static Utilisateurs UtilisateurFromNom(String nom) {
        Document doc = users.find(Filters.eq("Nom", nom)).first();

        if (doc == null) {
            return null;
        }

        String role = doc.getString("Role");

        if ("Patient".equalsIgnoreCase(role)) {
            return new Patient(
                    doc.getObjectId("_id"),
                    doc.getString("Prénom"),
                    doc.getString("Nom"),
                    doc.getString("Email"),
                    doc.getString("Password"),
                    doc.getDate("Date of birth").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    doc.getString("Role")
            );
        } else if ("Médecin".equalsIgnoreCase(role)) {
            String jsonHoraires = doc.getString("horairesConsultation");
            Map<String, List<ConsultationService>> horaires = parseHoraires(jsonHoraires);

            return new Medecin(
                    doc.getObjectId("_id"),
                    doc.getString("Prénom"),
                    doc.getString("Nom"),
                    doc.getString("Email"),
                    doc.getString("Password"),
                    doc.getDate("Date of birth").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    doc.getString("Role"),
                    horaires
            );
        }
        return null;
    }
}

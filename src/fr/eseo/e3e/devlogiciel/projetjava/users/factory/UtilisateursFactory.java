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


public class UtilisateursFactory {
    private static MongoCollection<Document> users = DatabaseConnection.getDatabase().getCollection("User");

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

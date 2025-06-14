package fr.eseo.e3e.devlogiciel.projetjava.users.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.RDV;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.service.CréneauxGenerator;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.users.factory.UtilisateursFactory;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PatientService {

    public static List<RDV> getRdvPatient(String emailPatient) {
        List<RDV> rdvs = new ArrayList<>();
        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");

        for (Document doc : rdvCollection.find(Filters.eq("patientEmail", emailPatient))) {
            Patient patient = (Patient) UtilisateursFactory.UtilisateurFromEmail(emailPatient);
            Medecin medecin = (Medecin) UtilisateursFactory.UtilisateurFromEmail(doc.getString("medecinEmail"));

            if (doc.getString("date") == null || doc.getString("debut") == null || doc.getString("fin") == null) {
                System.err.println("Un RDV contient des données manquantes et sera ignoré : " + doc);
                continue;
            }

            try {
                RDV rdv = new RDV(
                        doc.getObjectId("_id"),
                        patient,
                        medecin,
                        LocalDate.parse(doc.getString("date")),
                        LocalTime.parse(doc.getString("heureDebut")),
                        LocalTime.parse(doc.getString("heureFin")),
                        doc.getString("jour"),
                        doc.getString("type")
                );
                rdv.setType(doc.getString("type"));

                rdvs.add(rdv);
            } catch (Exception e) {
                System.err.println("Erreur lors de la conversion des données du document RDV : " + doc);
                e.printStackTrace(); // Pour faciliter le débogage
            }
        }
        return rdvs;
    }

    public static void afficherCreneauxPourMedecin(String emailMedecin) {
        Medecin medecin = (Medecin) UtilisateursFactory.UtilisateurFromEmail(emailMedecin);
        if (medecin == null) {
            System.out.println("Médecin introuvable");
            return;
        }

        List<String> creneaux = CréneauxGenerator.genererCreneauxPourMedecin(medecin, 30);
        for (String creneau : creneaux) {
            System.out.println(creneau);
        }
    }
}

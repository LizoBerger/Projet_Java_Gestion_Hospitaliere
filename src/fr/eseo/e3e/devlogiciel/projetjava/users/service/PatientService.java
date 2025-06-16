package fr.eseo.e3e.devlogiciel.projetjava.users.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.RDV;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.users.factory.UtilisateursFactory;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @class PatientService
 * @brief Service gérant les opérations liées aux patients.
 *
 * Cette classe fournit notamment une méthode pour récupérer la liste
 * des rendez-vous d'un patient depuis la base de données MongoDB.
 */
public class PatientService {

    /**
     * @brief Récupère la liste des rendez-vous d'un patient donné.
     *
     * @param emailPatient L'adresse email du patient.
     * @return Une liste d'objets RDV correspondant aux rendez-vous du patient.
     *
     * Cette méthode interroge la collection "RDV" dans la base MongoDB,
     * filtre les documents correspondant au patient par email,
     * convertit les documents en objets RDV, et gère les données manquantes
     * ou les erreurs lors de la conversion.
     */
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
                        LocalTime.parse(doc.getString("debut")),
                        LocalTime.parse(doc.getString("fin")),
                        doc.getString("jour"),
                        doc.getString("type"),
                        doc.getString("traitementPrescrit"),
                        doc.getString("bilan")
                );

                rdv.setType(doc.getString("type"));
                rdv.setTraitementPrescrit(doc.getString("traitementPrescrit"));
                rdv.setBilan(doc.getString("bilan"));

                rdvs.add(rdv);
            } catch (Exception e) {
                System.err.println("Erreur lors de la conversion des données du document RDV : " + doc);
                e.printStackTrace();
            }
        }
        return rdvs;
    }
}

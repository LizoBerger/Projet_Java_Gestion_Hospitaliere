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

public class PatientService {

    public static List<RDV> getRdvPatient(String emailPatient) {
        List<RDV> rdvs = new ArrayList<>();
        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");

        for (Document doc : rdvCollection.find(Filters.eq("patientEmail", emailPatient))) {
            Patient patient = (Patient) UtilisateursFactory.UtilisateurFromEmail(emailPatient);
            Medecin medecin = (Medecin) UtilisateursFactory.UtilisateurFromEmail(doc.getString("medecinEmail"));

            RDV rdv = new RDV(
                    doc.getObjectId("_id"),
                    patient,
                    medecin,
                    LocalDate.parse(doc.getString("date")),
                    LocalTime.parse(doc.getString("heure")),
                    doc.getString("type")
            );
            rdv.setType(doc.getString("type"));

            rdvs.add(rdv);
        }




        return rdvs;
    }
}

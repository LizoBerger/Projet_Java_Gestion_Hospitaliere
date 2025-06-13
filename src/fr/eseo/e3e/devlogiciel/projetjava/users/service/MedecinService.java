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

public class MedecinService {
    public List<RDV> getRdvMedecin(String emailMedecin) {
        List<RDV> rdvs = new ArrayList<>();
        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");

        for (Document doc : rdvCollection.find(Filters.eq("medecinEmail", emailMedecin))) {
            Patient patient = (Patient) UtilisateursFactory.UtilisateurFromEmail(doc.getString("patientEmail"));
            Medecin medecin = (Medecin) UtilisateursFactory.UtilisateurFromEmail(emailMedecin);

            RDV rdv = new RDV(
                    doc.getObjectId("_id"),
                    patient,
                    medecin,
                    LocalDate.parse(doc.getString("date")),
                    LocalTime.parse(doc.getString("heure")),
                    doc.getString("type")
            );

            rdv.setType(doc.getString("type"));
            rdv.setFeedback(doc.getString("feedback"));
            rdv.setTraitementPrescrit(doc.getString("traitementPrescrit"));

            rdvs.add(rdv);
        }
        return rdvs;
    }
}

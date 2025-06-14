package fr.eseo.e3e.devlogiciel.projetjava.users.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.RDV;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.users.factory.UtilisateursFactory;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MedecinService {
    public static List<RDV> getRdvMedecin(String emailMedecin, LocalDate date) {
        List<RDV> rdvs = new ArrayList<>();
        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");

        for (Document doc : rdvCollection.find(Filters.and(
                Filters.eq("medecinEmail", emailMedecin),
                Filters.eq("date", date.toString())
        ))) {
            // Récupération des informations nécessaires
            String heureDebutStr = doc.getString("debut");
            String heureFinStr = doc.getString("fin");

            // Vérification de la validité des données
            if (heureDebutStr == null || heureFinStr == null) {
                System.err.println("Erreur : RDV invalide détecté pour le médecin " + emailMedecin +
                        " à la date " + date + ". Données incomplètes dans la base.");
                continue; // Ignore ce document
            }

            try {
                Patient patient = (Patient) UtilisateursFactory.UtilisateurFromEmail(doc.getString("patientEmail"));
                Medecin medecin = (Medecin) UtilisateursFactory.UtilisateurFromEmail(emailMedecin);

                RDV rdv = new RDV(
                        doc.getObjectId("_id"),
                        patient,
                        medecin,
                        LocalDate.parse(doc.getString("date")),
                        LocalTime.parse(heureDebutStr), // Analyse sécurisée
                        LocalTime.parse(heureFinStr),  // Analyse sécurisée
                        doc.getString("jour"),
                        doc.getString("type")
                );
                rdv.setType(doc.getString("type"));

                rdvs.add(rdv);
            } catch (Exception e) {
                System.err.println("Erreur lors de la création d'un RDV : " + e.getMessage());
            }
        }

        return rdvs;
    }


    public static void addBilan(ObjectId rdvId, String bilan) {
        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        rdvCollection.updateOne(
                Filters.eq("_id", rdvId),
                new Document("$set", new Document("bilan", bilan))
        );
    }

    public static void afficherHorairesDisponibles(Medecin medecin, LocalDate dateChoisie) {
        System.out.println("Horaires disponibles pour Dr. " + medecin.getNom() + " le " + dateChoisie + " :");

        List<LocalTime> tousLesHoraires = List.of(
                LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0),
                LocalTime.of(12, 0), LocalTime.of(14, 0), LocalTime.of(15, 0),
                LocalTime.of(16, 0), LocalTime.of(17, 0)
        );

        List<RDV> rdvsMedecinCeJour = MedecinService.getRdvMedecin(medecin.getEmail(), dateChoisie);

        List<LocalTime> horairesDispos = tousLesHoraires.stream()
                .filter(heure -> rdvsMedecinCeJour.stream().noneMatch(r -> r.getHeureDebut().equals(heure)))
                .toList();

        if (horairesDispos.isEmpty()) {
            System.out.println("Aucune disponibilité pour ce jour.");
        } else {
            for (LocalTime heure : horairesDispos) {
                System.out.println("- " + heure);
            }
        }
    }


}
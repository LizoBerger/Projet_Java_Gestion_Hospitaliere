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

/**
 * @class MedecinService
 * @brief Service fournissant les fonctionnalités liées aux médecins.
 *
 * Cette classe permet d'accéder aux rendez-vous d'un médecin,
 * d'ajouter un bilan à un rendez-vous, et d'afficher les créneaux horaires disponibles.
 */
public class MedecinService {

    /**
     * @brief Récupère la liste des rendez-vous d'un médecin pour une date donnée.
     *
     * Cette méthode interroge la base MongoDB, filtre les rendez-vous
     * par email du médecin et date, puis crée les objets RDV correspondants.
     * Elle gère également les cas d'incohérences dans les données.
     *
     * @param emailMedecin L'adresse email du médecin.
     * @param date La date pour laquelle récupérer les rendez-vous.
     * @return La liste des rendez-vous pour ce médecin à cette date.
     */
    public static List<RDV> getRdvMedecin(String emailMedecin, LocalDate date) {
        List<RDV> rdvs = new ArrayList<>();
        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");

        for (Document doc : rdvCollection.find(Filters.and(
                Filters.eq("medecinEmail", emailMedecin),
                Filters.eq("date", date.toString())
        ))) {
            String heureDebutStr = doc.getString("debut");
            String heureFinStr = doc.getString("fin");

            if (heureDebutStr == null || heureFinStr == null) {
                System.err.println("Erreur : RDV invalide détecté pour le médecin " + emailMedecin +
                        " à la date " + date + ". Données incomplètes dans la base.");
                continue;
            }

            try {
                Patient patient = (Patient) UtilisateursFactory.UtilisateurFromEmail(doc.getString("patientEmail"));
                Medecin medecin = (Medecin) UtilisateursFactory.UtilisateurFromEmail(emailMedecin);

                if (patient == null) {
                    System.err.println("Le patient avec email " + doc.getString("patientEmail") + " n'existe plus. Suppression du RDV...");
                    RDV.dropRdv(doc.getObjectId("_id"));
                    continue;
                }

                if (medecin == null) {
                    System.err.println("Le médecin avec email " + emailMedecin + " n'existe pas !");
                    continue;
                }

                RDV rdv = new RDV(
                        doc.getObjectId("_id"),
                        patient,
                        medecin,
                        LocalDate.parse(doc.getString("date")),
                        LocalTime.parse(heureDebutStr),
                        LocalTime.parse(heureFinStr),
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
                System.err.println("Erreur lors de la création d'un RDV : " + e.getMessage());
            }
        }

        return rdvs;
    }

    /**
     * @brief Met à jour le bilan d'un rendez-vous identifié par son ID.
     *
     * @param rdvId L'identifiant MongoDB du rendez-vous.
     * @param bilan Le texte du bilan à enregistrer.
     */
    public static void addBilan(ObjectId rdvId, String bilan) {
        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        rdvCollection.updateOne(
                Filters.eq("_id", rdvId),
                new Document("$set", new Document("bilan", bilan))
        );
    }

    /**
     * @brief Affiche dans la console les horaires disponibles d'un médecin à une date donnée.
     *
     * Cette méthode calcule les créneaux horaires standards non occupés par des rendez-vous.
     *
     * @param medecin L'objet Médecin concerné.
     * @param dateChoisie La date pour laquelle afficher les disponibilités.
     */
    public static void afficherHorairesDisponibles(Medecin medecin, LocalDate dateChoisie) {
        System.out.println("Horaires disponibles pour Dr. " + medecin.getNom() + " le " + dateChoisie + " :");

        List<LocalTime> tousLesHoraires = List.of(
                LocalTime.of(9, 0), LocalTime.of(9, 30), LocalTime.of(10, 0),
                LocalTime.of(10, 30), LocalTime.of(11, 0), LocalTime.of(11, 30),
                LocalTime.of(13, 0), LocalTime.of(13, 30), LocalTime.of(14, 0),
                LocalTime.of(14, 30), LocalTime.of(15, 0), LocalTime.of(15, 30),
                LocalTime.of(16, 0), LocalTime.of(16, 30)
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

package fr.eseo.e3e.devlogiciel.projetjava.consultation.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.users.factory.UtilisateursFactory;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;
import fr.eseo.e3e.devlogiciel.projetjava.users.service.MedecinService;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Classe représentant la gestion des urgences médicales.
 * Elle gère la prise de rendez-vous d'urgence en fonction des symptômes,
 * la liste des médecins disponibles, et les horaires libres.
 */
public class Urgences {

    /** Email du patient demandant une urgence */
    private String patientEmail;

    /** Email du médecin attribué à l'urgence */
    private String medecinEmail;

    /** Date du rendez-vous d'urgence */
    private LocalDate date;

    /** Heure de début du rendez-vous */
    private LocalTime debut;

    /** Heure de fin du rendez-vous */
    private LocalTime fin;

    /** Symptôme du patient justifiant l'urgence */
    private Symptomes symptome;

    /**
     * Constructeur de la classe Urgences.
     *
     * @param patientEmail Email du patient
     * @param medecinEmail Email du médecin
     * @param date Date du rendez-vous
     * @param debut Heure de début du rendez-vous
     * @param fin Heure de fin du rendez-vous
     * @param symptome Symptôme du patient
     */
    public Urgences(String patientEmail, String medecinEmail, LocalDate date, LocalTime debut, LocalTime fin, Symptomes symptome) {
        this.patientEmail = patientEmail;
        this.medecinEmail = medecinEmail;
        this.date = date;
        this.debut = debut;
        this.fin = fin;
        this.symptome = symptome;
    }

    // Getters

    /**
     * Retourne l'email du patient.
     *
     * @return Email du patient
     */
    public String getPatientEmail() {
        return patientEmail;
    }

    /**
     * Retourne l'email du médecin.
     *
     * @return Email du médecin
     */
    public String getMedecinEmail() {
        return medecinEmail;
    }

    /**
     * Retourne la date du rendez-vous.
     *
     * @return Date du rendez-vous
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Retourne l'heure de début du rendez-vous.
     *
     * @return Heure de début
     */
    public LocalTime getDebut() {
        return debut;
    }

    /**
     * Retourne l'heure de fin du rendez-vous.
     *
     * @return Heure de fin
     */
    public LocalTime getFin() {
        return fin;
    }

    /**
     * Demande à l'utilisateur de choisir un symptôme via la console.
     *
     * @param scanner Scanner utilisé pour la saisie utilisateur
     * @return Symptomes choisi par l'utilisateur
     */
    public static Symptomes demanderSymptome(Scanner scanner) {
        Symptomes[] symptomes = Symptomes.values();

        System.out.println("Veuillez choisir un symptôme parmi la liste suivante :");
        for (int i = 0; i < symptomes.length; i++) {
            System.out.println((i + 1) + ". " + symptomes[i].name() + " (Priorité : " + symptomes[i].getPriorite() + ")");
        }

        int choix = -1;
        while (choix < 1 || choix > symptomes.length) {
            System.out.print("Entrez le numéro du symptôme : ");
            if (scanner.hasNextInt()) {
                choix = scanner.nextInt();
                scanner.nextLine();
                if (choix < 1 || choix > symptomes.length) {
                    System.out.println("Choix invalide, veuillez réessayer.");
                }
            } else {
                System.out.println("Entrée invalide, veuillez entrer un nombre.");
                scanner.nextLine();
            }
        }

        return symptomes[choix - 1];
    }

    /**
     * Récupère la liste de tous les médecins enregistrés dans la base de données.
     *
     * @return Liste des médecins
     */
    public static List<Medecin> getAllMedecins() {
        List<Medecin> medecins = new ArrayList<>();
        MongoCollection<Document> usersCollection = DatabaseConnection.getDatabase().getCollection("User");
        try (MongoCursor<Document> cursor = usersCollection.find(Filters.eq("Role", "Médecin")).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Medecin medecin = (Medecin) UtilisateursFactory.UtilisateurFromEmail(doc.getString("Email"));
                if (medecin != null) {
                    medecins.add(medecin);
                }
            }
        }
        return medecins;
    }

    /**
     * Prend un rendez-vous d'urgence pour un patient en fonction des créneaux disponibles des médecins
     * et du symptôme déclaré.
     * Cherche les premiers créneaux libres dans les 7 prochains jours et crée un RDV d'urgence.
     *
     * @param patient Patient demandant un rendez-vous d'urgence
     * @param symptome Symptôme du patient
     * @return RDV d'urgence pris, ou null si aucun créneau disponible
     */
    public static RDV prendreUrgence(Patient patient, Symptomes symptome) {
        LocalDate aujourdHui = LocalDate.now();
        List<Medecin> medecins = getAllMedecins();

        List<LocalTime> tousLesHoraires = List.of(
                LocalTime.of(9, 0), LocalTime.of(9, 30), LocalTime.of(10, 0),
                LocalTime.of(10, 30), LocalTime.of(11, 0), LocalTime.of(11, 30),
                LocalTime.of(13, 0), LocalTime.of(13, 30), LocalTime.of(14, 0),
                LocalTime.of(14, 30), LocalTime.of(15, 0), LocalTime.of(15, 30),
                LocalTime.of(16, 0), LocalTime.of(16, 30)
        );

        class Creneau implements Comparable<Creneau> {
            Medecin medecin;
            LocalDate date;
            LocalTime heure;

            public Creneau(Medecin medecin, LocalDate date, LocalTime heure) {
                this.medecin = medecin;
                this.date = date;
                this.heure = heure;
            }

            @Override
            public int compareTo(Creneau o) {
                int cmpDate = this.date.compareTo(o.date);
                if (cmpDate != 0) return cmpDate;
                return this.heure.compareTo(o.heure);
            }
        }

        PriorityQueue<Creneau> creneauxLibres = new PriorityQueue<>();

        for (int joursAjoutes = 0; joursAjoutes < 7; joursAjoutes++) {
            LocalDate dateChoisie = aujourdHui.plusDays(joursAjoutes);

            for (Medecin medecin : medecins) {
                List<RDV> rdvsMedecin = MedecinService.getRdvMedecin(medecin.getEmail(), dateChoisie);
                List<LocalTime> horairesPris = rdvsMedecin.stream()
                        .map(RDV::getHeureDebut)
                        .toList();

                for (LocalTime horaire : tousLesHoraires) {
                    if (!horairesPris.contains(horaire)) {
                        creneauxLibres.add(new Creneau(medecin, dateChoisie, horaire));
                    }
                }
            }
        }

        if (creneauxLibres.isEmpty()) {
            return null;
        }

        Creneau premierCreneau = creneauxLibres.poll();
        RDV rdvUrgence = new RDV(null, patient, premierCreneau.medecin, premierCreneau.date,
                premierCreneau.heure, premierCreneau.heure.plusMinutes(30),
                premierCreneau.date.getDayOfWeek().toString(), "Urgence", null, null);

        RDV.setRdv(rdvUrgence);

        return rdvUrgence;
    }
}

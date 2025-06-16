package fr.eseo.e3e.devlogiciel.projetjava.consultation.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Représente un rendez-vous médical entre un patient et un médecin.
 * Contient les informations sur la date, l'heure, le type de rendez-vous,
 * le bilan et le traitement prescrit.
 */
public class RDV {

    /** Identifiant unique MongoDB du rendez-vous */
    private ObjectId _id;

    /** Patient concerné par le rendez-vous */
    Patient patient;

    /** Médecin concerné par le rendez-vous */
    Medecin medecin;

    /** Date du rendez-vous */
    LocalDate date;

    /** Heure de début du rendez-vous */
    LocalTime heureDebut;

    /** Heure de fin du rendez-vous */
    LocalTime heureFin;

    /** Jour du rendez-vous (ex: "lundi") */
    String jour;

    /** Type de rendez-vous (consultation, contrôle, etc.) */
    String type;

    /** Bilan médical réalisé lors du rendez-vous */
    String bilan;

    /** Traitement prescrit lors du rendez-vous */
    String traitementPrescrit;

    /**
     * Constructeur de RDV.
     *
     * @param _id Identifiant unique MongoDB
     * @param patient Patient du rendez-vous
     * @param medecin Médecin du rendez-vous
     * @param date Date du rendez-vous
     * @param heureDebut Heure de début
     * @param heureFin Heure de fin
     * @param jour Jour du rendez-vous
     * @param type Type de rendez-vous
     * @param bilan Bilan médical (initialisé vide)
     * @param traitementPrescrit Traitement prescrit (initialisé vide)
     */
    public RDV(ObjectId _id, Patient patient, Medecin medecin, LocalDate date, LocalTime heureDebut, LocalTime heureFin,
               String jour, String type, String bilan, String traitementPrescrit) {
        this._id = _id;
        this.patient = patient;
        this.medecin = medecin;
        this.date = date;
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.bilan = "";
        this.traitementPrescrit = "";
        this.type = type;
    }

    /**
     * @return L'identifiant unique MongoDB du rendez-vous.
     */
    public ObjectId getId() {
        return _id;
    }

    /**
     * @return Le patient associé au rendez-vous.
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * @return Le médecin associé au rendez-vous.
     */
    public Medecin getMedecin() {
        return medecin;
    }

    /**
     * @return La date du rendez-vous.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return L'heure de début du rendez-vous.
     */
    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    /**
     * @return L'heure de fin du rendez-vous.
     */
    public LocalTime getHeureFin() {
        return heureFin;
    }

    /**
     * @return Le jour du rendez-vous (ex: "lundi").
     */
    public String getJour() {
        return jour;
    }

    /**
     * @return Le type de rendez-vous (consultation, contrôle, etc.).
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return Le bilan médical du rendez-vous.
     */
    public String getBilan() {
        return this.bilan;
    }

    /**
     * @return Le traitement prescrit lors du rendez-vous.
     */
    public String getTraitementPrescrit() {
        return this.traitementPrescrit;
    }

    /**
     * Définit l'identifiant unique MongoDB du rendez-vous.
     * @param id Nouvel identifiant
     */
    public void setId(ObjectId id) {
        this._id = id;
    }

    /**
     * Définit le patient du rendez-vous.
     * @param patient Nouveau patient
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * Définit le médecin du rendez-vous.
     * @param medecin Nouveau médecin
     */
    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    /**
     * Définit la date du rendez-vous.
     * @param date Nouvelle date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Définit l'heure de début du rendez-vous.
     * @param heure Heure de début
     */
    public void setHeureDebut(LocalTime heure) {
        this.heureDebut = heure;
    }

    /**
     * Définit l'heure de fin du rendez-vous.
     * @param heure Heure de fin
     */
    public void setHeureFin(LocalTime heure) {
        this.heureFin = heure;
    }

    /**
     * Définit le jour du rendez-vous.
     * @param jour Nouveau jour
     */
    public void setJour(String jour) {
        this.jour = jour;
    }

    /**
     * Définit le traitement prescrit lors du rendez-vous.
     * @param traitementPrescrit Nouveau traitement
     */
    public void setTraitementPrescrit(String traitementPrescrit) {
        this.traitementPrescrit = traitementPrescrit;
    }

    /**
     * Définit le bilan médical du rendez-vous.
     * @param bilan Nouveau bilan
     */
    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    /**
     * Définit le type de rendez-vous.
     * @param type Nouveau type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Représentation textuelle d'un rendez-vous.
     * @return Une chaîne décrivant le rendez-vous.
     */
    @Override
    public String toString() {
        return "RDV{" +
                "patient=" + patient.getPrenom() + " " + patient.getNom() +
                ", medecin=" + medecin.getPrenom() + " " + medecin.getNom() +
                ", date=" + date +
                ", jour=" + jour +
                ", debut=" + heureDebut +
                ", fin=" + heureFin +
                ", type=" + type +
                ", bilan=" + bilan +
                ", traitementPrescrit=" + traitementPrescrit +
                "}";
    }

    /**
     * Enregistre un rendez-vous dans la base de données MongoDB.
     *
     * @param rdv Rendez-vous à insérer
     */
    public static void setRdv(RDV rdv) {
        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        ObjectId id = new ObjectId();
        Document doc = new Document("_id", id)
                .append("patientEmail", rdv.getPatient().getEmail())
                .append("medecinEmail", rdv.getMedecin().getEmail())
                .append("date", rdv.getDate().toString())
                .append("jour", rdv.getJour())
                .append("debut", rdv.getHeureDebut().toString())
                .append("fin", rdv.getHeureFin().toString())
                .append("type", rdv.getType())
                .append("bilan", rdv.getBilan())
                .append("traitementPrescrit", rdv.getTraitementPrescrit());

        rdvCollection.insertOne(doc);
        rdv.setId(id);
    }

    /**
     * Supprime un rendez-vous de la base de données MongoDB à partir de son identifiant.
     *
     * @param id Identifiant du rendez-vous à supprimer
     */
    public static void dropRdv(ObjectId id) {
        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        rdvCollection.deleteOne(Filters.eq("_id", id));
        System.out.println("Rendez-vous annulé.");
    }

    /**
     * Met à jour le traitement prescrit d'un rendez-vous dans la base de données.
     *
     * @param idRdv Identifiant du rendez-vous
     * @param traitement Nouveau traitement prescrit
     */
    public static void mettreAJourTraitement(ObjectId idRdv, TraitementPrescrit traitement) {
        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        rdvCollection.updateOne(
                Filters.eq("_id", idRdv),
                new Document("$set", new Document("traitementPrescrit", traitement.name()))
        );
    }
}

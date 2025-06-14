package fr.eseo.e3e.devlogiciel.projetjava.consultation.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.users.factory.UtilisateursFactory;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalTime;


public class RDV {
    private ObjectId _id;
    Patient patient;
    Medecin medecin;
    LocalDate date;
    LocalTime heureDebut;
    LocalTime heureFin;
    String jour;
    String type;
    String bilan;
    String traitementPrescrit;


    public RDV(ObjectId _id, Patient patient, Medecin medecin, LocalDate date, LocalTime heureDebut,LocalTime heureFin, String jour, String type) {
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

    public ObjectId getId() {
        return _id;
    }
    public Patient getPatient() {
        return patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }
    public String getJour() {
        return jour;
    }

    public String getType() {
        return this.type;
    }

    public String getBilan() {
        return this.bilan;
    }

    public String getTraitementPrescrit() {
        return this.traitementPrescrit;
    }

    public void setId(ObjectId id) {
        this._id = id;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setHeureDebut(LocalTime heure) {
        this.heureDebut = heureDebut;
    }
    public void setHeureFin(LocalTime heure) {
        this.heureFin = heure;
    }
    public void setJour(String jour) {
        this.jour = jour;
    }
    public void setTraitementPrescrit(String traitementPrescrit) {
        this.traitementPrescrit = traitementPrescrit;
    }

    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    public void setType(String type) {
        this.type = type;
    }




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


    public static void setRdv(RDV rdv) {
        MongoCollection<org.bson.Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
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

    public static void dropRdv(ObjectId id) {
        MongoCollection<org.bson.Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        rdvCollection.deleteOne(Filters.eq("_id", id));
        System.out.println("Rendez-vous annulé.");
    }


}

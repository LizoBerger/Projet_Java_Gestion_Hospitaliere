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
    LocalTime heure;
    String type;
    String feedback;
    String traitementPrescrit;


    public RDV(ObjectId _id, Patient patient, Medecin medecin, LocalDate date, LocalTime heure, String type) {
        this._id = _id;
        this.patient = patient;
        this.medecin = medecin;
        this.date = date;
        this.heure = heure;
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

    public LocalTime getHeure() {
        return heure;
    }

    public String getType() {
        return this.type;
    }

    public String getFeedback() {
        return this.feedback;
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
    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }
    public void setTraitementPrescrit(String traitementPrescrit) {
        this.traitementPrescrit = traitementPrescrit;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
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
                ", heure=" + heure +
                "}";
    }


    public static void setRdv(RDV rdv) {
        MongoCollection<org.bson.Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        ObjectId id = new ObjectId();
        Document doc = new Document("_id", id)
                .append("patientEmail", rdv.getPatient().getEmail())
                .append("medecinEmail", rdv.getMedecin().getEmail())
                .append("date", rdv.getDate().toString())
                .append("heure", rdv.getHeure().toString())
                .append("type", rdv.getType())
                .append("feedback", rdv.getFeedback())
                .append("traitementPrescrit", rdv.getTraitementPrescrit());

        rdvCollection.insertOne(doc);
        rdv.setId(id);
    }

    public static void dropRdv(ObjectId id) {
        MongoCollection<org.bson.Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        rdvCollection.deleteOne(Filters.eq("_id", id));
        System.out.println("Rendez-vous annulé.");
    }

    public static List<RDV> getRDVinBD(String emailPatient) {
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
            rdv.setFeedback(doc.getString("feedback"));
            rdv.setTraitementPrescrit(doc.getString("traitementPrescrit"));

            rdvs.add(rdv);
        }


        return rdvs;
    }

}

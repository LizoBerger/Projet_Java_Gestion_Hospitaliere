package fr.eseo.e3e.devlogiciel.projetjava.users.model;

import fr.eseo.e3e.devlogiciel.projetjava.consultation.service.ConsultationService;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Medecin extends Utilisateurs {
    ObjectId id;
    String prenom;
    String nom;
    LocalDate dateOfBirth;
    String email;
    String password;
    String role;
    private Map<String, List<ConsultationService>> horairesConsultation;


    public Medecin(ObjectId id, String prenom, String nom, String email, String password, LocalDate dateOfBirth, String role, Map<String, List<ConsultationService>> horairesConsultation) {
        super(id, nom, prenom, email, password);
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.horairesConsultation = horairesConsultation;
    }

    public String getEmail() {
        return email;
    }

    public String getMdp() {
        return password;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getAge() {
        return (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
    }

    public ObjectId getId() {
        return id;
    }

    public String getRole() {return "Médecin";}

    public Map<String, List<ConsultationService>> getHorairesConsultation() {
        return horairesConsultation;
    }

}

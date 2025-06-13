package fr.eseo.e3e.devlogiciel.projetjava.users.model;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Patient extends Utilisateurs {
    ObjectId id;
    String prenom;
    String nom;
    LocalDate dateOfBirth;
    String email;
    String password;
    String role;

    public Patient(ObjectId id, String prenom, String nom, String email, String password, LocalDate dateOfBirth, String role) {
        super(id, nom, prenom, email, password);
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
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

    @Override
    public String getRole() {return "Patient";}


}

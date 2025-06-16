package fr.eseo.e3e.devlogiciel.projetjava.users.model;

import org.bson.types.ObjectId;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @class Patient
 * @brief Modèle représentant un patient, héritant de la classe {@link Utilisateurs}.
 *
 * Cette classe encapsule les informations spécifiques à un patient.
 */
public class Patient extends Utilisateurs {

    ObjectId id;
    String prenom;
    String nom;
    LocalDate dateOfBirth;
    String email;
    String password;
    String role;

    /**
     * @brief Constructeur complet du patient.
     *
     * @param id Identifiant MongoDB unique
     * @param prenom Prénom
     * @param nom Nom
     * @param email Adresse email
     * @param password Mot de passe
     * @param dateOfBirth Date de naissance
     * @param role Rôle (devrait être "Patient")
     */
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

    /**
     * @return L'adresse email du patient.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Le mot de passe du patient.
     */
    public String getMdp() {
        return password;
    }

    /**
     * @return Le nom de famille du patient.
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return Le prénom du patient.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return L'âge du patient calculé à partir de la date de naissance.
     */
    public int getAge() {
        return (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
    }

    /**
     * @return L'identifiant unique MongoDB.
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * @return Le rôle sous forme de chaîne. Toujours "Patient".
     */
    @Override
    public String getRole() {
        return "Patient";
    }
}

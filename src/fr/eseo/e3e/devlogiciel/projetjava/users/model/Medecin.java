package fr.eseo.e3e.devlogiciel.projetjava.users.model;

import fr.eseo.e3e.devlogiciel.projetjava.consultation.service.ConsultationService;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * @class Medecin
 * @brief Modèle représentant un médecin, héritant de la classe {@link Utilisateurs}.
 *
 * Cette classe encapsule les informations spécifiques à un médecin,
 * notamment ses horaires de consultation.
 */
public class Medecin extends Utilisateurs {

    ObjectId id;
    String prenom;
    String nom;
    LocalDate dateOfBirth;
    String email;
    String password;
    String role;

    /** Horaires de consultation, mappés par jour vers une liste de créneaux */
    private Map<String, List<ConsultationService>> horairesConsultation;

    /**
     * @brief Constructeur complet du médecin.
     *
     * @param id Identifiant MongoDB unique
     * @param prenom Prénom
     * @param nom Nom
     * @param email Adresse email
     * @param password Mot de passe
     * @param dateOfBirth Date de naissance
     * @param role Rôle (devrait être "Médecin")
     * @param horairesConsultation Horaires de consultation
     */
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

    /**
     * @return L'adresse email du médecin.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Le mot de passe du médecin.
     */
    public String getMdp() {
        return password;
    }

    /**
     * @return Le nom de famille du médecin.
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return Le prénom du médecin.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return L'âge du médecin calculé à partir de la date de naissance.
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
     * @return Le rôle sous forme de chaîne. Toujours "Médecin".
     */
    public String getRole() {
        return "Médecin";
    }

    /**
     * @return Les horaires de consultation sous forme d'un mapping
     *         jour -> liste de créneaux {@link ConsultationService}.
     */
    public Map<String, List<ConsultationService>> getHorairesConsultation() {
        return horairesConsultation;
    }
}

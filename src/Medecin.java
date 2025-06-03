import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Medecin {
    int id;
    String prenom;
    String nom;
    LocalDate dateOfBirth;
    String email;
    String password;

    Medecin(int id, String prenom, String nom, String email, String password, LocalDate dateOfBirth) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
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

    public int getId() {
        return id;
    }
}

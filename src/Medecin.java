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

    public static List<Medecin> listeMedecins = new ArrayList<>(List.of(
            new Medecin(1, "Laurent", "Houtan", "laurent.houtan@dr-java.com", "Laurent1!", LocalDate.of(1970, 12, 25)),
            new Medecin(2, "Guy", "Tariste", "guy.tariste@dr-java.com", "Guy2$", LocalDate.of(1995, 2, 3)),
            new Medecin(3, "Jean", "Bon", "jean.bon@dr-java.com", "Jean3#", LocalDate.of(1980, 3, 5)),
            new Medecin(4, "Sarah", "Croche", "sarah.croche@dr-java.com", "Sarah4%", LocalDate.of(1965, 1, 21)),
            new Medecin(5, "Tom", "Pouce", "tom.pouce@dr-java.com", "Tom5&", LocalDate.of(1987, 5, 30))
    ));

}

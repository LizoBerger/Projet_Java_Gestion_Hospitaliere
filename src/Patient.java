import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    int id;
    String prenom;
    String nom;
    LocalDate dateOfBirth;
    String email;
    String password;

    Patient(int id, String prenom, String nom, String email, String password, LocalDate dateOfBirth) {
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
    public static List<Patient> listePatients = new ArrayList<>(List.of(
            new Patient(6, "Alain", "Therieur", "alain.therieur@gmail.com", "Alain6!", LocalDate.of(1990, 2, 2)),
            new Patient(7, "Anna", "Condas", "anna.condas@laposte.fr", "Anna7$", LocalDate.of(1986, 4, 1)),
            new Patient(8, "Luc", "Idaire", "luc.idaire@yahoo.com", "Luc8#", LocalDate.of(1992, 11, 27)),
            new Patient(9, "Emma", "Pirie", "emma.pirie@gmail.com", "Emma9%", LocalDate.of(1963, 9, 12)),
            new Patient(10, "Justin", "Bridou", "justin.bridou@outlook.com", "Justin10&", LocalDate.of(1985, 8, 16))
    ));
}

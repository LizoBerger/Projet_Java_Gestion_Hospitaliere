import java.util.HashMap;
import java.util.Map;

public class Utilisateurs {

    int id;
    private String Nom;
    private String Prenom;
    private String email;
    private String MDP;

    public static boolean seConnecter(String email, char[] mdp) {
        String password = new String(mdp);

        // Vérifie dans la liste des médecins
        for (Medecin m : Medecin.listeMedecins) {
            if (m.email.equalsIgnoreCase(email) && m.getMotDePasse().equals(password)) {
                return true;
            }
        }

        // Vérifie dans la liste des patients
        for (Patient p : Patient.listePatients) {
            if (p.email.equalsIgnoreCase(email) && p.getMotDePasse().equals(password)) {
                return true;
            }
        }

        return false;
    }

    public static Role detecterRole(String email) {
        if (email.endsWith("@dr-java.com")){
            return Role.MEDECIN;
        }
        else{
            return Role.PATIENT;
        }

    }
}

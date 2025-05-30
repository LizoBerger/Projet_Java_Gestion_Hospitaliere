import java.io.Console;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args){
        while(true) {
            System.out.println("\n=== Bienvenue sur Dr. Java ===");
            System.out.println("\n=== Veuillez vous connecter ===");

            Console console = System.console();

            if (console == null) {
                System.out.println("Console indisponible");
                return;
            }

            System.out.println("User/mail : ");
            String user = console.readLine();


            char[] passwordArray = console.readPassword("Password : ");
            String password = new String(passwordArray);

            Utilisateurs.seConnecter(user, passwordArray);

            if (Utilisateurs.seConnecter(user, passwordArray)) {
                if (Utilisateurs.detecterRole(user) == Role.MEDECIN) {
                    System.out.println("Je suis un medecin");
                } else {
                    System.out.println("Je suis un patient");
                }
                break;
            } else {
                System.out.println("Identifiants incorrects.");
            }
        }
    }
}


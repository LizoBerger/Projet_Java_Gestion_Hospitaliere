import java.io.Console;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args){
        while(true) {
            System.out.println("\n=== Bienvenue sur Dr. Java ===");
            System.out.println("=== Veuillez vous connecter ===");

            Console console = System.console();

            if (console == null) {
                System.out.println("Console indisponible");
                return;
            }

            System.out.println("\nUser/mail : ");
            String user = console.readLine();


            char[] passwordArray = console.readPassword("Password : ");

            boolean success = Utilisateurs.seConnecter(user, passwordArray);

            if (!success) {
                System.out.println("Échec de la connexion !");
                continue;
            }

            String prenom = Utilisateurs.getPrenom(user);
            String role = Utilisateurs.detecterRole(user);

            System.out.println("\nBienvenue sur la plateforme, " + prenom + " !");
            System.out.println("Vous êtes connecté en tant que : " + role);

            break;

        }
    }
}


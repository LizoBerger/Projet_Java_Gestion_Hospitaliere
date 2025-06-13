package fr.eseo.e3e.devlogiciel.projetjava;

import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.RDV;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.users.factory.UtilisateursFactory;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Utilisateurs;
import fr.eseo.e3e.devlogiciel.projetjava.users.service.AuthService;
import fr.eseo.e3e.devlogiciel.projetjava.users.service.PatientService;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;


import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    public static void main(String[] args) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        Scanner scanner = new Scanner(System.in);
        DatabaseConnection.Connect();


        while (true) {
            System.out.println("\n=== Bienvenue sur Dr. Java ===");
            System.out.println("=== Veuillez vous connecter ===");


            System.out.print("Email : ");
            String email = scanner.nextLine();

            System.out.print("Password : ");
            String mdp = scanner.nextLine();  // mot de passe en clair

            boolean connected = AuthService.seConnecter(email, mdp.toCharArray());
            Utilisateurs utilisateur = UtilisateursFactory.UtilisateurFromEmail(email);

            if (utilisateur == null) {
                System.out.println("Utilisateur introuvable !");
                continue;
            }

            if (connected) {
                System.out.println("Connexion réussie !\n");

            } else {
                System.out.println("Email ou mot de passe incorrect.\n");
            }
            String role = utilisateur.getRole();
            String nomPrenom = Utilisateurs.getNomComplet(email);
            System.out.println("Bonjour " + nomPrenom);
            System.out.println("Vous êtes connecté(e) en tant que " + role + "\n");


            if (utilisateur instanceof Patient patient) {
                while (true) {
                    System.out.println("Veuillez choisir une action : \n");
                    System.out.println("1. Prendre RDV");
                    System.out.println("2. Annuler RDV");
                    System.out.println("3. Prendre une urgence");
                    System.out.println("4. Mes Rendez-vous");
                    int choix;
                    if (scanner.hasNextInt()) {
                        choix = scanner.nextInt();
                        scanner.nextLine();
                    } else {
                        System.out.println("Entrée invalide, veuillez entrer un nombre !");
                        scanner.next();
                        continue;
                    }

                    switch (choix) {
                        case 1:
                            System.out.println("=== Prise de rendez-vous ===");


                            System.out.println("Choisissez un médecin parmi la liste suivante :");
                            System.out.println("1 - Dr. Houtan(laurent.houtan@dr-java.com)");
                            System.out.println("2 - Dr. Tariste (guy.tariste@dr-java.com)");
                            System.out.println("3 - Dr. Bon (jean.bon@dr-java.com)");
                            System.out.println("4 - Dr. Croche (sarah.croche@dr-java.com)");
                            System.out.println("5 - Dr. Pouce (tom.pouce@dr-java.com)");


                            System.out.print("Email du médecin : ");
                            String emailMedecin = scanner.nextLine();
                            Medecin medecin = (Medecin) UtilisateursFactory.UtilisateurFromEmail(emailMedecin);
                            if (medecin == null) {
                                System.out.println("Médecin introuvable avec cet email.");
                            }

                            LocalDateTime dateHeureChoisie = demanderDateHeureRdv(scanner);
                            LocalDate dateChoisie = dateHeureChoisie.toLocalDate();
                            LocalTime heureChoisie = dateHeureChoisie.toLocalTime();

                            System.out.println("Quel est le type de consultation ?");
                            String typeConsultation = scanner.nextLine();


                            try {
                                LocalDate date = LocalDate.parse(dateChoisie.toString());
                                LocalTime heure = LocalTime.parse(heureChoisie.toString());
                                if (patient != null && medecin != null) {
                                    ObjectId id = new ObjectId();
                                    RDV rdv = new RDV(id, patient, medecin, date, heure, typeConsultation);
                                    RDV.setRdv(rdv);
                                    System.out.println("Rendez-vous ajouté : " + rdv);
                                } else {
                                    System.out.println("Erreur : Patient ou Médecin introuvable !");
                                }
                            } catch (Exception e) {
                                System.out.println("Format invalide. Veuillez réessayer.");
                            }

                            break;
                        case 2:
                            List<RDV> rdvsPatient = PatientService.getRdvPatient(patient.getEmail());

                            if (rdvsPatient.isEmpty()) {
                                System.out.println("Vous n'avez aucun rendez-vous.");
                            } else {
                                for (int i = 0; i < rdvsPatient.size(); i++) {
                                    System.out.println((i+1) + " - " + rdvsPatient.get(i));
                                }

                                System.out.println("Entrez le numéro du rendez-vous à annuler :");
                                int choix_annuler = scanner.nextInt();
                                RDV rdvToDelete = rdvsPatient.get(choix_annuler - 1);
                                ObjectId id = rdvToDelete.getId();
                                RDV.dropRdv(id);
                                System.out.println(id);
                            }
                            break;
                        case 3:
                            break;
                        case 4:
                            List<RDV> mesRdv = RDV.getRDVinBD(patient.getEmail());
                            if (mesRdv.isEmpty()) {
                                System.out.println("Vous n'avez aucun rendez-vous.");
                            } else {
                                for (int i = 0; i < mesRdv.size(); i++) {
                                    System.out.println((i + 1) + " - " + mesRdv.get(i));
                                }
                            }
                            break;

                    }
                }


            }

            if (utilisateur instanceof Medecin medecin) {
                System.out.println("Veuillez choisir une action : \n");
                System.out.println("1. Afficher le planning du jour");
                System.out.println("2. Faire un bilan sur une consultation");
                System.out.println("3. Historique des consultations d'un patient ");
            }

            scanner.close();
        }
    }

    public static LocalDateTime demanderDateHeureRdv(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Date du RDV (format AAAA-MM-JJ) : ");
                String dateStr = scanner.nextLine();
                LocalDate dateChoisie = LocalDate.parse(dateStr);

                System.out.print("Heure du RDV (format HH:MM) : ");
                String heureStr = scanner.nextLine();
                LocalTime heureChoisie = LocalTime.parse(heureStr);

                LocalDateTime dateHeureChoisie = LocalDateTime.of(dateChoisie, heureChoisie);

                if (dateHeureChoisie.isBefore(LocalDateTime.now())) {
                    System.out.println("Erreur : vous ne pouvez pas prendre un rendez-vous dans le passé ! Réessayez.");
                } else {
                    return dateHeureChoisie;
                }
            } catch (Exception e) {
                System.out.println("Format invalide. Veuillez respecter le format AAAA-MM-JJ pour la date et HH:MM pour l'heure.");
            }
        }
    }
}



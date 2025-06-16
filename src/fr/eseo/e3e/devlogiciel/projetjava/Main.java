package fr.eseo.e3e.devlogiciel.projetjava;

import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.RDV;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.Symptomes;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.TraitementPrescrit;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.Urgences;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.users.factory.UtilisateursFactory;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Utilisateurs;
import fr.eseo.e3e.devlogiciel.projetjava.users.service.AuthService;
import fr.eseo.e3e.devlogiciel.projetjava.users.service.MedecinService;
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
    /**
     * @brief Point d'entrée de l'application.
     *
     * Initialise les ressources et démarre le programme.
     *
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        Scanner scanner = new Scanner(System.in);
        DatabaseConnection.Connect();
        boolean running = true;

        while (running) {
            System.out.println("\n=== Bienvenue sur Dr. Java ===");
            System.out.println("=== Veuillez vous connecter ===");


            System.out.print("Email : ");
            String email = scanner.nextLine();

            System.out.print("Password : ");
            String mdp = scanner.nextLine();  // mot de passe en clair

            boolean connected = AuthService.seConnecter(email, mdp.toCharArray());
            Utilisateurs utilisateur = UtilisateursFactory.UtilisateurFromEmail(email);


            if (!connected || utilisateur == null) {
                System.out.println("Email ou mot de passe incorrect.\n");
                continue; // Retour à la demande de connexion
            }

            System.out.println("Connexion réussie !");

            String role = utilisateur.getRole();
            String nomPrenom = Utilisateurs.getNomComplet(email);
            System.out.println("Bonjour " + nomPrenom);
            System.out.println("Vous êtes connecté(e) en tant que " + role + "\n");

            boolean sessionActive = true;
            while (sessionActive) {
                if (utilisateur instanceof Patient patient) {
                    System.out.println("Veuillez choisir une action : \n");
                    System.out.println("1. Prendre RDV");
                    System.out.println("2. Annuler RDV");
                    System.out.println("3. Prendre une urgence");
                    System.out.println("4. Mes Rendez-vous");
                    System.out.println("5. Déconnexion");
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
                            System.out.println("1 - Dr. Durand");
                            System.out.println("2 - Dr. Bernard");
                            System.out.println("3 - Dr. Dupont");



                            System.out.print("Nom du médecin : ");
                            String nomMedecin = scanner.nextLine();
                            Medecin medecin = (Medecin) UtilisateursFactory.UtilisateurFromNom(nomMedecin);
                            if (medecin == null) {
                                System.out.println("Médecin introuvable avec ce nom.");
                                break;
                            }


                            LocalDateTime dateHeureChoisie = demanderDateHeureRdv(scanner, medecin);
                            LocalDate dateChoisie = dateHeureChoisie.toLocalDate();

                            LocalTime heureDebut = dateHeureChoisie.toLocalTime();
                            LocalTime heureFin = heureDebut.plusMinutes(30);



                            System.out.println("Quel est le type de consultation ?");
                            System.out.println("1 - Consultation générale");
                            System.out.println("2 - Consultation spécialisée");
                            System.out.println("3 - Suivi");

                            int typeChoisi = 0;
                            while (true) {
                                if (scanner.hasNextInt()) {
                                    typeChoisi = scanner.nextInt();
                                    scanner.nextLine();
                                    if (typeChoisi >= 1 && typeChoisi <= 4) {
                                        break;
                                    } else {
                                        System.out.println("Veuillez choisir un nombre entre 1 et 4.");
                                    }
                                } else {
                                    System.out.println("Entrée invalide, veuillez entrer un nombre.");
                                    scanner.nextLine();
                                }
                            }

                            String typeRdv;
                            switch (typeChoisi) {
                                case 1 -> typeRdv = "Consultation générale";
                                case 2 -> typeRdv = "Consultation spécialisée";
                                case 3 -> typeRdv = "Suivi";
                                default -> typeRdv = "Inconnu";
                            }


                            try {
                                LocalDate date = LocalDate.parse(dateChoisie.toString());
                                String jour = dateChoisie.getDayOfWeek().toString();
                                if (patient != null && medecin != null) {
                                    ObjectId id = new ObjectId();
                                    RDV rdv = new RDV(id, patient, medecin, date, heureDebut, heureFin, jour, typeRdv, null, null);
                                    rdv.setType(typeRdv);
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
                                    System.out.println((i + 1) + " - " + rdvsPatient.get(i));
                                }

                                System.out.println("Entrez le numéro du rendez-vous à annuler :");
                                int choix_annuler = scanner.nextInt();
                                RDV rdvToDelete = rdvsPatient.get(choix_annuler - 1);
                                ObjectId id = rdvToDelete.getId();
                                RDV.dropRdv(id);
                            }
                            break;
                        case 3:
                            Symptomes symptomeUrgence = Urgences.demanderSymptome(scanner);
                            System.out.println("Vous avez choisi : " + symptomeUrgence.name() + " avec priorité " + symptomeUrgence.getPriorite());

                            RDV rdvUrgence = Urgences.prendreUrgence(patient, symptomeUrgence);

                            if (rdvUrgence != null) {
                                System.out.println("RDV d'urgence pris avec succès !");
                                System.out.println("Médecin : Dr. " + rdvUrgence.getMedecin().getNom());
                                System.out.println("Date : " + rdvUrgence.getDate());
                                System.out.println("Heure : " + rdvUrgence.getHeureDebut() + " - " + rdvUrgence.getHeureFin());
                                System.out.println("Symptôme : " + symptomeUrgence.name());
                            } else {
                                System.out.println("Désolé, aucun créneau d'urgence disponible dans la semaine à venir.");
                            }
                            break;
                        case 4:
                            List<RDV> mesRdv = PatientService.getRdvPatient(patient.getEmail());
                            if (mesRdv.isEmpty()) {
                                System.out.println("Vous n'avez aucun rendez-vous.");
                            } else {
                                for (int i = 0; i < mesRdv.size(); i++) {
                                    System.out.println((i + 1) + " - " + mesRdv.get(i));
                                }
                            }
                            break;
                        case 5:
                            sessionActive = false;
                            break;
                        default:
                            System.out.println("Action invalide");
                            break;

                    }
                }




                if (utilisateur instanceof Medecin medecin) {
                    System.out.println("Veuillez choisir une action : \n");
                    System.out.println("1. Afficher le planning du jour");
                    System.out.println("2. Faire un bilan sur une consultation");
                    System.out.println("3. Historique des consultations d'un patient ");
                    System.out.println("4. Déconnexion");

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
                            LocalDate today = LocalDate.now();
                            List<RDV> rdvsDuJour = MedecinService.getRdvMedecin(medecin.getEmail(), today);

                            if (rdvsDuJour.isEmpty()) {
                                System.out.println("Vous n'avez aucun patient aujourd'hui.");
                            } else {
                                System.out.println("Planning du jour (" + today + ") :");
                                for (int i = 0; i < rdvsDuJour.size(); i++) {
                                    System.out.println((i + 1) + " - " + rdvsDuJour.get(i));
                                }
                            }
                            break;
                        case 2:
                            List<RDV> rdvs = MedecinService.getRdvMedecin(medecin.getEmail(), LocalDate.now());

                            if (rdvs.isEmpty()) {
                                System.out.println("Vous n'avez aucun rendez-vous aujourd'hui.");
                            } else {
                                for (int i = 0; i < rdvs.size(); i++) {
                                    System.out.println((i + 1) + " - " + rdvs.get(i));
                                }

                                System.out.println("Sélectionnez le rendez-vous pour ajouter un bilan :");
                                int choixRDV = scanner.nextInt();
                                scanner.nextLine();

                                RDV rdv = rdvs.get(choixRDV - 1);

                                System.out.println("Rédigez le bilan de cette consultation :");
                                String bilan = scanner.nextLine();

                                MedecinService.addBilan(rdv.getId(), bilan);
                                System.out.println("Bilan ajouté !");

                                System.out.println("Veuillez choisir un traitement prescrit :");
                                TraitementPrescrit.afficherTraitements();

                                int choixTraitement = -1;
                                while (choixTraitement < 1 || choixTraitement > TraitementPrescrit.values().length) {
                                    System.out.print("Entrez le numéro du traitement : ");
                                    if (scanner.hasNextInt()) {
                                        choixTraitement = scanner.nextInt();
                                        scanner.nextLine();
                                    } else {
                                        System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                                        scanner.nextLine();
                                    }
                                }

                                TraitementPrescrit traitementChoisi = TraitementPrescrit.fromInt(choixTraitement);
                                System.out.println("Traitement prescrit : " + traitementChoisi);

                                rdv.setTraitementPrescrit(traitementChoisi.name());

                                RDV.mettreAJourTraitement(rdv.getId(), traitementChoisi);
                            }



                            break;
                        case 3:
                            System.out.print("Entrez l'email du patient : ");
                            String emailPatient = scanner.nextLine();

                            List<RDV> rdvsPatient = PatientService.getRdvPatient(emailPatient);

                            if (rdvsPatient.isEmpty()) {
                                System.out.println("Aucun rendez-vous trouvé pour ce patient.");
                            } else {
                                System.out.println("Historique des consultations de " + Patient.getNomComplet(emailPatient) + " :");
                                for (RDV r : rdvsPatient) {
                                    System.out.println(r);
                                    if (r.getBilan() != null && !r.getBilan().isEmpty()) {
                                        System.out.println("  → Bilan : " + r.getBilan());
                                    }
                                }
                            }
                            break;
                        case 4:
                            sessionActive = false;
                            break;
                        default:
                            System.out.println("Action invalide");
                            break;
                    }
                }
            }


        }
        scanner.close();

    }

    public static LocalDateTime demanderDateHeureRdv(Scanner scanner, Medecin medecin) {
        while (true) {
            try {
                System.out.print("Date du RDV (format AAAA-MM-JJ) : ");
                String dateStr = scanner.nextLine();
                LocalDate dateChoisie = LocalDate.parse(dateStr);

                MedecinService.afficherHorairesDisponibles(medecin, dateChoisie);

                System.out.print("Heure du RDV (format HH:MM) : ");
                String heureStr = scanner.nextLine();
                LocalTime heureChoisie = LocalTime.parse(heureStr);

                LocalDateTime dateHeureChoisie = LocalDateTime.of(dateChoisie, heureChoisie);

                if (dateHeureChoisie.isBefore(LocalDateTime.now())) {
                    System.out.println("Erreur : vous ne pouvez pas prendre un rendez-vous dans le passé ! Réessayez.");
                } else {
                    return dateHeureChoisie;
                }
            } finally {
            }
        }
    }

}



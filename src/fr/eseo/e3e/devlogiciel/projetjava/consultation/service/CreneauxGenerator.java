package fr.eseo.e3e.devlogiciel.projetjava.consultation.service;

import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @class CréneauxGenerator
 * @brief Classe utilitaire pour générer des créneaux horaires à partir d'horaires de consultation.
 *
 * Cette classe propose des méthodes statiques pour générer des créneaux horaires à intervalle
 * régulier à partir d'un objet ConsultationService, ainsi que pour générer tous les créneaux
 * d'un médecin à partir de ses horaires de consultation.
 */
public class CreneauxGenerator {

    /**
     * @brief Génère une liste de créneaux horaires entre le début et la fin d'un horaire donné.
     *
     * @param horaire Objet ConsultationService définissant une plage horaire (heure de début et fin).
     * @param pasMinutes Intervalle en minutes entre chaque créneau.
     *
     * @return Liste de chaînes de caractères représentant les créneaux horaires sous la forme "HH:mm - HH:mm".
     *
     * @details
     * Parcourt la plage horaire en ajoutant des créneaux d'une durée égale à pasMinutes,
     * de l'heure de début jusqu'à l'heure de fin incluse.
     */
    public static List<String> genererCreneaux(ConsultationService horaire, int pasMinutes) {
        List<String> creneaux = new ArrayList<>();
        LocalTime courant = horaire.getDebut();

        while (courant.plusMinutes(pasMinutes).isBefore(horaire.getFin()) || courant.plusMinutes(pasMinutes).equals(horaire.getFin())) {
            LocalTime suivant = courant.plusMinutes(pasMinutes);
            creneaux.add(courant + " - " + suivant);
            courant = suivant;
        }

        return creneaux;
    }

    /**
     * @brief Génère tous les créneaux horaires possibles pour un médecin selon ses horaires de consultation.
     *
     * @param medecin Objet Medecin dont on récupère les horaires de consultation.
     * @param pasMinutes Intervalle en minutes pour chaque créneau.
     *
     * @return Liste de chaînes représentant tous les créneaux horaires générés pour le médecin.
     *
     * @details
     * Récupère la map des horaires de consultation du médecin (par jour),
     * puis génère les créneaux horaires pour chaque plage horaire enregistrée.
     */
    public static List<String> genererCreneauxPourMedecin(Medecin medecin, int pasMinutes) {
        List<String> tousCreneaux = new ArrayList<>();
        Map<String, List<ConsultationService>> horairesConsultation = medecin.getHorairesConsultation();

        for (Map.Entry<String, List<ConsultationService>> entry : horairesConsultation.entrySet()) {
            for (ConsultationService h : entry.getValue()) {
                tousCreneaux.addAll(genererCreneaux(h, pasMinutes));
            }
        }

        return tousCreneaux;
    }
}

package fr.eseo.e3e.devlogiciel.projetjava.consultation.service;

import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CréneauxGenerator {
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

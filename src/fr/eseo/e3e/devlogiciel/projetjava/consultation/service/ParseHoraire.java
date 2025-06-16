package fr.eseo.e3e.devlogiciel.projetjava.consultation.service;

import java.util.ArrayList;
import java.util.regex.*;
import java.util.*;

/**
 * @class ParseHoraire
 * @brief Classe utilitaire pour parser une chaîne JSON simplifiée en horaires de consultation.
 *
 * Cette classe fournit une méthode statique pour extraire à partir d'une chaîne JSON
 * (format simplifié, non standardisé) une map associant les jours aux plages horaires de consultation.
 */
public class ParseHoraire {

    /**
     * @brief Analyse une chaîne JSON simplifiée pour extraire les horaires de consultation.
     *
     * @param json Chaîne JSON au format simplifié, par exemple :
     *             {
     *               "Lundi": ["08:00-12:00", "14:00-18:00"],
     *               "Mardi": ["09:00-12:00"]
     *             }
     *
     * @return Map associant chaque jour (String) à une liste de ConsultationService correspondant aux plages horaires.
     *
     * @details
     * Utilise des expressions régulières pour extraire les jours et leurs plages horaires.
     * Chaque plage horaire est convertie en un objet ConsultationService via son constructeur avec chaîne.
     */
    public static Map<String, List<ConsultationService>> parseHoraires(String json) {
        Map<String, List<ConsultationService>> horaires = new HashMap<>();

        // Extrait chaque "jour": [...]
        Pattern dayPattern = Pattern.compile("\"(\\w+)\"\\s*:\\s*\\[(.*?)\\]");
        Matcher dayMatcher = dayPattern.matcher(json);

        while (dayMatcher.find()) {
            String jour = dayMatcher.group(1);
            String plagesStr = dayMatcher.group(2); // ex: "\"08:00-12:00\", \"14:00-18:00\""

            // Extrait les plages horaires entre guillemets
            Pattern plagePattern = Pattern.compile("\"(\\d{2}:\\d{2}-\\d{2}:\\d{2})\"");
            Matcher plageMatcher = plagePattern.matcher(plagesStr);

            List<ConsultationService> plages = new ArrayList<>();

            while (plageMatcher.find()) {
                String plage = plageMatcher.group(1); // ex: "08:00-12:00"
                plages.add(new ConsultationService(plage));
            }

            horaires.put(jour, plages);
        }

        return horaires;
    }
}

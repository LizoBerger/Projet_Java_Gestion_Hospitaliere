package fr.eseo.e3e.devlogiciel.projetjava.consultation.service;

import java.util.ArrayList;
import java.util.regex.*;
import java.util.*;
public class ParseHoraire {


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

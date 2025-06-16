package fr.eseo.e3e.devlogiciel.projetjava.consultation.service;

import java.time.LocalTime;
/**
 * @class ConsultationService
 * @brief Représente une plage horaire de consultation avec une heure de début et une heure de fin.
 *
 * Cette classe permet de stocker une plage horaire exprimée sous la forme "HH:mm-HH:mm".
 * Elle fournit des méthodes pour récupérer l'heure de début, l'heure de fin et afficher la plage.
 */
public class ConsultationService {
    private LocalTime debut;
    private LocalTime fin;

    /**
     * @brief Constructeur qui initialise la plage horaire à partir d'une chaîne de caractères.
     * @param plage Chaîne au format "HH:mm-HH:mm" représentant la plage horaire.
     *
     * Parse la chaîne pour extraire l'heure de début et l'heure de fin.
     */
    public ConsultationService(String plage) {
        String[] parts = plage.split("-");
        this.debut = LocalTime.parse(parts[0]);
        this.fin = LocalTime.parse(parts[1]);
    }

    /**
     * @brief Retourne l'heure de début de la plage.
     * @return Heure de début (LocalTime)
     */
    public LocalTime getDebut() { return debut; }

    /**
     * @brief Retourne l'heure de fin de la plage.
     * @return Heure de fin (LocalTime)
     */
    public LocalTime getFin() { return fin; }

    /**
     * @brief Retourne une représentation textuelle de la plage horaire.
     * @return Chaîne au format "HH:mm-HH:mm"
     */
    @Override
    public String toString() {
        return debut + "-" + fin;
    }


}

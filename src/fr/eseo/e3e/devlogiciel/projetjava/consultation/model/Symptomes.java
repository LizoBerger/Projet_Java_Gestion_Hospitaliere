package fr.eseo.e3e.devlogiciel.projetjava.consultation.model;

/**
 * Enumération représentant différents symptômes médicaux,
 * chacun associé à une priorité numérique indiquant la gravité ou l'urgence.
 */
public enum Symptomes {


    FIEVRE(2),
    TOUX(1),
    MIGRAINE(3),
    NAUSEES(3),
    DIFFICULTE_RESPIRATOIRE(5),
    DOULEUR_THORACIQUE(5),
    PERTE_CONNAISSANCE(6),
    VOMISSEMENTS(3),
    PLAIE_IMPORTANTE(4),
    HEMORRAGIE(6);

    /**
     * Niveau de priorité associé au symptôme.
     */
    private final int priorite;

    /**
     * Constructeur privé pour initialiser un symptôme avec sa priorité.
     *
     * @param priorite la priorité associée au symptôme
     */
    Symptomes(int priorite) {
        this.priorite = priorite;
    }

    /**
     * Retourne la priorité associée au symptôme.
     *
     * @return un entier représentant la priorité
     */
    public int getPriorite() {
        return priorite;
    }
}

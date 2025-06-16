package fr.eseo.e3e.devlogiciel.projetjava.consultation.model;

/**
 * Enumération représentant différents traitements médicaux prescrits.
 */
public enum TraitementPrescrit {
    PARACETAMOL,
    IBUPROFENE,
    ASPIRINE,
    AMOXICILLINE,
    CIPROFLOXACINE,
    METFORMINE,
    ATORVASTATINE,
    LISINOPRIL,
    OMEPRAZOLE;

    /**
     * Affiche la liste des traitements disponibles dans la console,
     * numérotée à partir de 1.
     */
    public static void afficherTraitements() {
        TraitementPrescrit[] traitements = values();
        for (int i = 0; i < traitements.length; i++) {
            System.out.println((i + 1) + ". " + traitements[i].name());
        }
    }

    /**
     * Retourne le traitement correspondant au numéro choisi.
     *
     * @param choix numéro du traitement (commence à 1)
     * @return le traitement correspondant, ou null si le choix est invalide
     */
    public static TraitementPrescrit choisirTraitement(int choix) {
        TraitementPrescrit[] traitements = values();
        if (choix < 1 || choix > traitements.length) {
            return null;
        }
        return traitements[choix - 1];
    }

    /**
     * Méthode équivalente à {@link #choisirTraitement(int)},
     * retourne le traitement à partir d'un entier.
     *
     * @param choix numéro du traitement (commence à 1)
     * @return le traitement correspondant, ou null si le choix est invalide
     */
    public static TraitementPrescrit fromInt(int choix) {
        if (choix < 1 || choix > values().length) return null;
        return values()[choix - 1];
    }
}

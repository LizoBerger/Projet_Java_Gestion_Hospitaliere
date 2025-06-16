package fr.eseo.e3e.devlogiciel.projetjava.consultation.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.service.ConsultationService;

public class ConsultationServiceTest {

    @Test
    void testPlageHoraireValide() {
        ConsultationService plage = new ConsultationService("09:30-17:45");

        assertEquals(LocalTime.of(9, 30), plage.getDebut(), "Heure de début incorrecte");
        assertEquals(LocalTime.of(17, 45), plage.getFin(), "Heure de fin incorrecte");
        assertEquals("09:30-17:45", plage.toString(), "Représentation texte incorrecte");
    }

    @Test
    void testPlageHoraireInvalideFormat() {
        // On peut vérifier qu'une chaîne mal formée lance une exception (DateTimeParseException)
        assertThrows(java.time.format.DateTimeParseException.class, () -> {
            new ConsultationService("9h30-17h45");
        });
    }
}
package fr.eseo.e3e.devlogiciel.projetjava.consultation.service;
import static org.junit.jupiter.api.Assertions.*;

import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.*;
public class GenererCreneauxTest {
    @Test
    void testGenererCreneauxSimple() {
        ConsultationService cs = new ConsultationService(LocalTime.of(9, 0), LocalTime.of(10, 0));
        List<String> creneaux = CreneauxGenerator.genererCreneaux(cs, 30);

        List<String> attendu = Arrays.asList("09:00 - 09:30", "09:30 - 10:00");
        assertEquals(attendu, creneaux);
    }

    @Test
    void testGenererCreneauxPasExact() {
        ConsultationService cs = new ConsultationService(LocalTime.of(9, 0), LocalTime.of(10, 0));
        List<String> creneaux = CreneauxGenerator.genererCreneaux(cs, 25);

        // Doit être 09:00-09:25, 09:25-09:50 mais pas 09:50-10:15 car dépasse 10:00
        List<String> attendu = Arrays.asList("09:00 - 09:25", "09:25 - 09:50");
        assertEquals(attendu, creneaux);
    }

    @Test
    void testGenererCreneauxVide() {
        ConsultationService cs = new ConsultationService(LocalTime.of(10, 0), LocalTime.of(9, 0));
        List<String> creneaux = CreneauxGenerator.genererCreneaux(cs, 30);
        assertTrue(creneaux.isEmpty());
    }

    @Test
    void testGenererCreneauxPourMedecin() {
        Medecin medecin = Mockito.mock(Medecin.class);

        Map<String, List<ConsultationService>> horaires = new HashMap<>();
        horaires.put("lundi", Arrays.asList(new ConsultationService(LocalTime.of(9, 0), LocalTime.of(10, 0))));
        horaires.put("mardi", Arrays.asList(new ConsultationService(LocalTime.of(14, 0), LocalTime.of(15, 0))));

        Mockito.when(medecin.getHorairesConsultation()).thenReturn(horaires);

        List<String> creneaux = CreneauxGenerator.genererCreneauxPourMedecin(medecin, 30);

        List<String> attendu = Arrays.asList("09:00 - 09:30", "09:30 - 10:00", "14:00 - 14:30", "14:30 - 15:00");
        assertEquals(attendu, creneaux);
    }

    @Test
    void testGenererCreneauxPourMedecinSansHoraires() {
        Medecin medecin = Mockito.mock(Medecin.class);
        Mockito.when(medecin.getHorairesConsultation()).thenReturn(Collections.emptyMap());

        List<String> creneaux = CreneauxGenerator.genererCreneauxPourMedecin(medecin, 30);
        assertTrue(creneaux.isEmpty());
    }
}

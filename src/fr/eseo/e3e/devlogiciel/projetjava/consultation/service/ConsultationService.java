package fr.eseo.e3e.devlogiciel.projetjava.consultation.service;

import java.time.LocalTime;

public class ConsultationService {
    private LocalTime debut;
    private LocalTime fin;

    // Constructeur avec chaînes "HH:mm-HH:mm"
    public ConsultationService(String plage) {
        // Ex : plage = "13:00-16:00"
        String[] parts = plage.split("-");
        this.debut = LocalTime.parse(parts[0]);
        this.fin = LocalTime.parse(parts[1]);
    }

    public LocalTime getDebut() { return debut; }
    public LocalTime getFin() { return fin; }

    @Override
    public String toString() {
        return debut + "-" + fin;
    }
}

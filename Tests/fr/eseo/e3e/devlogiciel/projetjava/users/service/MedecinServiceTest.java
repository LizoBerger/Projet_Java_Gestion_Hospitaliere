package fr.eseo.e3e.devlogiciel.projetjava.users.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.RDV;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.service.ConsultationService;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;
import fr.eseo.e3e.devlogiciel.projetjava.users.factory.UtilisateursFactory;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MedecinServiceTest {

    private MongoDatabase database;
    private MongoCollection<Document> rdvCollection;

    @BeforeAll
    public void setup() {

        DatabaseConnection.Connect();
        database = DatabaseConnection.getDatabase();
        rdvCollection = database.getCollection("RDV");
    }


    @BeforeEach
    public void cleanInsertData() {
        rdvCollection.deleteMany(new Document());
        ObjectId id = new ObjectId();

        MongoCollection<Document> userCollection = database.getCollection("User");
        userCollection.deleteMany(new Document());

        // Insérer un médecin
        Document medecinDoc = new Document()
                .append("Prénom", "Jean")
                .append("Nom", "Dupont")
                .append("Email", "medecin@example.com")
                .append("Password", "mdp")
                .append("Date of birth", java.util.Date.from(LocalDate.of(1980, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .append("Role", "Médecin")
                .append("horairesConsultation", "{}"); // ou ton JSON d'horaires

        userCollection.insertOne(medecinDoc);

        // Insérer un patient
        Document patientDoc = new Document()
                .append("Prénom", "Alice")
                .append("Nom", "Martin")
                .append("Email", "patient@example.com")
                .append("Password", "mdp")
                .append("Date of birth", java.util.Date.from(LocalDate.of(1990, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .append("Role", "Patient");

        userCollection.insertOne(patientDoc);

        Document rdvDoc = new Document("_id", id)
                .append("medecinEmail", "medecin@example.com")
                .append("patientEmail", "patient@example.com")
                .append("date", LocalDate.of(2025, 6, 16).toString())
                .append("debut", LocalTime.of(9, 0).toString())
                .append("fin", LocalTime.of(9, 30).toString())
                .append("jour", "lundi")
                .append("type", "consultation")
                .append("traitementPrescrit", "aucun")
                .append("bilan", "bon état");

        rdvCollection.insertOne(rdvDoc);
    }

    @Test
    public void testGetRdvMedecin_ReturnsRdv() {
        List<RDV> rdvs = MedecinService.getRdvMedecin("medecin@example.com", LocalDate.of(2025, 6, 16));
        assertNotNull(rdvs);
        assertEquals(1, rdvs.size());

        RDV rdv = rdvs.get(0);
        assertEquals("medecin@example.com", rdv.getMedecin().getEmail());
        assertEquals("patient@example.com", rdv.getPatient().getEmail());
        assertEquals(LocalDate.of(2025, 6, 16), rdv.getDate());
        assertEquals(LocalTime.of(9, 0), rdv.getHeureDebut());
        assertEquals(LocalTime.of(9, 30), rdv.getHeureFin());
        assertEquals("lundi", rdv.getJour());
        assertEquals("consultation", rdv.getType());
        assertEquals("aucun", rdv.getTraitementPrescrit());
        assertEquals("bon état", rdv.getBilan());
    }

    @Test
    public void testAddBilan_UpdatesBilanInDatabase() {
        Document rdvDoc = rdvCollection.find().first();
        assertNotNull(rdvDoc);

        ObjectId rdvId = rdvDoc.getObjectId("_id");

        MedecinService.addBilan(rdvId, "nouveau bilan");

        Document updatedDoc = rdvCollection.find(new Document("_id", rdvId)).first();
        assertNotNull(updatedDoc);
        assertEquals("nouveau bilan", updatedDoc.getString("bilan"));
    }



}

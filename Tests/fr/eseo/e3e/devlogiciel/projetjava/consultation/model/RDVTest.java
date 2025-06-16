package fr.eseo.e3e.devlogiciel.projetjava.consultation.model;

import static fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection.database;
import static org.junit.jupiter.api.Assertions.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.RDV;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Medecin;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Patient;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.consultation.model.TraitementPrescrit;

public class RDVTest {

    private static MongoCollection<Document> rdvCollection;
    private static MongoDatabase database;

    @BeforeAll
    public static void setUp() {

        DatabaseConnection.Connect();
        database = DatabaseConnection.getDatabase();
        rdvCollection = database.getCollection("RDV");
    }


    private Patient patient;
    private Medecin medecin;

    @BeforeAll
    public static void setupAll() {
        rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
    }

    @BeforeEach
    public void setup() {
        patient = new Patient(null, "Jean", "Dupont", "patient.test@example.com", null, null, null);


        medecin = new Medecin(null, "Alice", "Martin", "medecin.test@example.com", null, null, null, null);

        testRdv = new RDV(null, patient, medecin,
                LocalDate.now(),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                "lundi",
                "consultation",
                "",
                "");

        RDV.setRdv(testRdv);
    }

    @Test
    public void testInsertionMAJSuppressionRDV() {
        RDV rdv = new RDV(null, patient, medecin,
                LocalDate.of(2025, 6, 16),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                "lundi",
                "consultation",
                "",
                ""
        );

        RDV.setRdv(rdv);
        assertNotNull(rdv.getId(), "L'ID du RDV doit être défini après insertion");

        Document docTrouve = rdvCollection.find(Filters.eq("_id", rdv.getId())).first();
        assertNotNull(docTrouve, "Le RDV doit exister en base");

        RDV.mettreAJourTraitement(rdv.getId(), TraitementPrescrit.AUCUN);
        docTrouve = rdvCollection.find(Filters.eq("_id", rdv.getId())).first();
        assertEquals("AUCUN", docTrouve.getString("traitementPrescrit"), "Le traitement doit être mis à jour");

        RDV.dropRdv(rdv.getId());

        docTrouve = rdvCollection.find(Filters.eq("_id", rdv.getId())).first();
        assertNull(docTrouve, "Le RDV doit avoir été supprimé");
    }
    private RDV testRdv;

    @AfterEach
    public void tearDown() {
        if (testRdv != null && testRdv.getId() != null) {
            MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
            Document doc = rdvCollection.find(Filters.eq("_id", testRdv.getId())).first();
            if (doc != null) {
                RDV.dropRdv(testRdv.getId());
            }
        }
    }

    @Test
    public void testSetRdv() {
        assertNotNull(testRdv.getId(), "L'ID du RDV doit être défini après insertion");

        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        Document doc = rdvCollection.find(Filters.eq("_id", testRdv.getId())).first();
        assertNotNull(doc, "Le document RDV doit exister en base");
        assertEquals(testRdv.getPatient().getEmail(), doc.getString("patientEmail"));
        assertEquals(testRdv.getMedecin().getEmail(), doc.getString("medecinEmail"));
        assertEquals(testRdv.getDate().toString(), doc.getString("date"));
    }

    @Test
    public void testDropRdv() {
        ObjectId id = testRdv.getId();
        assertNotNull(id, "L'ID du RDV doit être défini avant suppression");

        RDV.dropRdv(id);

        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        Document doc = rdvCollection.find(Filters.eq("_id", id)).first();
        assertNull(doc, "Le document RDV doit être supprimé de la base");

        // Annule la suppression pour éviter erreurs dans tearDown
        testRdv = null;
    }

    @Test
    public void testMettreAJourTraitement() {
        ObjectId id = testRdv.getId();
        assertNotNull(id, "L'ID du RDV doit être défini avant mise à jour");

        // Met à jour le traitement prescrit
        RDV.mettreAJourTraitement(id, TraitementPrescrit.NOUVEAU_TRAITEMENT); // adapte selon ton enum TraitementPrescrit

        MongoCollection<Document> rdvCollection = DatabaseConnection.getDatabase().getCollection("RDV");
        Document doc = rdvCollection.find(Filters.eq("_id", id)).first();
        assertNotNull(doc, "Le document RDV doit exister en base");
        assertEquals("NOUVEAU_TRAITEMENT", doc.getString("traitementPrescrit"));
    }


}

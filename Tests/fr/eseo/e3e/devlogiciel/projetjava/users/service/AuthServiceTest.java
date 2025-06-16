package fr.eseo.e3e.devlogiciel.projetjava.users.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import fr.eseo.e3e.devlogiciel.projetjava.database.DatabaseConnection;
import fr.eseo.e3e.devlogiciel.projetjava.users.factory.UtilisateursFactory;
import fr.eseo.e3e.devlogiciel.projetjava.users.model.Utilisateurs;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {
    private static MongoDatabase database;
    private static MongoCollection<Document> users;


    @BeforeAll
    static void setUp() {
        DatabaseConnection.Connect(); // Assure que la base est connectée AVANT d'utiliser AuthService
        AuthService authService = new AuthService();


    }

    @Test
    void authentificationValide() {
        boolean connecter = AuthService.seConnecter("john.doe@example.com", "password123".toCharArray());
        Utilisateurs user = UtilisateursFactory.UtilisateurFromEmail("john.doe@example.com");
        assertTrue(connecter);
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void authentificationEmailInvalide() {
        assertThrows(IllegalArgumentException.class, () ->
                AuthService.seConnecter("wrong.email@example.com", "password123".toCharArray()));
    }

    @Test
    void authentificationMotDePasseIncorrect() {
        assertThrows(IllegalArgumentException.class, () ->
                AuthService.seConnecter("john.doe@example.com", "wrongpass".toCharArray()));
    }

    @Test
    void authentificationRoleIncorrect() {
        boolean connecter = AuthService.seConnecter("john.doe@example.com", "password123".toCharArray());
        Utilisateurs user = UtilisateursFactory.UtilisateurFromEmail("john.doe@example.com");
        assertNotEquals("Médecin", user.getRole());
    }
}

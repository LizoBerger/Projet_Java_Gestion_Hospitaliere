# Projet_Java_Gestion_Hospitaliere
Ce projet s'inscrit dans le cadre de l'UE Développement Logiciel. L'objectif est de mobiliser toute les matières internes de cette UE (OOP, Structure de données, Modélisation &amp; Tests). En binôme, nous avons choisis de reproduire une gestion hospitalière (connexion Patient/Médecin, Prise de RDV, suivi de consultations, ect.)

# Projet Gestion Hospitalière - Java & MongoDB

## Description

Application Java de gestion hospitalière permettant :
- **Gestion des utilisateurs** (Patients / Médecins)
- **Consultations** avec horaires par médecin
- **Recherche** par nom / email
- **Gestion des priorités** (symptômes, urgences...)

---

## Structure des données

### Utilisateurs (collection `User` dans MongoDB)

#### Médecin (exemple JSON)
```json
{
  "Prénom": "Jean",
  "Nom": "Martin",
  "Email": "jean.martin@hopital.fr",
  "Password": "motdepasse123",
  "Date of birth": "1970-05-15T00:00:00Z",
  "Role": "Médecin",
  "horairesConsultation": {
    "lundi": ["09:00-12:00", "14:00-17:00"],
    "mardi": ["09:00-12:00"],
    "mercredi": [],
    "jeudi": ["14:00-18:00"],
    "vendredi": ["09:00-12:00"]
  }
}


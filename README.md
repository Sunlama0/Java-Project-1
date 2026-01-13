# Boutique de Thés — Spring Boot / JPA / MySQL / Thymeleaf

Application web CRUD permettant de gérer une boutique de thés (produits) avec :
- Liste des produits
- Ajout / modification / suppression
- Recherche par nom
- Filtre par type de thé
- Tri (nom, prix, stock)
- Pagination (10 produits / page)
- Export CSV
- Interface thème clair avec icônes SVG et CSS custom

---

## Prérequis

- Java 17  
- Maven  
- MySQL (port 3306 par défaut)  
- IDE : IntelliJ IDEA recommandé  

---

## Base de données MySQL

### Création de la base

```sql
CREATE DATABASE boutique_thes;
```

### Configuration (`application.properties`)

```properties
server.port=8000

spring.datasource.url=jdbc:mysql://localhost:3306/boutique_thes
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Lancement du projet

```bash
mvn spring-boot:run
```

Accès : http://localhost:8000/

---

## Routes principales

| Méthode | URL | Description |
|-------|-----|------------|
| GET | / | Liste des produits |
| GET | /nouveau | Ajout |
| POST | /enregistrer | Enregistrement |
| GET | /modifier/{id} | Modification |
| POST | /modifier/{id} | Mise à jour |
| GET | /supprimer/{id} | Suppression |
| GET | /export/csv | Export CSV |

---

## Modèle Produit

- id  
- nom  
- typeThe  
- origine  
- prix  
- quantiteStock  
- description  
- dateReception  

---

## Structure du projet

```
src/main/java/com/example/demo
 ├── DemoApplication.java
 ├── controller
 ├── service
 ├── repository
 └── model
```

---

Projet pédagogique — Sup de Vinci  
Spring Boot / JPA / MySQL

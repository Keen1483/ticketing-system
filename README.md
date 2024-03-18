# Système de gestion de tickets

Cette application permet de manager les tickets.
Vous pouvez créer les utilisateurs et les tickets, puis assigner les tickets aux utilisateurs.
Chaque utilisateur connecté peut avoir accès au tickets qui lui ont étés assignés.  

_Le projet s'exécute sur le port 9000_  
_Et l'URL de la documentation OpenAPI est [http://localhost:9000/swagger-ui/index.html](http://localhost:9000/swagger-ui/index.html)_

_NB_ : Par souci de simplicité, j'ai reparti le code sur deux branches: _master_ et _authorisation_  
    * La branche _master_ contient les parties 1, 2, 3, 5 et 6
    * La branch _authorisation_ contient la partie 4
J'ai fait ce choix parce que la partie 4 requiert les système d'authentification et d'authorisation qui alourdissent un peu les tests de l'application.

## Sur la Branche _master_

#### Comment exécuter l'application (en utilisation Spring Tool Suite ou Eclipse)

1. Cloner le projet.
    Ouvrez votre terminal dans le repertoire de travail et tapez la commande:  
    `git clone https://github.com/Keen1483/ticketing-system.git`
<br/><br/>

2. Importer le projet sur votre IDE (Spring Tool Suite ou Eclipse)  
    Sur la bare de navigation de votre IDE:
    * cliquez sur _File_
    * cliquez sur _Import_
    * cliquez sur _Maven_
    * cliquez sur _Existing Maven Projects_
    * cliquez sur _Next_
    * cliquez sur _Browse_
    * Sélectionnez le projet que vous avez cloner
    * cliquez sur _Finish_
<br/><br/>

3. Installer les dépendances du projet  
    Clique droit sur le projet:
    * cliquez sur _Run As_
    * cliquez sur _Maven Install_ pour installer les dépendances du projet
<br/><br/>

4. Lancer les test unitaires  
    Clique droit sur le projet:
    * cliquez sur _Run As_
    * cliquez sur _JUnit Test_
<br/><br/>

5. Lancer le projet  
    Clique droit sur le projet:
    * cliquez sur _Run As_
    * cliquez sur _Spring Boot App_
<br/><br/>

6. Ouvrir la documentation du projet  
    Ouvrez votre navigateur web à l'URL [http://localhost:9000/swagger-ui/index.html](http://localhost:9000/swagger-ui/index.html)
<br/><br/>

7. Ouvrir la base de données du projet  
    Ouvrez votre navigateur web à l'URL [http://localhost:9000/h2-console](http://localhost:9000/h2-console)  
    Les crédentials pour la base de données h2 sont:
    * Driver Class: `org.h2.Driver`
    * JDBC URL: `jdbc:h2:mem:testdb`
    * User Name: `sa`
    * Password: 
<br/><br/>

8. Exécutez la collection des requêtes sur Postman    
    * Importez sur Postman la collection `Ticketing System.postman_collection` depuis le repertiore du projet.
    * Lancez la collection et observez les résultats des requêtes.
    * Comparez les résultats à ceux présentés par les images de test Postman qui se trouve dans le repertoire du projet.
<br/><br/>

## Sur la branche _authorization_


. En plus des fonctionnalités de la précédente partie, l'authentification et l'authorisation a été ajouté à l'application:  
    * Un champ mot de passe a été ajouter sur les propriétés d'un utiisateur
    * Une classe Role (avec Repository, Service et Controller) a été ajoutée aux entités  
    * A la création, les utilisateur ont par défaut un rôle _USER_

Encore par souci de simplicité j'ai laissé toutes les endpoints à accès publique et seule la route permetant d'otenir les tickets assignés à un utilisateur spécifique requiert l'authentification et ne retourne les données avec statut 200 que si les tickets demandés correspondent à l'utilisateur connecté.

#### Comment tester l'authorisation sur les tickets

. A l'exécution de l'application, les données suivantes sont crées (l'initialisation des données est faite par Faker dans la classe principale du projet):  
    * Deux utilisateurs
    * Deux tickets
    * Deux roles
    * Assignation du ticket 1 à l'utilisateur 1 et assignation du ticket 2 à l'utilisateur 2  

Il ne reste plus qu'à se logger et appélé les tickets

1. Importer sur Postman ma collection `Ticketing System Authorization.postman_collection` depuis le repertoire du project
2. Lancez la première requête (Login) pour connecter l'utilisateur 1 et copiez le access_token qui a été générée
3. Complétez l'_Authorization_ des requêtes 2 et 3 (Get tickets User 1 et Get tickets User 2) avec le token que vous avez copié, puis exécutez les.
4. Les dernières requêtes (Get All Users, Get All tickets et Get Roles) vous permet de voir les données qui ont été générées par Faker au démarrage de l'application.

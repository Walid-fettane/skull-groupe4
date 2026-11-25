# skull-groupe4

le Projet : on développe le jeu Skull en ligne avec des web services Spring Boot, une authentification JWT et Docker.

## L'équipe

| Membre | Partie |
|---|---|
| Wissal OUCHEN | Responsable de l'interface graphique et de la mise en place sur docker |
| Clément LANGLOIS | Responsable du Moteur de jeu et de l'IA |
| Margerit DEL CARMEN VILLILO | Responsable des Salons et du Service classment|
| Walid FETTANE | Responsable de la Gestion utilisateur et de la Marketplace|

On a découpé le projet en 5 parties : PlayerService, GameService pour les salons, GameService pour le moteur de jeu et l'IA, LadderService avec MarketService, et enfin le client avec Docker.

## Planning prévisionnel

| Lot | Date | Ce qu'on livre |
|---|---|---|
| 1 | 31 octobre | Inscription, connexion, compte admin, création d'un salon et accès à un salon, sauvegarde des données |
| 2 | 30 novembre | Une partie complète contre l'IA, la sécurité JWT, le remplacement par une IA quand un joueur quitte |
| 3 | 15 décembre | Historique, classement, marketplace, bannissement, docker-compose et rapport final |

Pour chaque lot, on rendra une description de ce qu'on a fait, un plan de tests et le rapport des tests.


## Base de données

La structure de la base de données (version 1) est disponible dans le fichier [database.sql]
Assurez vous que la base de données est bien crée.
Test effectués afin de confirmé que les données de la base de données sont corrects:
http://localhost:8083/api/v1/ladder
http://localhost:8083/api/v1/historique/1
http://localhost:8083/api/v1/historique/games/1

CREATE TABLE IF NOT EXISTS Joueur
(
    idJoueur INTEGER PRIMARY KEY ,
    email varchar(100) not null UNIQUE,
    mdp varchar(255) not null,
    role varchar(50) not null default 'ROLE_USER',
    nbVictoires INTEGER not null default 0,
    nbParties INTEGER not null default 0
    
);
--besoin pour joindre et quitter sinon error
CREATE TABLE IF NOT EXISTS salon_joueurs (
    id_salon  INTEGER NOT NULL,
    id_joueur INTEGER NOT NULL,
    PRIMARY KEY (id_salon, id_joueur),
    FOREIGN KEY (id_salon)  REFERENCES Salon(idSalon),
    FOREIGN KEY (id_joueur) REFERENCES Joueur(idJoueur)
);

CREATE TABLE IF NOT EXISTS Salon
(
    idSalon INTEGER PRIMARY KEY ,
    idCreateur INTEGER NOT NULL,
    nbMaxJoueur INTEGER  not null,
    nbAi INTEGER not null,
    status varchar(50) not null,
    FOREIGN KEY (idCreateur) REFERENCES joueur (idJoueur)
    
);

CREATE TABLE IF NOT EXISTS PARTIE(
    idPartie INTEGER PRIMARY KEY ,
    datefin DATETIME not null,
    idSalon INTEGER not null,
    gameServiceId BIGINT UNIQUE NOT NULL,
    FOREIGN KEY (idSalon) REFERENCES Salon(idSalon)

);

CREATE TABLE IF NOT EXISTS historique_participation(
    idJoueur INTEGER not null,
    idPartie INTEGER not null,
    score INTEGER not null default 0,
    nbtuillesRestantes INTEGER not null,
    est_gagnant INTEGER not null default 0, --0(perdu) ou 1(gagné) pour les valeur vrai ou faux(sqlite ne supporte pas le type boolean)
    PRIMARY KEY (idJoueur, idPartie),
    FOREIGN KEY (idJoueur) REFERENCES Joueur(idJoueur),
    FOREIGN KEY (idPartie) REFERENCES PARTIE(idPartie)

);

INSERT OR REPLACE INTO Joueur (idJoueur, email, mdp, nbVictoires, nbParties) VALUES
                                                                      (1, 'alice@test.fr', 'x', 1, 1),
                                                                      (2, 'bob@test.fr',   'x', 0, 1),

                                                                      (3, 'carol@test.fr', 'x', 0, 0);
INSERT OR REPLACE INTO Salon (idSalon, idCreateur, nbMaxJoueur, nbAi, status) VALUES
                                                                       (1, 1, 2, 0, 'TERMINEE'),
                                                                       (2, 3, 4, 1, 'EN_ATTENTE');

INSERT OR REPLACE INTO salon_joueurs (id_salon, id_joueur) VALUES (2, 3);
INSERT OR REPLACE INTO PARTIE (idPartie, datefin, idSalon, gameServiceId) VALUES
    (1, '2026-11-25 20:00:00.000', 1, 100);

INSERT OR REPLACE INTO historique_participation (idJoueur, idPartie, score, nbtuillesRestantes, est_gagnant) VALUES

                                                                                                  (1, 1, 12, 2, 1),
                                                                                                      (2, 1, 7, 5, 0);
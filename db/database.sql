CREATE TABLE Joueur
(
    idJoueur INTEGER PRIMARY KEY ,
    email varchar(100) not null UNIQUE,
    mdp varchar(255) not null,
    role varchar(50) not null default 'ROLE_USER',
    nbVictoires INTEGER not null default 0,
    nbParties INTEGER not null default 0
    
);

CREATE TABLE Salon
(
    idSalon INTEGER PRIMARY KEY ,
    idCreateur INTEGER NOT NULL,
    nbMaxJoueur INTEGER  not null,
    nbAi INTEGER not null,
    status varchar(50) not null,
    FOREIGN KEY (idCreateur) REFERENCES joueur (idJoueur)
    
);

CREATE TABLE PARTIE(
    idPartie INTEGER PRIMARY KEY ,
    datefin DATETIME not null,
    idSalon INTEGER not null,
    FOREIGN KEY (idSalon) REFERENCES Salon(idSalon)

);

CREATE TABLE historique_participation(
    idJoueur INTEGER not null,
    idPartie INTEGER not null,
    score INTEGER not null default 0,
    nbtuillesRestantes INTEGER not null,
    est_gagnant INTEGER not null default 0, --0(perdu) ou 1(gagné) pour les valeur vrai ou faux(sqlite ne supporte pas le type boolean)
    PRIMARY KEY (idJoueur, idPartie),
    FOREIGN KEY (idJoueur) REFERENCES Joueur(idJoueur),
    FOREIGN KEY (idPartie) REFERENCES PARTIE(idPartie) 

);

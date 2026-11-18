package fr.miage.skull.model;

import fr.miage.skull.exception.EnchereInvalideException;

import java.util.Objects;

public class Enchere {

    private int enchereCourrante;
    private int playerIdEnchere;
    private int enchereId;

    public Enchere(int id){
        this.enchereCourrante = 0;
        this.playerIdEnchere = -1;
        this.enchereId = id;
    }

    //Quand on arrive ici, on sait que c'est le tour du joueur
    //On sait qu'il peut encherir
    //On verifi donc seulement que l'enchère est plus haute que la précédente / valide
    public void encherir(Integer playerId, int enchere) throws EnchereInvalideException {
        //On vérifie que le joueur qui demande d'encherir n'est pas l'enchérisseur le plus grand courrant
        if(Objects.equals(playerId, playerIdEnchere)){
            throw new EnchereInvalideException();
        }
        //Si l'enchère est en dessous ou égale de la plus grosse enregistré --> invalide
        if(enchere <= enchereCourrante){
           throw new EnchereInvalideException();
        }
        //Sinon la demande d'enchère est enregistré et devient la courrante..
        this.enchereCourrante = enchere;
        this.playerIdEnchere = playerId;
    }

    //TODO : voir comment se fait l'enchère côté client
    public Integer getWinnerId() {
        return playerIdEnchere;
    }

    //TODO : voir comment se fait l'enchère côté client
    public int getWinnerEnchereNB() {
        return enchereCourrante;
    }
}
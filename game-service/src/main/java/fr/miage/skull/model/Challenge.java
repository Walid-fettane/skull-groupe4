package fr.miage.skull.model;

public class Challenge {

    private int playerChallengerId;
    private int challengeNb;
    private int fleursRevelesId;
    private int challengeId;

    public Challenge(int playerChallengerId, int challengeNb, int challengeId){
        this.playerChallengerId = playerChallengerId;
        this.challengeNb = challengeNb;
        this.challengeId = challengeId;
        this.fleursRevelesId = 0;
    }

    public void revelerSaPile() {
    }

    public void revelerLePremierDisque(Integer targetPlayerId) {
    }

    public void finChallenge() {
    }
}
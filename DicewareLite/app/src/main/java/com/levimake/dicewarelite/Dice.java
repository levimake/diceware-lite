package com.levimake.dicewarelite;

/**
 * Created by ${USER} on ${DATE}.
 */


class Dice {
    private int counter;
    private String dicePhrase;
    private String diceSequence;
    private int imageId;

    public Dice() {
    }

    public Dice(String dicePhrase, String diceSequence, int imageId, int counter) {
        this.dicePhrase = dicePhrase;
        this.diceSequence = diceSequence;
        this.imageId = imageId;
        this.counter = counter;
    }

    public String getDicePhrase() {
        return dicePhrase;
    }

    public String getDiceSequence() {
        return diceSequence;
    }

    public int getImageId() {
        return imageId;
    }

    public void setDicePhrase(String dicePhrase) {
        this.dicePhrase = dicePhrase;
    }

    public void setDiceSequence(String diceSequence) {
        this.diceSequence = diceSequence;
    }

    /*public void setImageId(int imageId) {
        this.imageId=imageId;
    }*/

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}

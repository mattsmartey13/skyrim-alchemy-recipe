package model;

import java.lang.reflect.Array;

public class Player {

    private int alchemyLevel, playerGearTotal;
    private double basePercentStrength;
    private String[] playerPerks;

    public Player() {
        alchemyLevel = 15;
        playerPerks = new String[6];
        playerGearTotal = 0;
    }

    public int getAlchemyLevel() {
        return alchemyLevel;
    }

    public void setAlchemyLevel(int alchemyLevel) {
        this.alchemyLevel = alchemyLevel;
    }

    public int getPlayerGearTotal() {
        return playerGearTotal;
    }

    public void setPlayerGearTotal(int playerGearTotal) {
        this.playerGearTotal = playerGearTotal;
    }

    public double getBasePercentStrength() {
        return basePercentStrength;
    }

    public void setBasePercentStrength(double basePercentStrength) {
        this.basePercentStrength = basePercentStrength;
    }

    public String[] getPlayerPerks() {
        return playerPerks;
    }

    public void setPlayerPerks(String[] playerPerks) {
        this.playerPerks = playerPerks;
    }
}

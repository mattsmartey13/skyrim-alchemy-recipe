package model;

import java.util.Set;
import java.util.TreeSet;

public class Player {

    private int alchemyLevel;
    private double basePercentStrength;
    private Set<Perk> playerPerks;
    private Set<Item> playerGear;

    public Player() {
        alchemyLevel = 15;
        playerPerks = new TreeSet<Perk>();
        playerGear = new TreeSet<Item>();
    }

    public int getAlchemyLevel() {
        return alchemyLevel;
    }

    public void setAlchemyLevel(int alchemyLevel) {
        this.alchemyLevel = alchemyLevel;
    }

    public Set<Perk> getPlayerPerks() {
        return playerPerks;
    }

    public void setPlayerPerks(Set<Perk> playerPerks) {
        this.playerPerks = playerPerks;
    }

    public Set<Item> getPlayerGear() {
        return playerGear;
    }

    public void setPlayerGear(Set<Item> playerGear) {
        this.playerGear = playerGear;
    }

    public void addPerk(Perk m) {
        playerPerks.add(m);
    }

    public void clearPerks() {
        playerPerks.clear();
    }

    public void addPlayerArmorItem(Item i) {
        playerGear.add(i);
    }

    public void clearArmorItems() {
        playerPerks.clear();
    }

    public double getBasePercentStrength() {
        return basePercentStrength;
    }

    public void setBasePercentStrength(double basePercentStrength) {
        this.basePercentStrength = basePercentStrength;
    }

    @Override
    public String toString() {
        return "Player [alchemyLevel=" + alchemyLevel + ", playerPerks=" + playerPerks + ", playerGear=" + playerGear + "]";
    }
}

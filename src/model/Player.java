package model;

import java.util.ArrayList;

public class Player {

    private int alchemyLevel;
    private ArrayList<Perk> playerPerks;
    private ArrayList<Item> items;

    public Player() {
        alchemyLevel = 15;
        playerPerks = new ArrayList<>();
        items = new ArrayList<>();
    }

    public int getAlchemyLevel() {
        return alchemyLevel;
    }

    public void setAlchemyLevel(int alchemyLevel) {
        this.alchemyLevel = alchemyLevel;
    }

    public ArrayList<Perk> getPlayerPerks() {
        return playerPerks;
    }

    public void setPlayerPerks(ArrayList<Perk> playerPerks) {
        this.playerPerks = playerPerks;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Player{" +
                "alchemyLevel=" + alchemyLevel +
                ", playerPerks=" + playerPerks +
                ", items=" + items +
                '}';
    }
}

package model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int alchemyLevel;
    private List<Perk> playerPerks;
    private List<Item> items;

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

    public List<Perk> getPlayerPerks() {
        return playerPerks;
    }

    public void setPlayerPerks(List<Perk> playerPerks) {
        this.playerPerks = playerPerks;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
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

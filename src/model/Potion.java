package model;

import java.util.*;

public class Potion {
    private String name;

    private List<Ingredient> ingredients;
    private HashSet<Effect> effects;
    private int totalGoldCost;

    public Potion(String name, List<Ingredient> ingredients, HashSet<Effect> effects, int totalGoldCost) {
        this.name = name;
        this.ingredients = ingredients;
        this.effects = effects;
        this.totalGoldCost = totalGoldCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<Effect> getEffects() {
        return effects;
    }

    public void setEffects(HashSet<Effect> effects) {
        this.effects = effects;
    }

    public int getTotalGoldCost() {
        return totalGoldCost;
    }

    public void setTotalGoldCost(int totalGoldCost) {
        this.totalGoldCost = totalGoldCost;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String actualToString() {
        return "Potion{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", effects=" + effects +
                ", totalGoldCost=" + totalGoldCost +
                '}';
    }

    @Override
    public String toString() {
        return name + " - " + totalGoldCost + " coins" + "\n" +  ingredients.toString() + "\n" + effects.toString();
    }
}

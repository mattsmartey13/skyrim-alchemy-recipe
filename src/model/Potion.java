package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Potion {

    private String name;
    private boolean potion;
    private Map<String, Ingredient> ingredients;
    private int goldCost;
    private String description;

    public Potion(String name, int goldCost, HashMap<String, Ingredient> ingredients) {
        this.name = name;
        this.goldCost = goldCost;
        ingredients = new HashMap<String, Ingredient>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public void setGoldCost(int goldCost) {
        this.goldCost = goldCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addIngredient(Ingredient m) {
        ingredients.put(m.getName(), m);
    }

    public Ingredient getIngredientByHash(String code) {
        return ingredients.get(code);
    }

    public Collection<Ingredient> getAllIngredients() {
        return ingredients.values();
    }

    public boolean isPotion() {
        return potion;
    }

    public void setPotion(boolean potion) {
        this.potion = potion;
    }

}

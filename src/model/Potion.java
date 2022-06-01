package model;

import java.util.*;

public class Potion {
    private String name;
    private HashSet<Effect> effects;

    public Potion(String name, HashSet<Effect> effects) {
        this.name = name;
        this.effects = effects;
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

    public String toSkyrimPotionString() {
        return "";
    }
}

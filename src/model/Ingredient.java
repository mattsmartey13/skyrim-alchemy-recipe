package model;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Ingredient implements Comparator<Ingredient>, Comparable<Ingredient> {

    private String name;
    private Map<String, Effect> effects;
    private float baseMag;

    //constructor
    public Ingredient(String name) {
        this.name = name;
        effects = new HashMap<String, Effect>();
    }

    //getter setters
    public Map<String, Effect> getEffects() {
        return effects;
    }

    public void setEffects(Map<String, Effect> effects) {
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEffect(Effect e) {
        effects.put(e.getHashValue(), e);
    }

    public Effect getEffect(String e) {
        return effects.get(e);
    }

    public Collection<Effect> getAllEffectsForIngredient() {
        return effects.values();
    }

    public float getbaseMag() {
        return baseMag;
    }

    public void setbaseMag(float baseMag) {
        this.baseMag = baseMag;
    }

    //change values of duration, magnitude and gold based on multiplier
    //this affects all ingredients and needs to be potion specific
    public void refactorMagnitude(Effect e, int multiplier) {
        e.setBaseMag(e.getBaseMag() * multiplier);
    }

    public void refactorDuration(Effect e, int multiplier) {
        e.setBaseDur(e.getBaseDur() * multiplier);
    }

    public void refactorValue(Effect e, int multiplier) {
        e.setBaseCost(e.getBaseCost() * multiplier);
    }

    //toString
    @Override
    public String toString() {
        return "Ingredient [name=" + name + ", effects=" + effects + "]";
    }

    @Override
    public int compareTo(Ingredient i) {
        return (this.name).compareTo(i.name);
    }

    @Override
    public int compare(Ingredient i1, Ingredient i2) {
        if (i1.name.equals(i2.name)) {
            return 1;
        }
        return 0;
    }

}

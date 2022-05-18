package model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Potion {
    private String name;
    private String[] effectStrings;
    private int[] effectValues;
    private int goldCost;

    public Potion(String name, String[] effectStrings, int[] effectValues, int goldCost) {
        this.name = name;
        this.effectStrings = effectStrings;
        this.effectValues = effectValues;
        this.goldCost = goldCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getEffectStrings() {
        return effectStrings;
    }

    public void setEffectStrings(String[] effectStrings) {
        this.effectStrings = effectStrings;
    }

    public int[] getEffectValues() {
        return effectValues;
    }

    public void setEffectValues(int[] effectValues) {
        this.effectValues = effectValues;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public void setGoldCost(int goldCost) {
        this.goldCost = goldCost;
    }

    @Override
    public String toString() {
        return "Potion{" +
                "name='" + name + '\'' +
                ", effectStrings=" + Arrays.toString(effectStrings) +
                ", effectValues=" + Arrays.toString(effectValues) +
                ", goldCost=" + goldCost +
                '}';
    }

    public String toSkyrimPotionString() {
        return "";
    }
}

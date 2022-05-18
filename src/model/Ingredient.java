package model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

public class Ingredient implements Comparator<Ingredient>, Comparable<Ingredient> {

    @JsonProperty("name")
    private String name;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("effects")
    private String[] effects;
    @JsonProperty("magnitudeEffect")
    private Optional<HashMap<Integer, Double>> magnitudeEffect;

    @JsonProperty("magnitudeTime")
    private Optional<HashMap<Integer, Double>> magnitudeTime;
    @JsonProperty("magnitudeValue")
    private Optional<HashMap<Integer, Double>> magnitudeValue;

    @ConstructorProperties({"name", "hash", "effects", "magnitudeEffect", "magnitudeTime", "magnitudeValue"})
    public Ingredient(String name, String hash, String[] effects, Optional<HashMap<Integer, Double>> magnitudeEffect, Optional<HashMap<Integer, Double>> magnitudeTime, Optional<HashMap<Integer, Double>> magnitudeValue) {
        this.name = name;
        this.hash = hash;
        this.effects = effects;
        this.magnitudeEffect = magnitudeEffect;
        this.magnitudeTime = magnitudeTime;
        this.magnitudeValue = magnitudeValue;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("hash")
    public String getHash() {
        return hash;
    }

    @JsonSetter("hash")
    public void setHash(String hash) {
        this.hash = hash;
    }

    @JsonGetter("effects")
    public String[] getEffects() {
        return effects;
    }

    @JsonSetter("effects")
    public void setEffects(String[] effects) {
        this.effects = effects;
    }

    @JsonGetter("magnitudeEffect")
    public Optional<HashMap<Integer, Double>> getMagnitudeEffect() {
        return magnitudeEffect;
    }

    @JsonSetter("magnitudeEffect")
    public void setMagnitudeEffect(Optional<HashMap<Integer, Double>> magnitudeEffect) {
        this.magnitudeEffect = magnitudeEffect;
    }

    @JsonGetter("magnitudeTime")
    public Optional<HashMap<Integer, Double>> getMagnitudeTime() {
        return magnitudeTime;
    }

    @JsonSetter("magnitudeTime")
    public void setMagnitudeTime(Optional<HashMap<Integer, Double>> magnitudeTime) {
        this.magnitudeTime = magnitudeTime;
    }

    @JsonGetter("magnitudeValue")
    public Optional<HashMap<Integer, Double>> getMagnitudeValue() {
        return magnitudeValue;
    }

    @JsonSetter("magnitudeValue")
    public void setMagnitudeValue(Optional<HashMap<Integer, Double>> magnitudeValue) {
        this.magnitudeValue = magnitudeValue;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", hash='" + hash + '\'' +
                ", effects=" + Arrays.toString(effects) +
                '}';
    }

    @Override
    public int compareTo(Ingredient i) {
        return (this.hash).compareTo(i.hash);
    }

    @Override
    public int compare(Ingredient i1, Ingredient i2) {
        if (i1.hash.equals(i2.hash)) {
            return 0;
        }
        return 1;
    }

}

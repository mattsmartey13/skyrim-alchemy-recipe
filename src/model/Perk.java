package model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.beans.ConstructorProperties;

public class Perk {

    @JsonProperty("name")
    private String name;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("multiplier")
    private double multiplier;

    @ConstructorProperties({"name", "hash", "multiplier"})
    public Perk(String name, String hash, double multiplier) {
        this.name = name;
        this.hash = hash;
        this.multiplier = multiplier;
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

    @JsonGetter("multiplier")
    public double getMultiplier() {
        return multiplier;
    }

    @JsonSetter("multiplier")
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}

package model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.beans.ConstructorProperties;
import java.util.Comparator;

public class Effect implements Comparator<Effect>, Comparable<Effect> {

    @JsonProperty("name")
    private String name;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("effect")
    private String effect;
    @JsonProperty("baseCost")
    private double baseCost;
    @JsonProperty("baseMag")
    private int baseMag;
    @JsonProperty("baseDur")
    private int baseDur;
    @JsonProperty("goldLvl100")
    private int goldLvl100;
    @JsonProperty("style")
    private String style;

    @ConstructorProperties({"name", "hash", "effect", "baseCost", "baseMag", "baseDur", "goldLvl100", "style"})
    public Effect(String name, String hash, String effect, double baseCost, int baseMag, int baseDur, int goldLvl100, String style) {
        this.name = name;
        this.hash = hash;
        this.effect = effect;
        this.baseCost = baseCost;
        this.baseMag = baseMag;
        this.baseDur = baseDur;
        this.goldLvl100 = goldLvl100;
        this.style = style;
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

    @JsonGetter("effect")
    public String getEffect() {
        return effect;
    }

    @JsonSetter("effect")
    public void setEffect(String effect) {
        this.effect = effect;
    }

    @JsonGetter("style")
    public String getStyle() {
        return style;
    }

    @JsonSetter("style")
    public void setStyle(String style) {
        this.style = style;
    }

    @JsonGetter("baseMag")
    public int getBaseMag() {
        return baseMag;
    }

    @JsonSetter("baseMag")
    public void setBaseMag(int baseMag) {
        this.baseMag = baseMag;
    }

    @JsonGetter("baseDur")
    public int getBaseDur() {
        return baseDur;
    }

    @JsonSetter("baseDur")
    public void setBaseDur(int baseDur) {
        this.baseDur = baseDur;
    }

    @JsonGetter("goldLvl100")
    public int getGoldLvl100() {
        return goldLvl100;
    }

    @JsonSetter("goldLvl100")
    public void setGoldLvl100(int goldLvl100) {
        this.goldLvl100 = goldLvl100;
    }

    @JsonGetter("baseCost")
    public double getBaseCost() {
        return baseCost;
    }

    @JsonSetter("baseCost")
    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }

    @Override
    public String toString() {
        return "Effect{" +
                "name='" + name + '\'' +
                ", hash='" + hash + '\'' +
                ", effect='" + effect + '\'' +
                ", style='" + style + '\'' +
                ", baseMag=" + baseMag +
                ", baseDur=" + baseDur +
                ", goldLvl100=" + goldLvl100 +
                ", baseCost=" + baseCost +
                '}';
    }

    @Override
    public int compareTo(Effect e) {
        return (this.hash).compareTo(e.hash);
    }

    @Override
    public int compare(Effect e1, Effect e2) {
        if (e1.hash.equals(e2.hash)) {
            return 0;
        }
        return 1;
    }

}

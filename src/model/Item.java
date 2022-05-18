package model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.beans.ConstructorProperties;

public class Item {

    @JsonProperty("name")
    private String name;
    private int alchemyBoost;

    @ConstructorProperties({"name"})
    public Item(String name) {
        this.name = name;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    public int getAlchemyBoost() {
        return alchemyBoost;
    }

    public void setAlchemyBoost(int alchemyBoost) {
        this.alchemyBoost = alchemyBoost;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", alchemyBoost=" + alchemyBoost +
                '}';
    }
}

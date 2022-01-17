package model;

public class Enchantment {

    private int percentImprovement;

    public Enchantment(int percentImprovement) {
        this.percentImprovement = percentImprovement;
    }

    public int getPercentImprovement() {
        return percentImprovement;
    }

    public void setPercentImprovement(int percentImprovement) {
        this.percentImprovement = percentImprovement;
    }

    @Override
    public String toString() {
        return "Enchantement [percentImprovement=" + percentImprovement + "]";
    }
}

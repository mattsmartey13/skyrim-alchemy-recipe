package model;

import java.util.Comparator;

public class Effect implements Comparator<Effect>, Comparable<Effect> {

    private String effectName, hashValue;
    private boolean helpful, harmful, stats, skills;
    private int baseMag, baseDur, goldLvl100;
    private double baseCost;

    public Effect(
            String effectName,
            String hashValue,
            boolean helpful,
            boolean harmful,
            double baseCost,
            int baseMag,
            int baseDur,
            int goldLvl100,
            boolean stats,
            boolean skills
    ) {
        this.effectName = effectName;
        this.hashValue = hashValue;
        this.helpful = helpful;
        this.harmful = harmful;
        this.baseCost = baseCost;
        this.baseMag = baseMag;
        this.baseDur = baseDur;
        this.goldLvl100 = goldLvl100;
        this.stats = stats;
        this.skills = skills;
    }

    public boolean isStats() {
        return stats;
    }

    public void setStats(boolean stats) {
        this.stats = stats;
    }

    public boolean isSkills() {
        return skills;
    }

    public void setSkills(boolean skills) {
        this.skills = skills;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public boolean isHelpful() {
        return helpful;
    }

    public void setHelpful(boolean helpful) {
        this.helpful = helpful;
    }

    public boolean isHarmful() {
        return harmful;
    }

    public void setHarmful(boolean harmful) {
        this.harmful = harmful;
    }

    public int getBaseMag() {
        return baseMag;
    }

    public void setBaseMag(int baseMag) {
        this.baseMag = baseMag;
    }

    public int getBaseDur() {
        return baseDur;
    }

    public void setBaseDur(int baseDur) {
        this.baseDur = baseDur;
    }

    public int getGoldLvl100() {
        return goldLvl100;
    }

    public void setGoldLvl100(int goldLvl100) {
        this.goldLvl100 = goldLvl100;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }

    @Override
    public String toString() {
        return "Effect [effectName=" + effectName + ", hashValue=" + hashValue + ", helpful=" + helpful + ", harmful="
                + harmful + ", baseMag=" + baseMag + ", baseDur=" + baseDur + ", goldLvl100=" + goldLvl100
                + ", baseCost=" + baseCost + "]";
    }

    @Override
    public int compareTo(Effect e) {
        return (this.hashValue).compareTo(e.hashValue);
    }

    @Override
    public int compare(Effect e1, Effect e2) {
        if (e1.hashValue.equals(e2.hashValue)) {
            return 1;
        }
        return 0;
    }

}

package model;

public class Perk {

    private String perkName, hashCode;
    private int rank, alchemyLvlReq;
    private double boost;
    private Perk perkReq;

    //constructor
    public Perk(String perkName, String hashCode, int rank, int alchemyLvlReq, Perk perkReq, double boost) {
        this.perkName = perkName;
        this.hashCode = hashCode;
        this.rank = rank;
        this.alchemyLvlReq = alchemyLvlReq;
        this.perkReq = perkReq;
        this.boost = boost;
    }

    //getter setters
    public String getPerkName() {
        return perkName;
    }

    public void setPerkName(String perkName) {
        this.perkName = perkName;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getAlchemyLvlReq() {
        return alchemyLvlReq;
    }

    public void setAlchemyLvlReq(int alchemyLvlReq) {
        this.alchemyLvlReq = alchemyLvlReq;
    }

    public Perk getPerkReq() {
        return perkReq;
    }

    public void setPerkReq(Perk perkReq) {
        this.perkReq = perkReq;
    }

    public double getBoost() {
        return boost;
    }

    public void setBoost(double boost) {
        this.boost = boost;
    }
}

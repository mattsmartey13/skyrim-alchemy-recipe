package model;

public class Item {

    private String type;
    private Enchantment armorEnchantement;

    public Item(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Item(Enchantment armorEnchantement) {
        this.armorEnchantement = armorEnchantement;
    }

    public Enchantment getArmorEnchantement() {
        return armorEnchantement;
    }

    public void setArmorEnchantement(Enchantment armorEnchantement) {
        this.armorEnchantement = armorEnchantement;
    }

    @Override
    public String toString() {
        return "Item [armorEnchantement=" + armorEnchantement + "]";
    }

}

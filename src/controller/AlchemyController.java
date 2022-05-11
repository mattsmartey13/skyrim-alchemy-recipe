package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.*;
import view.*;
import data.*;

import java.util.*;

/**
 * TO DO:
 * <p>
 * Finish duration and gold methods
 * Add effects to ingredients using hashcodes as Effects are currently created twice
 * Create makePotion()
 * Account for time complexity (everything is O(n) at least currently and will drain memory if unchecked)
 * Event handlers
 *
 * @author matth
 */
public class AlchemyController {

    private Player model;
    private AlchemyRootPane rootPane;
    private AlchemyMenuBar amb;
    private IngredientEnchantmentPane ipep;
    private PlayerDetailsPane pdp;
    private ViewRecipePane vrp;

    /**
     * Constructor
     *
     * @param model
     * @param rootPane
     */
    public AlchemyController(Player model, AlchemyRootPane rootPane) {
        //initialise model and view fields
        this.model = model;
        this.rootPane = rootPane;
        this.amb = rootPane.getAlchemyMenuBar();
        this.ipep = rootPane.getIngredientEnchantmentPane();
        this.pdp = rootPane.getPlayerDetailsPane();
        this.vrp = rootPane.getViewRecipePane();

        //populate comboboxes
//        this.setupAndGetEffects();
//        this.setupAndGetIngredients();
//        this.setupAndGetItems();
//        this.setupAndGetPerks();

        //attach event handlers using private helper method
        //this.attachEventHandlers();
    }

    /**
     * Helper method to attach event handlers
     */
    private void attachEventHandlers() {
        this.amb.addExitEventHandler((e) -> {
            System.exit(0);
        });
        this.amb.addAboutEventHandler(new AboutApplicationHandler());
        this.amb.addResetEventHandler(new ResetDetailsHandler());
        this.pdp.addClearEventHandler(new ClearPlayerDetailsHandler());
        this.pdp.addSubmitEventHandler(new SubmitPlayerDetailsHandler());
        this.pdp.addEnchantmentEventHandler(new AddEnchantmentHandler());
        this.pdp.addRemoveEnchantmentEventHandler(new RemoveEnchantmentHandler());
        this.ipep.addIngredientEventHandler(new AddIngredientHandler());
        this.ipep.addItemEventHandler(new AddItemHandler());
    }

    /**
     * Helper method to find if an ingredient possesses an effect.
     *
     * @param ingredient
     * @return boolean
     **/
//    private boolean isEffectPresent(Ingredient ingredient, String hashValue) {
//        Effect effect = getEffectByHashValue(hashValue);
//        if (effect != null) {
//            if (ingredient.getAllEffectsForIngredient().contains(effect)) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * Helper method to obtain effect from its in game hashValue.
     *
     * @param hashValue
     * @return String
     */
//    private Effect getEffectByHashValue(String hashValue) {
//        for (Effect e : setupAndGetEffects()) {
//            if (Objects.equals(hashValue, e.getHashValue())) {
//                return e;
//            }
//        }
//        return null;
//    }

    /**
     * @param list
     * @return List<Effect>
     */
    private static List<Effect> getDuplicateEffects(Collection<Effect> list) {
        final List<Effect> duplicatedEffects = new ArrayList<Effect>();
        Set<Effect> set = new HashSet<Effect>() {
            @Override
            public boolean add(Effect e) {
                if (contains(e)) {
                    duplicatedEffects.add(e);
                }
                return super.add(e);
            }
        };

        set.addAll(list);
        return duplicatedEffects;
    }

    /**
     * @param list
     * @return boolean
     */
    private static boolean hasDuplicateEffect(Collection<Effect> list) {
        return !getDuplicateEffects(list).isEmpty();
    }

    /**
     * The alchemy bonus from equipment is the sum of all item enchantments.
     *
     * @param player
     * @return double
     */
    private double getEnchantmentPerkMultiplier(Player player) {
        double improvement = 100;
        for (Item e : player.getPlayerGear()) {
            improvement += e.getArmorEnchantement().getPercentImprovement();
        }
        return improvement / 100;
    }

    /**
     * 10% bonus to Potion strength after obtaining the Seeker of Shadows perk from the Dragonborn DLC.
     *
     * @param player
     * @return double
     */
    private double getSeekerShadowsPerkMultiplier(Player player) {
        for (Perk p : player.getPlayerPerks()) {
            if (Objects.equals(p.getHashCode(), "xx034838"))
                return 1.1;
        }
        return 1.0;
    }

    /**
     * The player will see a 20% boost to their potion by increasing their Alchemist perk levels
     * A level 5 perk will see the player double their potion strength.
     *
     * @param player
     * @return double
     */
    private double getAlchemistPerkMultiplier(Player player) {
        for (Perk p : player.getPlayerPerks()) {
            if (Objects.equals(p.getHashCode(), "000c07cd"))
                return 2.0;
            else if (Objects.equals(p.getHashCode(), "000c07cc"))
                return 1.8;
            else if (Objects.equals(p.getHashCode(), "000c07cb"))
                return 1.6;
            else if (Objects.equals(p.getHashCode(), "000c07ca"))
                return 1.4;
            else if (Objects.equals(p.getHashCode(), "000be127"))
                return 1.2;
        }
        return 1.0;
    }

    /**
     * The potion's strength will intensify by 25% if any of its effects restore stats  (health, magicka, stamina)
     * and the Player has the Physician perk equipped.
     *
     * @param player
     * @return double
     */
    private double getPhysicianPerkMultiplier(Player player, Effect effect) {
        String restoreHealth = "00042503";
        String restoreMagicka = "00042508";
        String restoreStamina = "00042503";
        String physician = "00058215";

        boolean physicianEffects = false;
        boolean physicianPerk = false;

        for (Perk p : player.getPlayerPerks()) {
            if (Objects.equals(p.getHashCode(), physician)) {
                physicianPerk = true;
            }
        }

        if (restoreHealth.equals(effect.getHashValue()) || restoreMagicka.equals(effect.getHashValue()) || restoreStamina.equals(effect.getHashValue())) {
            physicianEffects = true;
        }

        if (physicianEffects && physicianPerk)
            return 1.25;
        else
            return 1.0;
    }

    /**
     * The potion's strength will increase by 25% if the Player has the benefactor perk equipped and if
     * the potion contains positive effects to the Player.
     *
     * @param player
     * @return double
     */
    private double getBenefactorPerkMultiplier(Player player, Effect effect) {
        String benefactor = "00058216";
        boolean benefactorPerk = false;

        for (Perk p : player.getPlayerPerks()) {
            if (Objects.equals(p.getHashCode(), benefactor) && effect.isHelpful()) {
                benefactorPerk = true;
            }
        }

        if (benefactorPerk)
            return 1.2;
        else
            return 1.0;
    }

    /**
     * The potion's strength will increase by 25% if the Player has the poisoner perk equipped and if
     * the potion contains negative effects to the Player.
     **/
    private double getPoisonerPerkMultiplier(Player player, Effect effect) {
        String poisoner = "00042509";
        boolean poisonerPerk = false;

        for (Perk p : player.getPlayerPerks()) {
            if (Objects.equals(p.getHashCode(), poisoner) && effect.isHarmful()) {
                poisonerPerk = true;
            }
        }

        if (poisonerPerk)
            return 1.2;
        else
            return 1.0;
    }

    /**
     * Accounts for all multipliers and gives a final multiplier value for this specific effect
     *
     * @param player
     * @return final multiplier
     */
    private double returnEffectStrengthMultiplier(Player player, Effect effect) {
        //set values
        final int fAlchemyIngredientInitMult = 4;
        final double fAlchemySkillFactor = 1.5;

        //player specific perks
        double seekerShadow = getSeekerShadowsPerkMultiplier(player);
        double alchemistPerk = getAlchemistPerkMultiplier(player);
        double enchantments = getEnchantmentPerkMultiplier(player);

        //ingredient based perks
        double physician = getPhysicianPerkMultiplier(player, effect);
        double benefactor = getBenefactorPerkMultiplier(player, effect);
        double poisoner = getPoisonerPerkMultiplier(player, effect);

        double result = fAlchemyIngredientInitMult
                * effect.getBaseMag()
                * (1 + (fAlchemySkillFactor - 1) * player.getAlchemyLevel() / 100)
                * alchemistPerk
                * benefactor
                * physician
                * poisoner
                * enchantments
                * seekerShadow;

        if (result < 0)
            return effect.getBaseMag();
        return result;
    }


    /**
     * TO DO
     * return effect duration
     *
     * @return
     */
    private int returnEffectDuration() {
        return 0;
    }

    /**
     * TO DO
     * return gold cost for specific effect
     *
     * @return
     */
    private int returnEffectGoldCost() {
        return 0;
    }

    /**
     * TO DO
     * <p>
     * Generate potion method
     * A potion is comprised from 2-3 ingredients which share at least one common effect
     * Multiple effect potions can be made, multipliers for each effect therefore must be calculated
     * <p>
     * If player possesses purity perk, all negative effects are removed from potions and all positive effects are
     * removed from poisons
     * Multi effect: the strongest effect determines the primary type (whether it's a potion or poison)
     * <p>
     * This means the value will be recalculated as the potion will lose an effect
     * <p>
     * I suggest you create some further helper methods
     *
     * @param ingredients
     * @return
     */
    private Potion makePotion(Player player, Ingredient[] ingredients) {
        return null;
    }

    //event handler private classes
    private class AboutApplicationHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
        }
    }

    private class AddEnchantmentHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
        }
    }

    private class AddIngredientHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
        }
    }

    private class AddItemHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
        }
    }

    private class ClearPlayerDetailsHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
        }
    }

    private class RemoveEnchantmentHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
        }
    }

    private class RemoveIngredientHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
        }
    }

    private class RemoveItemHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
        }
    }

    private class ResetDetailsHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
        }
    }

    /**
     * Submit player details and move to ingredients screen
     **/
    private class SubmitPlayerDetailsHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            model.setAlchemyLevel(Integer.parseInt(pdp.getAlchemyLevelTxt()));
        }
    }

}

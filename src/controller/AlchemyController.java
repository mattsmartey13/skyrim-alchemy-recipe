package controller;

import model.*;
import view.*;

import java.util.*;
import java.io.IOException;
import java.nio.file.Paths;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;


/**
 * TO DO:
 *
 * BACKEND:
 *  Load ingredients from JSON [done]
 *  Load effects from JSON [done]
 *  Load perks from JSON [done]
 *  Load item types from JSON [done]
 *  Test JSON imports
 *  Is effect present in ingredient?
 *  Get effect object from has value
 *  Get common effects from ingredients
 *  Collate perk multipliers
 *  Get enchantment multipliers
 *  Purity/benefactor/poisoner effect removal
 *  Apply result formula to obtain final alchemy multiplier
 *  Calculate effect strength
 *  Calculate effect time
 *  Calculate effect value
 *  Create potion
 *
 *  FRONTEND
 *  Event handlers
 *
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
        List<Effect> effects = getEffectsFromData();
        List<Ingredient> ingredients = getIngredientsFromData();
        List<Item> items = getItemsFromData();
        List<Perk> perks = getPerksFromData();


        //attach event handlers using private helper method
        //this.attachEventHandlers();
    }

    private List<Effect> getEffectsFromData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get("src/data/effects.json").toFile(), new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private List<Ingredient> getIngredientsFromData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Jdk8Module());
            return mapper.readValue(Paths.get("src/data/ingredients.json").toFile(), new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private List<Item> getItemsFromData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get("src/data/items.json").toFile(), new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Perk> getPerksFromData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get("src/data/perks.json").toFile(), new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Helper method to attach event handlers
     */
    private void attachEventHandlers() {
        this.amb.addExitEventHandler((e) -> {
            System.exit(0);
        });
        this.pdp.addSubmitEventHandler(new SubmitPlayerDetailsHandler());
    }

    /**
     * Helper method to find if an ingredient possesses an effect.
     *
     * @param ingredient
     * @return boolean
     **/
    private boolean isEffectPresent(Ingredient ingredient, String hashValue) {
//        Effect effect = getEffectByHashValue(hashValue);
//        if (effect != null) {
//            if (ingredient.getAllEffectsForIngredient().contains(effect)) {
//                return true;
//            }
//        }
        return false;
    }

    /**
     * Helper method to obtain effect from its in game hashValue.
     *
     * @param hashValue
     * @return String
     */
//    private Effect getEffectByHashValue(String hashValue) {
//        for (Effect e : ) {
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
//    private static List<Effect> getDuplicateEffects(Collection<Effect> list) {
//        final List<Effect> duplicatedEffects = new ArrayList<Effect>();
//        Set<Effect> set = new HashSet<Effect>() {
//            @Override
//            public boolean add(Effect e) {
//                if (contains(e)) {
//                    duplicatedEffects.add(e);
//                }
//                return super.add(e);
//            }
//        };
//
//        set.addAll(list);
//        return duplicatedEffects;
//    }

    /**
     * @param list
     * @return boolean
     */
//    private static boolean hasDuplicateEffect(Collection<Effect> list) {
//        return !getDuplicateEffects(list).isEmpty();
//    }

    /**
     * The alchemy bonus from equipment is the sum of all item enchantments.
     *
     * @param player
     * @return double
     */
//    private double getEnchantmentPerkMultiplier(Player player) {
//        double improvement = 100;
////        for (Item e : player.getPlayerGear()) {
////            improvement += e.getArmorEnchantement().getPercentImprovement();
////        }
//        return improvement / 100;
//    }

    /**
     * 10% bonus to Potion strength after obtaining the Seeker of Shadows perk from the Dragonborn DLC.
     *
     * @param player
     * @return double
     */
//    private double getSeekerShadowsPerkMultiplier(Player player) {
//        for (String hash : player.getPlayerPerks()) {
//            if (Objects.equals(hash, "xx034838"))
//                return 1.1;
//        }
//        return 1.0;
//    }

    /**
     * The player will see a 20% boost to their potion by increasing their Alchemist perk levels
     * A level 5 perk will see the player double their potion strength.
     *
     * @param player
     * @return double
     */
//    private double getAlchemistPerkMultiplier(Player player) {
//        for (String hash : player.getPlayerPerks()) {
//            if (Objects.equals(hash, "000c07cd"))
//                return 2.0;
//            else if (Objects.equals(hash, "000c07cc"))
//                return 1.8;
//            else if (Objects.equals(hash, "000c07cb"))
//                return 1.6;
//            else if (Objects.equals(hash, "000c07ca"))
//                return 1.4;
//            else if (Objects.equals(hash, "000be127"))
//                return 1.2;
//        }
//        return 1.0;
//    }

    /**
     * The potion's strength will intensify by 25% if any of its effects restore stats  (health, magicka, stamina)
     * and the Player has the Physician perk equipped.
     *
     * @param player
     * @return double
     */
//    private double getPhysicianPerkMultiplier(Player player, Effect effect) {
//        String restoreHealth = "00042503";
//        String restoreMagicka = "00042508";
//        String restoreStamina = "00042503";
//        String physician = "00058215";
//
//        boolean physicianEffects = false;
//        boolean physicianPerk = false;
//
//        for (String hash : player.getPlayerPerks()) {
//            if (Objects.equals(hash, physician)) {
//                physicianPerk = true;
//            }
//        }
//
//        if (restoreHealth.equals(effect.getHashValue()) || restoreMagicka.equals(effect.getHashValue()) || restoreStamina.equals(effect.getHashValue())) {
//            physicianEffects = true;
//        }
//
//        if (physicianEffects && physicianPerk)
//            return 1.25;
//        else
//            return 1.0;
//    }

    /**
     * The potion's strength will increase by 25% if the Player has the benefactor perk equipped and if
     * the potion contains positive effects to the Player.
     *
     * @param player The player
     * @return double
     */
//    private double getBenefactorPerkMultiplier(Player player, Effect effect) {
//        String benefactor = "00058216";
//        boolean benefactorPerk = false;
//
//        for (String hash : player.getPlayerPerks()) {
//            if (Objects.equals(hash, benefactor) && Objects.equals(effect.getStyle(), "Helpful")) {
//                benefactorPerk = true;
//                break;
//            }
//        }
//
//        if (benefactorPerk)
//            return 1.2;
//        else
//            return 1.0;
//    }

    /**
     * The potion's strength will increase by 25% if the Player has the poisoner perk equipped and if
     * the potion contains negative effects to the Player.
     **/
//    private double getPoisonerPerkMultiplier(Player player, Effect effect) {
//        String poisoner = "00042509";
//        boolean poisonerPerk = false;
//
//        for (String hash : player.getPlayerPerks()) {
//            if (Objects.equals(hash, poisoner) && Objects.equals(effect.getStyle(), "Harmful")) {
//                poisonerPerk = true;
//                break;
//            }
//        }
//
//        if (poisonerPerk)
//            return 1.2;
//        else
//            return 1.0;
//    }

    /**
     * Accounts for all multipliers and gives a final multiplier value for this specific effect
     *
     * @param player
     * @return final multiplier
     */
//    private double returnEffectStrengthMultiplier(Player player, Effect effect) {
//        //set values
//        final int fAlchemyIngredientInitMult = 4;
//        final double fAlchemySkillFactor = 1.5;
//
//        //player specific perks
//        double seekerShadow = getSeekerShadowsPerkMultiplier(player);
//        double alchemistPerk = getAlchemistPerkMultiplier(player);
//        double enchantments = getEnchantmentPerkMultiplier(player);
//
//        //ingredient based perks
//        double physician = getPhysicianPerkMultiplier(player, effect);
//        double benefactor = getBenefactorPerkMultiplier(player, effect);
//        double poisoner = getPoisonerPerkMultiplier(player, effect);
//
//        double result = fAlchemyIngredientInitMult
//                * effect.getBaseMag()
//                * (1 + (fAlchemySkillFactor - 1) * player.getAlchemyLevel() / 100)
//                * alchemistPerk
//                * benefactor
//                * physician
//                * poisoner
//                * enchantments
//                * seekerShadow;
//
//        if (result < 0)
//            return effect.getBaseMag();
//        return result;
//    }

    /**
     * Submit player details and move to ingredients screen
     **/
    private class SubmitPlayerDetailsHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            model.setAlchemyLevel(Integer.parseInt(pdp.getAlchemyLevelTxt()));
        }
    }

}

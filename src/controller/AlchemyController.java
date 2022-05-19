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
 * SAR-18
 *  Load ingredients from JSON [done]
 *  Load effects from JSON [done]
 *  Load perks from JSON [done]
 *  Load item types from JSON [done]
 *  Test JSON imports [done]
 *  Is effect present in ingredient? [done]
 *  Get effect object from hash value [done]
 *  Get all ingredients with effect [done]
 *  Get all ingredients with same effects [done]
 *  Collate perk multipliers [done]
 *  Get enchantment multipliers [done]
 *  Benefactor/poisoner effect removal [done]
 *  Calculate base duration [done]
 *  Calculate base magnitude [done]
 *  Calculate base cost [done]
 *  Create potion
 *  Write controller tests
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

    private final List<Ingredient> allIngredients = getIngredientsFromData();

    private final List<Effect> allEffects = getEffectsFromData();

    private final List<Perk> allPerks = getPerksFromData();

    private final List<Item> allItems = getItemsFromData();

    /**
     * Constructor
     *
     * @param model the player with all their details
     * @param rootPane the rootpane with all panes attached
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

        //attach event handlers using private helper method
        //this.attachEventHandlers();
    }

    /**
     * Extract effects from JSON into a list of Effect objects
     * @return the list of effect objects
     */
    private List<Effect> getEffectsFromData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get("src/data/effects.json").toFile(), new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Extract ingredients from JSON into a list of Ingredient objects
     * @return the list of ingredient objects
     */
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

    /**
     * Extract items from JSON into a list of Item objects
     * @return the list of item objects
     */
    private List<Item> getItemsFromData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get("src/data/items.json").toFile(), new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extract perks from JSON into a list of Perk objects
     * @return the list of perk objects
     */
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
     * @param ingredient The ingredient
     * @return boolean
     **/
    private boolean isEffectPresent(Ingredient ingredient, String effectHash) {
        String[] effects = ingredient.getEffects();
        return Arrays.stream(effects).anyMatch(effectHash::contains);
    }

    /**
     * Helper method to obtain effect from its in game hash value.
     *
     * @param effectHash the effect's associated hashcode
     * @return The effect with all its properties
     */
    private Effect getEffectByHash(String effectHash) {
        for (Effect e : Objects.requireNonNull(allEffects)) {
            if (Objects.equals(effectHash, e.getHash())) {
                return e;
            }
        }
        return null;
    }

    /**
     * Helper method to obtain ingredient from its in game hash value
     * @param ingredientHash the ingredient's hash code
     * @return the ingredient with all its properties
     */
    public Ingredient getIngredientFromHash(String ingredientHash) {
        for (Ingredient i : Objects.requireNonNull(allIngredients)) {
            if (Objects.equals(ingredientHash, i.getHash())) {
                return i;
            }
        }
        return null;
    }

    /**
     * Take an effect and find all ingredients with effect
     *
     * @param effectHash the effect's hash
     * @param allIngredients the list of all ingredients
     * @return An ArrayList with all the matching ingredients
     */
    private ArrayList<Ingredient> getIngredientsWithEffect(String effectHash, List<Ingredient> allIngredients) {
        ArrayList<Ingredient> returnedIngredients = new ArrayList<>();

        for (Ingredient i : allIngredients) {
            if (Arrays.stream(i.getEffects()).anyMatch(effectHash::contains))
                returnedIngredients.add(i);
        }

        return returnedIngredients;
    }

    /**
     * Take an ingredient and find all ingredients that share at least one effect with it
     *
     * @param ingredient the ingredient we want common effects for
     * @param allIngredients List of all ingredients
     * @return the ingredients that have a commonality with the ingredient we want
     */
    private ArrayList<Ingredient> getAllCommonIngredients(Ingredient ingredient, List<Ingredient> allIngredients) {
        String[] ingredientEffects = ingredient.getEffects();
        ArrayList<Ingredient> returnedIngredients = new ArrayList<>();

        for (Ingredient ing : allIngredients) {
            for (String effect : ingredientEffects) {
                if (Arrays.stream(ing.getEffects()).anyMatch(effect::contains))
                    returnedIngredients.add(ing);
            }
        }

        return returnedIngredients;
    }

    private Perk getPerkFromHash(String perkHash) {
        for (Perk p : Objects.requireNonNull(allPerks)) {
            if (Objects.equals(perkHash, p.getHash())) {
                return p;
            }
        }
        return null;
    }

    private ArrayList<Double> getPerkMultiplierList(Player player) {
        ArrayList<Double> perkArrayList = new ArrayList<>();
        for (Perk perk : player.getPlayerPerks()) {
            perkArrayList.add(perk.getMultiplier());
        }
        return perkArrayList;
    }


    /**
     * Return base magnitude based on the player's parameters and the effect
     * @param player the player
     * @param effect the effect
     */
    private double getPotionBaseMagnitude(Player player, Effect effect) {
        List<Perk> playerPerks = player.getPlayerPerks();
        dropBenefactorOrPoisoner(effect, playerPerks);

        double result = getBaseDurAndMagValue(player) * effect.getBaseMag();
        List<Double> perkMultipliers = new ArrayList<>(getPerkMultiplierList(player));

        for (double perkMultiplier : perkMultipliers) {
            result *= perkMultiplier;
        }

        if (result < 0) {
            return Math.round(result);
        } else {
            return Math.round(effect.getBaseMag());
        }
    }

    /**
     * Return base duration based on the player's parameters and the effect
     * @param player the player
     * @param effect the effect
     * @return the potion's base duration
     */
    private double getPotionBaseDuration(Player player, Effect effect) {
        List<Perk> playerPerks = player.getPlayerPerks();
        dropBenefactorOrPoisoner(effect, playerPerks);

        double result = getBaseDurAndMagValue(player) * effect.getBaseDur();
        List<Double> perkMultipliers = new ArrayList<>(getPerkMultiplierList(player));

        for (double perkMultiplier : perkMultipliers) {
            result *= perkMultiplier;
        }

        if (result < 0) {
            return Math.round(result);
        } else {
            return Math.round(effect.getBaseDur());
        }
    }

    /**
     * Get the potion's base cost, following the obtaining of the magnitude and duration
     * @param effect The effect
     * @param magnitude The potion's base magnitude
     * @param duration The potion's base duration
     * @return The potion's base cost
     */
    private double getPotionBaseCost(Effect effect, double magnitude, double duration) {
        if (magnitude == 0) {
            return Math.floor(effect.getBaseCost() * Math.max(Math.pow(duration / 10, 1.1), 1));
        } else if (duration == 0) {
            return Math.floor(effect.getBaseCost() * Math.pow(magnitude, 1.1));
        } else {
            return Math.floor(effect.getBaseCost() * Math.max(Math.pow(magnitude, 1.1), 1) * Math.pow(duration / 10, 1.1));
        }
    }

    /**
     * Hardcoded method to drop Benefactor if creating a Poison, or drop Poisoner if creating a Potion
     * @param effect the effect
     * @param playerPerks the list of the player's perks
     */
    private void dropBenefactorOrPoisoner(Effect effect, List<Perk> playerPerks) {
        if (Objects.equals(effect.getEffect(), "Helpful")) {
            if (playerPerks.contains(getPerkFromHash("00058217"))) {
                Objects.requireNonNull(playerPerks).remove(getPerkFromHash("00058217"));
            }
        }

        if (Objects.equals(effect.getEffect(), "Harmful")) {
            if (playerPerks.contains(getPerkFromHash("00058216"))) {
                Objects.requireNonNull(playerPerks).remove(getPerkFromHash("00058216"));
            }
        }
    }

    private double getBaseDurAndMagValue(Player player) {
        int alchemyInitMulti = 4;
        double skillMultiplier = 1.5 * ((double) player.getAlchemyLevel() / 100);

        List<Integer> enchantments = new ArrayList<>();
        player.getItems().forEach(item -> enchantments.add(item.getAlchemyBoost()));

        int sumOfEnchantments;
        if (enchantments.stream().mapToInt(i -> i).sum() != 0) {
            sumOfEnchantments = enchantments.stream().mapToInt(i -> i).sum();
        } else {
            sumOfEnchantments = 1;
        }

        return alchemyInitMulti * skillMultiplier * sumOfEnchantments;
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

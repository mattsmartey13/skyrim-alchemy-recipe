package controller;

import model.*;
import view.*;

import java.util.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * TO DO:
 *
 * SAR-18
 *  Create potion [done]
 *  Ensure controller methods do not generate errors [done]
 *  Error free does not mean bug free [done]
 *
 * SAR-28
 *  Write controller tests
 *  This will confirm the lack of bugs
 *
 * SAR-21
 *  User pane
 *  Ingredient pane - addition of ingredient quantities
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
     * @param effectHash the effect's hash code
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
    private double getPotionEffectBaseMagnitude(Player player, Effect effect) {
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
    private double getPotionEffectBaseDuration(Player player, Effect effect) {
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
     * @return The potion's base cost
     */
    private double getPotionEffectBaseCost(Effect effect) {
        if (effect.getBaseMag() == 0) {
            return Math.floor(effect.getBaseCost() * Math.max(Math.pow((double) effect.getBaseDur() / 10, 1.1), 1));
        } else if (effect.getBaseDur() == 0) {
            return Math.floor(effect.getBaseCost() * Math.pow(effect.getBaseMag(), 1.1));
        } else {
            return Math.floor(effect.getBaseCost() * Math.max(Math.pow(effect.getBaseMag(), 1.1), 1) * Math.pow((double) effect.getBaseDur() / 10, 1.1));
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
     * Key method that finds common effect hashes between ingredients
     * @param ingredients the two or three ingredients
     * @return hashset with the effects
     */
    private HashSet<Effect> getCommonEffects(ArrayList<Ingredient> ingredients) {
        HashSet<Effect> effects = new HashSet<>();
        ArrayList<String> allEffects = new ArrayList<>();

        for (Ingredient ing : ingredients) {
            allEffects.addAll(Arrays.asList(ing.getEffects()));
        }

        for (String eff : allEffects)
            if (allEffects.lastIndexOf(eff) != allEffects.indexOf(eff)) {
                // you have at least two ingredients with the effect
                Effect effect = getEffectByHash(eff);
                effects.add(effect);
            }

        return effects;
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

    /**
     * Tasks:
     *  Obtain effects from ingredients [done]
     *  Change ingredients.json keys to effect hash codes [done]
     *  Change model fields, getter setters etc to match database changes [done]
     *  Multiplier changes to effects [done]
     *  Sort by effect strength
     *  Enchantments etc.
     *  Use player fields to get final potion values
     *  Make potion
     *  Finish toString potion method
     *
     * @param player the player
     * @param ingredients the ingredients
     * @return potion
     */
    private Potion makePotion(Player player, ArrayList<Ingredient> ingredients) {
        String header;

        //get ingredients in common
        HashSet<Effect> potionEffects = getCommonEffects(ingredients);

        if (!potionEffects.isEmpty()) {

            //multiply duration, strength and cost by multipliers
            //instead of creating new effect, we must modify the existing one
            for (Ingredient ingredient : ingredients) {
                //if any have a magnitude effect
                //find the matching pairs
                if (ingredient.getMagnitudeEffect().isPresent()) {
                    for (Map.Entry<String, Double> pair : ingredient.getMagnitudeEffect().get().entrySet()) {
                        if (doesHashMatchEffect(potionEffects, pair)) {
                            Effect effect = getEffectByHash(pair.getKey());
                            if (effect != null) {
                                effect.setBaseMag((int) (effect.getBaseMag() * pair.getValue()));
                                potionEffects.remove(getEffectByHash(pair.getKey()));
                                potionEffects.add(effect);
                            }
                        }
                    }
                }

                if (ingredient.getMagnitudeValue().isPresent()) {
                    //if any have a magnitude value
                    //find the matching pairs
                    for (Map.Entry<String, Double> pair : ingredient.getMagnitudeValue().get().entrySet()) {
                        if (doesHashMatchEffect(potionEffects, pair)) {
                            Effect effect = getEffectByHash(pair.getKey());
                            if (effect != null) {
                                effect.setBaseCost((int) (effect.getBaseCost() * pair.getValue()));
                                potionEffects.remove(getEffectByHash(pair.getKey()));
                                potionEffects.add(effect);
                            }
                        }
                    }
                }

                if (ingredient.getMagnitudeTime().isPresent()) {
                    //if any have a magnitude time
                    //find the matching pairs
                    for (Map.Entry<String, Double> pair : ingredient.getMagnitudeTime().get().entrySet()) {
                        if (doesHashMatchEffect(potionEffects, pair)) {
                            Effect effect = getEffectByHash(pair.getKey());
                            if (effect != null) {
                                effect.setBaseDur((int) (effect.getBaseDur() * pair.getValue()));
                                potionEffects.remove(getEffectByHash(pair.getKey()));
                                potionEffects.add(effect);
                            }
                        }
                    }
                }
            }

            //sort by effect intensity
            if (durationVariantEffects(potionEffects))
                potionEffects = potionEffects.stream().sorted(Comparator.comparingInt(Effect::getBaseDur).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
            else {
                potionEffects = potionEffects.stream().sorted(Comparator.comparingInt(Effect::getBaseMag).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
            }

            String mainEffect = potionEffects.stream().findFirst().get().getEffect();

            if (Objects.equals(mainEffect, "Helpful")) {
                header = "Potion of ";
            } else {
                header = "Poison of ";
            }

            //purity modifications
            if (player.getPlayerPerks().stream().anyMatch(perk -> Objects.equals(perk.getName(), "Purity")) && potionEffects.stream().findFirst().isPresent()) {
                potionEffects = (HashSet<Effect>) potionEffects.stream().filter(effect -> Objects.equals(effect.getEffect(), mainEffect)).collect(Collectors.toSet());
            }

            String mainName = potionEffects.stream().findFirst().get().getName();

            for (Effect effect : potionEffects) {
                effect.setBaseMag((int) (effect.getBaseMag() * getPotionEffectBaseMagnitude(player, effect)));
                effect.setBaseDur((int) (effect.getBaseDur() * getPotionEffectBaseDuration(player, effect)));
                effect.setBaseCost(effect.getBaseCost() * getPotionEffectBaseCost(effect));
            }

            return new Potion(header + mainName, potionEffects);
        }

        return null;
    }

    private boolean durationVariantEffects(HashSet<Effect> effects) {
        return effects.contains(getEffectByHash("0003EB3D")) || effects.contains(getEffectByHash("00073F30")) || effects.contains(getEffectByHash("00073F25")) || effects.contains(getEffectByHash("0003AC2D"));
    }

    private boolean doesHashMatchEffect(HashSet<Effect> potionEffects, Map.Entry<String, Double> pair) {
        return potionEffects.stream().anyMatch(effect -> effect.getHash().equals(pair.getKey()));
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

package controller;

import model.*;
import view.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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

    private final Player model;
//    private AlchemyRootPane rootPane;
//    private AlchemyMenuBar amb;
//    private IngredientEnchantmentPane ipep;
//    private PlayerDetailsPane pdp;
//    private ViewRecipePane vrp;

    private final List<Ingredient> allIngredients;

    private final List<Effect> allEffects;

    private final List<Perk> allPerks;

    private final List<Item> allItems;

    /**
     * Constructor
     *
     * @param model the player with all their details
     *
     */
    public AlchemyController(Player model) {
        //initialise player entity and load database;
        this.model = model;
        allEffects = this.getEffectsFromData("src/data/effects.json");
        allIngredients = this.getIngredientsFromData("src/data/ingredients.json");
        allItems = this.getItemsFromData("src/data/items.json");
        allPerks = this.getPerksFromData("src/data/perks.json");

//        this.rootPane = rootPane;
//        this.amb = rootPane.getAlchemyMenuBar();
//        this.ipep = rootPane.getIngredientEnchantmentPane();
//        this.pdp = rootPane.getPlayerDetailsPane();
//        this.vrp = rootPane.getViewRecipePane();

        //populate comboboxes

        //attach event handlers using private helper method
        //this.attachEventHandlers();
    }

    /**
     * Extract effects from JSON into a list of Effect objects
     * @return the list of effect objects
     */
    protected List<Effect> getEffectsFromData(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(path).toFile(), new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Extract ingredients from JSON into a list of Ingredient objects
     * @return the list of ingredient objects
     */
    protected List<Ingredient> getIngredientsFromData(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Jdk8Module());
            return mapper.readValue(Paths.get(path).toFile(), new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Extract items from JSON into a list of Item objects
     * @return the list of item objects
     */
    protected List<Item> getItemsFromData(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(path).toFile(), new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extract perks from JSON into a list of Perk objects
     * @return the list of perk objects
     */
    protected List<Perk> getPerksFromData(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(path).toFile(), new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

//    /**
//     * Helper method to attach event handlers
//     */
//    private void attachEventHandlers() {
//        this.amb.addExitEventHandler((e) -> {
//            System.exit(0);
//        });
//        this.pdp.addSubmitEventHandler(new SubmitPlayerDetailsHandler());
//    }

    /**
     * Helper method to find if an ingredient possesses an effect.
     *
     * @param ingredient The ingredient
     * @return boolean
     **/
    protected boolean isEffectPresent(Ingredient ingredient, String effectHash) {
        String[] effects = ingredient.getEffects();
        return Arrays.stream(effects).anyMatch(effectHash::contains);
    }

    /**
     * Helper method to obtain effect from its in game hash value.
     *
     * @param effectHash the effect's hash code
     * @return The effect with all its properties
     */
    protected Effect getEffectFromHash(String effectHash, List<Effect> effects) {
        for (Effect e : Objects.requireNonNull(effects)) {
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
    protected Ingredient getIngredientFromHash(String ingredientHash, List<Ingredient> ingredientList) {
        for (Ingredient i : Objects.requireNonNull(ingredientList)) {
            if (Objects.equals(ingredientHash, i.getHash())) {
                return i;
            }
        }
        return null;
    }

    protected Perk getPerkFromHash(String perkHash, List<Perk> perkList) {
        for (Perk p : Objects.requireNonNull(perkList)) {
            if (Objects.equals(perkHash, p.getHash())) {
                return p;
            }
        }
        return null;
    }

    protected List<Double> getPerkMultiplierList(List<Perk> playerPerkList) {
        List<Double> perkList = new ArrayList<>();
        for (Perk perk : playerPerkList) {
            perkList.add(perk.getMultiplier());
        }

        if (!perkList.isEmpty())
            return perkList;
        else
            return null;
    }


    /**
     * Return base magnitude based on the player's parameters and the effect
     * @param player the player
     * @param effect the effect
     */
    protected double getPotionEffectBaseMagnitude(Player player, Effect effect) {
        List<Perk> playerPerks = player.getPlayerPerks();
        if (!playerPerks.isEmpty()) {
            dropBenefactorOrPoisoner(effect, playerPerks, allPerks);
            dropPhysician(effect, playerPerks, allPerks);
        }

        double result = getBaseDurAndMagValue(player) * effect.getBaseMag();

        if (!playerPerks.isEmpty()) {
            List<Double> perkMultipliers = new ArrayList<>(getPerkMultiplierList(playerPerks));

            for (double perkMultiplier : perkMultipliers) {
                result *= perkMultiplier;
            }
        }

        if (result > 0) {
            return round(result, 3);
        } else {
            return effect.getBaseMag();
        }
    }

    /**
     * Return base duration based on the player's parameters and the effect
     * @param player the player
     * @param effect the effect
     * @return the potion's base duration
     */
    protected int getPotionEffectBaseDuration(Player player, Effect effect) {
        List<Perk> playerPerks = player.getPlayerPerks();
        if (!playerPerks.isEmpty()) {
            dropBenefactorOrPoisoner(effect, playerPerks, allPerks);
            dropPhysician(effect, playerPerks, allPerks);
        }

        double result = getBaseDurAndMagValue(player) * effect.getBaseDur();

        if (!playerPerks.isEmpty()) {
            List<Double> perkMultipliers = new ArrayList<>(getPerkMultiplierList(playerPerks));
            for (double perkMultiplier : perkMultipliers) {
                result *= perkMultiplier;
            }
        }


        if (result > 0) {
            return (int) round(result, 0);
        } else {
            return effect.getBaseDur();
        }
    }

    /**
     * Get the potion's base cost, following the obtaining of the magnitude and duration
     * @param effect The effect
     * @return The potion's base cost
     */
    protected double getPotionEffectBaseCost(Effect effect) {
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
    protected void dropBenefactorOrPoisoner(Effect effect, List<Perk> playerPerks, List<Perk> perksList) {
        if (Objects.equals(effect.getEffect(), "Helpful")) {
            if (playerPerks.contains(getPerkFromHash("00058217", perksList))) {
                Objects.requireNonNull(playerPerks).remove(getPerkFromHash("00058217", perksList));
            }
        }

        if (Objects.equals(effect.getEffect(), "Harmful")) {
            if (playerPerks.contains(getPerkFromHash("00058216", perksList))) {
                Objects.requireNonNull(playerPerks).remove(getPerkFromHash("00058216", perksList));
            }
        }
    }

    /**
     * Drop physician perk if the effect does not restore health, stamina or magicka
     * @param effect the effect
     * @param playerPerks the player's perks
     */
    protected void dropPhysician(Effect effect, List<Perk> playerPerks, List<Perk> perksList) {
        if (!Objects.equals(effect.getHash(), "0003EB15") && !Objects.equals(effect.getHash(), "0003EB17") && !Objects.equals(effect.getHash(), "0003EB16")) {
            if (playerPerks.contains(getPerkFromHash("00058215", perksList))) {
                Objects.requireNonNull(playerPerks).remove(getPerkFromHash("00058215", perksList));
            }
        }
    }

    /**
     * Base method used as the precursor to duration and mag calculations, takes into account enchantments and the player's alchemy level
     * @param player the player
     * @return the base value to be multiplied against the effect's values
     */
    protected double getBaseDurAndMagValue(Player player) {
        int alchemyInitMulti = 4;
        double skillMultiplier = 1.5 * ((double) player.getAlchemyLevel() / 100);

        List<Integer> enchantments = new ArrayList<>();
        player.getItems().forEach(item -> enchantments.add(item.getAlchemyBoost()));

        double sumOfEnchantments;
        if (enchantments.stream().mapToDouble(i -> i).sum() != 0) {
            sumOfEnchantments = 1 + ((enchantments.stream().mapToDouble(i -> i).sum()) / 100);
        } else {
            sumOfEnchantments = 1;
        }

        return round(alchemyInitMulti * skillMultiplier * sumOfEnchantments, 2);
    }

    /**
     * Key method that finds common effect hashes between ingredients
     * @param ingredients the two or three ingredients
     * @return hashset with the effects
     */
    protected HashSet<Effect> getCommonEffects(List<Ingredient> ingredients, List<Effect> effectList) {
        HashSet<Effect> commonEffects = new HashSet<>();
        List<String> effectsList = new ArrayList<>();

        for (Ingredient ing : ingredients) {
            effectsList.addAll(Arrays.asList(ing.getEffects()));
        }

        for (String eff : effectsList)
            if (effectsList.lastIndexOf(eff) != effectsList.indexOf(eff)) {
                // you have at least two ingredients with the effect
                Effect effect = getEffectFromHash(eff, effectList);
                commonEffects.add(effect);
            }

        if (!commonEffects.isEmpty())
            return commonEffects;
        else
            return null;
    }

    /**
     * Take an ingredient and find all ingredients that share at least one effect with it
     *
     * @param ingredient the ingredient we want common effects for
     * @param allIngredients List of all ingredients
     * @return the ingredients that have a commonality with the ingredient we want
     */
    protected List<Ingredient> getAllCommonIngredients(Ingredient ingredient, List<Ingredient> allIngredients) {
        ArrayList<Ingredient> returnedIngredients = new ArrayList<>();

        for (String effect: ingredient.getEffects()) {
            for (Ingredient ing : allIngredients) {
                if (Arrays.stream(ing.getEffects()).anyMatch(effect::contains) && ingredient != ing) {
                    returnedIngredients.add(ing);
                }
            }
        }

        if (!returnedIngredients.isEmpty())
            return returnedIngredients;
        else
            return null;
    }

    /**
     * Tasks:
     *  Obtain effects from ingredients [done]
     *  Change ingredients.json keys to effect hash codes [done]
     *  Change model fields, getter setters etc to match database changes [done]
     *  Multiplier changes to effects [done]
     *  Sort by effect strength [done]
     *  Enchantments etc. [done]
     *  Use player fields to get final potion values [done]
     *  Make potion [done]
     *  Finish toString potion method [done]
     *
     * @param player the player
     * @param ingredients the ingredients
     * @return potion
     */
    protected Potion makePotion(Player player, List<Ingredient> ingredients) {
        String header;

        //get ingredients in common
        HashSet<Effect> potionEffects = getCommonEffects(ingredients, allEffects);

        if (!potionEffects.isEmpty()) {

            //multiply duration, strength and cost by multipliers
            //instead of creating new effect, we must modify the existing one
            for (Ingredient ingredient : ingredients) {
                //if any have a magnitude effect
                //find the matching pairs
                if (ingredient.getMagnitudeEffect().isPresent()) {
                    for (Map.Entry<String, Double> pair : ingredient.getMagnitudeEffect().get().entrySet()) {
                        if (doesHashMatchEffect(potionEffects, pair)) {
                            Effect effect = getEffectFromHash(pair.getKey(), allEffects);
                            if (effect != null) {
                                effect.setBaseMag((int) (effect.getBaseMag() * pair.getValue()));
                                potionEffects.remove(getEffectFromHash(pair.getKey(), allEffects));
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
                            Effect effect = getEffectFromHash(pair.getKey(), allEffects);
                            if (effect != null) {
                                effect.setBaseCost((int) (effect.getBaseCost() * pair.getValue()));
                                potionEffects.remove(getEffectFromHash(pair.getKey(), allEffects));
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
                            Effect effect = getEffectFromHash(pair.getKey(), allEffects);
                            if (effect != null) {
                                effect.setBaseDur((int) (effect.getBaseDur() * pair.getValue()));
                                potionEffects.remove(getEffectFromHash(pair.getKey(), allEffects));
                                potionEffects.add(effect);
                            }
                        }
                    }
                }
            }

            //sort by effect intensity
            if (durationVariantEffects(potionEffects)) {
                potionEffects = potionEffects.stream().sorted(Comparator.comparingInt(Effect::getBaseDur).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
            } else {
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
                effect.setBaseMag((int) getPotionEffectBaseMagnitude(player, effect));
                effect.setBaseDur(getPotionEffectBaseDuration(player, effect));
                effect.setBaseCost(getPotionEffectBaseCost(effect));

                if (effect.getDescription().contains("<mag>")) {
                    effect.setDescription(effect.getDescription().replace("<mag>", Integer.toString(effect.getBaseMag())));
                } else if (effect.getDescription().contains("<dur>")) {
                    effect.setDescription(effect.getDescription().replace("<dur>", Integer.toString(effect.getBaseDur())));
                }
            }

            return new Potion(header + mainName, potionEffects);
        }

        return null;
    }

    private boolean durationVariantEffects(HashSet<Effect> effects) {
        return effects.contains(getEffectFromHash("0003EB3D", allEffects)) || effects.contains(getEffectFromHash("00073F30", allEffects)) || effects.contains(getEffectFromHash("00073F25", allEffects)) || effects.contains(getEffectFromHash("0003AC2D", allEffects));
    }

    private boolean doesHashMatchEffect(HashSet<Effect> potionEffects, Map.Entry<String, Double> pair) {
        return potionEffects.stream().anyMatch(effect -> effect.getHash().equals(pair.getKey()));
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.CEILING);
        return bd.doubleValue();
    }

//    /**
//     * Submit player details and move to ingredients screen
//     **/
//    private class SubmitPlayerDetailsHandler implements EventHandler<ActionEvent> {
//
//        public void handle(ActionEvent e) {
//            model.setAlchemyLevel(Integer.parseInt(pdp.getAlchemyLevelTxt()));
//        }
//    }

}

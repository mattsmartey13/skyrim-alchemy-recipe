package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import model.*;
import org.paukov.combinatorics3.Generator;
import org.paukov.combinatorics3.IGenerator;
import view.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * TO DO:
 * Debug value, duration and magnitude of generated potions [todo]
 * Potion details pane for debugging and functionality [in progress]
 *
 * @author matth
 */
public class AlchemyController {

    private final Player model;
    private AlchemyRootPane rootPane;
    private AlchemyMenuBar amb;
    private PlayerDetailsPane pdp;
    private PotionGenerationPane pgp;

    private final HashSet<Ingredient> allIngredients;
    private final HashSet<Effect> allEffects;

    private final HashSet<Perk> allPerks;
    private final HashSet<Item> allItems;

    public final String POISON_OF = "Poison of ";
    public final String POTION_OF = "Potion of ";

    /**
     * Constructor
     *
     * @param model the player with all their details
     */
    public AlchemyController(Player model, AlchemyRootPane rootPane) {
        //initialise player entity and load database;
        this.model = model;
        allEffects = this.getEffectsFromData("src/data/effects.json");
        allIngredients = this.getIngredientsFromData("src/data/ingredients.json");
        allItems = this.getItemsFromData("src/data/items.json");
        allPerks = this.getPerksFromData("src/data/perks.json");

        this.rootPane = rootPane;
        this.pdp = rootPane.getPlayerDetailsPane();
        this.amb = rootPane.getAlchemyMenuBar();
        this.pgp = rootPane.getPotionGenerationPane();

        //populate comboboxes
        this.pgp.populateIngredientCombobox(allIngredients);

        //attach event handlers using private helper method
        this.attachEventHandlers();
    }

    /**
     * Extract effects from JSON into a list of Effect objects
     *
     * @return the list of effect objects
     */
    protected HashSet<Effect> getEffectsFromData(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Jdk8Module());
            return mapper.readValue(Paths.get(path).toFile(), new TypeReference<>() {
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Extract ingredients from JSON into a list of Ingredient objects
     *
     * @return the list of ingredient objects
     */
    protected HashSet<Ingredient> getIngredientsFromData(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Jdk8Module());
            return mapper.readValue(Paths.get(path).toFile(), new TypeReference<>() {
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Extract items from JSON into a list of Item objects
     *
     * @return the list of item objects
     */
    protected HashSet<Item> getItemsFromData(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Jdk8Module());
            return mapper.readValue(Paths.get(path).toFile(), new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extract perks from JSON into a list of Perk objects
     *
     * @return the list of perk objects
     */
    protected HashSet<Perk> getPerksFromData(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Jdk8Module());
            return mapper.readValue(Paths.get(path).toFile(), new TypeReference<>() {
            });
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
        this.pdp.addClearEventHandler(new ClearEventHandler());

        this.pgp.addIngredientHandler(new AddIngredientToListHandler());
        this.pgp.addAddAllIngredientsHandler(new AddAllIngredientsHandler());
        this.pgp.removeIngredientHandler(new RemoveIngredientFromListHandler());
        this.pgp.removeAllIngredientsHandler(new RemoveAllIngredientsHandler());
    }

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
    protected Effect getEffectFromHash(String effectHash, HashSet<Effect> effects) {
        for (Effect e : Objects.requireNonNull(effects)) {
            if (Objects.equals(effectHash, e.getHash())) {
                return e;
            }
        }
        return null;
    }

    /**
     * Helper method to obtain ingredient from its in game hash value
     *
     * @param ingredientHash the ingredient's hash code
     * @return the ingredient with all its properties
     */
    protected Ingredient getIngredientFromHash(String ingredientHash, HashSet<Ingredient> ingredientList) {
        for (Ingredient i : Objects.requireNonNull(ingredientList)) {
            if (Objects.equals(ingredientHash, i.getHash())) {
                return i;
            }
        }
        return null;
    }

    protected Perk getPerkFromHash(String perkHash, HashSet<Perk> perkList) {
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
     *
     * @param player the player
     * @param effect the effect
     */
    protected double getPotionEffectBaseMagnitude(Player player, Effect effect, HashSet<Perk> perkHashSet) {
        List<Perk> playerPerks = player.getPlayerPerks();
        if (!playerPerks.isEmpty()) {
            dropBenefactorOrPoisoner(effect, playerPerks, perkHashSet);
            dropPhysician(effect, playerPerks, perkHashSet);
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
     *
     * @param player the player
     * @param effect the effect
     * @return the potion's base duration
     */
    protected int getPotionEffectBaseDuration(Player player, Effect effect, HashSet<Perk> perkHashSet) {
        List<Perk> playerPerks = player.getPlayerPerks();
        if (!playerPerks.isEmpty()) {
            dropBenefactorOrPoisoner(effect, playerPerks, perkHashSet);
            dropPhysician(effect, playerPerks, perkHashSet);
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
     *
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
     *
     * @param effect      the effect
     * @param playerPerks the list of the player's perks
     */
    protected void dropBenefactorOrPoisoner(Effect effect, List<Perk> playerPerks, HashSet<Perk> perksList) {
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
     *
     * @param effect      the effect
     * @param playerPerks the player's perks
     */
    protected void dropPhysician(Effect effect, List<Perk> playerPerks, HashSet<Perk> perksList) {
        if (!Objects.equals(effect.getHash(), "0003EB15") && !Objects.equals(effect.getHash(), "0003EB17") && !Objects.equals(effect.getHash(), "0003EB16")) {
            if (playerPerks.contains(getPerkFromHash("00058215", perksList))) {
                Objects.requireNonNull(playerPerks).remove(getPerkFromHash("00058215", perksList));
            }
        }
    }

    /**
     * Base method used as the precursor to duration and mag calculations, takes into account enchantments and the player's alchemy level
     *
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

    protected List<String> getCommonEffectHashes(List<Ingredient> ingredients) {
        List<String> effectCodes = new ArrayList<>();

        for (Ingredient ing : ingredients) {
            effectCodes.addAll(Arrays.asList(ing.getEffects()));
        }

        for (String eff : effectCodes)
            if (effectCodes.lastIndexOf(eff) == effectCodes.indexOf(eff)) {
                // you have at least two ingredients with the effect
                effectCodes.remove(eff);
            }

        return effectCodes;
    }

    /**
     * Key method that finds common effect hashes between ingredients
     *
     * @param ingredients the two or three ingredients
     * @return hashset with the effects
     */
    protected HashSet<Effect> getCommonEffects(List<Ingredient> ingredients, HashSet<Effect> effectList) {
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
     * @param ingredient     the ingredient we want common effects for
     * @param allIngredients List of all ingredients
     * @return the ingredients that have a commonality with the ingredient we want
     */
    protected List<Ingredient> getAllCommonIngredients(Ingredient ingredient, List<Ingredient> allIngredients) {
        ArrayList<Ingredient> returnedIngredients = new ArrayList<>();

        for (String effect : ingredient.getEffects()) {
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

    protected Potion makePotion(Player player, List<Ingredient> ingredients) {
        List<String> effectHashes = getCommonEffectHashes(ingredients);
        HashSet<Effect> potionEffects = new HashSet<>();
        int sumOfGold = 0;
        String header;

        for (String hash : effectHashes) {
            //base multiplier
            Effect effect = this.getEffectFromHash(hash, allEffects);
            effect.setBaseMag((int) getPotionEffectBaseMagnitude(player, effect, allPerks));
            effect.setBaseDur(getPotionEffectBaseDuration(player, effect, allPerks));
            effect.setBaseCost(getPotionEffectBaseCost(effect));

            potionEffects.add(effect);
        }

        //multipliers, throw values in description
        for (Effect effect : potionEffects) {
            this.multiplyEffectBaseValues(effect, ingredients);
            sumOfGold += effect.getBaseCost();

            if (effect.getDescription().contains("<mag>")) {
                effect.setDescription(effect.getDescription().replace("<mag>", Integer.toString(effect.getBaseMag())));
            } else if (effect.getDescription().contains("<dur>")) {
                effect.setDescription(effect.getDescription().replace("<dur>", Integer.toString(effect.getBaseDur())));
            }
        }

        //sort by effect intensity
        if (isEffectDurationBased(potionEffects)) {
            potionEffects = potionEffects.stream().sorted(Comparator.comparingInt(Effect::getBaseDur).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            potionEffects = potionEffects.stream().sorted(Comparator.comparingInt(Effect::getBaseMag).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        String mainEffect = potionEffects.stream().findFirst().get().getEffect();
        String mainName = potionEffects.stream().findFirst().get().getName();

        //purity modifications
        if (player.getPlayerPerks().stream().anyMatch(perk -> Objects.equals(perk.getName(), "Purity")) && potionEffects.stream().findFirst().isPresent()) {
            potionEffects = (HashSet<Effect>) potionEffects.stream().filter(effect -> Objects.equals(effect.getEffect(), mainEffect)).collect(Collectors.toSet());
        }

        if (Objects.equals(mainEffect, "Helpful")) {
            header = POTION_OF;
        } else {
            header = POISON_OF;
        }

        return new Potion(header + mainName, ingredients, potionEffects, sumOfGold);
    }

    protected void multiplyEffectBaseValues(Effect effect, List<Ingredient> ingredients) {
        double value;

        for (Ingredient ingredient : ingredients) {
            if (ingredient.getMagnitudeValue().isPresent()) {
                if (ingredient.getMagnitudeValue().get().containsKey(effect.getHash())) {
                    value = ingredient.getMagnitudeValue().get().get(effect.getHash());
                    effect.setBaseCost((int) (effect.getBaseCost() * value));
                }
            } if (ingredient.getMagnitudeTime().isPresent()) {
                if (ingredient.getMagnitudeTime().get().containsKey(effect.getHash())) {
                    value = ingredient.getMagnitudeTime().get().get(effect.getHash());
                    effect.setBaseDur((int) (effect.getBaseDur() * value));
                }
            } if (ingredient.getMagnitudeEffect().isPresent()) {
                if (ingredient.getMagnitudeEffect().get().containsKey(effect.getHash())) {
                    value = ingredient.getMagnitudeValue().get().get(effect.getHash());
                    effect.setBaseMag((int) (effect.getBaseMag() * value));
                }
            }
        }
    }

    protected boolean canIngredientsFormPotion(List<Ingredient> ingredients) {
        return false;
    }

    /**
     * Takes the list of existing ingredients and generate a list of potions
     * No combination can be repeated
     * <p>
     * TODO:
     * Better mechanism for the foreach loop [done]
     * Group ingredients via effects [done]
     * Valid combinationGenerator [done]
     * Make potions - potion mechanism is fine, but gold duration and mags multiply/concatenate for each method call???
     *
     * @param ingredientList the list of ingredients
     * @return the list of potions
     */
    private HashSet<Potion> getPotionsFromIngredientList(Player player, List<Ingredient> ingredientList) {
        HashSet<Potion> potionList = new HashSet<>();
        HashSet<Effect> effectList = getCommonEffects(ingredientList, allEffects);

        if (effectList != null) {
            for (Effect effect : effectList) {
                HashSet<Ingredient> ingredientsEffectHashSet = getIngredientsFromEffect(effect, ingredientList);

                if (ingredientsEffectHashSet != null) {
                    IGenerator<List<Ingredient>> combinationGenerator = Generator.subset(ingredientsEffectHashSet).simple();
                    List<List<Ingredient>> combinations = new ArrayList<>(combinationGenerator.stream().toList());
                    combinations.removeIf(combo -> combo.size() < 2 || combo.size() > 3);

                    List<List<Ingredient>> sortedIngredients = new ArrayList<>();

                    for (List<Ingredient> ingredients : combinations) {
                        List<Ingredient> sorted = ingredients.stream().sorted(Comparator.comparing(Ingredient::getName)).collect(Collectors.toList());
                        sortedIngredients.add(sorted);
                    }

                    List<List<Ingredient>> candidates = sortedIngredients.stream().distinct().toList();

                    for (List<Ingredient> ingredients : candidates) {
                        Potion potion = makePotion(player, ingredients);
                        potionList.add(potion);
                    }
                }
            }

            potionList = potionList.stream().sorted(Comparator.comparing(Potion::getTotalGoldCost).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
            return potionList;
        }

        return null;
    }

    private HashSet<Ingredient> getIngredientsFromEffect(Effect effect, List<Ingredient> ingredients) {
        HashSet<Ingredient> ingredientHashSet = new HashSet<>();

        for (Ingredient ingredient : ingredients) {
            if (Arrays.stream(ingredient.getEffects()).anyMatch(hash -> (hash.equals(effect.getHash())))) {
                ingredientHashSet.add(ingredient);
            }
        }

        if (ingredientHashSet.isEmpty())
            return null;
        else
            return ingredientHashSet;
    }

    private boolean isEffectDurationBased(HashSet<Effect> effects) {
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

    /**
     * Submit player details and move to ingredients screen
     **/
    private class SubmitPlayerDetailsHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            model.setAlchemyLevel(pdp.getAlchemyLevelTxt().getValue());

            //items
            if (rootPane.getPlayerDetailsPane().getHeadGearCheck().isSelected()) {
                Item item = allItems.stream().toList().get(0);
                item.setAlchemyBoost(pdp.getHeadgearTxt().getValue());
                model.getItems().add(item);
            }

            if (rootPane.getPlayerDetailsPane().getGlovesCheck().isSelected()) {
                Item item = allItems.stream().toList().get(1);
                item.setAlchemyBoost(pdp.getGlovesTxt().getValue());
                model.getItems().add(item);
            }

            if (rootPane.getPlayerDetailsPane().getRingCheck().isSelected()) {
                Item item = allItems.stream().toList().get(2);
                item.setAlchemyBoost(pdp.getGlovesTxt().getValue());
                model.getItems().add(item);
            }

            if (rootPane.getPlayerDetailsPane().getFootwearCheck().isSelected()) {
                Item item = allItems.stream().toList().get(3);
                item.setAlchemyBoost(pdp.getFootwearTxt().getValue());
                model.getItems().add(item);
            }

            //perks
            List<Perk> perks = new ArrayList<>();
            RadioButton selected = (RadioButton) rootPane.getPlayerDetailsPane().getToggleGroup().getSelectedToggle();
            if (selected != null) {
                Perk alchemist = null;
                switch (selected.getText()) {
                    case "Alchemist 1" -> alchemist = allPerks.stream().toList().get(0);
                    case "Alchemist 2" -> alchemist = allPerks.stream().toList().get(1);
                    case "Alchemist 3" -> alchemist = allPerks.stream().toList().get(2);
                    case "Alchemist 4" -> alchemist = allPerks.stream().toList().get(3);
                    case "Alchemist 5" -> alchemist = allPerks.stream().toList().get(4);
                }
                perks.add(alchemist);
            }

            if (rootPane.getPlayerDetailsPane().getPhysicianCheck().isSelected()) {
                Perk physician = allPerks.stream().toList().get(5);
                perks.add(physician);
            }

            if (rootPane.getPlayerDetailsPane().getBenefactorCheck().isSelected()) {
                Perk benefactor = allPerks.stream().toList().get(6);
                perks.add(benefactor);
            }

            if (rootPane.getPlayerDetailsPane().getPoisonerCheck().isSelected()) {
                Perk poisoner = allPerks.stream().toList().get(7);
                perks.add(poisoner);
            }

            if (rootPane.getPlayerDetailsPane().getPurityCheck().isSelected()) {
                Perk purity = allPerks.stream().toList().get(8);
                perks.add(purity);
            }

            if (rootPane.getPlayerDetailsPane().getSeekerCheck().isSelected()) {
                Perk seeker = allPerks.stream().toList().get(9);
                perks.add(seeker);
            }

            if (!perks.isEmpty()) {
                model.setPlayerPerks(perks);
            }

            rootPane.changeTab(1);
        }
    }

    private class ClearEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            model.setPlayerPerks(null);
            model.setItems(null);
            model.setAlchemyLevel(15);

            pdp.getToggleGroup().getSelectedToggle().setSelected(false);
            pdp.getPhysicianCheck().setSelected(false);
            pdp.getBenefactorCheck().setSelected(false);
            pdp.getPoisonerCheck().setSelected(false);
            pdp.getPurityCheck().setSelected(false);
            pdp.getSeekerCheck().setSelected(false);
        }
    }

    private class AddIngredientToListHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            pgp.getPotionListView().setItems(null);
            Ingredient ingredient = rootPane.getPotionGenerationPane().getIngredientCombobox().getValue();
            pgp.getIngredientListView().getItems().add(ingredient);

            if (getCommonEffects(pgp.getIngredientListView().getItems(), allEffects) != null) {
                HashSet<Potion> potionHashSet = getPotionsFromIngredientList(model, rootPane.getPotionGenerationPane().getIngredientListView().getItems().stream().toList());
                pgp.getPotionListView().setItems(FXCollections.observableArrayList(potionHashSet));
            }
        }
    }

    private class RemoveIngredientFromListHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent actionEvent) {
            pgp.getPotionListView().setItems(null);
            Ingredient ingredient = pgp.getIngredientListView().getSelectionModel().getSelectedItem();
            pgp.getIngredientListView().getItems().remove(ingredient);

            if (getCommonEffects(pgp.getIngredientListView().getItems(), allEffects) != null) {
                HashSet<Potion> potionHashSet = getPotionsFromIngredientList(model, rootPane.getPotionGenerationPane().getIngredientListView().getItems().stream().toList());
                pgp.getPotionListView().setItems(FXCollections.observableArrayList(potionHashSet));
            }
        }
    }

    private class AddAllIngredientsHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            pgp.getPotionListView().setItems(null);
            pgp.getIngredientListView().getItems().addAll(rootPane.getPotionGenerationPane().getIngredientCombobox().getItems());
            HashSet<Potion> potionHashSet = getPotionsFromIngredientList(model, rootPane.getPotionGenerationPane().getIngredientListView().getItems().stream().toList());
            pgp.getPotionListView().setItems(FXCollections.observableArrayList(potionHashSet));
        }
    }

    private class RemoveAllIngredientsHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            pgp.getIngredientListView().getItems().removeAll(rootPane.getPotionGenerationPane().getIngredientListView().getItems());
            pgp.getPotionListView().setItems(null);
        }
    }

    private class GetPotionDetailsHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {

        }
    }
}



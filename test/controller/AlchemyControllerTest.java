package controller;

import model.*;

import java.util.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.Suite;
import view.AlchemyRootPane;

import static org.junit.jupiter.api.Assertions.*;

@Suite
@DisplayName("Testing the alchemy controller methods...")
public class AlchemyControllerTest {

    Player player = new Player();
    AlchemyRootPane view = new AlchemyRootPane();
    AlchemyController alchemyController = new AlchemyController(player, view);
    List<Effect> allEffects = alchemyController.getEffectsFromData("test/data/effects.json");
    List<Ingredient> allIngredients = alchemyController.getIngredientsFromData("test/data/ingredients.json");
    List<Item> allItems = alchemyController.getItemsFromData("test/data/items.json");
    List<Perk> allPerks = alchemyController.getPerksFromData("test/data/perks.json");

    @Test
    public void testGetEffectFromData() {
        Effect comparisonEffect = new Effect("Cure Disease", "000AE722", "Helpful", 0.5, 5 ,0, 21, "Physical", "Cure all diseases.");
        Effect realEffect = allEffects.get(0);
        assertEquals(comparisonEffect.toString(), realEffect.toString());
    }

    @Test
    public void testGetAllEffectsFromData() {
        Effect cureDisease = new Effect("Cure Disease", "000AE722", "Helpful", 0.5, 5 ,0, 21, "Physical", "Cure all diseases.");
        Effect damageHealth = new Effect("Damage Health", "0003EB42", "Harmful", 3.0, 2, 1, 3, "Stats", "Causes <mag> points of poison damage.");
        Effect damageMagicka = new Effect("Damage Magicka", "0003A2B6", "Harmful", 2.2, 3, 0, 52, "Stats", "Drains the target's Magicka by <mag> points.");
        Effect invisibility = new Effect("Invisibility","0003EB3D", "Helpful", 100.0, 0, 4, 261, "Physical", "Invisibility for <dur> seconds.");
        Effect restoreHealth = new Effect("Restore Health", "0003EB15", "Helpful", 0.5, 5, 0, 21, "Stats", "Restore <mag> points of Health.");
        Effect slow = new Effect("Slow", "00073F25", "Harmful", 1.0, 50, 5, 247, "Physical", "Target moves at 50% speed for <dur> seconds.");
        Effect waterbreathing = new Effect("Waterbreathing", "0003AC2D", "Helpful", 30.0, 0, 5, 100, "Physical", "Can breathe underwater for <dur> seconds.");
        List<Effect> comparisonList = new ArrayList<>();
        comparisonList.add(cureDisease);
        comparisonList.add(damageHealth);
        comparisonList.add(damageMagicka);
        comparisonList.add(invisibility);
        comparisonList.add(restoreHealth);
        comparisonList.add(slow);
        comparisonList.add(waterbreathing);

        assertEquals(allEffects.toString(), comparisonList.toString());
    }

    @Test
    public void testGetIngredientFromData() {
        Ingredient realIngredient = allIngredients.get(0);
        Ingredient comparisonIngredient = new Ingredient("Ash Creep Cluster", "xx01cd74", new String[]{"0003A2C6", "0003EB3D", "0003EAEA", "0003EB26"}, null, null, null);
        assertEquals(realIngredient.actualToString(), comparisonIngredient.actualToString());
    }

    @Test
    public void testGetIngredientWithMagnitude() {
        Ingredient realIngredient = allIngredients.get(2);
        HashMap<String, Double> hashOne = new HashMap<>();
        HashMap<String, Double> hashTwo = new HashMap<>();
        hashOne.put("0003EAEA", 1.33);
        hashTwo.put("0003EAEA", 1.36);

        Ingredient comparisonIngredient = new Ingredient("Ashen Grass Pod", "xx016e26", new String[]{"0003EAEA", "00073F2F", "0003EB21", "0003EB22"}, Optional.of(hashOne), null, Optional.of(hashTwo));
        assertEquals(realIngredient.actualToString(), comparisonIngredient.actualToString());
    }

    public void testGetAllIngredientsFromData() {
        List<Ingredient> comparisonIngredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("Ash Creep Cluster", "xx01cd74", new String[]{"0003A2C6", "0003EB3D", "0003EAEA", "0003EB26"}, null, null, null);
        Ingredient ingredient2 = new Ingredient("Ash Hopper Jelly", "xx01cd71", new String[]{"0003EB15", "0003EB1F", "0003EAEC", "0003AC2E"}, null, null, null);
        HashMap<String, Double> hashOne = new HashMap<>();
        HashMap<String, Double> hashTwo = new HashMap<>();
        hashOne.put("0003EAEA", 1.33);
        hashTwo.put("0003EAEA", 1.36);

        Ingredient ingredient3 = new Ingredient("Ashen Grass Pod", "xx016e26", new String[]{"0003EAEA", "00073F2F", "0003EB21", "0003EB22"}, Optional.of(hashOne), null, Optional.of(hashTwo));
        Ingredient ingredient4 = new Ingredient("Butterfly Wing", "000727e0", new String[]{"0003EB15", "0003EB23", "0010DE5E", "0003A2B6"}, null, null, null);
        Ingredient ingredient5 = new Ingredient("Chaurus Eggs", "0003ad56", new String[]{"00090042", "0003EAF9", "0003A2B6", "0003EB3D"}, null, null, null);

        comparisonIngredients.add(ingredient1);
        comparisonIngredients.add(ingredient2);
        comparisonIngredients.add(ingredient3);
        comparisonIngredients.add(ingredient4);
        comparisonIngredients.add(ingredient5);

        assertEquals(allIngredients.toString(), comparisonIngredients.toString());
    }

    @Test
    public void testGetItemFromData() {
        Item realItem = allItems.get(0);
        Item comparisonItem = new Item("Headgear");

        assertEquals(realItem.toString(), comparisonItem.toString());
    }

    @Test
    public void testGetAllItemsFromData() {
        List<Item> comparisonItems = new ArrayList<>();
        Item headgear = new Item("Headgear");
        Item gloves = new Item("Gloves");
        Item necklace = new Item("Necklace");
        Item ring = new Item("Ring");

        comparisonItems.add(headgear);
        comparisonItems.add(gloves);
        comparisonItems.add(necklace);
        comparisonItems.add(ring);

        assertEquals(allItems.toString(), comparisonItems.toString());
    }

    @Test
    public void testGetPerkFromData() {
        Perk realPerk = allPerks.get(0);
        Perk comparisonPerk = new Perk("Alchemist 1", "000be127", 1.2);

        assertEquals(realPerk.toString(), comparisonPerk.toString());
    }

    @Test
    public void testGetAllPerksFromData() {
        List<Perk> comparisonPerks = new ArrayList<>();
        Perk alchemist1 = new Perk("Alchemist 1", "000be127", 1.2);
        Perk alchemist2 = new Perk("Alchemist 2", "000c07ca", 1.4);
        Perk alchemist3 = new Perk("Alchemist 3", "000c07cb", 1.6);
        Perk physician = new Perk("Physician", "00058215", 1.25);
        Perk benefactor = new Perk("Benefactor", "00058216", 1.25);
        Perk poisoner = new Perk("Poisoner", "00058217", 1.25);

        comparisonPerks.add(alchemist1);
        comparisonPerks.add(alchemist2);
        comparisonPerks.add(alchemist3);
        comparisonPerks.add(physician);
        comparisonPerks.add(benefactor);
        comparisonPerks.add(poisoner);

        assertEquals(allPerks.toString(), comparisonPerks.toString());
    }

    @Test
    public void testLoadNonexistentEffectFile() {
        assertNull(alchemyController.getEffectsFromData("test/data/effect.json"));
    }

    @Test
    public void testLoadNonexistentIngredientFile() {
        assertNull(alchemyController.getIngredientsFromData("test/data/ingredient.json"));
    }

    @Test
    public void testLoadNonexistentItemFile() {
        assertNull(alchemyController.getItemsFromData("test/data/item.json"));
    }

    @Test
    public void testLoadNonexistentPerkFile() {
        assertNull(alchemyController.getPerksFromData("test/data/perk.json"));
    }

    @Test
    public void testPresentEffectHash() {
        String hash = "0003EB15";
        assertTrue(alchemyController.isEffectPresent(allIngredients.get(1), hash));
    }

    @Test
    public void testInvalidEffectHash() {
        String hash = "467EBF1J";
        assertFalse(alchemyController.isEffectPresent(allIngredients.get(1), hash));
    }

    @Test
    public void testGetRealEffectFromHash() {
        String hash = "0003EB42";
        assertEquals(alchemyController.getEffectFromHash(hash, allEffects), allEffects.get(1));
    }

    @Test
    public void testGetNoEffectFromHash() {
        String hash = "467EBF1J";
        assertNull(alchemyController.getEffectFromHash(hash, allEffects));
    }

    @Test
    public void testGetRealIngredientFromHash() {
        String hash = "xx01cd74";
        assertEquals(alchemyController.getIngredientFromHash(hash, allIngredients), allIngredients.get(0));
    }

    @Test
    public void testGetNoIngredientFromHash() {
        String hash = "476gdx82";
        assertNull(alchemyController.getIngredientFromHash(hash, allIngredients));
    }

    @Test
    public void testGetRealPerkFromHash() {
        String hash = "000c07cb";
        assertEquals(alchemyController.getPerkFromHash(hash, allPerks), allPerks.get(2));
    }

    @Test
    public void testGetNoPerkFromHash() {
        String hash = "784nf1n2";
        assertNull(alchemyController.getPerkFromHash(hash, allPerks));
    }

    @Test
    public void testGetPerkMultiplierList() {
        List<Perk> perks = allPerks;
        perks.remove(2);

        List<Double> multipliers = new ArrayList<>();
        multipliers.add(1.2);
        multipliers.add(1.4);
        multipliers.add(1.25);
        multipliers.add(1.25);
        multipliers.add(1.25);

        assertEquals(alchemyController.getPerkMultiplierList(perks), multipliers);
    }

    @Test
    public void testGetPotionEffectBaseMagnitude() {
        Effect effect = allEffects.get(2);
        Perk alchemist = allPerks.get(1);
        Item headgear = allItems.get(0);
        Item gloves = allItems.get(1);
        headgear.setAlchemyBoost(20);
        gloves.setAlchemyBoost(5);

        List<Perk> perks = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        items.add(headgear);
        items.add(gloves);
        perks.add(alchemist);
        player.setItems(items);
        player.setPlayerPerks(perks);
        player.setAlchemyLevel(45);

        assertEquals(alchemyController.getPotionEffectBaseMagnitude(player, effect), 14.196);
    }

    @Test
    public void testGetPotionBaseEffectDuration() {
        Effect effect = allEffects.get(5);
        Perk alchemist = allPerks.get(1);
        Item headgear = allItems.get(0);
        Item gloves = allItems.get(1);
        headgear.setAlchemyBoost(20);
        gloves.setAlchemyBoost(5);

        List<Perk> perks = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        items.add(headgear);
        items.add(gloves);
        perks.add(alchemist);
        player.setItems(items);
        player.setPlayerPerks(perks);
        player.setAlchemyLevel(45);

        assertEquals(alchemyController.getPotionEffectBaseDuration(player, effect), 24);
    }

    @Test
    public void testGetBaseCostNoMag() {
        Effect effect = allEffects.get(6);
        assertEquals(alchemyController.getPotionEffectBaseCost(effect), 30);
    }

    @Test
    public void testGetBaseCostNoDur() {
        Effect effect = allEffects.get(0);
        assertEquals(alchemyController.getPotionEffectBaseCost(effect), 2);
    }

    @Test
    public void testGetBaseCost() {
        Effect effect = allEffects.get(5);
        assertEquals(alchemyController.getPotionEffectBaseCost(effect), 34);
    }

    @Test
    public void dropBenefactor() {
        Effect poison = allEffects.get(1);
        Perk alchemist = allPerks.get(1);
        Perk benefactor = allPerks.get(4);

        List<Perk> playerPerks = new ArrayList<>();
        playerPerks.add(alchemist);
        playerPerks.add(benefactor);

        List<Perk> comparator = new ArrayList<>();
        comparator.add(alchemist);

        alchemyController.dropBenefactorOrPoisoner(poison, playerPerks, allPerks);

        assertEquals(playerPerks.toString(), comparator.toString());
    }

    @Test
    public void dropPoisoner() {
        Effect potion = allEffects.get(0);
        Perk alchemist = allPerks.get(1);
        Perk poisoner = allPerks.get(5);

        List<Perk> playerPerks = new ArrayList<>();
        playerPerks.add(alchemist);
        playerPerks.add(poisoner);

        List<Perk> comparator = new ArrayList<>();
        comparator.add(alchemist);

        alchemyController.dropBenefactorOrPoisoner(potion, playerPerks, allPerks);

        assertEquals(playerPerks.toString(), comparator.toString());
    }

    @Test
    public void dropBenefactorNoBenefactor() {
        Effect poison = allEffects.get(1);
        Perk alchemist = allPerks.get(1);

        List<Perk> playerPerks = new ArrayList<>();
        playerPerks.add(alchemist);

        List<Perk> comparator = new ArrayList<>();
        comparator.add(alchemist);

        alchemyController.dropBenefactorOrPoisoner(poison, playerPerks, allPerks);

        assertEquals(playerPerks.toString(), comparator.toString());
    }

    @Test
    public void dropPoisonerNoPoisoner() {
        Effect potion = allEffects.get(0);
        Perk alchemist = allPerks.get(1);

        List<Perk> playerPerks = new ArrayList<>();
        playerPerks.add(alchemist);

        List<Perk> comparator = new ArrayList<>();
        comparator.add(alchemist);

        alchemyController.dropBenefactorOrPoisoner(potion, playerPerks, allPerks);

        assertEquals(playerPerks.toString(), comparator.toString());
    }

    @Test
    public void dropPhysician() {
        Effect effect = allEffects.get(0);
        Perk alchemist = allPerks.get(2);
        Perk physician = allPerks.get(3);

        List<Perk> playerPerks = new ArrayList<>();
        playerPerks.add(alchemist);
        playerPerks.add(physician);

        List<Perk> comparisonPerks = new ArrayList<>();
        comparisonPerks.add(alchemist);

        alchemyController.dropPhysician(effect, playerPerks, allPerks);
        assertEquals(playerPerks.toString(), comparisonPerks.toString());
    }

    @Test
    public void dropPhysicianWhenRestoringPlayerPhysicals() {
        Effect effect = allEffects.get(4);
        Perk alchemist = allPerks.get(2);
        Perk physician = allPerks.get(3);

        List<Perk> playerPerks = new ArrayList<>();
        playerPerks.add(alchemist);
        playerPerks.add(physician);

        List<Perk> comparisonPerks = new ArrayList<>();
        comparisonPerks.add(alchemist);
        comparisonPerks.add(physician);

        alchemyController.dropPhysician(effect, playerPerks, allPerks);
        assertEquals(playerPerks.toString(), comparisonPerks.toString());
    }

    @Test
    public void testGetBaseValueWithEnchantments() {
        player.setAlchemyLevel(45);
        Item headgear = new Item("Headgear");
        Item gloves = new Item("Gloves");
        headgear.setAlchemyBoost(25);
        gloves.setAlchemyBoost(12);
        List<Item> items = new ArrayList<>();
        items.add(headgear);
        items.add(gloves);
        player.setItems(items);

        assertEquals(alchemyController.getBaseDurAndMagValue(player), 3.70);
    }

    @Test
    public void testGetBaseValueWithoutEnchantments() {
        player.setAlchemyLevel(45);
        ArrayList<Item> items = new ArrayList<>();
        player.setItems(items);

        assertEquals(alchemyController.getBaseDurAndMagValue(player), 2.70);
    }

    @Test
    public void testGetCommonEffects() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(allIngredients.get(3));
        ingredients.add(allIngredients.get(4));

        HashSet<Effect> effectResult = new HashSet<>();
        effectResult.add(allEffects.get(2));

        assertEquals(alchemyController.getCommonEffects(ingredients, allEffects).toString(), effectResult.toString());
    }

    @Test
    public void testGetNoCommonEffects() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(allIngredients.get(1));
        ingredients.add(allIngredients.get(4));

        assertNull(alchemyController.getCommonEffects(ingredients, allEffects));
    }

    @Test
    public void testGetCommonIngredients() {
        Ingredient ingredient = allIngredients.get(4);
        assertTrue(alchemyController.getAllCommonIngredients(ingredient, allIngredients).contains(allIngredients.get(0)));
        assertTrue(alchemyController.getAllCommonIngredients(ingredient, allIngredients).contains(allIngredients.get(3)));
    }

    @Test
    public void testGetNoCommonIngredients() {
        Ingredient ingredient = new Ingredient("Test Ingredient", "784hih8", new String[]{"516516d", "eub233fd", "2efrr21", "fi861qa"}, null, null, null);
        assertNull(alchemyController.getAllCommonIngredients(ingredient, allIngredients));
    }

    @Test
    public void testMakePotion() {
        List<Ingredient> ingredientsList = new ArrayList<>();
        player.setAlchemyLevel(45);
        player.setPlayerPerks(new ArrayList<>());
        player.setItems(new ArrayList<>());
        ingredientsList.add(allIngredients.get(4));
        ingredientsList.add(allIngredients.get(3));

        HashSet<Effect> potionEffects = new HashSet<>();
        Effect invisibility = allEffects.get(2);
        invisibility.setBaseMag((int) alchemyController.getPotionEffectBaseMagnitude(player, invisibility));
        invisibility.setBaseDur(alchemyController.getPotionEffectBaseDuration(player, invisibility));
        invisibility.setBaseCost(alchemyController.getPotionEffectBaseCost(invisibility));
        invisibility.setDescription("Drains the target's Magicka by " + invisibility.getBaseMag() + " points.");
        potionEffects.add(invisibility);

        Potion potion = alchemyController.makePotion(player, ingredientsList);
        Potion comparison = new Potion("Poison of Damage Magicka", ingredientsList, potionEffects, (int) alchemyController.getPotionEffectBaseCost(invisibility));

        assertEquals(potion.toString(), comparison.toString());
    }

    public void testMakePotionWithBoosters() {

    }
}
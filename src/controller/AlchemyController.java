
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Effect;
import model.Ingredient;
import model.Item;
import model.Perk;
import model.Player;
import model.Potion;
import view.AlchemyMenuBar;
import view.AlchemyRootPane;
import view.IngredientEnchantmentPane;
import view.PlayerDetailsPane;
import view.ViewRecipePane;

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
        this.setupAndGetEffects();
        this.setupAndGetIngredients();
        this.setupAndGetItems();
        this.setupAndGetPerks();

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
     * @param effect
     * @return boolean
     */
    private boolean isEffectPresent(Ingredient ingredient, String hashValue) {
        Effect effect = getEffectByHashValue(hashValue);
        if (effect != null) {
            if (ingredient.getAllEffectsForIngredient().contains(effect)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to obtain effect from its in game hashValue.
     *
     * @param hashValue
     * @return String
     */
    private Effect getEffectByHashValue(String hashValue) {
        for (Effect e : setupAndGetEffects()) {
            if (hashValue == e.getHashValue()) {
                return e;
            }
        }
        return null;
    }

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

        for (Effect effect : list) {
            set.add(effect);
        }

        return duplicatedEffects;
    }

    /**
     * @param list
     * @return boolean
     */
    private static boolean hasDuplicateEffect(Collection<Effect> list) {
        if (getDuplicateEffects(list).isEmpty())
            return false;
        return true;
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
            if (p.getHashCode() == "xx034838")
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
            if (p.getHashCode() == "000c07cd")
                return 2.0;
            else if (p.getHashCode() == "000c07cc")
                return 1.8;
            else if (p.getHashCode() == "000c07cb")
                return 1.6;
            else if (p.getHashCode() == "000c07ca")
                return 1.4;
            else if (p.getHashCode() == "000be127")
                return 1.2;
        }
        return 1.0;
    }

    /**
     * The potion's strength will intensify by 25% if any of its effects restore stats  (health, magicka, stamina)
     * and the Player has the Physician perk equipped.
     *
     * @param player
     * @param ingredients
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
            if (p.getHashCode() == physician) {
                physicianPerk = true;
            }
        }

        if (restoreHealth == effect.getHashValue() || restoreMagicka == effect.getHashValue() || restoreStamina == effect.getHashValue()) {
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
     * @param ingredients
     * @return double
     */
    private double getBenefactorPerkMultiplier(Player player, Effect effect) {
        String benefactor = "00058216";
        boolean benefactorPerk = false;

        for (Perk p : player.getPlayerPerks()) {
            if (p.getHashCode() == benefactor && effect.isHelpful()) {
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
            if (p.getHashCode() == poisoner && effect.isHarmful()) {
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
     * @param ingredients
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

    /**
     * Helper method generating items
     * This will be assigned as tick boxes in the GUI with a text box alongside to allocate
     * alchemy percentage boost
     *
     * @return array
     */
    private Item[] setupAndGetItems() {
        Item headgear = new Item("Headgear");
        Item gloves = new Item("Gloves");
        Item necklace = new Item("Necklace");
        Item ring = new Item("Ring");

        Item[] items = new Item[4];
        items[0] = headgear;
        items[1] = gloves;
        items[2] = necklace;
        items[3] = ring;

        return items;
    }

    /**
     * Helper method generating perks
     * This will be assigned as tick boxes in the GUI
     * Stored as array
     *
     * @return array
     */
    private Perk[] setupAndGetPerks() {
        Perk alchemist1 = new Perk("Alchemist", "000be127", 1, 0, null, 1.2);
        Perk alchemist2 = new Perk("Alchemist", "000c07ca", 2, 20, alchemist1, 1.4);
        Perk alchemist3 = new Perk("Alchemist", "000c07cb", 3, 40, alchemist2, 1.6);
        Perk alchemist4 = new Perk("Alchemist", "000c07cc", 4, 60, alchemist3, 1.8);
        Perk alchemist5 = new Perk("Alchemist", "000c07cd", 5, 80, alchemist4, 2.0);
        Perk physician = new Perk("Physician", "00058215", 1, 20, alchemist1, 1.25);
        Perk benefactor = new Perk("Benefactor", "00058216", 1, 30, physician, 1.25);
        Perk poisoner = new Perk("Poisoner", "00058217", 1, 30, physician, 1.25);
        Perk purity = new Perk("Purity", "0005821d", 1, 100, alchemist5, 0);
        Perk seekerShadows = new Perk("Seeker of Shadows power - Dragonborn DLC", "xx034838", 0, 0, null, 1.1);

        Perk[] perks = new Perk[10];
        perks[0] = alchemist1;
        perks[1] = alchemist2;
        perks[2] = alchemist3;
        perks[3] = alchemist4;
        perks[4] = alchemist5;
        perks[5] = physician;
        perks[6] = benefactor;
        perks[7] = poisoner;
        perks[8] = purity;
        perks[9] = seekerShadows;

        return perks;
    }

    private Effect[] setupAndGetEffects() {
        Effect cureDisease = new Effect("Cure Disease", "000AE722", true, false, 0.5D, 5, 0, 21, false, false);
        Effect damageHealth = new Effect("Damage Health", "0003EB42", false, true, 3.0D, 2, 1, 3, true, false);
        Effect damageMagicka = new Effect("Damage Magicka", "0003A2B6", false, true, 2.2D, 3, 0, 52, true, false);
        Effect damageMagickaRegen = new Effect("Damage Magicka Regen", "00073F2B", false, true, 0.5D, 100, 5, 265, true, false);
        Effect damageStamina = new Effect("Damage Stamina", "0003A2C6", false, true, 1.8D, 3, 0, 43, true, false);
        Effect damageStaminaRegen = new Effect("Damage Stamina Regen", "0003A2C6", false, true, 0.3D, 100, 5, 159, true, false);
        Effect fear = new Effect("Fear", "00073F20", true, false, 5.0D, 1, 30, 120, false, false);
        Effect fortifyAlteration = new Effect("Fortify Alteration", "0003EB24", true, false, 0.2D, 4, 60, 47, false, true);
        Effect fortifyBarter = new Effect("Fortify Barter", "0003EB23", true, false, 2.0D, 1, 30, 48, false, true);
        Effect fortifyBlock = new Effect("Fortify Block", "0003EB1C", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifyCarryWeight = new Effect("Fortify Carry Weight", "0003EB01", true, false, 0.15D, 4, 300, 208, false, true);
        Effect fortifyConjuration = new Effect("Fortify Conjuration", "0003EB25", true, false, 0.25D, 5, 60, 75, false, true);
        Effect fortifyDestruction = new Effect("Fortify Destruction", "0003EB26", true, false, 0.5D, 5, 60, 151, false, true);
        Effect fortifyEnchanting = new Effect("Fortify Enchanting", "0003EB29", true, false, 0.6D, 1, 30, 14, false, true);
        Effect fortifyHealth = new Effect("Fortify Health", "0003EAF3", true, false, 0.35D, 4, 60, 82, false, true);
        Effect fortifyHeavyArmor = new Effect("Fortify Heavy Armor", "0003EB1E", true, false, 0.5D, 2, 60, 55, false, true);
        Effect fortifyIllusion = new Effect("Fortify Illusion", "0003EB27", true, false, 0.4D, 4, 60, 94, false, true);
        Effect fortifyLightArmor = new Effect("Fortify Light Armor", "0003EB1F", true, false, 0.5D, 2, 60, 55, false, true);
        Effect fortifyLockpicking = new Effect("Fortify Lockpicking", "0003EB21", true, false, 0.5D, 2, 30, 25, false, true);
        Effect fortifyMagicka = new Effect("Fortify Magicka", "0003EAF8", true, false, 0.3D, 4, 60, 71, false, true);
        Effect fortifyMarksman = new Effect("Fortify Marksman", "0003EB1B", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifyOneHanded = new Effect("Fortify One-handed", "0003EB19", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifyPickpocket = new Effect("Fortify Pickpocket", "0003EB20", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifyRestoration = new Effect("Fortify Restoration", "0003EB28", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifySmithing = new Effect("Fortify Smithing", "0003EB1D", true, false, 0.75D, 4, 30, 82, false, true);
        Effect fortifySneak = new Effect("Fortify Sneak", "0003EB22", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifyStamina = new Effect("Fortify Stamina", "0003EAF9", true, false, 0.3D, 4, 60, 71, false, true);
        Effect fortifyTwoHanded = new Effect("Fortify Two-handed", "0003EB1A", true, false, 0.5D, 4, 60, 118, false, true);
        Effect frenzy = new Effect("Frenzy", "00073F29", false, true, 15.0D, 1, 10, 107, false, false);
        Effect invisibility = new Effect("Invisibility", "0003EB3D", true, false, 100.0D, 0, 4, 261, false, false);
        Effect lingeringHealth = new Effect("Lingering Damage Health", "0010AA4A", false, true, 12.0D, 1, 10, 86, true, false);
        Effect lingeringMagicka = new Effect("Lingering Damage Magicka", "0010DE5F", false, true, 10.0D, 1, 10, 71, true, false);
        Effect lingeringStamina = new Effect("Lingering Damage Stamina", "0010DE5E", false, true, 1.8D, 1, 10, 12, true, false);
        Effect paralysis = new Effect("Paralysis", "00073F30", false, true, 500.0D, 0, 1, 285, false, false);
        Effect ravageHealth = new Effect("Ravage Health", "00073F26", false, true, 0.4D, 2, 10, 6, true, false);
        Effect ravageMagicka = new Effect("Ravage Magicka", "00073F27", false, true, 1.0D, 2, 10, 15, true, false);
        Effect ravageStamina = new Effect("Ravage Stamina", "00073F23", false, true, 1.6D, 2, 10, 24, true, false);
        Effect regenHealth = new Effect("Regenerate Health", "0003EB06", true, false, 0.1D, 5, 300, 177, true, false);
        Effect regenMagicka = new Effect("Regenerate Magicka", "0003EB07", true, false, 0.1D, 5, 300, 177, true, false);
        Effect regenStamina = new Effect("Regenerate Stamina", "0003EB08", true, false, 0.1D, 5, 300, 177, true, false);
        Effect resistFire = new Effect("Resist Fire", "0003EAEA", true, false, 0.5D, 3, 60, 86, false, false);
        Effect resistFrost = new Effect("Resist Frost", "0003EAEB", true, false, 0.5D, 3, 60, 86, false, false);
        Effect resistMagic = new Effect("Resist Magic", "00039E51", true, false, 1.0D, 1, 60, 51, false, false);
        Effect resistPoison = new Effect("Resist Poison", "00090041", true, false, 0.5D, 4, 60, 118, false, false);
        Effect resistShock = new Effect("Resist Shock", "0003EAEC", true, false, 0.5D, 3, 60, 86, false, false);
        Effect restoreHealth = new Effect("Restore Health", "0003EB15", true, false, 0.5D, 5, 0, 21, true, false);
        Effect restoreMagicka = new Effect("Restore Magicka", "0003EB17", true, false, 0.6D, 5, 0, 25, true, false);
        Effect restoreStamina = new Effect("Restore Stamina", "0003EB16", true, false, 0.6D, 5, 0, 25, true, false);
        Effect slow = new Effect("Slow", "00073F25", false, true, 1.0D, 50, 5, 247, false, false);
        Effect waterbreathing = new Effect("Waterbreathing", "0003AC2D", true, false, 30.0D, 0, 5, 100, false, false);
        Effect weakFire = new Effect("Weakness to Fire", "00073F2D", false, true, 0.6D, 3, 30, 48, false, false);
        Effect weakFrost = new Effect("Weakness to Frost", "00073F2E", false, true, 0.5D, 3, 30, 40, false, false);
        Effect weakMagic = new Effect("Weakness to Magic", "00073F51", false, true, 1.0D, 2, 30, 51, false, false);
        Effect weakPoison = new Effect("Weakness to Poison", "00090042", false, true, 1.0D, 2, 30, 51, false, false);
        Effect weakShock = new Effect("Weakness to Shock", "00073F2F", false, true, 0.7D, 3, 30, 56, false, false);

        Effect[] effects = new Effect[55];
        effects[0] = cureDisease;
        effects[1] = damageHealth;
        effects[2] = damageMagicka;
        effects[3] = damageMagickaRegen;
        effects[4] = damageStamina;
        effects[5] = damageStaminaRegen;
        effects[6] = fear;
        effects[7] = fortifyAlteration;
        effects[8] = fortifyBarter;
        effects[9] = fortifyBlock;
        effects[10] = fortifyCarryWeight;
        effects[11] = fortifyConjuration;
        effects[12] = fortifyDestruction;
        effects[13] = fortifyEnchanting;
        effects[14] = fortifyHealth;
        effects[15] = fortifyHeavyArmor;
        effects[16] = fortifyIllusion;
        effects[17] = fortifyLightArmor;
        effects[18] = fortifyLockpicking;
        effects[19] = fortifyMagicka;
        effects[20] = fortifyMarksman;
        effects[21] = fortifyOneHanded;
        effects[22] = fortifyPickpocket;
        effects[23] = fortifyRestoration;
        effects[24] = fortifySmithing;
        effects[25] = fortifySneak;
        effects[26] = fortifyStamina;
        effects[27] = fortifyTwoHanded;
        effects[28] = frenzy;
        effects[29] = invisibility;
        effects[30] = lingeringHealth;
        effects[31] = lingeringMagicka;
        effects[32] = lingeringStamina;
        effects[33] = paralysis;
        effects[34] = ravageHealth;
        effects[35] = ravageMagicka;
        effects[36] = ravageStamina;
        effects[37] = regenHealth;
        effects[38] = regenMagicka;
        effects[39] = regenStamina;
        effects[40] = resistFire;
        effects[41] = resistFrost;
        effects[42] = resistMagic;
        effects[43] = resistPoison;
        effects[44] = resistShock;
        effects[45] = restoreHealth;
        effects[46] = restoreMagicka;
        effects[47] = restoreStamina;
        effects[48] = slow;
        effects[49] = waterbreathing;
        effects[50] = weakFire;
        effects[51] = weakFrost;
        effects[52] = weakMagic;
        effects[53] = weakPoison;
        effects[54] = weakShock;

        return effects;
    }

    /**
     * Generates effects, then generates ingredients and assigns effects to ingredients
     * Stores ingredients as array
     *
     * @return array
     */
    private Ingredient[] setupAndGetIngredients() {

        Effect[] effects = setupAndGetEffects();

        Effect cureDisease = new Effect("Cure Disease", "000AE722", true, false, 0.5D, 5, 0, 21, false, false);
        Effect damageHealth = new Effect("Damage Health", "0003EB42", false, true, 3.0D, 2, 1, 3, true, false);
        Effect damageMagicka = new Effect("Damage Magicka", "0003A2B6", false, true, 2.2D, 3, 0, 52, true, false);
        Effect damageMagickaRegen = new Effect("Damage Magicka Regen", "00073F2B", false, true, 0.5D, 100, 5, 265, true, false);
        Effect damageStamina = new Effect("Damage Stamina", "0003A2C6", false, true, 1.8D, 3, 0, 43, true, false);
        Effect damageStaminaRegen = new Effect("Damage Stamina Regen", "00073F2C", false, true, 0.3D, 100, 5, 159, true, false);
        Effect fear = new Effect("Fear", "00073F20", true, false, 5.0D, 1, 30, 120, false, false);
        Effect fortifyAlteration = new Effect("Fortify Alteration", "0003EB24", true, false, 0.2D, 4, 60, 47, false, true);
        Effect fortifyBarter = new Effect("Fortify Barter", "0003EB23", true, false, 2.0D, 1, 30, 48, false, true);
        Effect fortifyBlock = new Effect("Fortify Block", "0003EB1C", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifyCarryWeight = new Effect("Fortify Carry Weight", "0003EB01", true, false, 0.15D, 4, 300, 208, false, true);
        Effect fortifyConjuration = new Effect("Fortify Conjuration", "0003EB25", true, false, 0.25D, 5, 60, 75, false, true);
        Effect fortifyDestruction = new Effect("Fortify Destruction", "0003EB26", true, false, 0.5D, 5, 60, 151, false, true);
        Effect fortifyEnchanting = new Effect("Fortify Enchanting", "0003EB29", true, false, 0.6D, 1, 30, 14, false, true);
        Effect fortifyHealth = new Effect("Fortify Health", "0003EAF3", true, false, 0.35D, 4, 60, 82, false, true);
        Effect fortifyHeavyArmor = new Effect("Fortify Heavy Armor", "0003EB1E", true, false, 0.5D, 2, 60, 55, false, true);
        Effect fortifyIllusion = new Effect("Fortify Illusion", "0003EB27", true, false, 0.4D, 4, 60, 94, false, true);
        Effect fortifyLightArmor = new Effect("Fortify Light Armor", "0003EB1F", true, false, 0.5D, 2, 60, 55, false, true);
        Effect fortifyLockpicking = new Effect("Fortify Lockpicking", "0003EB21", true, false, 0.5D, 2, 30, 25, false, true);
        Effect fortifyMagicka = new Effect("Fortify Magicka", "0003EAF8", true, false, 0.3D, 4, 60, 71, false, true);
        Effect fortifyMarksman = new Effect("Fortify Marksman", "0003EB1B", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifyOneHanded = new Effect("Fortify One-handed", "0003EB19", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifyPickpocket = new Effect("Fortify Pickpocket", "0003EB20", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifyRestoration = new Effect("Fortify Restoration", "0003EB28", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifySmithing = new Effect("Fortify Smithing", "0003EB1D", true, false, 0.75D, 4, 30, 82, false, true);
        Effect fortifySneak = new Effect("Fortify Sneak", "0003EB22", true, false, 0.5D, 4, 60, 118, false, true);
        Effect fortifyStamina = new Effect("Fortify Stamina", "0003EAF9", true, false, 0.3D, 4, 60, 71, false, true);
        Effect fortifyTwoHanded = new Effect("Fortify Two-handed", "0003EB1A", true, false, 0.5D, 4, 60, 118, false, true);
        Effect frenzy = new Effect("Frenzy", "00073F29", false, true, 15.0D, 1, 10, 107, false, false);
        Effect invisibility = new Effect("Invisibility", "0003EB3D", true, false, 100.0D, 0, 4, 261, false, false);
        Effect lingeringHealth = new Effect("Lingering Damage Health", "0010AA4A", false, true, 12.0D, 1, 10, 86, true, false);
        Effect lingeringMagicka = new Effect("Lingering Damage Magicka", "0010DE5F", false, true, 10.0D, 1, 10, 71, true, false);
        Effect lingeringStamina = new Effect("Lingering Damage Stamina", "0010DE5E", false, true, 1.8D, 1, 10, 12, true, false);
        Effect paralysis = new Effect("Paralysis", "00073F30", false, true, 500.0D, 0, 1, 285, false, false);
        Effect ravageHealth = new Effect("Ravage Health", "00073F26", false, true, 0.4D, 2, 10, 6, true, false);
        Effect ravageMagicka = new Effect("Ravage Magicka", "00073F27", false, true, 1.0D, 2, 10, 15, true, false);
        Effect ravageStamina = new Effect("Ravage Stamina", "00073F23", false, true, 1.6D, 2, 10, 24, true, false);
        Effect regenHealth = new Effect("Regenerate Health", "0003EB06", true, false, 0.1D, 5, 300, 177, true, false);
        Effect regenMagicka = new Effect("Regenerate Magicka", "0003EB07", true, false, 0.1D, 5, 300, 177, true, false);
        Effect regenStamina = new Effect("Regenerate Stamina", "0003EB08", true, false, 0.1D, 5, 300, 177, true, false);
        Effect resistFire = new Effect("Resist Fire", "0003EAEA", true, false, 0.5D, 3, 60, 86, false, false);
        Effect resistFrost = new Effect("Resist Frost", "0003EAEB", true, false, 0.5D, 3, 60, 86, false, false);
        Effect resistMagic = new Effect("Resist Magic", "00039E51", true, false, 1.0D, 1, 60, 51, false, false);
        Effect resistPoison = new Effect("Resist Poison", "00090041", true, false, 0.5D, 4, 60, 118, false, false);
        Effect resistShock = new Effect("Resist Shock", "0003EAEC", true, false, 0.5D, 3, 60, 86, false, false);
        Effect restoreHealth = new Effect("Restore Health", "0003EB15", true, false, 0.5D, 5, 0, 21, true, false);
        Effect restoreMagicka = new Effect("Restore Magicka", "0003EB17", true, false, 0.6D, 5, 0, 25, true, false);
        Effect restoreStamina = new Effect("Restore Stamina", "0003EB16", true, false, 0.6D, 5, 0, 25, true, false);
        Effect slow = new Effect("Slow", "00073F25", false, true, 1.0D, 50, 5, 247, false, false);
        Effect waterbreathing = new Effect("Waterbreathing", "0003AC2D", true, false, 30.0D, 0, 5, 100, false, false);
        Effect weakFire = new Effect("Weakness to Fire", "00073F2D", false, true, 0.6D, 3, 30, 48, false, false);
        Effect weakFrost = new Effect("Weakness to Frost", "00073F2E", false, true, 0.5D, 3, 30, 40, false, false);
        Effect weakMagic = new Effect("Weakness to Magic", "00073F51", false, true, 1.0D, 2, 30, 51, false, false);
        Effect weakPoison = new Effect("Weakness to Poison", "00090042", false, true, 1.0D, 2, 30, 51, false, false);
        Effect weakShock = new Effect("Weakness to Shock", "00073F2F", false, true, 0.7D, 3, 30, 56, false, false);

        Ingredient abeceanLongfin = new Ingredient("Abecean Longfin");
//        abeceanLongfin.addEffect(weakFrost);
//        abeceanLongfin.addEffect(fortifySneak);
//        abeceanLongfin.addEffect(weakPoison);
//        abeceanLongfin.addEffect(fortifyRestoration);
        abeceanLongfin.addEffect(getEffectByHashValue("00073F2E"));
        abeceanLongfin.addEffect(getEffectByHashValue("0003EB22"));
        abeceanLongfin.addEffect(getEffectByHashValue("00090042"));
        abeceanLongfin.addEffect(getEffectByHashValue("0003EB28"));
        Ingredient ancestorMothWing = new Ingredient("Ancestor Moth Wing");
//        ancestorMothWing.addEffect(damageStamina);
//        ancestorMothWing.addEffect(fortifyConjuration);
//        ancestorMothWing.addEffect(damageMagickaRegen);
//        ancestorMothWing.addEffect(fortifyEnchanting);
        ancestorMothWing.addEffect(getEffectByHashValue("0003A2C6"));
        ancestorMothWing.addEffect(getEffectByHashValue("0003EB25"));
        ancestorMothWing.addEffect(getEffectByHashValue("00073F2B"));
        ancestorMothWing.addEffect(getEffectByHashValue("0003EB29"));
        Ingredient ashCreepCluster = new Ingredient("Ash Creep Cluster");
//        ashCreepCluster.addEffect(damageStamina);
//        ashCreepCluster.addEffect(invisibility);
//        ashCreepCluster.addEffect(resistFire);
//        ashCreepCluster.addEffect(fortifyDestruction);
        ashCreepCluster.addEffect(getEffectByHashValue("0003A2C6"));
        ashCreepCluster.addEffect(getEffectByHashValue("0003EB3D"));
        ashCreepCluster.addEffect(getEffectByHashValue("0003EAEA"));
        ashCreepCluster.addEffect(getEffectByHashValue("0003EB26"));
        Ingredient ashHopperJelly = new Ingredient("Ash Hopper Jelly");
//        ashHopperJelly.addEffect(restoreHealth);
//        ashHopperJelly.addEffect(fortifyLightArmor);
//        ashHopperJelly.addEffect(weakFrost);
//        ashHopperJelly.addEffect(resistShock);
        ashHopperJelly.addEffect(getEffectByHashValue("0003EB15"));
        ashHopperJelly.addEffect(getEffectByHashValue("0003EB1F"));
        ashHopperJelly.addEffect(getEffectByHashValue("00073F2E"));
        ashHopperJelly.addEffect(getEffectByHashValue("0003EAEC"));
        Ingredient ashenGrassPod = new Ingredient("Ashen Grass Pod");
//        ashenGrassPod.addEffect(restoreHealth);
//        ashenGrassPod.addEffect(fortifyLightArmor);
//        ashenGrassPod.addEffect(fortifyLockpicking);
//        ashenGrassPod.addEffect(fortifySneak);
        ashenGrassPod.addEffect(getEffectByHashValue("0003EB15"));
        ashenGrassPod.addEffect(getEffectByHashValue("0003EB1F"));
        ashenGrassPod.addEffect(getEffectByHashValue("0003EB21"));
        ashenGrassPod.addEffect(getEffectByHashValue("0003EB22"));
        Ingredient bearClaws = new Ingredient("Bear Claws");
//        bearClaws.addEffect(restoreStamina);
//        bearClaws.addEffect(fortifyHealth);
//        bearClaws.addEffect(fortifyOneHanded);
//        bearClaws.addEffect(damageMagickaRegen);
        bearClaws.addEffect(getEffectByHashValue("0003EB16"));
        bearClaws.addEffect(getEffectByHashValue("0003EAF3"));
        bearClaws.addEffect(getEffectByHashValue("0003EB19"));
        bearClaws.addEffect(getEffectByHashValue("00073F2B"));
        Ingredient bee = new Ingredient("Bee");
//        bee.addEffect(restoreStamina);
//        bee.addEffect(ravageStamina);
//        bee.addEffect(regenStamina);
//        bee.addEffect(weakShock);
        bee.addEffect(getEffectByHashValue("0003EB16"));
        bee.addEffect(getEffectByHashValue("00073F23"));
        bee.addEffect(getEffectByHashValue("0003EB08"));
        bee.addEffect(getEffectByHashValue("00073F2F"));
        Ingredient beehiveHusk = new Ingredient("Beehive Husk");
//        beehiveHusk.addEffect(resistPoison);
//        beehiveHusk.addEffect(fortifyLightArmor);
//        beehiveHusk.addEffect(fortifySneak);
//        beehiveHusk.addEffect(fortifyDestruction);
        beehiveHusk.addEffect(getEffectByHashValue("00090041"));
        beehiveHusk.addEffect(getEffectByHashValue("0003EB1F"));
        beehiveHusk.addEffect(getEffectByHashValue("0003EB22"));
        beehiveHusk.addEffect(getEffectByHashValue("0003EB26"));
        Ingredient beritAshes = new Ingredient("Berit's Ashes");
//        beritAshes.addEffect(damageStamina);
//        beritAshes.addEffect(resistFire);
//        beritAshes.addEffect(fortifyConjuration);
//        beritAshes.addEffect(ravageStamina);
        beritAshes.addEffect(getEffectByHashValue("0003A2C6"));
        beritAshes.addEffect(getEffectByHashValue("0003EAEA"));
        beritAshes.addEffect(getEffectByHashValue("0003EB25"));
        beritAshes.addEffect(getEffectByHashValue("00073F23"));
        Ingredient bleedingCrown = new Ingredient("Bleeding Crown");
//        bleedingCrown.addEffect(weakFire);
//        bleedingCrown.addEffect(fortifyBlock);
//        bleedingCrown.addEffect(weakPoison);
//        bleedingCrown.addEffect(resistMagic);
        bleedingCrown.addEffect(getEffectByHashValue("00073F2D"));
        bleedingCrown.addEffect(getEffectByHashValue("0003EB1C"));
        bleedingCrown.addEffect(getEffectByHashValue("00090042"));
        bleedingCrown.addEffect(getEffectByHashValue("00039E51"));
        Ingredient blisterwort = new Ingredient("Blisterwort");
//        blisterwort.addEffect(damageStamina);
//        blisterwort.addEffect(frenzy);
//        blisterwort.addEffect(restoreHealth);
//        blisterwort.addEffect(fortifySmithing);
        blisterwort.addEffect(getEffectByHashValue("0003A2C6"));
        blisterwort.addEffect(getEffectByHashValue("00073F29"));
        blisterwort.addEffect(getEffectByHashValue("0003EB15"));
        blisterwort.addEffect(getEffectByHashValue("0003EB1D"));
        Ingredient blueButterWing = new Ingredient("Blue Butterfly Wing");
//        blueButterWing.addEffect(damageStamina);
//        blueButterWing.addEffect(fortifyConjuration);
//        blueButterWing.addEffect(damageMagickaRegen);
//        blueButterWing.addEffect(fortifyEnchanting);
        blueButterWing.addEffect(getEffectByHashValue("0003A2C6"));
        blueButterWing.addEffect(getEffectByHashValue("0003EB25"));
        blueButterWing.addEffect(getEffectByHashValue("00073F2B"));
        blueButterWing.addEffect(getEffectByHashValue("0003EB29"));
        Ingredient blueDartwing = new Ingredient("Blue Dartwing");
//        blueDartwing.addEffect(resistShock);
//        blueDartwing.addEffect(fortifyPickpocket);
//        blueDartwing.addEffect(restoreHealth);
//        blueDartwing.addEffect(fear);
        blueDartwing.addEffect(getEffectByHashValue("0003EAEC"));
        blueDartwing.addEffect(getEffectByHashValue("0003EB20"));
        blueDartwing.addEffect(getEffectByHashValue("0003EB15"));
        blueDartwing.addEffect(getEffectByHashValue("00073F20"));
        Ingredient blueMFlower = new Ingredient("Blue Mountain Flower");
//        blueMFlower.addEffect(restoreHealth);
//        blueMFlower.addEffect(fortifyConjuration);
//        blueMFlower.addEffect(fortifyHealth);
//        blueMFlower.addEffect(damageMagickaRegen);
        blueMFlower.addEffect(getEffectByHashValue("0003EB15"));
        blueMFlower.addEffect(getEffectByHashValue("0003EB25"));
        blueMFlower.addEffect(getEffectByHashValue("0003EAF3"));
        blueMFlower.addEffect(getEffectByHashValue("00073F2B"));
        Ingredient boarTusk = new Ingredient("Boar Tusk");
//        boarTusk.addEffect(fortifyStamina);
//        boarTusk.addEffect(fortifyHealth);
//        boarTusk.addEffect(fortifyBlock);
//        boarTusk.addEffect(frenzy);
        boarTusk.addEffect(getEffectByHashValue("0003EAF9"));
        boarTusk.addEffect(getEffectByHashValue("0003EAF3"));
        boarTusk.addEffect(getEffectByHashValue("0003EB1C"));
        boarTusk.addEffect(getEffectByHashValue("00073F29"));
        Ingredient boneMeal = new Ingredient("Bone Meal");
//        boneMeal.addEffect(damageStamina);
//        boneMeal.addEffect(resistFire);
//        boneMeal.addEffect(fortifyConjuration);
//        boneMeal.addEffect(ravageStamina);
        boneMeal.addEffect(getEffectByHashValue("0003A2C6"));
        boneMeal.addEffect(getEffectByHashValue("0003EAEA"));
        boneMeal.addEffect(getEffectByHashValue("0003EB25"));
        boneMeal.addEffect(getEffectByHashValue("00073F23"));
        Ingredient briarHeart = new Ingredient("Briar Heart");
//        briarHeart.addEffect(restoreMagicka);
//        briarHeart.addEffect(fortifyBlock);
//        briarHeart.addEffect(paralysis);
//        briarHeart.addEffect(fortifyHealth);
        briarHeart.addEffect(getEffectByHashValue("0003EB17"));
        briarHeart.addEffect(getEffectByHashValue("0003EB1C"));
        briarHeart.addEffect(getEffectByHashValue("00073F30"));
        briarHeart.addEffect(getEffectByHashValue("0003EAF3"));
        Ingredient burntSprigganWood = new Ingredient("Burnt Spriggan Wood");
//        burntSprigganWood.addEffect(weakFire);
//        burntSprigganWood.addEffect(fortifyAlteration);
//        burntSprigganWood.addEffect(damageMagickaRegen);
//        burntSprigganWood.addEffect(slow);
        burntSprigganWood.addEffect(getEffectByHashValue("00073F2D"));
        burntSprigganWood.addEffect(getEffectByHashValue("0003EB24"));
        burntSprigganWood.addEffect(getEffectByHashValue("00073F2B"));
        burntSprigganWood.addEffect(getEffectByHashValue("00073F25"));
        Ingredient butterflyWing = new Ingredient("Butterfly Wing");
//        butterflyWing.addEffect(restoreHealth);
//        butterflyWing.addEffect(fortifyBarter);
//        butterflyWing.addEffect(lingeringStamina);
//        butterflyWing.addEffect(damageMagicka);
        butterflyWing.addEffect(getEffectByHashValue("0003EB15"));
        butterflyWing.addEffect(getEffectByHashValue("0003EB23"));
        butterflyWing.addEffect(getEffectByHashValue("0010DE5E"));
        butterflyWing.addEffect(getEffectByHashValue("0010DE5E"));
        Ingredient canisRoot = new Ingredient("Canis Root");
//        canisRoot.addEffect(damageStamina);
//        canisRoot.addEffect(fortifyOneHanded);
//        canisRoot.addEffect(fortifyMarksman);
//        canisRoot.addEffect(paralysis);
        canisRoot.addEffect(getEffectByHashValue("0003A2C6"));
        canisRoot.addEffect(getEffectByHashValue("0003EB19"));
        canisRoot.addEffect(getEffectByHashValue("0003EB1B"));
        canisRoot.addEffect(getEffectByHashValue("00073F30"));
        Ingredient charredSkeeverHide = new Ingredient("Charred Skeever Hide");
//        charredSkeeverHide.addEffect(restoreStamina);
//        charredSkeeverHide.addEffect(cureDisease);
//        charredSkeeverHide.addEffect(resistPoison);
//        charredSkeeverHide.addEffect(restoreHealth);
        charredSkeeverHide.addEffect(getEffectByHashValue("0003EB16"));
        charredSkeeverHide.addEffect(getEffectByHashValue("000AE722"));
        charredSkeeverHide.addEffect(getEffectByHashValue("00090041"));
        charredSkeeverHide.addEffect(getEffectByHashValue("0003EB15"));
        Ingredient chaurusEggs = new Ingredient("Chaurus Eggs");
//        chaurusEggs.addEffect(weakPoison);
//        chaurusEggs.addEffect(fortifyStamina);
//        chaurusEggs.addEffect(damageMagicka);
//        chaurusEggs.addEffect(invisibility);
        chaurusEggs.addEffect(getEffectByHashValue("00090042"));
        chaurusEggs.addEffect(getEffectByHashValue("0003EAF9"));
        chaurusEggs.addEffect(getEffectByHashValue("0003A2B6"));
        chaurusEggs.addEffect(getEffectByHashValue("0003EB3D"));
        Ingredient chaurusAntenna = new Ingredient("Chaurus Hunter Antennae");
//        chaurusAntenna.addEffect(damageStamina);
//        chaurusAntenna.addEffect(fortifyConjuration);
//        chaurusAntenna.addEffect(damageMagickaRegen);
//        chaurusAntenna.addEffect(fortifyEnchanting);
        chaurusAntenna.addEffect(getEffectByHashValue("0003A2C6"));
        chaurusAntenna.addEffect(getEffectByHashValue("0003EB25"));
        chaurusAntenna.addEffect(getEffectByHashValue("00073F2B"));
        chaurusAntenna.addEffect(getEffectByHashValue("0003EB29"));
        Ingredient chickenEgg = new Ingredient("Chicken's Egg");
//        chickenEgg.addEffect(resistMagic);
//        chickenEgg.addEffect(damageMagickaRegen);
//        chickenEgg.addEffect(waterbreathing);
//        chickenEgg.addEffect(lingeringStamina);
        chickenEgg.addEffect(getEffectByHashValue("00039E51"));
        chickenEgg.addEffect(getEffectByHashValue("00073F2B"));
        chickenEgg.addEffect(getEffectByHashValue("0003AC2D"));
        chickenEgg.addEffect(getEffectByHashValue("0010DE5E"));
        Ingredient creepCluster = new Ingredient("Creep Cluster");
//        creepCluster.addEffect(restoreMagicka);
//        creepCluster.addEffect(damageStaminaRegen);
//        creepCluster.addEffect(fortifyCarryWeight);
//        creepCluster.addEffect(weakMagic);
        creepCluster.addEffect(getEffectByHashValue("0003EB17"));
        creepCluster.addEffect(getEffectByHashValue("00073F2C"));
        creepCluster.addEffect(getEffectByHashValue("0003EB01"));
        creepCluster.addEffect(getEffectByHashValue("00073F51"));
        Ingredient crimsonNirnroot = new Ingredient("Crimson Nirnroot");
//        crimsonNirnroot.addEffect(damageHealth);
//        crimsonNirnroot.addEffect(damageStamina);
//        crimsonNirnroot.addEffect(invisibility);
//        crimsonNirnroot.addEffect(resistMagic);
        crimsonNirnroot.addEffect(getEffectByHashValue("0003EB42"));
        crimsonNirnroot.addEffect(getEffectByHashValue("0003A2C6"));
        crimsonNirnroot.addEffect(getEffectByHashValue("0003EB3D"));
        crimsonNirnroot.addEffect(getEffectByHashValue("00039E51"));
        Ingredient cyrodilicSpade = new Ingredient("Cyrodilic Spadetail");
//        cyrodilicSpade.addEffect(damageStamina);
//        cyrodilicSpade.addEffect(fortifyRestoration);
//        cyrodilicSpade.addEffect(fear);
//        cyrodilicSpade.addEffect(ravageHealth);
        cyrodilicSpade.addEffect(getEffectByHashValue("0003A2C6"));
        cyrodilicSpade.addEffect(getEffectByHashValue("0003EB28"));
        cyrodilicSpade.addEffect(getEffectByHashValue("00073F20"));
        cyrodilicSpade.addEffect(getEffectByHashValue("00073F26"));
        Ingredient daedraHeart = new Ingredient("Daedra Heart");
//        daedraHeart.addEffect(restoreHealth);
//        daedraHeart.addEffect(damageStaminaRegen);
//        daedraHeart.addEffect(damageMagicka);
//        daedraHeart.addEffect(fear);
        daedraHeart.addEffect(getEffectByHashValue("0003EB15"));
        daedraHeart.addEffect(getEffectByHashValue("00073F2C"));
        daedraHeart.addEffect(getEffectByHashValue("0003A2B6"));
        daedraHeart.addEffect(getEffectByHashValue("00073F20"));
        Ingredient deathbell = new Ingredient("Deathbell");
//        deathbell.addEffect(damageHealth);
//        deathbell.addEffect(ravageStamina);
//        deathbell.addEffect(slow);
//        deathbell.addEffect(weakPoison);
        deathbell.addEffect(getEffectByHashValue("0003EB42"));
        deathbell.addEffect(getEffectByHashValue("00073F23"));
        deathbell.addEffect(getEffectByHashValue("00073F25"));
        deathbell.addEffect(getEffectByHashValue("00090042"));
        Ingredient dragonTongue = new Ingredient("Dragon's Tongue");
//        dragonTongue.addEffect(resistFire);
//        dragonTongue.addEffect(fortifyBarter);
//        dragonTongue.addEffect(fortifyIllusion);
//        dragonTongue.addEffect(fortifyTwoHanded);
        dragonTongue.addEffect(getEffectByHashValue("0003EAEA"));
        dragonTongue.addEffect(getEffectByHashValue("0003EB23"));
        dragonTongue.addEffect(getEffectByHashValue("0003EB27"));
        dragonTongue.addEffect(getEffectByHashValue("0003EB1A"));
        Ingredient ectoplasm = new Ingredient("Ectoplasm");
//        ectoplasm.addEffect(restoreMagicka);
//        ectoplasm.addEffect(fortifyDestruction);
//        ectoplasm.addEffect(fortifyMagicka);
//        ectoplasm.addEffect(damageHealth);
        ectoplasm.addEffect(getEffectByHashValue("0003EB17"));
        ectoplasm.addEffect(getEffectByHashValue("0003EB26"));
        ectoplasm.addEffect(getEffectByHashValue("0003EAF8"));
        ectoplasm.addEffect(getEffectByHashValue("0003EB42"));
        Ingredient elvesEar = new Ingredient("Elves Ear");
//        elvesEar.addEffect(restoreMagicka);
//        elvesEar.addEffect(fortifyMarksman);
//        elvesEar.addEffect(weakFrost);
//        elvesEar.addEffect(resistFire);
        elvesEar.addEffect(getEffectByHashValue("0003EB17"));
        elvesEar.addEffect(getEffectByHashValue("0003EB1B"));
        elvesEar.addEffect(getEffectByHashValue("00073F2E"));
        elvesEar.addEffect(getEffectByHashValue("0003EAEA"));
        Ingredient emperorMoss = new Ingredient("Emperor Parasol Moss");
//        emperorMoss.addEffect(damageHealth);
//        emperorMoss.addEffect(fortifyMagicka);
//        emperorMoss.addEffect(regenHealth);
//        emperorMoss.addEffect(fortifyTwoHanded);
        emperorMoss.addEffect(getEffectByHashValue("0003EB42"));
        emperorMoss.addEffect(getEffectByHashValue("0003EAF8"));
        emperorMoss.addEffect(getEffectByHashValue("0003EB06"));
        emperorMoss.addEffect(getEffectByHashValue("0003EB1A"));
        Ingredient eyeSaberCat = new Ingredient("Eye of Saber Cat");
//        eyeSaberCat.addEffect(restoreStamina);
//        eyeSaberCat.addEffect(ravageHealth);
//        eyeSaberCat.addEffect(damageMagicka);
//        eyeSaberCat.addEffect(restoreHealth);
        eyeSaberCat.addEffect(getEffectByHashValue("0003EB16"));
        eyeSaberCat.addEffect(getEffectByHashValue("00073F26"));
        eyeSaberCat.addEffect(getEffectByHashValue("0003A2B6"));
        eyeSaberCat.addEffect(getEffectByHashValue("0003EB15"));
        Ingredient falmerEar = new Ingredient("Falmer Ear");
//        falmerEar.addEffect(damageHealth);
//        falmerEar.addEffect(frenzy);
//        falmerEar.addEffect(resistPoison);
//        falmerEar.addEffect(fortifyLockpicking);
        falmerEar.addEffect(getEffectByHashValue("0003EB42"));
        falmerEar.addEffect(getEffectByHashValue("00073F29"));
        falmerEar.addEffect(getEffectByHashValue("00090041"));
        falmerEar.addEffect(getEffectByHashValue("0003EB21"));
        Ingredient felsaad = new Ingredient("Felsaad Tern Feathers");
//        felsaad.addEffect(restoreHealth);
//        felsaad.addEffect(fortifyLightArmor);
//        felsaad.addEffect(cureDisease);
//        felsaad.addEffect(resistMagic);
        felsaad.addEffect(getEffectByHashValue("0003EB15"));
        felsaad.addEffect(getEffectByHashValue("0003EB1F"));
        felsaad.addEffect(getEffectByHashValue("000AE722"));
        felsaad.addEffect(getEffectByHashValue("00039E51"));
        Ingredient fireSalts = new Ingredient("Fire Salts");
//        fireSalts.addEffect(weakFrost);
//        fireSalts.addEffect(resistFire);
//        fireSalts.addEffect(restoreMagicka);
//        fireSalts.addEffect(regenMagicka);
        fireSalts.addEffect(getEffectByHashValue("00073F2E"));
        fireSalts.addEffect(getEffectByHashValue("0003EAEA"));
        fireSalts.addEffect(getEffectByHashValue("0003EB17"));
        fireSalts.addEffect(getEffectByHashValue("0003EB07"));
        Ingredient flyAmanita = new Ingredient("Fly Amanita");
//        flyAmanita.addEffect(resistFire);
//        flyAmanita.addEffect(fortifyTwoHanded);
//        flyAmanita.addEffect(frenzy);
//        flyAmanita.addEffect(regenStamina);
        flyAmanita.addEffect(getEffectByHashValue("0003EAEA"));
        flyAmanita.addEffect(getEffectByHashValue("0003EB1A"));
        flyAmanita.addEffect(getEffectByHashValue("00073F29"));
        flyAmanita.addEffect(getEffectByHashValue("0003EB08"));
        Ingredient frostMirriam = new Ingredient("Frost Mirriam");
//        frostMirriam.addEffect(resistFrost);
//        frostMirriam.addEffect(fortifySneak);
//        frostMirriam.addEffect(ravageMagicka);
//        frostMirriam.addEffect(damageStaminaRegen);
        frostMirriam.addEffect(getEffectByHashValue("0003EAEB"));
        frostMirriam.addEffect(getEffectByHashValue("0003EB22"));
        frostMirriam.addEffect(getEffectByHashValue("00073F27"));
        frostMirriam.addEffect(getEffectByHashValue("00073F2C"));
        Ingredient frostSalts = new Ingredient("Frost Salts");
//        frostSalts.addEffect(weakFire);
//        frostSalts.addEffect(resistFrost);
//        frostSalts.addEffect(restoreMagicka);
//        frostSalts.addEffect(fortifyConjuration);
        frostSalts.addEffect(getEffectByHashValue("00073F2D"));
        frostSalts.addEffect(getEffectByHashValue("0003EAEB"));
        frostSalts.addEffect(getEffectByHashValue("0003EB17"));
        frostSalts.addEffect(getEffectByHashValue("0003EB25"));
        Ingredient garlic = new Ingredient("Garlic");
//        garlic.addEffect(resistPoison);
//        garlic.addEffect(fortifyStamina);
//        garlic.addEffect(regenMagicka);
//        garlic.addEffect(regenHealth);
        garlic.addEffect(getEffectByHashValue("00090041"));
        garlic.addEffect(getEffectByHashValue("0003EAF9"));
        garlic.addEffect(getEffectByHashValue("0003EB07"));
        garlic.addEffect(getEffectByHashValue("0003EB06"));
        Ingredient giantLichen = new Ingredient("Giant Lichen");
//        giantLichen.addEffect(weakShock);
//        giantLichen.addEffect(ravageHealth);
//        giantLichen.addEffect(weakPoison);
//        giantLichen.addEffect(restoreMagicka);
        giantLichen.addEffect(getEffectByHashValue("00073F2F"));
        giantLichen.addEffect(getEffectByHashValue("00073F26"));
        giantLichen.addEffect(getEffectByHashValue("00090042"));
        giantLichen.addEffect(getEffectByHashValue("0003EB17"));
        Ingredient giantToe = new Ingredient("Giant's Toe");
//        giantToe.addEffect(damageStamina);
//        giantToe.addEffect(fortifyHealth);
//        giantToe.addEffect(fortifyCarryWeight);
//        giantToe.addEffect(damageStaminaRegen);
        giantToe.addEffect(getEffectByHashValue("0003A2C6"));
        giantToe.addEffect(getEffectByHashValue("0003EAF3"));
        giantToe.addEffect(getEffectByHashValue("0003EB01"));
        giantToe.addEffect(getEffectByHashValue("00073F2C"));
        Ingredient gleamblossom = new Ingredient("Gleamblossom");
//        gleamblossom.addEffect(resistMagic);
//        gleamblossom.addEffect(fear);
//        gleamblossom.addEffect(regenHealth);
//        gleamblossom.addEffect(paralysis);
        gleamblossom.addEffect(getEffectByHashValue("00039E51"));
        gleamblossom.addEffect(getEffectByHashValue("00073F20"));
        gleamblossom.addEffect(getEffectByHashValue("0003EB06"));
        gleamblossom.addEffect(getEffectByHashValue("00073F30"));
        Ingredient glowDust = new Ingredient("Glow Dust");
//        glowDust.addEffect(damageMagicka);
//        glowDust.addEffect(damageMagickaRegen);
//        glowDust.addEffect(fortifyDestruction);
//        glowDust.addEffect(resistShock);
        glowDust.addEffect(getEffectByHashValue("0003A2B6"));
        glowDust.addEffect(getEffectByHashValue("00073F2B"));
        glowDust.addEffect(getEffectByHashValue("0003EB26"));
        glowDust.addEffect(getEffectByHashValue("0003EAEC"));
        Ingredient glowingMushroom = new Ingredient("Glowing Mushroom");
//        glowingMushroom.addEffect(resistShock);
//        glowingMushroom.addEffect(fortifyDestruction);
//        glowingMushroom.addEffect(fortifySmithing);
//        glowingMushroom.addEffect(fortifyHealth);
        glowingMushroom.addEffect(getEffectByHashValue("0003EAEC"));
        glowingMushroom.addEffect(getEffectByHashValue("0003EB26"));
        glowingMushroom.addEffect(getEffectByHashValue("0003EB1D"));
        glowingMushroom.addEffect(getEffectByHashValue("0003EAF3"));
        Ingredient grassPod = new Ingredient("Grass Pod");
//        grassPod.addEffect(resistPoison);
//        grassPod.addEffect(ravageMagicka);
//        grassPod.addEffect(fortifyAlteration);
//        grassPod.addEffect(restoreMagicka);
        grassPod.addEffect(getEffectByHashValue("00090041"));
        grassPod.addEffect(getEffectByHashValue("00073F27"));
        grassPod.addEffect(getEffectByHashValue("0003EB24"));
        grassPod.addEffect(getEffectByHashValue("0003EB17"));
        Ingredient hagravenClaw = new Ingredient("Hagraven Claw");
//        hagravenClaw.addEffect(resistMagic);
//        hagravenClaw.addEffect(lingeringMagicka);
//        hagravenClaw.addEffect(fortifyEnchanting);
//        hagravenClaw.addEffect(fortifyBarter);
        hagravenClaw.addEffect(getEffectByHashValue("00039E51"));
        hagravenClaw.addEffect(getEffectByHashValue("0010DE5F"));
        hagravenClaw.addEffect(getEffectByHashValue("0003EB29"));
        hagravenClaw.addEffect(getEffectByHashValue("0003EB23"));
        Ingredient hagravenFeathers = new Ingredient("Hagraven Feathers");
//        hagravenFeathers.addEffect(damageMagicka);
//        hagravenFeathers.addEffect(fortifyConjuration);
//        hagravenFeathers.addEffect(frenzy);
//        hagravenFeathers.addEffect(weakShock);
        hagravenFeathers.addEffect(getEffectByHashValue("0003A2B6"));
        hagravenFeathers.addEffect(getEffectByHashValue("0003EB25"));
        hagravenFeathers.addEffect(getEffectByHashValue("00073F29"));
        hagravenFeathers.addEffect(getEffectByHashValue("00073F2F"));
        Ingredient hangingMoss = new Ingredient("Hanging Moss");
//        hangingMoss.addEffect(damageMagicka);
//        hangingMoss.addEffect(fortifyHealth);
//        hangingMoss.addEffect(damageMagickaRegen);
//        hangingMoss.addEffect(fortifyOneHanded);
        hangingMoss.addEffect(getEffectByHashValue("0003A2B6"));
        hangingMoss.addEffect(getEffectByHashValue("0003EAF3"));
        hangingMoss.addEffect(getEffectByHashValue("00073F2B"));
        hangingMoss.addEffect(getEffectByHashValue("0003EB19"));
        Ingredient hawkBeak = new Ingredient("Hawk Beak");
//        hawkBeak.addEffect(restoreStamina);
//        hawkBeak.addEffect(resistFrost);
//        hawkBeak.addEffect(fortifyCarryWeight);
//        hawkBeak.addEffect(resistShock);
        hawkBeak.addEffect(getEffectByHashValue("0003EB16"));
        hawkBeak.addEffect(getEffectByHashValue("0003EAEB"));
        hawkBeak.addEffect(getEffectByHashValue("0003EB01"));
        hawkBeak.addEffect(getEffectByHashValue("0003EAEC"));
        Ingredient hawkFeathers = new Ingredient("Hawk Feathers");
//        hawkFeathers.addEffect(cureDisease);
//        hawkFeathers.addEffect(fortifyLightArmor);
//        hawkFeathers.addEffect(fortifyOneHanded);
//        hawkFeathers.addEffect(fortifySneak);
        hawkFeathers.addEffect(getEffectByHashValue("000AE722"));
        hawkFeathers.addEffect(getEffectByHashValue("0003EB1F"));
        hawkFeathers.addEffect(getEffectByHashValue("0003EB19"));
        hawkFeathers.addEffect(getEffectByHashValue("0003EB22"));
        Ingredient hawkEgg = new Ingredient("Hawk's Egg");
//        hawkEgg.addEffect(resistMagic);
//        hawkEgg.addEffect(damageMagickaRegen);
//        hawkEgg.addEffect(waterbreathing);
//        hawkEgg.addEffect(lingeringStamina);
        hawkEgg.addEffect(getEffectByHashValue("00039E51"));
        hawkEgg.addEffect(getEffectByHashValue("00073F2B"));
        hawkEgg.addEffect(getEffectByHashValue("0003AC2D"));
        hawkEgg.addEffect(getEffectByHashValue("0010DE5E"));
        Ingredient histcarp = new Ingredient("Histcarp");
//        histcarp.addEffect(restoreStamina);
//        histcarp.addEffect(fortifyMagicka);
//        histcarp.addEffect(damageStaminaRegen);
//        histcarp.addEffect(waterbreathing);
        histcarp.addEffect(getEffectByHashValue("0003EB16"));
        histcarp.addEffect(getEffectByHashValue("0003EAF8"));
        histcarp.addEffect(getEffectByHashValue("00073F2C"));
        histcarp.addEffect(getEffectByHashValue("0003AC2D"));
        Ingredient honeycomb = new Ingredient("Honeycomb");
//        honeycomb.addEffect(restoreStamina);
//        honeycomb.addEffect(fortifyBlock);
//        honeycomb.addEffect(fortifyLightArmor);
//        honeycomb.addEffect(ravageStamina);
        honeycomb.addEffect(getEffectByHashValue("0003EB16"));
        honeycomb.addEffect(getEffectByHashValue("0003EB1C"));
        honeycomb.addEffect(getEffectByHashValue("0003EB1F"));
        honeycomb.addEffect(getEffectByHashValue("00073F23"));
        Ingredient humanFlesh = new Ingredient("Human Flesh");
//        humanFlesh.addEffect(damageHealth);
//        humanFlesh.addEffect(paralysis);
//        humanFlesh.addEffect(restoreMagicka);
//        humanFlesh.addEffect(fortifySneak);
        humanFlesh.addEffect(getEffectByHashValue("0003EB42"));
        humanFlesh.addEffect(getEffectByHashValue("00073F30"));
        humanFlesh.addEffect(getEffectByHashValue("0003EB17"));
        humanFlesh.addEffect(getEffectByHashValue("0003EB22"));
        Ingredient humanHeart = new Ingredient("Human Heart");
//        humanHeart.addEffect(damageHealth);
//        humanHeart.addEffect(damageMagicka);
//        humanHeart.addEffect(damageMagickaRegen);
//        humanHeart.addEffect(frenzy);
        humanHeart.addEffect(getEffectByHashValue("0003EB42"));
        humanHeart.addEffect(getEffectByHashValue("0003A2B6"));
        humanHeart.addEffect(getEffectByHashValue("00073F2B"));
        humanHeart.addEffect(getEffectByHashValue("00073F29"));
        Ingredient iceWraithTeeth = new Ingredient("Ice Wraith Teeth");
//        iceWraithTeeth.addEffect(weakFrost);
//        iceWraithTeeth.addEffect(fortifyHeavyArmor);
//        iceWraithTeeth.addEffect(invisibility);
//        iceWraithTeeth.addEffect(weakFire);
        iceWraithTeeth.addEffect(getEffectByHashValue("00073F2E"));
        iceWraithTeeth.addEffect(getEffectByHashValue("0003EB1E"));
        iceWraithTeeth.addEffect(getEffectByHashValue("0003EB3D"));
        iceWraithTeeth.addEffect(getEffectByHashValue("00073F2D"));
        Ingredient impStool = new Ingredient("Imp Stool");
//        impStool.addEffect(damageHealth);
//        impStool.addEffect(lingeringHealth);
//        impStool.addEffect(paralysis);
//        impStool.addEffect(restoreHealth);
        impStool.addEffect(getEffectByHashValue("0003EB42"));
        impStool.addEffect(getEffectByHashValue("0010AA4A"));
        impStool.addEffect(getEffectByHashValue("00073F30"));
        impStool.addEffect(getEffectByHashValue("0003EB15"));
        Ingredient jazbayGrapes = new Ingredient("Jazbay Grapes");
//        jazbayGrapes.addEffect(weakMagic);
//        jazbayGrapes.addEffect(fortifyMagicka);
//        jazbayGrapes.addEffect(regenMagicka);
//        jazbayGrapes.addEffect(ravageHealth);
        jazbayGrapes.addEffect(getEffectByHashValue("00073F51"));
        jazbayGrapes.addEffect(getEffectByHashValue("0003EAF8"));
        jazbayGrapes.addEffect(getEffectByHashValue("0003EB07"));
        jazbayGrapes.addEffect(getEffectByHashValue("00073F26"));
        Ingredient juniperBerries = new Ingredient("Juniper Berries");
//        juniperBerries.addEffect(weakFire);
//        juniperBerries.addEffect(fortifyMarksman);
//        juniperBerries.addEffect(regenHealth);
//        juniperBerries.addEffect(damageStaminaRegen);
        juniperBerries.addEffect(getEffectByHashValue("00073F2D"));
        juniperBerries.addEffect(getEffectByHashValue("0003EB1B"));
        juniperBerries.addEffect(getEffectByHashValue("00073F26"));
        juniperBerries.addEffect(getEffectByHashValue("00073F2C"));
        Ingredient largeAntlers = new Ingredient("Large Antlers");
//        largeAntlers.addEffect(restoreStamina);
//        largeAntlers.addEffect(fortifyStamina);
//        largeAntlers.addEffect(slow);
//        largeAntlers.addEffect(damageStaminaRegen);
        largeAntlers.addEffect(getEffectByHashValue("0003EB16"));
        largeAntlers.addEffect(getEffectByHashValue("0003EAF9"));
        largeAntlers.addEffect(getEffectByHashValue("00073F25"));
        largeAntlers.addEffect(getEffectByHashValue("00073F2C"));
        Ingredient lavender = new Ingredient("Lavender");
//        lavender.addEffect(resistMagic);
//        lavender.addEffect(fortifyStamina);
//        lavender.addEffect(ravageMagicka);
//        lavender.addEffect(fortifyConjuration);
        lavender.addEffect(getEffectByHashValue("00039E51"));
        lavender.addEffect(getEffectByHashValue("0003EAF9"));
        lavender.addEffect(getEffectByHashValue("00073F23"));
        lavender.addEffect(getEffectByHashValue("0003EB25"));
        Ingredient lunaMoth = new Ingredient("Luna Moth Wing");
//        lunaMoth.addEffect(damageMagicka);
//        lunaMoth.addEffect(fortifyLightArmor);
//        lunaMoth.addEffect(regenHealth);
//        lunaMoth.addEffect(invisibility);
        lunaMoth.addEffect(getEffectByHashValue("0003A2B6"));
        lunaMoth.addEffect(getEffectByHashValue("0003EB1F"));
        lunaMoth.addEffect(getEffectByHashValue("0003EB06"));
        lunaMoth.addEffect(getEffectByHashValue("0003EB3D"));
        Ingredient moonSugar = new Ingredient("Moon Sugar");
//        moonSugar.addEffect(weakFire);
//        moonSugar.addEffect(resistFrost);
//        moonSugar.addEffect(restoreMagicka);
//        moonSugar.addEffect(regenMagicka);
        moonSugar.addEffect(getEffectByHashValue("00073F2D"));
        moonSugar.addEffect(getEffectByHashValue("0003EAEB"));
        moonSugar.addEffect(getEffectByHashValue("0003EB17"));
        moonSugar.addEffect(getEffectByHashValue("0003EB07"));
        Ingredient moraTapinella = new Ingredient("Mora Tapinella");
//        moraTapinella.addEffect(restoreMagicka);
//        moraTapinella.addEffect(lingeringHealth);
//        moraTapinella.addEffect(regenStamina);
//        moraTapinella.addEffect(fortifyIllusion);
        moraTapinella.addEffect(getEffectByHashValue("0003EB17"));
        moraTapinella.addEffect(getEffectByHashValue("0010AA4A"));
        moraTapinella.addEffect(getEffectByHashValue("0003EB08"));
        moraTapinella.addEffect(getEffectByHashValue("0003EB27"));
        Ingredient mudcrabChitin = new Ingredient("Mudcrab Chitin");
//        mudcrabChitin.addEffect(restoreStamina);
//        mudcrabChitin.addEffect(cureDisease);
//        mudcrabChitin.addEffect(resistPoison);
//        mudcrabChitin.addEffect(resistFire);
        mudcrabChitin.addEffect(getEffectByHashValue("0003EB16"));
        mudcrabChitin.addEffect(getEffectByHashValue("000AE722"));
        mudcrabChitin.addEffect(getEffectByHashValue("00090041"));
        mudcrabChitin.addEffect(getEffectByHashValue("0003EAEA"));
        Ingredient namirasRot = new Ingredient("Namira's Rot");
        namirasRot.addEffect(damageMagicka);
        namirasRot.addEffect(fortifyLockpicking);
        namirasRot.addEffect(fear);
        namirasRot.addEffect(regenHealth);
        Ingredient netchJelly = new Ingredient("Netch Jelly");
        netchJelly.addEffect(paralysis);
        netchJelly.addEffect(fortifyCarryWeight);
        netchJelly.addEffect(restoreStamina);
        netchJelly.addEffect(fear);
        Ingredient nightshade = new Ingredient("Nightshade");
        nightshade.addEffect(damageHealth);
        nightshade.addEffect(damageMagickaRegen);
        nightshade.addEffect(lingeringStamina);
        nightshade.addEffect(fortifyDestruction);
        Ingredient nirnroot = new Ingredient("Nirnroot");
        nirnroot.addEffect(damageHealth);
        nirnroot.addEffect(damageStamina);
        nirnroot.addEffect(invisibility);
        nirnroot.addEffect(resistMagic);
        Ingredient nordicBarnacle = new Ingredient("Nordic Barnacle");
        nordicBarnacle.addEffect(damageMagicka);
        nordicBarnacle.addEffect(waterbreathing);
        nordicBarnacle.addEffect(regenHealth);
        nordicBarnacle.addEffect(fortifyPickpocket);
        Ingredient orangeDartwing = new Ingredient("Orange Dartwing");
        orangeDartwing.addEffect(restoreHealth);
        orangeDartwing.addEffect(ravageMagicka);
        orangeDartwing.addEffect(fortifyPickpocket);
        orangeDartwing.addEffect(lingeringHealth);
        Ingredient pearl = new Ingredient("Pearl");
        pearl.addEffect(restoreStamina);
        pearl.addEffect(fortifyBlock);
        pearl.addEffect(restoreHealth);
        pearl.addEffect(resistShock);
        Ingredient pineThrush = new Ingredient("Pine Thrush Egg");
        pineThrush.addEffect(restoreStamina);
        pineThrush.addEffect(fortifyLockpicking);
        pineThrush.addEffect(weakPoison);
        pineThrush.addEffect(resistShock);
        Ingredient poisonBloom = new Ingredient("Poison Bloom");
        poisonBloom.addEffect(damageHealth);
        poisonBloom.addEffect(slow);
        poisonBloom.addEffect(fortifyCarryWeight);
        poisonBloom.addEffect(fear);
        Ingredient powderedTusk = new Ingredient("Powdered Mammoth Tusk");
        powderedTusk.addEffect(restoreStamina);
        powderedTusk.addEffect(fortifySneak);
        powderedTusk.addEffect(weakFire);
        powderedTusk.addEffect(fear);
        Ingredient purpleMFlower = new Ingredient("Purple Mountain Flower");
        purpleMFlower.addEffect(restoreStamina);
        purpleMFlower.addEffect(fortifySneak);
        purpleMFlower.addEffect(lingeringMagicka);
        purpleMFlower.addEffect(resistFrost);
        Ingredient redMFlower = new Ingredient("Red Mountain Flower");
        redMFlower.addEffect(restoreMagicka);
        redMFlower.addEffect(ravageMagicka);
        redMFlower.addEffect(fortifyMagicka);
        redMFlower.addEffect(damageHealth);
        Ingredient riverBetty = new Ingredient("River Betty");
        riverBetty.addEffect(damageHealth);
        riverBetty.addEffect(fortifyAlteration);
        riverBetty.addEffect(slow);
        riverBetty.addEffect(fortifyCarryWeight);
        Ingredient rockWarblerEgg = new Ingredient("Rock Warbler Egg");
        rockWarblerEgg.addEffect(restoreHealth);
        rockWarblerEgg.addEffect(fortifyOneHanded);
        rockWarblerEgg.addEffect(damageStamina);
        rockWarblerEgg.addEffect(weakMagic);
        Ingredient sabreCatTooth = new Ingredient("Sabre Cat Tooth");
        sabreCatTooth.addEffect(restoreStamina);
        sabreCatTooth.addEffect(fortifyHeavyArmor);
        sabreCatTooth.addEffect(fortifySmithing);
        sabreCatTooth.addEffect(weakPoison);
        Ingredient salmonRoe = new Ingredient("Salmon Roe");
        salmonRoe.addEffect(restoreStamina);
        salmonRoe.addEffect(waterbreathing);
        salmonRoe.addEffect(fortifyMagicka);
        salmonRoe.addEffect(weakPoison);
        Ingredient saltPile = new Ingredient("Salt Pile");
        saltPile.addEffect(weakMagic);
        saltPile.addEffect(fortifyRestoration);
        saltPile.addEffect(slow);
        saltPile.addEffect(regenMagicka);
        Ingredient scalyPholiota = new Ingredient("Scaly Pholiota");
        scalyPholiota.addEffect(weakMagic);
        scalyPholiota.addEffect(fortifyIllusion);
        scalyPholiota.addEffect(regenStamina);
        scalyPholiota.addEffect(fortifyCarryWeight);
        Ingredient scatheraw = new Ingredient("Scatheraw");
        scatheraw.addEffect(ravageHealth);
        scatheraw.addEffect(ravageStamina);
        scatheraw.addEffect(ravageMagicka);
        scatheraw.addEffect(lingeringHealth);
        Ingredient silversidePerch = new Ingredient("Silverside Perch");
        silversidePerch.addEffect(restoreStamina);
        silversidePerch.addEffect(damageStaminaRegen);
        silversidePerch.addEffect(ravageHealth);
        silversidePerch.addEffect(resistFrost);
        Ingredient skeeverTail = new Ingredient("Skeever Tail");
        skeeverTail.addEffect(damageStaminaRegen);
        skeeverTail.addEffect(ravageHealth);
        skeeverTail.addEffect(damageHealth);
        skeeverTail.addEffect(fortifyLightArmor);
        Ingredient slaughterfishEgg = new Ingredient("Slaughterfish Egg");
        slaughterfishEgg.addEffect(resistPoison);
        slaughterfishEgg.addEffect(fortifyPickpocket);
        slaughterfishEgg.addEffect(lingeringHealth);
        slaughterfishEgg.addEffect(fortifyStamina);
        Ingredient slaughterfishScales = new Ingredient("Slaughterfish Scales");
        slaughterfishScales.addEffect(resistFrost);
        slaughterfishScales.addEffect(lingeringHealth);
        slaughterfishScales.addEffect(fortifyHeavyArmor);
        slaughterfishScales.addEffect(fortifyBlock);
        Ingredient smallAntlers = new Ingredient("Small Antlers");
        smallAntlers.addEffect(weakPoison);
        smallAntlers.addEffect(fortifyRestoration);
        smallAntlers.addEffect(lingeringStamina);
        smallAntlers.addEffect(damageHealth);
        Ingredient smallPearl = new Ingredient("Small Pearl");
        smallPearl.addEffect(restoreStamina);
        smallPearl.addEffect(fortifyOneHanded);
        smallPearl.addEffect(fortifyRestoration);
        smallPearl.addEffect(resistFrost);
        Ingredient snowberries = new Ingredient("Snowberries");
        snowberries.addEffect(resistFire);
        snowberries.addEffect(fortifyEnchanting);
        snowberries.addEffect(resistFrost);
        snowberries.addEffect(resistShock);
        Ingredient spawnAsh = new Ingredient("Spawn Ash");
        spawnAsh.addEffect(ravageStamina);
        spawnAsh.addEffect(resistFire);
        spawnAsh.addEffect(fortifyEnchanting);
        spawnAsh.addEffect(ravageStamina);
        Ingredient spiderEgg = new Ingredient("Spider Egg");
        spiderEgg.addEffect(damageStamina);
        spiderEgg.addEffect(damageMagickaRegen);
        spiderEgg.addEffect(fortifyLockpicking);
        spiderEgg.addEffect(fortifyMarksman);
        Ingredient sprigganSap = new Ingredient("Spriggan Sap");
        sprigganSap.addEffect(damageMagickaRegen);
        sprigganSap.addEffect(fortifyEnchanting);
        sprigganSap.addEffect(fortifySmithing);
        sprigganSap.addEffect(fortifyAlteration);
        Ingredient swampFungalPod = new Ingredient("Swamp Fungal Pod");
        swampFungalPod.addEffect(resistShock);
        swampFungalPod.addEffect(lingeringMagicka);
        swampFungalPod.addEffect(paralysis);
        swampFungalPod.addEffect(restoreHealth);
        Ingredient taproot = new Ingredient("Taproot");
        taproot.addEffect(weakMagic);
        taproot.addEffect(fortifyIllusion);
        taproot.addEffect(regenMagicka);
        taproot.addEffect(restoreMagicka);
        Ingredient thistleBranch = new Ingredient("Thistle Branch");
        thistleBranch.addEffect(resistFrost);
        thistleBranch.addEffect(ravageStamina);
        thistleBranch.addEffect(resistPoison);
        thistleBranch.addEffect(fortifyHeavyArmor);
        Ingredient torchbugThorax = new Ingredient("Torchbug Thorax");
        torchbugThorax.addEffect(restoreStamina);
        torchbugThorax.addEffect(lingeringMagicka);
        torchbugThorax.addEffect(weakMagic);
        torchbugThorax.addEffect(fortifyStamina);
        Ingredient trollFat = new Ingredient("Troll Fat");
        trollFat.addEffect(resistPoison);
        trollFat.addEffect(fortifyTwoHanded);
        trollFat.addEffect(frenzy);
        trollFat.addEffect(damageHealth);
        Ingredient tundraCotton = new Ingredient("Tundra Cotton");
        tundraCotton.addEffect(resistMagic);
        tundraCotton.addEffect(fortifyMagicka);
        tundraCotton.addEffect(fortifyBlock);
        tundraCotton.addEffect(fortifyBarter);
        Ingredient vampireDust = new Ingredient("Vampire Dust");
        vampireDust.addEffect(invisibility);
        vampireDust.addEffect(restoreMagicka);
        vampireDust.addEffect(regenHealth);
        vampireDust.addEffect(cureDisease);
        Ingredient voidSalts = new Ingredient("Void Salts");
        voidSalts.addEffect(weakShock);
        voidSalts.addEffect(resistMagic);
        voidSalts.addEffect(damageHealth);
        voidSalts.addEffect(fortifyMagicka);
        Ingredient wheat = new Ingredient("Wheat");
        wheat.addEffect(restoreHealth);
        wheat.addEffect(fortifyHealth);
        wheat.addEffect(damageStaminaRegen);
        wheat.addEffect(lingeringStamina);
        Ingredient whiteCap = new Ingredient("White Cap");
        whiteCap.addEffect(weakFrost);
        whiteCap.addEffect(fortifyHeavyArmor);
        whiteCap.addEffect(restoreMagicka);
        whiteCap.addEffect(fortifyMagicka);
        Ingredient wispWrappings = new Ingredient("Wisp Wrappings");
        wispWrappings.addEffect(restoreStamina);
        wispWrappings.addEffect(fortifyDestruction);
        wispWrappings.addEffect(fortifyCarryWeight);
        wispWrappings.addEffect(resistMagic);

        Ingredient[] ingredients = new Ingredient[104];
        ingredients[0] = abeceanLongfin;
        ingredients[1] = ancestorMothWing;
        ingredients[2] = ashCreepCluster;
        ingredients[3] = ashenGrassPod;
        ingredients[4] = ashHopperJelly;
        ingredients[5] = bearClaws;
        ingredients[6] = bee;
        ingredients[7] = beehiveHusk;
        ingredients[8] = beritAshes;
        ingredients[9] = bleedingCrown;
        ingredients[10] = blisterwort;
        ingredients[11] = blueButterWing;
        ingredients[12] = blueDartwing;
        ingredients[13] = blueMFlower;
        ingredients[14] = boarTusk;
        ingredients[15] = boneMeal;
        ingredients[16] = briarHeart;
        ingredients[17] = burntSprigganWood;
        ingredients[18] = butterflyWing;
        ingredients[19] = canisRoot;
        ingredients[20] = charredSkeeverHide;
        ingredients[21] = chaurusAntenna;
        ingredients[22] = chaurusEggs;
        ingredients[23] = chickenEgg;
        ingredients[24] = creepCluster;
        ingredients[25] = crimsonNirnroot;
        ingredients[26] = cyrodilicSpade;
        ingredients[27] = daedraHeart;
        ingredients[28] = deathbell;
        ingredients[29] = dragonTongue;
        ingredients[30] = ectoplasm;
        ingredients[31] = elvesEar;
        ingredients[32] = emperorMoss;
        ingredients[33] = eyeSaberCat;
        ingredients[34] = falmerEar;
        ingredients[35] = felsaad;
        ingredients[36] = fireSalts;
        ingredients[37] = flyAmanita;
        ingredients[38] = frostMirriam;
        ingredients[39] = frostSalts;
        ingredients[40] = garlic;
        ingredients[41] = giantLichen;
        ingredients[42] = giantToe;
        ingredients[43] = gleamblossom;
        ingredients[44] = glowDust;
        ingredients[45] = glowingMushroom;
        ingredients[46] = grassPod;
        ingredients[47] = hagravenClaw;
        ingredients[48] = hagravenFeathers;
        ingredients[49] = hangingMoss;
        ingredients[50] = hawkBeak;
        ingredients[51] = hawkEgg;
        ingredients[52] = hawkFeathers;
        ingredients[53] = histcarp;
        ingredients[54] = honeycomb;
        ingredients[55] = humanFlesh;
        ingredients[56] = humanHeart;
        ingredients[57] = jazbayGrapes;
        ingredients[58] = juniperBerries;
        ingredients[59] = largeAntlers;
        ingredients[60] = lavender;
        ingredients[61] = lunaMoth;
        ingredients[62] = moonSugar;
        ingredients[63] = moraTapinella;
        ingredients[64] = mudcrabChitin;
        ingredients[65] = namirasRot;
        ingredients[66] = netchJelly;
        ingredients[67] = nightshade;
        ingredients[68] = nirnroot;
        ingredients[69] = nordicBarnacle;
        ingredients[70] = orangeDartwing;
        ingredients[71] = pearl;
        ingredients[72] = pineThrush;
        ingredients[73] = poisonBloom;
        ingredients[74] = powderedTusk;
        ingredients[75] = purpleMFlower;
        ingredients[76] = redMFlower;
        ingredients[77] = riverBetty;
        ingredients[78] = rockWarblerEgg;
        ingredients[79] = sabreCatTooth;
        ingredients[80] = salmonRoe;
        ingredients[81] = saltPile;
        ingredients[82] = scalyPholiota;
        ingredients[83] = scatheraw;
        ingredients[84] = silversidePerch;
        ingredients[85] = skeeverTail;
        ingredients[86] = slaughterfishEgg;
        ingredients[87] = slaughterfishScales;
        ingredients[88] = smallAntlers;
        ingredients[89] = smallPearl;
        ingredients[90] = snowberries;
        ingredients[91] = spiderEgg;
        ingredients[92] = sprigganSap;
        ingredients[93] = swampFungalPod;
        ingredients[94] = taproot;
        ingredients[95] = thistleBranch;
        ingredients[96] = torchbugThorax;
        ingredients[97] = trollFat;
        ingredients[98] = tundraCotton;
        ingredients[99] = vampireDust;
        ingredients[100] = voidSalts;
        ingredients[101] = wheat;
        ingredients[102] = whiteCap;
        ingredients[103] = wispWrappings;

        return ingredients;
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

package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import model.Ingredient;
import model.Item;

public class IngredientEnchantmentPane extends GridPane {

    //add ingredients
    private ComboBox<Ingredient> ingComboBox;

    //display these in listviews
    private ListView<Ingredient> listviewIngredients;
    private ListView<Item> listviewEnchantedItems;

    private ObservableList<Ingredient> obsListIngredients;
    private ObservableList<Item> obsListEnchantedItems;

    //buttons
    private Button addIngredientBtn;
    private Button addItemBtn;

    private Button removeIngredientBtn;
    private Button removeItemBtn;


    //constructor
    public IngredientEnchantmentPane() {

        //styling
        this.setVgap(10);
        this.setHgap(10);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(15, 12, 15, 12));

    }

    //getter setters
    public ComboBox<Ingredient> getIngComboBox() {
        return ingComboBox;
    }

    public void setIngComboBox(ComboBox<Ingredient> ingComboBox) {
        this.ingComboBox = ingComboBox;
    }

    public ListView<Ingredient> getListviewIngredients() {
        return listviewIngredients;
    }

    public void setListviewIngredients(ListView<Ingredient> listviewIngredients) {
        this.listviewIngredients = listviewIngredients;
    }

    public ListView<Item> getListviewEnchantedItems() {
        return listviewEnchantedItems;
    }

    public void setListviewEnchantedItems(ListView<Item> listviewEnchantedItems) {
        this.listviewEnchantedItems = listviewEnchantedItems;
    }

    public ObservableList<Ingredient> getObsListIngredients() {
        return obsListIngredients;
    }

    public void setObsListIngredients(ObservableList<Ingredient> obsListIngredients) {
        this.obsListIngredients = obsListIngredients;
    }

    public ObservableList<Item> getObsListEnchantedItems() {
        return obsListEnchantedItems;
    }

    public void setObsListEnchantedItems(ObservableList<Item> obsListEnchantedItems) {
        this.obsListEnchantedItems = obsListEnchantedItems;
    }

    public Button getAddIngredientBtn() {
        return addIngredientBtn;
    }

    public void setAddIngredientBtn(Button addIngredientBtn) {
        this.addIngredientBtn = addIngredientBtn;
    }

    public Button getAddItemBtn() {
        return addItemBtn;
    }

    public void setAddItemBtn(Button addItemBtn) {
        this.addItemBtn = addItemBtn;
    }

    public Button getRemoveIngredientBtn() {
        return removeIngredientBtn;
    }

    public void setRemoveIngredientBtn(Button removeIngredientBtn) {
        this.removeIngredientBtn = removeIngredientBtn;
    }

    public Button getRemoveItemBtn() {
        return removeItemBtn;
    }

    public void setRemoveItemBtn(Button removeItemBtn) {
        this.removeItemBtn = removeItemBtn;
    }


    //event handler methods
    public void addItemEventHandler(EventHandler<ActionEvent> resetHandler) {
        addItemBtn.setOnAction(resetHandler);
    }

    public void addIngredientEventHandler(EventHandler<ActionEvent> aboutHandler) {
        addIngredientBtn.setOnAction(aboutHandler);
    }

    public void removeItemEventHandler(EventHandler<ActionEvent> resetHandler) {
        removeItemBtn.setOnAction(resetHandler);
    }

    public void removeIngredientEventHandler(EventHandler<ActionEvent> aboutHandler) {
        removeIngredientBtn.setOnAction(aboutHandler);
    }

}

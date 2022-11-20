package view;

import controller.IngredientAutoComplete;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import model.Ingredient;
import model.Potion;

public class PotionGenerationPane extends GridPane {
    private ComboBox<Ingredient> ingredientCombobox;
    private ListView<Ingredient> ingredientListView;
    private ObservableList<Ingredient> ingredientObservableList;
    private ListView<Potion> potionListView;
    private ObservableList<Potion> potionObservableList;
    private Button addAllIngredientsBtn, clearIngredientsBtn;
    //private Button viewPotionDetailsBtn, removeIngredientBtn;

    public PotionGenerationPane() {
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setAlignment(Pos.TOP_LEFT);
        this.setHgap(10);
        this.setVgap(40);

        ingredientCombobox = new ComboBox<>();
        ingredientCombobox.setEditable(true);
        ingredientListView = new ListView<>();
        ingredientObservableList = FXCollections.observableArrayList();
        new IngredientAutoComplete(ingredientCombobox);

        addAllIngredientsBtn = new Button("Add all ingredients");
        clearIngredientsBtn = new Button("Remove all ingredients");

        VBox ingredientsVBox = getIngredientListVBox();
        ingredientsVBox.setAlignment(Pos.CENTER_LEFT);

        VBox potionsVBox = getPotionsListVBox();
        potionsVBox.setAlignment(Pos.CENTER_LEFT);

        //set column spacing
        ColumnConstraints column0 = new ColumnConstraints();
        column0.setHalignment(HPos.RIGHT);
        column0.setPercentWidth(50);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.LEFT);
        column1.setPercentWidth(50);

        this.add(ingredientsVBox, 0, 0);
        this.add(potionsVBox, 1, 0);

        this.getColumnConstraints().addAll(column0, column1);
    }

    public void populateIngredientCombobox(HashSet<Ingredient> ingredientList) {
        ingredientCombobox.getItems().addAll(ingredientList.stream().sorted(Comparator.comparing(Ingredient::getName)).toList());
        ingredientCombobox.getSelectionModel().select(0);
    }

    public VBox getIngredientListVBox() {
        VBox vBox = new VBox(10);

        HBox buttonBox = getIngredientButtonHBox();

        vBox.getChildren().addAll(buttonBox, ingredientCombobox, ingredientListView);

        return vBox;
    }

    public HBox getIngredientButtonHBox() {
        HBox hBox = new HBox(10);

        hBox.getChildren().addAll(addAllIngredientsBtn, clearIngredientsBtn);

        return hBox;
    }

    public VBox getPotionsListVBox() {
        VBox vBox = new VBox(10);

        Label label = new Label("Generated potions: ");
        potionListView = new ListView<>();
        potionObservableList = FXCollections.observableArrayList();
        potionListView.getItems().addAll(potionObservableList);

        vBox.getChildren().addAll(label, potionListView);

        return vBox;
    }

    public Ingredient getSelectedIngredient() {
        return ingredientCombobox.getSelectionModel().getSelectedItem();
    }

    public void addIngredientToList(Ingredient ingredient) {
        ingredientListView.getSelectionModel().select(ingredient);
        ingredientListView.getItems().add(ingredient);
    }

    public void removeIngredientFromList(Ingredient ingredient) {
        ingredientListView.getSelectionModel().select(ingredient);
        ingredientListView.getItems().remove(ingredient);
    }

    public void populateIngredientsToList(ObservableList<Ingredient> list) {
        ingredientListView.getItems().addAll(list);
    }

    public void removeIngredientsFromList(ObservableList<Ingredient> list) {
        ingredientListView.getItems().removeAll(list);
    }

    public ComboBox<Ingredient> getIngredientCombobox() {
        return ingredientCombobox;
    }

    public void setIngredientCombobox(ComboBox<Ingredient> ingredientCombobox) {
        this.ingredientCombobox = ingredientCombobox;
    }

    public ListView<Ingredient> getIngredientListView() {
        return ingredientListView;
    }

    public void setIngredientListView(ListView<Ingredient> ingredientListView) {
        this.ingredientListView = ingredientListView;
    }

    public ObservableList<Ingredient> getIngredientObservableList() {
        return ingredientObservableList;
    }

    public void setIngredientObservableList(ObservableList<Ingredient> ingredientObservableList) {
        this.ingredientObservableList = ingredientObservableList;
    }

    public ListView<Potion> getPotionListView() {
        return potionListView;
    }

    public void setPotionListView(ListView<Potion> potionListView) {
        this.potionListView = potionListView;
    }

    public ObservableList<Potion> getPotionObservableList() {
        return potionObservableList;
    }

    public void setPotionObservableList(ObservableList<Potion> potionObservableList) {
        this.potionObservableList = potionObservableList;
    }

    public Button getAddAllIngredientsBtn() {
        return addAllIngredientsBtn;
    }

    public void setAddAllIngredientsBtn(Button addAllIngredientsBtn) {
        this.addAllIngredientsBtn = addAllIngredientsBtn;
    }

    public Button getClearIngredientsBtn() {
        return clearIngredientsBtn;
    }

    public void setClearIngredientsBtn(Button clearIngredientsBtn) {
        this.clearIngredientsBtn = clearIngredientsBtn;
    }

    //event handler methods
    public void addAddAllIngredientsHandler(EventHandler<ActionEvent> addIngredientsHandler) {
        addAllIngredientsBtn.setOnAction(addIngredientsHandler);
    }

    public void removeAllIngredientsHandler(EventHandler<ActionEvent> clearIngredientsHandler) {
        clearIngredientsBtn.setOnAction(clearIngredientsHandler);
    }

    public void addIngredientHandler(EventHandler<ActionEvent> addIngredientHandler) {
        ingredientCombobox.setOnAction(addIngredientHandler);
    }

    public void removeIngredientHandler(EventHandler<MouseEvent> clearIngredientHandler) {
        ingredientListView.setOnMouseClicked(clearIngredientHandler);
    }

}

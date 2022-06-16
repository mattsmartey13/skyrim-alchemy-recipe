package view;

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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import model.Ingredient;
import model.Potion;

public class PotionGenerationPane extends GridPane {
    private ComboBox<Ingredient> findIngredientBox;
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

        findIngredientBox = new ComboBox<>();
        ingredientListView = new ListView<>();
        ingredientObservableList = FXCollections.observableArrayList();
        ingredientListView.getItems().addAll(ingredientObservableList);
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

    public VBox getIngredientListVBox() {
        VBox vBox = new VBox(10);

        HBox buttonBox = getIngredientButtonHBox();

        vBox.getChildren().addAll(buttonBox, findIngredientBox, ingredientListView);

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

    public ComboBox<Ingredient> getFindIngredientBox() {
        return findIngredientBox;
    }

    public void setFindIngredientBox(ComboBox<Ingredient> findIngredientBox) {
        this.findIngredientBox = findIngredientBox;
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

    public void clearIngredientsHandler(EventHandler<ActionEvent> clearIngredientsHandler) {
        clearIngredientsBtn.setOnAction(clearIngredientsHandler);
    }

}

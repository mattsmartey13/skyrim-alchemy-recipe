package view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import model.Ingredient;
import model.Potion;

public class ViewRecipePane extends GridPane {

    //declare listviews and observable lists
    private ListView<Ingredient> listviewIngredients;
    private ListView<Potion> listViewPotions;

    private ObservableList<Ingredient> obsListIngredients;
    private ObservableList<Potion> obsListPotions;

    //remove ingredient once run out
    private Button removeIngredientBtn;

    //constructor
    public ViewRecipePane() {

        //styling
        this.setVgap(10);
        this.setHgap(10);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(15, 12, 15, 12));

    }

    public ListView<Ingredient> getListviewIngredients() {
        return listviewIngredients;
    }

    public void setListviewIngredients(ListView<Ingredient> listviewIngredients) {
        this.listviewIngredients = listviewIngredients;
    }

    public ListView<Potion> getListViewPotions() {
        return listViewPotions;
    }

    public void setListViewPotions(ListView<Potion> listViewPotions) {
        this.listViewPotions = listViewPotions;
    }

    public ObservableList<Ingredient> getObsListIngredients() {
        return obsListIngredients;
    }

    public void setObsListIngredients(ObservableList<Ingredient> obsListIngredients) {
        this.obsListIngredients = obsListIngredients;
    }

    public ObservableList<Potion> getObsListPotions() {
        return obsListPotions;
    }

    public void setObsListPotions(ObservableList<Potion> obsListPotions) {
        this.obsListPotions = obsListPotions;
    }

    public Button getRemoveIngredient() {
        return removeIngredientBtn;
    }

    public void setRemoveIngredient(Button removeIngredient) {
        this.removeIngredientBtn = removeIngredient;
    }

}

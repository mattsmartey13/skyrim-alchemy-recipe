package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Item;
import model.Perk;

public class PlayerDetailsPane extends GridPane {

    //enter levels and pick perks
    private TextField playerLevelTxt, alchemyLevelTxt;
    private ComboBox<Perk> perkComboBox;
    private ComboBox<Perk> playerPerkComboBox;
//    private ComboBox<Enchantment> encComboBox;
    private ComboBox<Item> itemComboBox;

    private Button addEnchantmentBtn;
    private Button removeEnchantmentBtn;
    private Button submitBtn;
    private Button clearBtn;

    //contructor
    public PlayerDetailsPane() {
        //styling
        this.setVgap(10);
        this.setHgap(10);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(15, 12, 15, 12));
    }

    public String getPlayerLevelTxt() {
        return playerLevelTxt.getText();
    }

    public void setPlayerLevelTxt(TextField playerLevelTxt) {
        this.playerLevelTxt = playerLevelTxt;
    }

    public String getAlchemyLevelTxt() {
        return alchemyLevelTxt.getText();
    }

    public void setAlchemyLevelTxt(TextField alchemyLevelTxt) {
        this.alchemyLevelTxt = alchemyLevelTxt;
    }

    public ComboBox<Perk> getPerkComboBox() {
        return perkComboBox;
    }

    public void setPerkComboBox(ComboBox<Perk> perkComboBox) {
        this.perkComboBox = perkComboBox;
    }

    public ComboBox<Perk> getPlayerPerkComboBox() {
        return playerPerkComboBox;
    }

    public void setPlayerPerkComboBox(ComboBox<Perk> playerPerkComboBox) {
        this.playerPerkComboBox = playerPerkComboBox;
    }

//    public ComboBox<Enchantment> getEncComboBox() {
//        return encComboBox;
//    }
//
//    public void setEncComboBox(ComboBox<Enchantment> encComboBox) {
//        this.encComboBox = encComboBox;
//    }

    public Button getSubmitBtn() {
        return submitBtn;
    }

    public void setSubmitBtn(Button submitBtn) {
        this.submitBtn = submitBtn;
    }

    public Button getClearBtn() {
        return clearBtn;
    }

    public void setClearBtn(Button clearBtn) {
        this.clearBtn = clearBtn;
    }

    //event handler methods
    public void addEnchantmentEventHandler(EventHandler<ActionEvent> addHandler) {
        addEnchantmentBtn.setOnAction(addHandler);
    }

    public void addRemoveEnchantmentEventHandler(EventHandler<ActionEvent> removeHandler) {
        removeEnchantmentBtn.setOnAction(removeHandler);
    }

    public void addSubmitEventHandler(EventHandler<ActionEvent> submitHandler) {
        submitBtn.setOnAction(submitHandler);
    }

    public void addClearEventHandler(EventHandler<ActionEvent> clearHandler) {
        clearBtn.setOnAction(clearHandler);
    }

}

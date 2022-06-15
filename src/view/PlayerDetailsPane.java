package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerDetailsPane extends HBox {

    //enter levels and pick perks
    private Spinner<Integer> alchemyLevelTxt, headgearTxt, glovesTxt, ringTxt, footwearTxt;
    private RadioButton alchemist1Radio, alchemist2Radio, alchemist3Radio, alchemist4Radio, alchemist5Radio;
    private ToggleGroup toggleGroup = new ToggleGroup();
    private CheckBox physicianCheck, benefactorCheck, poisonerCheck, purityCheck,
            seekerCheck, headgearCheck, glovesCheck, ringCheck, footwearCheck;
    private Button clearBtn, submitBtn;

    //contructor
    public PlayerDetailsPane() {
        //styling
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10, 10, 10, 10));

        Label playerLevelLabel = new Label("Player alchemy level: ");
        alchemyLevelTxt = new Spinner<>(1, 100, 15);

        Label playerItemLabel = new Label("Player items with enchantments: ");

        headgearCheck = new CheckBox("Headgear: ");
        glovesCheck = new CheckBox("Gloves: ");
        ringCheck = new CheckBox("Ring: ");
        footwearCheck = new CheckBox("Footwear: ");
        clearBtn = new Button("Clear");
        submitBtn = new Button("Submit");

        VBox headgearVBox = getHeadgearVBox();
        headgearVBox.managedProperty().bind(headgearCheck.selectedProperty());
        headgearVBox.visibleProperty().bind(headgearCheck.selectedProperty());

        VBox glovesVBox = getGlovesVBox();
        glovesVBox.managedProperty().bind(glovesCheck.selectedProperty());
        glovesVBox.visibleProperty().bind(glovesCheck.selectedProperty());

        VBox ringVBox = getRingVBox();
        ringVBox.managedProperty().bind(ringCheck.selectedProperty());
        ringVBox.visibleProperty().bind(ringCheck.selectedProperty());

        VBox footwearVBox = getFootwearVBox();
        footwearVBox.managedProperty().bind(footwearCheck.selectedProperty());
        footwearVBox.visibleProperty().bind(footwearCheck.selectedProperty());

        VBox detailsVBox = new VBox(10);
        detailsVBox.setAlignment(Pos.CENTER_LEFT);
        detailsVBox.getChildren().addAll(playerLevelLabel, alchemyLevelTxt, playerItemLabel, headgearCheck,
                headgearVBox, glovesCheck, glovesVBox, ringCheck, ringVBox, footwearCheck, footwearVBox);

        VBox perkVBox = getPerkVBox();
        perkVBox.setSpacing(10);
        perkVBox.setAlignment(Pos.CENTER_LEFT);

        VBox buttonVBox = new VBox(10);
        buttonVBox.getChildren().addAll(clearBtn, submitBtn);
        buttonVBox.setAlignment(Pos.CENTER_LEFT);

        this.getChildren().addAll(detailsVBox, perkVBox, buttonVBox);
    }

    public VBox getHeadgearVBox() {
        VBox vBox = new VBox();

        Label playerHeadgearLabel = new Label("Headgear % boost: ");
        headgearTxt = new Spinner<>(1, 1000000, 1);

        vBox.getChildren().addAll(playerHeadgearLabel, headgearTxt);
        return vBox;
    }

    public VBox getGlovesVBox() {
        VBox vBox = new VBox();

        Label playerGlovesLabel = new Label("Gloves % boost: ");
        glovesTxt = new Spinner<>(1, 1000000, 1);

        vBox.getChildren().addAll(playerGlovesLabel, glovesTxt);
        return vBox;
    }

    public VBox getFootwearVBox() {
        VBox vBox = new VBox(10);

        Label playerFootwearLabel = new Label("Footwear % boost: ");
        footwearTxt = new Spinner<>(1, 1000000, 1);

        vBox.getChildren().addAll(playerFootwearLabel, footwearTxt);
        return vBox;
    }

    public VBox getRingVBox() {
        VBox vBox = new VBox();

        Label playerRingLabel = new Label("Ring % boost: ");
        ringTxt = new Spinner<>(1, 1000000, 1);

        vBox.getChildren().addAll(playerRingLabel, ringTxt);
        return vBox;
    }

    public VBox getPerkVBox() {
        VBox vBox = new VBox();

        Label playerPerkLabel = new Label("Player perks: ");
        alchemist1Radio = new RadioButton("Alchemist 1");
        alchemist1Radio.setSelected(false);
        alchemist2Radio = new RadioButton("Alchemist 2");
        alchemist2Radio.setSelected(false);
        alchemist3Radio = new RadioButton("Alchemist 3");
        alchemist3Radio.setSelected(false);
        alchemist4Radio = new RadioButton("Alchemist 4");
        alchemist4Radio.setSelected(false);
        alchemist5Radio = new RadioButton("Alchemist 5");
        alchemist5Radio.setSelected(false);

        alchemist1Radio.setToggleGroup(toggleGroup);
        alchemist2Radio.setToggleGroup(toggleGroup);
        alchemist3Radio.setToggleGroup(toggleGroup);
        alchemist4Radio.setToggleGroup(toggleGroup);
        alchemist5Radio.setToggleGroup(toggleGroup);

        physicianCheck = new CheckBox("Physician");
        benefactorCheck = new CheckBox("Benefactor");
        poisonerCheck = new CheckBox("Poisoner");
        purityCheck = new CheckBox("Purity");
        seekerCheck = new CheckBox("Seeker of Shadows");

        vBox.getChildren().addAll(playerPerkLabel, alchemist1Radio, alchemist2Radio, alchemist3Radio, alchemist4Radio,
                alchemist5Radio, physicianCheck, benefactorCheck, poisonerCheck, purityCheck, seekerCheck);

        return vBox;
    }

    public Spinner<Integer> getAlchemyLevelTxt() {
        return alchemyLevelTxt;
    }

    public void setAlchemyLevelTxt(Spinner<Integer> alchemyLevelTxt) {
        this.alchemyLevelTxt = alchemyLevelTxt;
    }

    public Spinner<Integer> getHeadgearTxt() {
        return headgearTxt;
    }

    public void setHeadgearTxt(Spinner<Integer> headgearTxt) {
        this.headgearTxt = headgearTxt;
    }

    public Spinner<Integer> getGlovesTxt() {
        return glovesTxt;
    }

    public void setGlovesTxt(Spinner<Integer> glovesTxt) {
        this.glovesTxt = glovesTxt;
    }

    public Spinner<Integer> getRingTxt() {
        return ringTxt;
    }

    public void setRingTxt(Spinner<Integer> ringTxt) {
        this.ringTxt = ringTxt;
    }

    public Spinner<Integer> getFootwearTxt() {
        return footwearTxt;
    }

    public void setFootwearTxt(Spinner<Integer> footwearTxt) {
        this.footwearTxt = footwearTxt;
    }

    public RadioButton getAlchemist1Check() {
        return alchemist1Radio;
    }

    public void setAlchemist1Radio(RadioButton alchemist1Radio) {
        this.alchemist1Radio = alchemist1Radio;
    }

    public RadioButton getAlchemist2Radio() {
        return alchemist2Radio;
    }

    public void setAlchemist2Radio(RadioButton alchemist2Radio) {
        this.alchemist2Radio = alchemist2Radio;
    }

    public RadioButton getAlchemist3Radio() {
        return alchemist3Radio;
    }

    public void setAlchemist3Radio(RadioButton alchemist3Radio) {
        this.alchemist3Radio = alchemist3Radio;
    }

    public RadioButton getAlchemist4Radio() {
        return alchemist4Radio;
    }

    public void setAlchemist4Radio(RadioButton alchemist4Radio) {
        this.alchemist4Radio = alchemist4Radio;
    }

    public RadioButton getAlchemist5Radio() {
        return alchemist5Radio;
    }

    public void setAlchemist5Radio(RadioButton alchemist5Radio) {
        this.alchemist5Radio = alchemist5Radio;
    }

    public RadioButton getAlchemist1Radio() {
        return alchemist1Radio;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public void setToggleGroup(ToggleGroup toggleGroup) {
        this.toggleGroup = toggleGroup;
    }

    public CheckBox getPhysicianCheck() {
        return physicianCheck;
    }

    public void setPhysicianCheck(CheckBox physicianCheck) {
        this.physicianCheck = physicianCheck;
    }

    public CheckBox getBenefactorCheck() {
        return benefactorCheck;
    }

    public void setBenefactorCheck(CheckBox benefactorCheck) {
        this.benefactorCheck = benefactorCheck;
    }

    public CheckBox getPoisonerCheck() {
        return poisonerCheck;
    }

    public void setPoisonerCheck(CheckBox poisonerCheck) {
        this.poisonerCheck = poisonerCheck;
    }

    public CheckBox getPurityCheck() {
        return purityCheck;
    }

    public void setPurityCheck(CheckBox purityCheck) {
        this.purityCheck = purityCheck;
    }

    public CheckBox getSeekerCheck() {
        return seekerCheck;
    }

    public void setSeekerCheck(CheckBox seekerCheck) {
        this.seekerCheck = seekerCheck;
    }

    public CheckBox getHeadGearCheck() {
        return headgearCheck;
    }

    public void setHeadGearCheck(CheckBox headgearCheck) {
        this.headgearCheck = headgearCheck;
    }

    public CheckBox getGlovesCheck() {
        return glovesCheck;
    }

    public void setGlovesCheck(CheckBox glovesCheck) {
        this.glovesCheck = glovesCheck;
    }

    public CheckBox getRingCheck() {
        return ringCheck;
    }

    public void setRingCheck(CheckBox ringCheck) {
        this.ringCheck = ringCheck;
    }

    public CheckBox getFootwearCheck() {
        return footwearCheck;
    }

    public void setFootwearCheck(CheckBox footwearCheck) {
        this.footwearCheck = footwearCheck;
    }

    public Button getClearBtn() {
        return clearBtn;
    }

    public void setClearBtn(Button clearBtn) {
        this.clearBtn = clearBtn;
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }

    public void setSubmitBtn(Button submitBtn) {
        this.submitBtn = submitBtn;
    }

    //event handler methods
    public void addClearEventHandler(EventHandler<ActionEvent> clearHandler) {
        clearBtn.setOnAction(clearHandler);
    }

    public void addSubmitEventHandler(EventHandler<ActionEvent> submitHandler) {
        submitBtn.setOnAction(submitHandler);
    }

}

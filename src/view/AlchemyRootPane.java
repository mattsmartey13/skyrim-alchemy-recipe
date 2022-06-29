package view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

public class AlchemyRootPane extends BorderPane {

    private TabPane tp;
    private AlchemyMenuBar amb;
    private PlayerDetailsPane pdp;

    private PotionGenerationPane pgp;

    public AlchemyRootPane() {

        //initiate tabpane which oversees our functional panes
        tp = new TabPane();
        tp.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        //initiate our panes where the action happens
        pdp = new PlayerDetailsPane();
        pgp = new PotionGenerationPane();

        //add the two panes to the parent
        Tab t1 = new Tab("Enter player details", pdp);
        Tab t2 = new Tab("Enter ingredients and make potions", pgp);

        tp.getTabs().addAll(t1, t2);

        amb = new AlchemyMenuBar();

        this.setTop(amb);
        this.setCenter(tp);
    }

    public PlayerDetailsPane getPlayerDetailsPane() {
        return pdp;
    }

    public void setPlayerDetailsPane(PlayerDetailsPane pdp) {
        this.pdp = pdp;
    }

    public AlchemyMenuBar getAmb() {
        return amb;
    }

    public void setAmb(AlchemyMenuBar amb) {
        this.amb = amb;
    }

    public PotionGenerationPane getPotionGenerationPane() {
        return pgp;
    }

    public void setPotionGenerationPane(PotionGenerationPane pgp) {
        this.pgp = pgp;
    }

    public TabPane getTp() {
        return tp;
    }

    public void setTp(TabPane tp) {
        this.tp = tp;
    }

    public AlchemyMenuBar getAlchemyMenuBar() {
        return amb;
    }

    public void setAlchemyMenuBar(AlchemyMenuBar amb) {
        this.amb = amb;
    }

    public void changeTab(int index) {
        tp.getSelectionModel().select(index);
    }

}

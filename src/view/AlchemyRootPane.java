package view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

public class AlchemyRootPane extends BorderPane {

    private TabPane tp;
    private AlchemyMenuBar amb;
    private PlayerDetailsPane pdp;

    public AlchemyRootPane() {

        //initiate tabpane which oversees our functional panes
        TabPane tp = new TabPane();
        tp.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        //initiate our panes where the action happens
        pdp = new PlayerDetailsPane();

        //add the two panes to the parent
        Tab t1 = new Tab("Enter player details", pdp);;

        tp.getTabs().addAll(t1);

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

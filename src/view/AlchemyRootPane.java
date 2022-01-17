import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

public class AlchemyRootPane extends BorderPane {

    private TabPane tp;
    private PlayerDetailsPane pdp;
    private IngredientEnchantmentPane ipep;
    private ViewRecipePane vrp;
    private AlchemyMenuBar amb;

    public AlchemyRootPane() {

        //initiate tabpane which oversees our functional panes
        TabPane tp = new TabPane();
        tp.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        //initiate our two panes where the action happens
        pdp = new PlayerDetailsPane();
        ipep = new IngredientEnchantmentPane();
        vrp = new ViewRecipePane();

        //add the two panes to the parent
        Tab t1 = new Tab("Add player details and perks", pdp);
        Tab t2 = new Tab("Add ingredients and enchantments", ipep);
        Tab t3 = new Tab("View created recipes", vrp);

        tp.getTabs().addAll(t1, t2, t3);

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

    public IngredientEnchantmentPane getIngredientEnchantmentPane() {
        return ipep;
    }

    public void setIngredientEnchantmentPane(IngredientEnchantmentPane ipep) {
        this.ipep = ipep;
    }

    public ViewRecipePane getViewRecipePane() {
        return vrp;
    }

    public void setViewRecipePane(ViewRecipePane vrp) {
        this.vrp = vrp;
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

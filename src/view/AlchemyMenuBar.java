package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

public class AlchemyMenuBar extends MenuBar {

    private MenuItem aboutItem, exitItem, resetItem;

    public AlchemyMenuBar() {
        //basis for menu bar - add more as required
        Menu alchemyMenu;

        alchemyMenu = new Menu("_File");

        resetItem = new MenuItem("_Reset");
        resetItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+R"));
        alchemyMenu.getItems().add(resetItem);

        exitItem = new MenuItem("_Exit");
        resetItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+X"));
        alchemyMenu.getItems().add(exitItem);

        this.getMenus().add(alchemyMenu);

        alchemyMenu = new Menu("_Help");

        aboutItem = new MenuItem("_About");
        aboutItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+A"));
        alchemyMenu.getItems().add(aboutItem);

        this.getMenus().add(alchemyMenu);
    }

    //event handler methods
    public void addResetEventHandler(EventHandler<ActionEvent> resetHandler) {
        resetItem.setOnAction(resetHandler);
    }

    public void addAboutEventHandler(EventHandler<ActionEvent> aboutHandler) {
        aboutItem.setOnAction(aboutHandler);
    }

    public void addExitEventHandler(EventHandler<ActionEvent> exitHandler) {
        exitItem.setOnAction(exitHandler);
    }
}

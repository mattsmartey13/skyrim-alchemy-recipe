package application;

import model.Player;
import view.AlchemyRootPane;
import controller.AlchemyController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationLoader extends Application {

    private AlchemyRootPane view;

    @Override
    public void init() {
        view = new AlchemyRootPane();
        Player model = new Player();
        new AlchemyController(model);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //sets min width and height for the stage window
        stage.setMinWidth(640);
        stage.setMinHeight(770);

        stage.setTitle("Skyrim Alchemy Recipe Calculator");
        stage.setScene(new Scene(view));
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

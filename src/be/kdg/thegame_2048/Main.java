package be.kdg.thegame_2048;

import be.kdg.thegame_2048.models.PlayerManager;
import be.kdg.thegame_2048.views.Start.StartPresenter;
import be.kdg.thegame_2048.views.Start.StartView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Bryan de Ridder, Jarne Van Aerde
 * @version 1.0 04-02-17 18:38
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        //MAKING CLASSES
        PlayerManager model = new PlayerManager();
        StartView view = new StartView();
        StartPresenter presenter = new StartPresenter(model, view);

        //PUTTING TOGETHER THE SCENE
        Scene scene = new Scene(view);

        //add stylesheet:
        scene.getStylesheets().add("be/kdg/thegame_2048/css/stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(750 + 20);
        primaryStage.setMinWidth(550);
        primaryStage.setTitle("2048");
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
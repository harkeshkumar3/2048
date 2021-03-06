package be.kdg.thegame_2048.views.result;

import be.kdg.thegame_2048.models.Game;
import be.kdg.thegame_2048.models.PlayerManager;
import be.kdg.thegame_2048.views.game.GamePresenter;
import be.kdg.thegame_2048.views.game.GameView;
import be.kdg.thegame_2048.views.start.StartPresenter;
import be.kdg.thegame_2048.views.start.StartView;
import javafx.event.Event;

/**
 * Links the result view to the model classes.
 * All keys are blocked when this presenter is active.
 *
 * @author Bryan de Ridder, Jarne van Aerde
 * @version 1.0 17-02-17 21:57
 */
public class ResultPresenter {
    private final PlayerManager modelPlayerManager;
    private final ResultView view;
    private final GameView gameView;
    private Game modelGame;

    public ResultPresenter(PlayerManager model, ResultView view, Game modelGame, GameView gameView) {
        this.modelPlayerManager = model;
        this.view = view;
        this.modelGame = modelGame;
        this.gameView = gameView;
        this.addEventHandlers();
        this.updateView();
    }

    private void addEventHandlers() {
        view.getBtnContinue().setOnAction(event -> gameView.layoutNodes());
        view.getBtnExit().setOnAction(event -> {
            modelPlayerManager.setCurrentPlayerToNull();
            StartView startView = new StartView();
            new StartPresenter(modelPlayerManager, startView);
            view.getScene().setRoot(startView);
        });
        view.getBtnRestart().setOnAction(event -> {
            gameView.layoutNodes();
            gameView.getLblScoreInput().setText("0");
            modelGame = new Game();
            new GamePresenter(modelGame, modelPlayerManager, gameView);
        });

        //Keyboard is blocked when resultpresenter is active
        view.setOnKeyPressed(Event::consume);

    }

    private void updateView() {
        int score = modelGame.getScore();
        view.getLblFinalScore().setText(Integer.toString(score));

        if (modelGame.hasLost()) {
            view.getLblResult().setText("You lose!");
        } else if (modelGame.hasWon()) {
            view.getLblResult().setText("You win!");
            view.addContinueBtn();
        }
    }
}

package be.kdg.thegame_2048.views.game;

import be.kdg.thegame_2048.models.Game;
import be.kdg.thegame_2048.models.PlayerManager;
import be.kdg.thegame_2048.views.undo.UndoPresenter;
import be.kdg.thegame_2048.views.undo.UndoView;
import be.kdg.thegame_2048.views.highscores.HighScorePresenter;
import be.kdg.thegame_2048.views.highscores.HighScoreView;
import be.kdg.thegame_2048.views.result.ResultPresenter;
import be.kdg.thegame_2048.views.result.ResultView;
import be.kdg.thegame_2048.views.start.StartPresenter;
import be.kdg.thegame_2048.views.start.StartView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

/**
 * @author Jarne Van Aerde, Bryan de Ridder
 * @version 1.0 17/02/2017 9:28
 */
public class GamePresenter {
    private Game modelGame;
    private final PlayerManager modelPlayerManager;
    private final GameView view;
    private final GameBottomView bottomView;
    private final GameMiddleView midView;
    private final GameTopView topView;
    private final Animation animation;
    private boolean alreadyWon;
    private boolean firstRun;
    private int prevScore;
    private int currentScore;

    public GamePresenter(Game modelGame, PlayerManager modelPlayerManager, GameView view) {
        this.modelGame = modelGame;
        this.modelPlayerManager = modelPlayerManager;
        this.view = view;
        this.bottomView = view.getBottomView();
        this.midView = view.getMiddleView();
        this.topView = view.getTopView();
        this.animation = new Animation(topView, midView, this);
        this.firstRun = true;
        this.addEventHandlers();
        this.currentScore = modelGame.getScore();
        updateViewScore(currentScore);
        updateView();
    }

    private void addEventHandlers() {
        this.bottomView.getBtnRestart().setOnAction(event -> {
            this.alreadyWon = false;
            this.firstRun = true;
            if (!modelGame.isPlayingUndo()) modelPlayerManager.saveInfoCurrentPlayer();
            this.modelGame = new Game();
            this.topView.getLblCurrentScoreInput().setText("0");
            disableUndoButton(false);
            updateView();
        });

        this.bottomView.getBtnUndo().setOnAction(event -> {
            UndoView alert = new UndoView();
            new UndoPresenter(modelGame, alert, view, this);
            this.view.setView(alert);
        });

        this.bottomView.getBtnHighScores().setOnAction(event -> {
            if (!modelGame.isPlayingUndo()) modelPlayerManager.saveInfoCurrentPlayer();
            HighScoreView hsView = new HighScoreView();
            new HighScorePresenter(modelGame, modelPlayerManager, hsView);
            this.view.getScene().setRoot(hsView);
        });

        this.bottomView.getBtnExit().setOnAction(event -> {
            if (!modelGame.isPlayingUndo()) modelPlayerManager.saveInfoCurrentPlayer();
            modelPlayerManager.setCurrentPlayerToNull();
            StartView startView = new StartView();
            new StartPresenter(modelPlayerManager, startView);
            this.view.getScene().setRoot(startView);
        });

        this.view.setOnKeyPressed(event -> {
            if (animation.getParallelTransition().getStatus() != javafx.animation.Animation.Status.RUNNING) {
                final KeyCode direction = event.getCode();
                this.prevScore = modelGame.getScore();
                switch (direction) {
                    case DOWN:
                        updateViewBlocks(Game.Direction.DOWN);
                        animation.animateMovement(direction);
                        break;
                    case UP:
                        updateViewBlocks(Game.Direction.UP);
                        animation.animateMovement(direction);
                        break;
                    case RIGHT:
                        updateViewBlocks(Game.Direction.RIGHT);
                        animation.animateMovement(direction);
                        break;
                    case LEFT:
                        updateViewBlocks(Game.Direction.LEFT);
                        animation.animateMovement(direction);
                        break;
                    default:
                        event.consume();
                }
                this.currentScore = modelGame.getScore();
                updateViewScore(currentScore);
                if (currentScore - prevScore > 0) {
                    this.animation.animateScore(currentScore - prevScore);
                }
            }
        });
        this.animation.getParallelTransition().setOnFinished(event -> {
            this.animation.resetMoveAnimation();
            updateView();
        });
    }

    /**
     * Transfers all calculated model output to the GameView.
     * Also decides which block should get a popIn animation.
     **/
    public void updateView() {
        int randomblockX = modelGame.getCoordRandomBlockX();
        int randomblockY = modelGame.getCoordRandomBlockY();
        updateRandomBlockView(randomblockX, randomblockY);

        for (int i = 0; i < GameMiddleView.GRID_SIZE; i++) {
            for (int j = 0; j < GameMiddleView.GRID_SIZE; j++) {
                int value = getModelBlockValue(i, j);
                if (firstRun) {
                    midView.changeBlockValue(value, i, j);
                    animation.popIn(i, j);
                } else if (!(i == randomblockX && j == randomblockY)) {
                    midView.changeBlockValue(value, i, j);
                }
            }
        }
        this.firstRun = false;
    }

    private void updateRandomBlockView(int x, int y) {
        if (!firstRun && isMovable()) {
            animation.popIn(y, x);
            midView.changeBlockValue(2, x, y);
        }
    }

    private int getModelBlockValue(int x, int y) {
        if (modelGame.getPiece(x, y) == null) return 0;
        return modelGame.getPieceValue(x, y);
    }

    /**
     * After a move key is pressed, first animates blocks into a certain direction.
     * Then transfers its direction to the model classes.
     * @param direction should contain the direction for the game model class.
     **/
    private void updateViewBlocks(Game.Direction direction) {
        modelGame.runGameCycle(direction);
        checkIfLostOrWin();
    }

    /**
     * Scores on the top of the view are updated.
     * @param score can contain the current score or any other score.
     *
     * als score hoger is dan topscore -> topscore = score
     * score = altijd inputscore
     *
     **/
    public void updateViewScore(int score) {
        this.modelPlayerManager.setCurrentPlayerScore(score);
        int bestScore = modelPlayerManager.getCurrentPlayer().getBestScore();
        this.topView.getLblBestScoreInput().setText(String.valueOf(bestScore));
        if (score >= bestScore) {
            this.topView.getLblBestScoreInput().setText(String.valueOf(score));
        }
        this.topView.getLblCurrentScoreInput().setText(String.valueOf(score));
    }

    /**
     * Model classes check if the player has won or lost.
     * If so, the resultView must be shown.
     **/
    private void checkIfLostOrWin() {
        if (!modelGame.hasLost() && alreadyWon) return;
        if (modelGame.hasLost() || modelGame.hasWon()) {
            alreadyWon = true;
            if (!modelGame.isPlayingUndo()) modelPlayerManager.saveInfoCurrentPlayer();
            ResultView resultView = new ResultView();
            new ResultPresenter(modelPlayerManager, resultView, modelGame, view);
            view.setView(resultView);
        }
    }

    /**
     * @return true if there are any moves left.
     **/
    boolean isMovable() {
        if (modelGame.getLastMove() != null)
            if (!modelGame.getLastMove().equals(modelGame.getCurrentMove())) {
                return true;
            }
        return false;
    }

    public int getPrevScore() {
        return prevScore;
    }
    public void disableUndoButton(boolean bool) {
        bottomView.getBtnUndo().setDisable(bool);
    }
}
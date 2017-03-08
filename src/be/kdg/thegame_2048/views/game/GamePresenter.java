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

/**
 * @author Jarne Van Aerde
 * @version 1.0 17/02/2017 9:28
 */
public class GamePresenter {
    //ATTRIBUTES
    private Game modelGame;
    private final PlayerManager modelPlayerMananger;
    private final GameView view;
    private final GameBottomView bottomView;
    private final GameMiddleView midView;
    private final GameTopView topView;
    private final AnimationView animationView;
    private boolean alreadyWon;
    private boolean firstRun;

    //CONSTRUCTORS
    public GamePresenter(Game modelGame, PlayerManager modelPlayerManager, GameView view) {
        this.modelGame = modelGame;
        this.modelPlayerMananger = modelPlayerManager;
        this.modelPlayerMananger.setCurrentPlayerScore(modelGame.getScore().getScore());
        this.view = view;
        this.bottomView = view.getBottomView();
        this.midView = view.getMiddleView();
        this.topView = view.getTopView();
        this.animationView = new AnimationView(topView, midView, this);
        this.firstRun = true;
        this.addEventHandlers();
        topView.getLblBestScoreInput().setText(String.valueOf(modelPlayerManager.getCurrentPlayer().getBestScore()));
        //eenmalige updateview
        updateView();
    }

    //METHODEN
    private void addEventHandlers() {
        bottomView.getBtnRestart().setOnAction(event -> {
            alreadyWon = false;
            firstRun = true;
            modelPlayerMananger.saveInfoCurrentPlayer();
            modelGame = new Game(modelPlayerMananger);
            topView.getLblScoreInput().setText("0");
            updateView();
        });
        bottomView.getBtnUndo().setOnAction(event -> {
            UndoView alert = new UndoView();
            alert.getLblMessage().setText("Are you sure you want to undo \nyour last move? " +
                    "Your score will no longer \nbe added to the highscores.");
            new UndoPresenter(modelGame, alert, view);
            view.setView(alert);
        });
        bottomView.getBtnHighScores().setOnAction(event -> {
            modelPlayerMananger.saveInfoCurrentPlayer();
            HighScoreView hsView = new HighScoreView();
            new HighScorePresenter(modelGame, modelPlayerMananger, hsView);
            view.getScene().setRoot(hsView);
        });
        bottomView.getBtnExit().setOnAction(event -> {
            modelPlayerMananger.saveInfoCurrentPlayer();
            modelPlayerMananger.setCurrentPlayerToNull();
            StartView startView = new StartView();
            new StartPresenter(modelPlayerMananger, startView);
            view.getScene().setRoot(startView);
        });
        view.setOnKeyPressed(event -> {
            final int prevScore = modelGame.getScore().getScore();
            try {
                switch (event.getCode()) {
                    case DOWN:updateViewBlocks(Game.Direction.DOWN);
                        animationView.moveDown();break;
                    case UP:updateViewBlocks(Game.Direction.TOP);
                        animationView.moveUp();break;
                    case RIGHT:updateViewBlocks(Game.Direction.RIGHT);
                        animationView.moveRight();break;
                    case LEFT:updateViewBlocks(Game.Direction.LEFT);
                        animationView.moveLeft();break;
                    default:event.consume();
                }
            } catch (IllegalArgumentException e) {
                //TODO: IMPLEMENT PROPER ERROR HANDLING.
            }
            final int currScore = modelGame.getScore().getScore();
            if (currScore-prevScore > 0) {
                animationView.animateScore(currScore-prevScore);
            }

            animationView.getParallelTransition().play();
            modelPlayerMananger.setCurrentPlayerScore(currScore);
        });
        animationView.getParallelTransition().setOnFinished(event ->  {
                animationView.resetAnimation();
                updateView();
        });
    }

    private void updateView() {
        int randomblockX = modelGame.getCoordRandomBlockX();
        int randomblockY = modelGame.getCoordRandomBlockY();

        if (!firstRun && !isMovable()) {
            animationView.popIn(randomblockY, randomblockX);
            midView.putBlockOnGrid(2, randomblockX, randomblockY);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int value;
                if (modelGame.getPiece(i, j) == null) {
                    value = 0;
                } else {
                    value = modelGame.getPieceValue(i, j);
                }
                if (firstRun) {
                    midView.putBlockOnGrid(value, i, j);
                    animationView.popIn(i, j);
                } else if (!(i == randomblockX && j == randomblockY)) {
                    midView.putBlockOnGrid(value, i, j);
                }
            }
        }
        this.firstRun = false;
    }

    private void updateViewBlocks(Game.Direction direction) {
        modelGame.runGameCycle(direction);
        int score = modelGame.getScore().getScore();
        if (modelPlayerMananger.getCurrentPlayer().getBestScore() <= score) {
            topView.getLblBestScoreInput().setText(String.valueOf(score));
        }
        topView.getLblScoreInput().setText(String.valueOf(score));
        checkIfLostOrWin();
    }

    private void checkIfLostOrWin() {
        if (alreadyWon) return;
        if (modelGame.hasLost() || modelGame.hasWon()) {
            alreadyWon = true;
            modelPlayerMananger.saveInfoCurrentPlayer();
            ResultView resultView = new ResultView();
            new ResultPresenter(modelPlayerMananger, resultView, modelGame, view);
            view.setView(resultView);
        }
    }

    boolean isMovable() {
        return modelGame.getLastMove() == null ||
                modelGame.getLastMove().equals(modelGame.getCurrentMove());
    }
}
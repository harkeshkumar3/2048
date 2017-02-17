package be.kdg.thegame_2048.models;

/**
 * @author Jarne Van Aerde
 * @version 1.0 12/02/2017 19:40
 */
public final class Game {
    //ATTRIBUTES
    public enum Direction {
        TOP, DOWN, LEFT, RIGHT
    }
    private Player playerNowPlaying;
    private Score score;
    private PlayerManager manager;
    private Playground playground;

    //CONSTRUCTORS
    public Game(PlayerManager playerManager) {
        this.score = new Score();
        this.playground = new Playground(this.score);
        this.manager = playerManager;
    }

    //METHODS
    public void runGameCycle(Direction direction) {
        switch (direction) {
            case TOP:
                playground.moveBlocksTop(); break;
            case DOWN:
                playground.moveBlocksBottom(); break;
            case LEFT:
                playground.moveBlocksLeft(); break;
            case RIGHT:
                playground.moveBlocksRight();
        }
    }

    public boolean hasWon() {
        Section[][] sections = playground.getSections();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (sections[i][j].getBlock().getValue() == 2048) return true;
            }
        }
        return false;
    }

    public boolean hasLost() {
        int amountOfBlocks = 0;
        Section[][] sections = playground.getSections();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (sections[i][j].hasBlock()) amountOfBlocks++;
            }
        }
        return amountOfBlocks == 16;
    }
}

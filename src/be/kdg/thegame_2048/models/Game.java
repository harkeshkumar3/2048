package be.kdg.thegame_2048.models;

/**
 * @author Bryan de Ridder, Jarne van Aerde
 * @version 1.0 12/02/2017 19:40
 */
public final class Game {
    //ATTRIBUTES
    public enum Direction {
        TOP, DOWN, LEFT, RIGHT
    }
    //private Player playerNowPlaying;
    private Score score;
    private PlayerManager manager;
    private Playground playground;

    //CONSTRUCTORS
    public Game(PlayerManager playerManager) {
        this.score = new Score();
        this.playground = new Playground(this.score);
        this.manager = playerManager;
        this.playground.addRandomBlock();
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
        playground.addRandomBlock();
        System.out.println(score.getScore() + "\n" + playground.toString());
    }

    public boolean hasWon() {
        Section[][] sections = playground.getSections();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (sections[i][j].hasBlock() && sections[i][j].getBlock().getValue() == 2048)
                    return true;
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
        if (amountOfBlocks < 16) return false;

        //HORIZONTAL CONTROL
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j != 3) {
                    if (sections[i][j].getBlock().getValue() == sections[i][j+1].getBlock().getValue()) return false;
                }
            }
        }

        //VERTICAL CONTROL
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j != 3) {
                    if (sections[j][i].getBlock().getValue() == sections[j+1][i].getBlock().getValue()) return false;
                }
            }
        }

        return true;
    }

    public Score getScore() {
        return score;
    }

    public int getPieceValue(int x, int y) {
        return playground.getSections()[x][y].getBlock().getValue();
    }
    public Block getPiece(int x, int y) {
        return playground.getSections()[x][y].getBlock();
    }
}

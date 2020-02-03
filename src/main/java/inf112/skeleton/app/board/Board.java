package inf112.skeleton.app.board;

import java.util.Arrays;

public class Board implements IBoard {
    private int height;
    private int width;
    private Tile[][] board;

    Board(int height, int width) {
        if (height < 1 || width < 1) throw new IllegalArgumentException("Height and width should be positive integers");
        this.height = height;
        this.width = width;
        board = new Tile[height][width];
        for (Tile[] row : board) {
            Arrays.fill(row, new Tile()); // todo: what kind of tile?
        }
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public Tile getTile(IPos p) {
        return getTile(p.getX(), p.getY());
    }

    @Override
    public Tile getTile(int x, int y) {
        if (isValidPos(x, y)) return board[x][y];
        return null;
    }

    @Override
    public boolean isValidPos(IPos p) {
        return isValidPos(p.getX(), p.getY());
    }

    @Override
    public boolean isValidPos(int x, int y) {
        return x >= 0 && x < height && y >= 0 && y < width;
    }

}

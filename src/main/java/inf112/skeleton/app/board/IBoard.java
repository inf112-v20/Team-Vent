package inf112.skeleton.app.board;

public interface IBoard {
    int getHeight();

    int getWidth();

    Tile getTile(IPos p);

    Tile getTile(int x, int y);

    boolean isValidPos(IPos p);

    boolean isValidPos(int x, int y);
}

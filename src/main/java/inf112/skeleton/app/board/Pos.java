package inf112.skeleton.app.board;

public class Pos implements IPos {
    private final int x;
    private final int y;

    Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public IPos north() {
        return new Pos(x - 1, y);
    }

    @Override
    public IPos south() {
        return new Pos(x + 1, y);
    }

    @Override
    public IPos east() {
        return new Pos(x, y + 1);
    }

    @Override
    public IPos west() {
        return new Pos(x, y - 1);
    }
}

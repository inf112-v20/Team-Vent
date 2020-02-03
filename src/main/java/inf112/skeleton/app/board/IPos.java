package inf112.skeleton.app.board;

public interface IPos {
    int getX();

    int getY();

    IPos north();

    IPos south();

    IPos east();

    IPos west();
}

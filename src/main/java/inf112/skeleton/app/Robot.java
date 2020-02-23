package inf112.skeleton.app;

import inf112.skeleton.app.board.Location;
import inf112.skeleton.app.cards.IProgramCard;

public class Robot {
    private Location location;

    public Robot(Location location) {
        this.location = location;
    }

    public Robot() {
        this(new Location());
    }

    public void execute(IProgramCard card) {
        this.location = card.instruction(this.location);
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getX() {
        return (int) getLocation().getPosition().getVector().x;
    }

    public int getY() {
        return (int) getLocation().getPosition().getVector().y;
    }
}

package inf112.skeleton.app;

import inf112.skeleton.app.board.Location;
import inf112.skeleton.app.cards.IProgramCard;

public class Robot {
    private Location location;
    private final int MAX_DAMAGE = 3;
    private int damage;

    public Robot(Location location) {
        this.location = location;
        this.damage = 0;
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

    public void takeDamage() {
        damage = Math.min(damage + 1, MAX_DAMAGE);
    }

    public boolean alive() {
        return damage < MAX_DAMAGE;
    }

    public String status() {
        return String.format("DAMAGE: %d/%d", this.damage, this.MAX_DAMAGE);
    }
}

package inf112.skeleton.app.model;

import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.cards.IProgramCard;


public class Robot {
    private final int MAX_DAMAGE = 1;
    private Location location;
    private int damage;
    private boolean dead;

    public Robot(Location location) {
        this.location = location;
        this.damage = 0;
        this.dead = false;
    }

    public Robot() {
        this(new Location());
    }

    public void execute(IProgramCard card) {
        this.location = card.instruction(this.location);
    }

    public Location getLocation() {
        return this.location.copy();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Direction getDirection() {
        return getLocation().getDirection();
    }

    public int getX() {
        return getLocation().getPosition().getX();
    }

    public int getY() {
        return getLocation().getPosition().getY();
    }

    public void updateState (StateInfo state) {
        location = state.location.copy();
        damage = state.damage;
        dead = state.dead;
    }

    public StateInfo getState() {
        return  new StateInfo(this, location, damage, dead);
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

    public void die() {
        this.damage = MAX_DAMAGE;
    }

    public void moveInDirection(Direction moveDirection) {
        this.location = this.location.moveDirection(moveDirection);
    }
}

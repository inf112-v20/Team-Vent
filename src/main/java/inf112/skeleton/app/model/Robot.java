package inf112.skeleton.app.model;

import com.badlogic.gdx.maps.MapObject;
import inf112.skeleton.app.model.board.Direction;
import inf112.skeleton.app.model.board.Location;
import inf112.skeleton.app.model.board.RVector2;
import inf112.skeleton.app.model.cards.IProgramCard;

public class Robot extends MapObject {
    private final int MAX_DAMAGE = 1;
    private Location location;
    private int damage;

    public Robot(String name, Location location) {
        this.damage = 0;
        this.location = location;
        this.setName(name);
    }

    /**
     * Initialize a robot with no identifier/name. The name helps find the robot in the list of MapObjects.
     */
    public Robot(Location location) {
        this.damage = 0;
        this.location = location;
    }

    /**
     * Initialize with standard values.
     */
    public Robot() {
        this.damage = 0;
        this.location = new Location(new RVector2(0, 0), Direction.EAST);
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

    public Direction getDirection() {
        return getLocation().getDirection();
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

    public void die() {
        this.damage = MAX_DAMAGE;
    }
}

package inf112.skeleton.app.model;
import inf112.skeleton.app.model.board.Location;

public class StateInfo {
    Location location;
    int damage;
    boolean dead;
    Robot robot;

    public StateInfo(Robot robot, Location location, int damage, boolean dead) {
        this.robot = robot;
        this.location = location;
        this.damage = damage;
        this.dead = dead;
    }

    public StateInfo updateLocation(Location loc) {
        location = loc.copy();
        return new StateInfo(robot, location, damage, dead);
    }

    public StateInfo updateDamage(int dmg) {
        this.damage = dmg;
        return new StateInfo(robot, location, damage, dead);
    }

    public StateInfo updateLifeStates(boolean dead) {
        this.dead = dead;
        return new StateInfo(robot, location, damage, dead);
    }

}

package inf112.skeleton.app.model;
import inf112.skeleton.app.model.board.Location;

public class StateInfo {
    public Location location;
    public int damage;
    public boolean dead;
    public Robot robot;

    public StateInfo(Robot robot, Location location, int damage, boolean dead) {
        this.robot = robot;
        this.location = location;
        this.damage = damage;
        this.dead = dead;
    }

    public StateInfo updateLocation(Location loc) {
        return new StateInfo(robot, loc.copy(), damage, dead);
    }


    public StateInfo updateDamage(int dmg) {

        return new StateInfo(robot, location.copy(), dmg, dead);
    }

    public StateInfo updateLifeStates(boolean dead) {
        return new StateInfo(robot, location.copy(), damage, dead);
    }

    public StateInfo copy(){return new StateInfo(robot.copy(),location.copy(),this.damage,this.dead);}
}

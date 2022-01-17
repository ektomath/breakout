package breakout.model;

/*
        A wall for the ball to bounce
 */
public class Wall extends AbstractPositionable implements IPositionable {

    Dir dir;

    public Wall(double x, double y, double width, double height, double dx, double dy, Dir dir) {
        super(x, y, width, height, dx, dy);
        this.dir = dir;
    }

    public enum Dir {
        HORIZONTAL, VERTICAL;
    }

}

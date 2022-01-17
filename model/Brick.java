package breakout.model;

/*
 *   A brick for the rows of bricks
 */

public class Brick extends AbstractPositionable implements IPositionable {

    public static final double BRICK_WIDTH = 20;  // Default values, use in constructors, not directly
    public static final double BRICK_HEIGHT = 10;
    private final int points;

    public Brick(int x, int y, int points) {
        super(x, y, BRICK_WIDTH, BRICK_HEIGHT, 0, 0);
        this.points = points;
    }


    public int getPoints() {
        return points;
    }
}


package breakout.model;

import static breakout.model.Breakout.GAME_HEIGHT;
import static breakout.model.Breakout.GAME_WIDTH;

/*
 * A Paddle for the Breakout game
 *
 */
public class Paddle extends AbstractPositionable implements IPositionable {

    public static final double PADDLE_WIDTH = 60;  // Default values, use in constructors, not directly
    public static final double PADDLE_HEIGHT = 10;
    public static final double PADDLE_SPEED = 5;

    public Paddle(double x, double y, double width, double height, double dx, double dy) {
        super(x, y, width, height, dx, dy);
    }
}

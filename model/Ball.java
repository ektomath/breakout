package breakout.model;

import java.util.Random;

import static breakout.model.Breakout.GAME_HEIGHT;
import static breakout.model.Breakout.GAME_WIDTH;

/*
 *    A Ball for the Breakout game
 */

public class Ball extends AbstractPositionable implements IPositionable {


    public Ball(double x, double y, double width, double height, double dx, double dy) {
        super(x, y, width, height, dx, dy);
    }
}

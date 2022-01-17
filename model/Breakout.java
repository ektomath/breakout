package breakout.model;


import breakout.event.EventBus;
import breakout.event.ModelEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.lang.System.out;

/*
 *  Overall all logic for the Breakout Game
 *  Model class representing the full game
 *  This class should use other objects delegate work.
 *
 *  NOTE: Nothing visual here
 *
 */
public class Breakout {

    public static final double GAME_WIDTH = 400;
    public static final double GAME_HEIGHT = 400;
    public static final double BALL_SPEED_FACTOR = 1.05; // Increase ball speed
    public static final long SEC = 1_000_000_000;  // Nano seconds used by JavaFX
    Random rand = new Random();

    private int nBalls = 5;
    int playerPoints;

    private final Ball ball;
    private final Paddle paddle;
    private final Wall rightwall;
    private final Wall topwall;
    private final Wall leftWall;
    private final List<Brick> bricks;


    // --------  Game Logic -------------

    private long timeForLastHit;         // To avoid multiple collisions

    public Breakout(Ball ball, Paddle paddle, Wall leftWall, Wall topWall, Wall rightWall, List<Brick> bricks) { // konstruktor
        this.ball = ball;
        this.paddle = paddle;
        this.leftWall = leftWall;
        this.topwall = topWall;
        this.rightwall = rightWall;
        this.bricks = bricks;
    }

    public void update(long now) { // calls our 3 handler functions 60/s defined elsewhere
        ballhandler();
        movementHandler();
        collisionhandler();
    }

    // ----- Helper methods--------------

    // Used for functional decomposition

    private void movementHandler() { // loopar igenom alla objekt och flyttar dem
        for ( int i = 0; i < getPositionables().size(); i++) {
            move((AbstractPositionable) getPositionables().get(i));
        }
        // if cases keep paddle within bounds
        if (paddle.getX() > GAME_WIDTH - paddle.getWidth()) {
            paddle.setX(GAME_WIDTH - paddle.getWidth());
        }
        if (paddle.getX() < 0) {
            paddle.setX(0);
        }
    }

    private void move( AbstractPositionable thing ) { // moves individual objects, used by handler
        thing.setX( thing.getX() + thing.getDx() );
        thing.setY( thing.getY() + thing.getDy() );
    }


    private void collisionhandler(){ // loop through all objects, checks if collision, performs appropriate action

        for(int i = 0; i < getPositionables().size(); i++) {
            AbstractPositionable positionable = (AbstractPositionable) getPositionables().get(i);
            if (positionable instanceof Ball) { continue;}// så vi inte kollar collision med bollen själv


            if (ball.intersects( positionable )) {
                ball.setDx(ball.getDx()*BALL_SPEED_FACTOR); // increases speed on hit
                bounce(positionable); // bounces the ball
                if (positionable instanceof Brick) {
                    EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.BALL_HIT_BRICK)); // sends event with the bus
                    score((Brick) positionable);
                    removebrick((Brick) positionable);
                } else {EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.BALL_HIT_PADDLE));} // could also have hit the wall.. but whats the difference?

                break; } // vill inte kolla om vi krockat med mer än ett objekt
        }

    }
    private void bounce(AbstractPositionable positionable){ // studsar bollen olika baserat på om vägg eller ej
        if (positionable instanceof Wall && ((Wall) positionable).dir == Wall.Dir.VERTICAL)
            ball.setDx(-ball.getDx());
        else {
            ball.setDy(-ball.getDy());
        }
    }


    private void score(Brick brick) { // adds score
        playerPoints = playerPoints + brick.getPoints();
    }

    private void removebrick(Brick brick) { // removes brick
        bricks.remove(brick);
    }

    private void ballhandler(){ // resetar boll så länge det finns bollar kvar, sätter random hastighet
        if (ball.getY() > GAME_HEIGHT && nBalls > 0) {
            ball.setDy(rand.nextInt(5)+1);
            ball.setDx(rand.nextInt(5));
            ball.setY(100);
            ball.setX(100);
            nBalls--;
        }
        if (ball.getY() > GAME_HEIGHT) {
            ball.setDy(0);
            // add GAME OVER text
        }
    }






    // --- Used by GUI  ------------------------

    public List<IPositionable> getPositionables() { // lägger till alla objekt i vår lista med positionables
        List<IPositionable> ps = new ArrayList<>(bricks);
        ps.add(ball);
        ps.add(paddle);
        ps.add(topwall);
        ps.add(leftWall);
        ps.add(rightwall);
        return ps;//
    }

    public int getPlayerPoints() {return playerPoints;}
    public int getnBalls() {return nBalls;}
    public void setPaddleSpeed(double v) {this.paddle.setDx(v);}
}

package breakout.view;

import breakout.event.EventBus;
import breakout.event.IEventHandler;
import breakout.event.ModelEvent;
import breakout.model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static breakout.model.Breakout.GAME_HEIGHT;
import static breakout.model.Breakout.GAME_WIDTH;
import static breakout.model.Brick.BRICK_HEIGHT;
import static breakout.model.Brick.BRICK_WIDTH;
import static java.lang.System.exit;
import static java.lang.System.out;


/*
 * The GUI for the Breakout game (except the menu).
 * No application logic here just GUI and event handling
 *
 * Run this to run the game
 *
 * See: https://en.wikipedia.org/wiki/Breakout_(video_game)
 *
 */
public class BreakoutGUI extends Application implements IEventHandler {

    private Breakout breakout;          // The only reference to the model allowed
    private boolean running = false;    // Is game running?
    Random rand = new Random();

    // ------- Keyboard events ----------------------------------

    private void keyPressed(KeyEvent event) {
        if (!running) {
            return;
        }
        KeyCode kc = event.getCode();
        switch (kc) {
            case LEFT:
                breakout.setPaddleSpeed(-Paddle.PADDLE_SPEED); // flyttar paddeln vänster
                break;
            case RIGHT:
                breakout.setPaddleSpeed(Paddle.PADDLE_SPEED);
                break;
            default:  // Nothing
        }
    }

    private void keyReleased(KeyEvent event) {
        if (!running) {
            return;
        }
        KeyCode kc = event.getCode();
        switch (kc) {
            case LEFT:        // No break, fall through
            case RIGHT:
                breakout.setPaddleSpeed(0); // när vi släpper tangenten stannar paddeln
                break;
            default: // Nothing
        }
    }

    // ---------- Menu actions ---------------------

    private void newGame() {
        // GUI handling
        menu.fixMenusNewGame();
        renderBackground();

        // --- Build the model ----- skapar alla objekt, anropar konstruktorer för objekt och spelmodelen
        Ball ball = new Ball(100 , 100, 20, 20, rand.nextInt(4)+1, rand.nextInt(4)+1);
        Paddle paddle = new Paddle(100, 360, Paddle.PADDLE_WIDTH, Paddle.PADDLE_HEIGHT, 0, 0);
        Wall leftwall = new Wall(0, 0, 0, GAME_HEIGHT , 0,0, Wall.Dir.VERTICAL);
        Wall topwall = new Wall(0, 0, GAME_WIDTH, 0 , 0,0, Wall.Dir.HORIZONTAL);
        Wall rightwall = new Wall(GAME_WIDTH, 0, 0, GAME_HEIGHT , 0,0, Wall.Dir.VERTICAL);
        List<Brick> bricks = getBricks(3,16);
        breakout = new Breakout( ball, paddle, leftwall, topwall, rightwall, bricks );





        // Bind bricks to images
        bindBricks(bricks);

        // Start game
        timer.start();
        running = true;
    }


    private void killGame() {
        timer.stop();
        menu.fixMenusKillGame();
        renderSplash();
        running = false;
    }

    // ---------- Helper to build model ------------


    // Create the formation of bricks
    private List<Brick> getBricks(int nRows, int nCols) {
        List<Brick> bricks = new ArrayList<>();
        int bw = (int) BRICK_WIDTH;
        int bh = (int) BRICK_HEIGHT;
        int offset = 5;
        int points = 300;
        for (int y = 10 * offset; y < nRows * (bh + offset) + 10*offset; y += bh + offset) {
            for (int x = offset - 2; x < nCols * (bw + offset); x += bw + offset) {
                Brick b = new Brick(x, y,points);
                bricks.add(b);
            }
            points -= 100;
        }
        return bricks;
    }

    // Bind bricks to images
    private void bindBricks(List<Brick> bricks) {
        for (Brick b : bricks) { // för varje brick i bricks
            switch ( b.getPoints()) { // beroende på hur många points bricken har bindar vi med olika bilder
                case 100:
                    assets.bind(b, assets.greenTile);
                    break;
                case 200:
                    assets.bind(b, assets.blueTile);
                    break;
                case 300:
                    assets.bind(b, assets.redTile);
                    break;
                default:

            }
        }
    }

    // -------- Event handling (events sent from model to GUI) -----------

    @Override
    public void onModelEvent(ModelEvent evt) {
        if (evt.type == ModelEvent.Type.BALL_HIT_PADDLE) {
            assets.ballHitPaddle.play(); // play sound from assets
        } else if (evt.type == ModelEvent.Type.BALL_HIT_BRICK) {
            assets.ballHitBrick.play();  // play sound from assets
        }
    }


    // Optional
    // Program will jump to here when clicking menu item
    private void handleMenuLevels(ActionEvent e) {
        // OPTIONAL: You decide what to do!
        RadioMenuItem i = (RadioMenuItem) e.getSource();
        if (i.isSelected()) {
            out.println(i.getText());
        }
    }

    // ***********  Nothing to do below ********************'''''

    // Program will jump to here when clicking menu item
    private void handleMenuFile(ActionEvent e) {
        String s = ((MenuItem) e.getSource()).getText();
        switch (s) {
            case "New":      // Using text on menu item
                newGame();
                break;
            case "Stop":
                killGame();
                break;
            case "Exit":
                exit(0);
            default:
                throw new IllegalArgumentException("No such menu choice " + s);
        }
    }

    private Assets assets;
    // For debugging, see render()
    private boolean renderDebug = false; //true;

    private void render() {
        fg.clearRect(0, 0, GAME_WIDTH, GAME_HEIGHT);    // Clear everything
        for (IPositionable d : breakout.getPositionables()) {
            if (renderDebug) {
                fg.strokeRect(d.getX(), d.getY(), d.getWidth(), d.getHeight());
            } else {
                fg.drawImage(assets.get(d), d.getX(), d.getY(), d.getWidth(), d.getHeight());
            }
        }
        fg.setFill(assets.colorFgText);
        fg.setFont(Font.font(14));
        fg.fillText("Points: " + breakout.getPlayerPoints(), 10, GAME_HEIGHT - 5);
        fg.fillText("Balls Left: " + breakout.getnBalls(), 300, GAME_HEIGHT - 5);
    }

    private void renderBackground() {
        if (!renderDebug) {
            bg.drawImage(assets.background, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        }
    }

    private void renderSplash() {
        fg.clearRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        bg.drawImage(assets.splash, 0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    // -------------- Build Scene and start graphics ---------------

    private AnimationTimer timer;
    private GraphicsContext fg;
    private GraphicsContext bg;
    private BreakoutMenu menu = new BreakoutMenu(this::handleMenuFile, this::handleMenuLevels);

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane root = new BorderPane();
        root.setTop(menu);

        // Drawing areas
        Canvas background = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        bg = background.getGraphicsContext2D();
        Canvas foreground = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        fg = foreground.getGraphicsContext2D();

        Pane pane = new Pane(background, foreground);
        root.setCenter(pane);

        timer = new AnimationTimer() {
            public void handle(long now) {
                breakout.update(now);
                render();
            }
        };

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Breakout");

        // Set assets, splash (order matters) and inital menu state
        assets = new Assets();
        menu.fixMenusKillGame();
        renderSplash();

        // Make it possible to send events from model to this
        EventBus.INSTANCE.register(this);

        // Show on screen
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

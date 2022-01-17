package breakout.view;

import breakout.model.Ball;;
import breakout.model.Paddle;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;


/*

   All asset handling here
   All assets in directory /assets

   *** Nothing to do here ***

*/

public class Assets {

    private static final String IMAGE_DIR = "file:assets/img/";
    private static final String SOUND_DIR = "file:assets/sound/";

    // A Map to store which image belongs to which object (or class)
    // Easy to lookup images (value) given object (key)
    private static final Map<Object, Image> objectImage = new HashMap<>();

    // ------------ Handling Colors and Images ------------------------
    public static final Color colorFgText = Color.WHITE;
    public final Image splash = getImage("splash.png");
    public final Image background = getImage("background.png");

    public final String greenTile = "03-Breakout-Tiles.png";
    public final String blueTile = "01-Breakout-Tiles.png";
    public final String redTile = "07-Breakout-Tiles.png";

    // Bind objects and classes
    // Individual object may be bound else where
    public Assets() {
        bind(Paddle.class, "48-Breakout-Tiles.png");
        bind(Ball.class, "58-Breakout-Tiles.png");
    }

    // -------------- Audio handling -----------------------------

    public AudioClip ballHitPaddle = getSound("ballhitpaddle.wav");
    public AudioClip ballHitBrick= getSound("ballhitpaddle.wav");

// -------------- Methods binding objects/classes to assets -----------------

    // Also possible to bind Class objects to images
    public void bind(Object object, String imageFileName) {
        Image i = getImage(imageFileName);
        if (i != null) {
            objectImage.put(object, i);
        } else {
            throw new IllegalArgumentException("Missing image: " + IMAGE_DIR + imageFileName);
        }
    }

    // Get image for object
    public Image get(Object object) {
        Image i = objectImage.get(object);  // Try to find bound object
        if (i == null) {
            return objectImage.get(object.getClass());  // .. if fail, check if class bound
        }
        return i;
    }

    // ---------- Helpers ------------------------

    private Image getImage(String fileName) {
        return new Image(IMAGE_DIR + fileName);
    }

    private Image getImage(String fileName, int width, int heigh) {
        return new Image(IMAGE_DIR + fileName, width, heigh, true, true);
    }

    private AudioClip getSound(String fileName) {
        return new AudioClip(SOUND_DIR + fileName);
    }
}

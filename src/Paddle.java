import java.awt.Rectangle;
import arkanoid.GameObject;

public class Paddle extends GameObject {
    public Paddle(int x, int y, int width, int height,
                  int dx, int dy, String imagePath) {
        super(x, y, width, height, dx, dy, imagePath);
    }
}

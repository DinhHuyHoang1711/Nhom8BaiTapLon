import java.awt.Rectangle;
import arkanoid.GameObject;

public class Paddle extends GameObject {

    public boolean movingLeft;
    public boolean movingRight;

    public Paddle(int x, int y, int width, int height,
                  int dx, int dy, String imagePath) {
        super(x, y, width, height, dx, dy, imagePath);
        this.movingLeft = false;
        this.movingRight = false;
    }

    public boolean isMovingLeft() {
        return this.movingLeft;
    }

    public boolean isMovingRight() {
        return this.movingRight;
    }

    public void setMovingLeft() {
        movingLeft = true;
        movingRight = false;
    }

    public void setMovingRight() {
        movingLeft = false;
        movingRight = true;
    }

    public void setDefaultMoving() {
        movingLeft = false;
        movingRight = false;
    }

}

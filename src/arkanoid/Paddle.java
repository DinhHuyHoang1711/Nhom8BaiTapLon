package arkanoid;

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
    //constructor copy
    public Paddle(Paddle other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(),
                other.getDx(), other.getDy(), other.getImagePath());
        this.movingLeft = other.movingLeft;
        this.movingRight = other.movingRight;
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

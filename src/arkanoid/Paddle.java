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

    public boolean collideFireball(arkanoid.GameObject fireball, float radiusScale) {
        int fx = fireball.getX();
        int fy = fireball.getY();
        int fw = fireball.getWidth();
        int fh = fireball.getHeight();

        int cx = fx + fw / 2;
        int cy = fy + fh / 2;
        int r  = Math.min(fw, fh) / 2;
        if (radiusScale > 0f && radiusScale < 1.0f) {
            r = Math.max(1, (int)(r * radiusScale));
        }
        return circleIntersectsRect(cx, cy, r, getX(), getY(), getWidth(), getHeight());
    }

    public boolean collideFireball(arkanoid.GameObject fireball) {
        return collideFireball(fireball, 1.0f);
    }

    public static boolean circleIntersectsRect(int cx, int cy, int r,
                                               int rx, int ry, int rw, int rh) {
        int nearestX = clamp(cx, rx, rx + rw);
        int nearestY = clamp(cy, ry, ry + rh);
        int dx = nearestX - cx;
        int dy = nearestY - cy;
        return dx * dx + dy * dy <= r * r;
    }

    private static int clamp(int v, int lo, int hi) {
        return (v < lo) ? lo : (v > hi) ? hi : v;
    }

}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Rectangle;
import java.util.*;
import arkanoid.GameObject;

public class Ball extends GameObject {
    private Random rand = new Random();
    public Ball(int x, int y, int size, int dx, int dy, String imgPath) {
        super(x, y, size, size, dx, dy, imgPath);
    }

    public Ball(int x, int y, int size, String imgPath) {
        super(x, y, size, size, 0, 0, imgPath);
    }

    public Ball(int x, int y, int width, int height, int dx, int dy, String imgPath) {
        super(x, y, width, height, dx, dy, imgPath);
    }

    public void step(Rectangle bounds) {
        setX(getX() + getDx());
        setY(getY() + getDy());

        if (getX() < 0) {
            setX(0);
            setDx(-getDx());
        } else if (getX() + getWidth() > bounds.width) {
            setX(Math.max(0, bounds.width - getWidth()));
            setDx(-getDx());
        }

        if (getY() < 0) {
            setY(0);
            setDy(-getDy());
        }
    }

    public boolean collide(Brick b) {
        if (b == null || b.isDestroyed()) return false;

        Rectangle br = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        Rectangle ba = new Rectangle(getX(), getY(), getWidth(), getHeight());

        if (!ba.intersects(br)) return false;

        int overlapLeft   = ba.x + ba.width - br.x;
        int overlapRight  = br.x + br.width - ba.x;
        int overlapTop    = ba.y + ba.height - br.y;
        int overlapBottom = br.y + br.height - ba.y;

        int minHoriz = Math.min(overlapLeft, overlapRight);
        int minVert  = Math.min(overlapTop, overlapBottom);

        if (minHoriz < minVert) {
            setDx(-getDx());
        } else {
            setDy(-getDy());
        }

        b.takeHit();
        return true;
    }

    public void collideWithPaddle(Paddle paddle) {
        Rectangle paddleRect = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
        Rectangle ballRect = new Rectangle(getX(), getY(), getWidth(), getHeight());
        if (!paddleRect.intersects(ballRect)) {
            return;
        } else {
            this.setDy(-Math.abs(this.getDy()));
            this.setDx(this.getDx() / Math.abs(this.getDx()) * (rand.nextInt(7 - 5) + 5));
        }
    }
}

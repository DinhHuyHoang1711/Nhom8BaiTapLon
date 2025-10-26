package arkanoid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Rectangle;
import java.util.*;
import arkanoid.GameObject;
import arkanoid.Brick;
import arkanoid.Paddle;

public class Ball extends GameObject{
    // Kích thước bóng
    private static final int BALL_SIZE = 31;

    // Thuộc tính
    private String element;
    private int baseDamage;
    private Random rand = new Random();

    // Ảnh
    private static final String NORMAL_BALL_IMG = "img/ball/normal.png";
    private static final String FIRE_BALL_IMG   = "img/ball/fire.png";
    private static final String WATER_BALL_IMG  = "img/ball/water.png";
    private static final String WIND_BALL_IMG   = "img/ball/wind.png";
    private static final String EARTH_BALL_IMG  = "img/ball/earth.png";

    // ==== CONSTRUCTOR ==== //
    public Ball(int x, int y, int size, int dx, int dy, String imgPath) {
        super(x, y, size, size, dx, dy, imgPath);
        setElementFromName("normal");
    }

    public Ball(int x, int y, int size, int dx, int dy, String imgPath, int damage) {
        super(x, y, size, size, dx, dy, imgPath);
        setElementFromName("normal", damage);
    }

    public Ball(int x, int y, int size, int dx, int dy, String element, String imgPath) {
        super(x, y, size, size, dx, dy, imgPath);
        setElementFromName(element);
    }

    public Ball(int x, int y, int size, int dx, int dy, String element, String imgPath, int damage) {
        super(x, y, size, size, dx, dy, imgPath);
        setElementFromName(element, damage);
    }

    public Ball(int x, int y, int size, String imgPath) {
        super(x, y, size, size, 0, 0, imgPath);
        setElementFromName("normal");
    }

    public Ball(int x, int y, int width, int height, int dx, int dy, String imgPath) {
        super(x, y, width, height, dx, dy, imgPath);
        setElementFromName("normal");
    }

    // ==== TYPE BALL ==== //
    public static Ball normalBall(int x, int y) {
        return new Ball(x, y, BALL_SIZE, 0, 0, "normal", NORMAL_BALL_IMG);
    }

    public static Ball fireBall(int x, int y) {
        return new Ball(x, y, BALL_SIZE, 0, 0, "fire", FIRE_BALL_IMG);
    }

    public static Ball waterBall(int x, int y) {
        return new Ball(x, y, BALL_SIZE, 0, 0, "water", WATER_BALL_IMG);
    }

    public static Ball windBall(int x, int y) {
        return new Ball(x, y, BALL_SIZE, 0, 0, "wind", WIND_BALL_IMG);
    }

    public static Ball earthBall(int x, int y) {
        return new Ball(x, y, BALL_SIZE, 0, 0, "earth", EARTH_BALL_IMG);
    }

    // ==== GETTER/SETTER ==== //
    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        setElementFromName(element);
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
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
        else {

            int overlapLeft = ba.x + ba.width - br.x;
            int overlapRight = br.x + br.width - ba.x;
            int overlapTop = ba.y + ba.height - br.y;
            int overlapBottom = br.y + br.height - ba.y;

            int minHoriz = Math.min(overlapLeft, overlapRight);
            int minVert = Math.min(overlapTop, overlapBottom);

            if (minHoriz < minVert) {
                setDx(-getDx());
            } else {
                setDy(-getDy());
            }

            if(this.element.equals("normal")) {
                b.takeHit(this.getBaseDamage());
            }
            else {
                b.takeHit(this.element.equals(b.getElement()) ? this.getBaseDamage() *  2 :
                this.getBaseDamage());
            }
        }
            return true;
    }

    public void collideWithPaddle(Paddle paddle) {
        Rectangle paddleRect = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
        Rectangle ballRect = new Rectangle(getX(), getY(), getWidth(), getHeight());
        if (!paddleRect.intersects(ballRect)) {
            return;
        } else {
            if(paddle.isMovingLeft()) {
                //Paddle di sang trai thi bong di sang trai
                this.setDy(-Math.abs(rand.nextInt(1) + Math.abs(this.getDy())));
                this.setDx(-1 * Math.abs((rand.nextInt(1) + Math.abs(this.getDx()))));
            } else {
                //Paddle di sang phai thi bong di sang phai
                if(paddle.isMovingRight()) {
                    this.setDy(-Math.abs(rand.nextInt(1) + Math.abs(this.getDy())));
                    this.setDx(Math.abs((rand.nextInt(1) + Math.abs(this.getDx()))));
                } else {
                    //Paddle dung yen thi bong tiep tuc di theo huong ban dau
                    this.setDy(-Math.abs(rand.nextInt(1) + Math.abs(this.getDy())));
                    this.setDx(this.getDx() / Math.abs(this.getDx()) * (rand.nextInt(1) + Math.abs(this.getDx())));
                }
            }
            //this.setDy(-Math.abs(rand.nextInt(2) + this.getDy()));
            //this.setDx(this.getDx() / Math.abs(this.getDx()) * (rand.nextInt(2) + this.getDx()));
        }
    }

    private void setElementFromName(String element) {
        this.element = (element.isEmpty() ? "normal" : element);
        this.baseDamage = this.element.equals("normal") ? 50 : 100;
    }

    private void setElementFromName(String element, int damage) {
        this.element = element;
        this.baseDamage = damage;
    }
}

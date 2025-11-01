package arkanoid;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.Rectangle;
import java.util.*;
import arkanoid.GameObject;
import arkanoid.Brick;
import arkanoid.Paddle;
import arkanoid.Sound;

import static arkanoid.Game.GAME_HEIGHT;
import static arkanoid.Game.PLAYFRAME_WIDTH;

public class Ball extends GameObject{
    // Kích thước bóng
    public static final int BALL_SIZE = 21;

    //am thanh bong va vao cac vat the
    public final Sound wallHit = new Sound("sound/wallHit.wav");
    public final Sound paddleHit = new Sound("sound/paddleHit.wav");
    public final Sound normalbrickHit = new Sound("sound/normalbrickHit.wav");
    public final Sound firebirckHit = new Sound("sound/firebrickHit.wav");
    public final Sound waterbrickHit = new Sound("sound/waterbrickHit.wav");
    public final Sound earthbrickHit = new Sound("sound/earthbrickHit.wav");
    public final Sound windbrickHit = new Sound("sound/windbrickHit.wav");

    // Thuộc tính
    private String element;
    private int baseDamage;
    private Random rand = new Random();
    private int baseDx;
    private int baseDy;

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
        this.baseDx = dx;
        this.baseDy = dy;
    }

    public Ball(int x, int y, int size, int dx, int dy, String imgPath, int damage) {
        super(x, y, size, size, dx, dy, imgPath);
        setElementFromName("normal", damage);
        this.baseDx = dx;
        this.baseDy = dy;
    }

    public Ball(int x, int y, int size, int dx, int dy, String element, String imgPath) {
        super(x, y, size, size, dx, dy, imgPath);
        setElementFromName(element);
        this.baseDx = dx;
        this.baseDy = dy;
    }

    public Ball(int x, int y, int size, int dx, int dy, String element, String imgPath, int damage) {
        super(x, y, size, size, dx, dy, imgPath);
        setElementFromName(element, damage);
        this.baseDx = dx;
        this.baseDy = dy;
    }

    public Ball(int x, int y, int size, String imgPath) {
        super(x, y, size, size, 0, 0, imgPath);
        setElementFromName("normal");
    }

    public Ball(int x, int y, int width, int height, int dx, int dy, String imgPath) {
        super(x, y, width, height, dx, dy, imgPath);
        setElementFromName("normal");
        this.baseDx = dx;
        this.baseDy = dy;
    }

    //constructor copy
    public Ball(Ball other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(),
                other.getDx(), other.getDy(), other.getImagePath());

        this.element = other.element;
        this.baseDamage = other.baseDamage;
        this.baseDx = other.baseDx;
        this.baseDy = other.baseDy;
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

    public static Ball normalBall() {
        return new Ball(PLAYFRAME_WIDTH / 2 - 30, GAME_HEIGHT - 120, BALL_SIZE, 6,
                -8, "normal", NORMAL_BALL_IMG, 50);
    }

    public static Ball fireBall() {
        return new Ball(PLAYFRAME_WIDTH / 2 - 30, GAME_HEIGHT - 120, BALL_SIZE, 6,
                -8, "fire", FIRE_BALL_IMG, 100);
    }

    public static Ball waterBall() {
        return new Ball(PLAYFRAME_WIDTH / 2 - 30, GAME_HEIGHT - 120, BALL_SIZE, 6,
                -8, "water", WATER_BALL_IMG, 100);
    }

    public static Ball windBall() {
        return new Ball(PLAYFRAME_WIDTH / 2 - 30, GAME_HEIGHT - 120, BALL_SIZE, 6,
                -8, "wind", WIND_BALL_IMG, 100);
    }

    public static Ball earthBall()
    {
        return new Ball(PLAYFRAME_WIDTH / 2 - 30, GAME_HEIGHT - 120, BALL_SIZE, 6,
                -8, "earth", EARTH_BALL_IMG, 100);
    }

    public static Ball bongnguhanh() {
        return new Ball(PLAYFRAME_WIDTH / 2 - 30, GAME_HEIGHT - 120, Ball.BALL_SIZE, 6,
                -8, "img/ball/bongnguhanh.png", 50);
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
            wallHit.play();
            setX(0);
            setDx(-getDx());
        } else if (getX() + getWidth() > bounds.width) {
            wallHit.play();
            setX(Math.max(0, bounds.width - getWidth()));
            setDx(-getDx());
        }

        if (getY() < 0) {
            wallHit.play();
            setY(0);
            setDy(-getDy());
        }
    }

    //Xu li va cham voi gach sao cho muot
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
                // Va chạm trái hoặc phải
                if (overlapLeft < overlapRight) {
                    setX(getX() - overlapLeft - 1); // Đẩy bóng sang trái
                } else {
                    setX(getX() + overlapRight + 1); // Đẩy bóng sang phải
                }
                setDx(-getDx());
            } else {
                // Va chạm trên hoặc dưới
                if (overlapTop < overlapBottom) {
                    setY(getY() - overlapTop - 1); // Đẩy bóng lên
                } else {
                    setY(getY() + overlapBottom + 1); // Đẩy bóng xuống
                }
                setDy(-getDy());
            }

            // Gây sát thương
            if (this.element.equals("normal")) {
                b.takeHit(this.getBaseDamage());
                if(b.getElement().equals("normal")) normalbrickHit.play();
                if(b.getElement().equals("fire")) firebirckHit.play();
                if(b.getElement().equals("water")) waterbrickHit.play();
                if(b.getElement().equals("earth")) earthbrickHit.play();
                if(b.getElement().equals("wind")) windbrickHit.play();
            } else {
                b.takeHit(this.element.equals(b.getElement()) ? this.getBaseDamage() * 2 : this.getBaseDamage());
                if(b.getElement().equals("normal")) normalbrickHit.play();
                if(b.getElement().equals("fire")) firebirckHit.play();
                if(b.getElement().equals("water")) waterbrickHit.play();
                if(b.getElement().equals("earth")) earthbrickHit.play();
                if(b.getElement().equals("wind")) windbrickHit.play();
            }

            return true;
        }
    }

    public void collideWithPaddle(Paddle paddle) {
        Rectangle paddleRect = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
        Rectangle ballRect = new Rectangle(getX(), getY(), getWidth(), getHeight());
        if (!paddleRect.intersects(ballRect)) {
            return;
        } else {
            paddleHit.play();
            if(paddle.isMovingLeft()) {
                //Paddle di sang trai thi bong di sang trai
                this.setDy(-Math.abs(rand.nextInt(4) + Math.abs(this.baseDy)));
                this.setDx(-1 * Math.abs((rand.nextInt(4) + Math.abs(this.baseDx))));
            } else {
                //Paddle di sang phai thi bong di sang phai
                if(paddle.isMovingRight()) {
                    this.setDy(-Math.abs(rand.nextInt(4) + Math.abs(this.baseDy)));
                    this.setDx(Math.abs((rand.nextInt(4) + Math.abs(this.baseDx))));
                } else {
                    //Paddle dung yen thi bong tiep tuc di theo huong ban dau
                    this.setDy(-Math.abs(rand.nextInt(4) + Math.abs(this.baseDy)));
                    this.setDx(this.getDx() / Math.abs(this.getDx()) * (rand.nextInt(4) + Math.abs(this.baseDx)));
                }
            }
            //this.setDy(-Math.abs(rand.nextInt(2) + this.getDy()));
            //this.setDx(this.getDx() / Math.abs(this.getDx()) * (rand.nextInt(2) + this.getDx()));
        }
    }

    public boolean collideWithBoss(Boss boss) {
        if (boss == null || boss.isDead()) return false;

        Rectangle br = new Rectangle(boss.getX(), boss.getY(), boss.getWidth(), boss.getHeight());
        Rectangle ba = new Rectangle(getX(), getY(), getWidth(), getHeight());

        if (!ba.intersects(br)) return false;

        // --- MTV: chọn hướng đẩy ra ít nhất ---
        int overlapLeft   = ba.x + ba.width  - br.x;
        int overlapRight  = br.x + br.width  - ba.x;
        int overlapTop    = ba.y + ba.height - br.y;
        int overlapBottom = br.y + br.height - ba.y;

        int minHoriz = Math.min(overlapLeft, overlapRight);
        int minVert  = Math.min(overlapTop, overlapBottom);

        if (minHoriz < minVert) {
            // Va chạm trái/phải
            if (overlapLeft < overlapRight) {
                setX(getX() - overlapLeft - 1);    // đẩy sang trái
                setDx(-Math.abs(getDx()));         // đảo hướng X
            } else {
                setX(getX() + overlapRight + 1);   // đẩy sang phải
                setDx(Math.abs(getDx()));
            }
        } else {
            // Va chạm trên/dưới
            if (overlapTop < overlapBottom) {
                setY(getY() - overlapTop - 1);     // đẩy lên
                setDy(-Math.abs(getDy()));         // đảo hướng Y
            } else {
                setY(getY() + overlapBottom + 1);  // đẩy xuống
                setDy(Math.abs(getDy()));
            }
        }

        // --- Gây sát thương lên boss ---
        int dmg = this.getBaseDamage();
        if (!"normal".equals(this.element)) {
            // Nếu bạn muốn tương tác nguyên tố (ví dụ trùng nguyên tố x2):
            dmg = this.element.equalsIgnoreCase(boss.getElement()) ? dmg * 2 : dmg;
        }

        return true; // đã va
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

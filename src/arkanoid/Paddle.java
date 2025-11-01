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

    public int getCenterX() { return getX() + getWidth()/2; }
    public int getCenterY() { return getY() + getHeight()/2; }

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

    // Wave coi như hình chữ nhật; có thể co hitbox theo insetX/insetY.
    public boolean collideWave(arkanoid.GameObject wave) {
        return collideWave(wave, 8, 6); // inset mặc định: 8px ngang, 6px dọc
    }

    public boolean collideWave(arkanoid.GameObject wave, int insetX, int insetY) {
        final int wx  = wave.getX() + Math.max(0, insetX);
        final int wy  = wave.getY() + Math.max(0, insetY);
        final int ww0 = wave.getWidth()  - 2 * Math.max(0, insetX);
        final int wh0 = wave.getHeight() - 2 * Math.max(0, insetY);
        if (ww0 <= 0 || wh0 <= 0) return false; // bị co quá mức
        final int ww = ww0, wh = wh0;

        final int px = getX(), py = getY();
        final int pw = getWidth(), ph = getHeight();

        // AABB intersect: (px..px+pw) vs (wx..wx+ww), (py..py+ph) vs (wy..wy+wh)
        final boolean overlap =
                px < wx + ww && px + pw > wx &&
                        py < wy + wh && py + ph > wy;

        return overlap;
    }

    // AABB với shuriken; cho phép co hitbox bằng insetX/insetY.
    public boolean collideShurikenAABB(arkanoid.GameObject shuriken, int insetX, int insetY) {
        if (shuriken == null) return false;

        final int sx = shuriken.getX() + Math.max(0, insetX);
        final int sy = shuriken.getY() + Math.max(0, insetY);
        final int sw0 = shuriken.getWidth()  - 2 * Math.max(0, insetX);
        final int sh0 = shuriken.getHeight() - 2 * Math.max(0, insetY);
        if (sw0 <= 0 || sh0 <= 0) return false;
        final int sw = sw0, sh = sh0;

        final int px = getX(), py = getY();
        final int pw = getWidth(), ph = getHeight();

        // (px..px+pw) vs (sx..sx+sw), (py..py+ph) vs (sy..sy+sh)
        return px < sx + sw && px + pw > sx && py < sy + sh && py + ph > sy;
    }

    // Xử lý va chạm shuriken: knockback + markDead. Game sẽ trừ máu nếu trả về true.
    public boolean collideWithShuriken(ShurikenWind s, int playframeWidth) {
        if (s == null || s.isDead()) return false;

        // co hitbox nhẹ nếu muốn:  insetX=2, insetY=2. Để 0 nếu không cần.
        boolean hit = collideShurikenAABB(s, 2, 2);
        if (!hit) return false;

        s.markDead();
        return true;
    }


}

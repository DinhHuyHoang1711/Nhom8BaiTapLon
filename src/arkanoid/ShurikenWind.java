package arkanoid;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ShurikenWind extends GameObject {
    private double heading;
    private final double speed;
    private final double maxTurn; // rad per tick
    private double visualAngle = 0.0;
    private final double spinRate;
    private int lifeTicks = 60 * 10; // ~10s TTL
    private boolean dead = false;
    private double targetAngle;        // góc đích đang theo

    public ShurikenWind(int x, int y, int size, double initHeading) {
        super(x, y, size, size, 0, 0, "img/Boss/WindShuriken.png");
        this.heading = initHeading;
        this.speed   = 6.0;
        this.maxTurn = 0.035;
        this.spinRate = 0.45;
    }

    public boolean isDead() { return dead; }      // <<< thêm
    public void markDead() { this.dead = true; }

    public void seekTowards(int tx, int ty) {
        double cx = getX() + getWidth()/2.0;
        double cy = getY() + getHeight()/2.0;

        // 1) (tuỳ chọn) retarget thưa hơn: nếu bạn chưa dùng thì bỏ phần này
        // double dxT = tx - cx, dyT = ty - cy;
        // double target = Math.atan2(dyT, dxT);

        // 2) Tính góc rẽ có giới hạn
        double dxT = tx - cx, dyT = ty - cy;
        double target = Math.atan2(dyT, dxT);
        double delta  = wrapToPi(target - heading);
        double turn   = clamp(delta, -maxTurn, +maxTurn);
        heading += turn;

        // 3) Vận tốc theo heading
        int vx = (int) Math.round(speed * Math.cos(heading));
        int vy = (int) Math.round(speed * Math.sin(heading));

        // (đảm bảo luôn có thành phần rơi xuống) <<<
        if (vy < 2) vy = 2;

        setDx(vx);
        setDy(vy);
    }



    public void step(int playW, int playH) {
        // dự báo bước tiếp theo
        int nx = getX() + getDx();
        int ny = getY() + getDy();

        // one-way valve: hễ bước tiếp theo chui xuống đáy là kill ngay
        if (ny > playH) { dead = true; return; }

        // cập nhật vị trí
        setX(nx);
        setY(ny);

        // hiệu ứng quay
        visualAngle += spinRate;

        // biên trái/phải
        if (getX() < -getWidth() || getX() > playW + getWidth()) dead = true;

        // TTL
        if (--lifeTicks <= 0) dead = true;
    }

    public void paintRotating(Graphics2D g2d, Image img) {
        int cx = getX() + getWidth()/2;
        int cy = getY() + getHeight()/2;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(visualAngle, cx, cy);
        g2d.drawImage(img, getX(), getY(), getWidth(), getHeight(), null);
        g2d.setTransform(old);
    }

    private static double wrapToPi(double a) {
        while (a > Math.PI) a -= 2*Math.PI;
        while (a < -Math.PI) a += 2*Math.PI;
        return a;
    }
    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}

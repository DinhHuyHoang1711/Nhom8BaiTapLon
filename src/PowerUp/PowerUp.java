package PowerUp;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import arkanoid.*;

public abstract class PowerUp extends GameObject {
    protected final PowerUpType type;
    protected final long duration;
    protected BufferedImage image;
    protected boolean isCaught = false;
    protected boolean isActive = false;
    protected long startTime;
    protected Sound catchedSound;

    public enum PowerUpType {
        INCREASE_DAMAGE,
        EXTRA_HEART,
        EXPAND_PADDLE,
        SLOW_PADDLE
    }

    public PowerUp(int x, int y, int width, int height, String imagePath, PowerUpType type, long duration) {
        super(x, y, width, height, 0, 5, imagePath);
        this.type = type;
        this.duration = duration;
        loadImage(imagePath);
        catchedSound = new Sound("sound/click.wav");
    }

    private void loadImage(String path) {
        try {
            Image img = new ImageIcon(path).getImage();
            BufferedImage buffered = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffered.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();
            this.image = buffered;
        } catch (Exception e) {
            System.err.println("Cannot load power-up image: " + path);
        }
    }

    // Áp dụng hiệu ứng (khi bắt được)
    public abstract void applyEffect();

    // Hủy hiệu ứng (sau khi hết hạn)
    public abstract void removeEffect();

    // Cập nhật trạng thái rơi và hiệu ứng
    public void update(Paddle paddle, PowerUpManager manager) {
        if (!isCaught) {
            setY(getY() + getDy());
            if (checkCollision(paddle)) {
                isCaught = true;
                catchedSound.play();
                manager.apply(this);
            }
        }
    }

    public PowerUpType getType() { return type; }
    public long getDuration() { return duration; }

    public boolean isOutOfBounds() {
        return getY() > GAME_HEIGHT;
    }

    public void draw(Graphics g) {
        if (!isCaught && image != null) {
            g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
        }
    }

    // Kiểm tra va chạm với Paddle
    public boolean checkCollision(Paddle paddle) {
        Rectangle r1 = new Rectangle(getX(), getY(), getWidth(), getHeight());
        Rectangle r2 = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
        return r1.intersects(r2);
    }
}

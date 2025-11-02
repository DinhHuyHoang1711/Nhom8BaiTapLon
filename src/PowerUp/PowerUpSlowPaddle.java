package PowerUp;

import arkanoid.Paddle;

/**
 * Làm chậm tốc độ của Paddle
  */
public class PowerUpSlowPaddle extends PowerUp {
    private static final int PENALTY_SPEED = 5;     // Tốc độ bị giảm
    private static final int MIN_SPEED = 5;     // Tốc độ tối thiểu nếu cộng dồn
    private static final double SIZE_MULTIPLIER = 1.5;  // Hệ số kích thước giảm
    private final Paddle paddle;

    // Kích thước ban đầu
    private final int originalWidth;
    private final int originalHeight;
    private final int originalSpeed;

    // Constructor
    public PowerUpSlowPaddle(int x, int y, Paddle paddle) {
        super(x, y ,40, 40, "img/powerup/slow_paddle.png", PowerUpType.SLOW_PADDLE, 6000);
        this.paddle = paddle;
        this.originalWidth = paddle.getWidth();
        this.originalHeight = paddle.getHeight();
        this.originalSpeed = paddle.getDx();
    }


    @Override
    public void applyEffect() {
        if(paddle == null) {
            return;
        }

        // Giảm tốc độ và kích thước
        int newSpeed = Math.max(originalSpeed - PENALTY_SPEED, MIN_SPEED);
        int newWidth = (int) (originalWidth / SIZE_MULTIPLIER);
        int newHeight = (int) (originalHeight / SIZE_MULTIPLIER);

        paddle.setWidth(newWidth);
        paddle.setHeight(newHeight);
        paddle.setDx(newSpeed);

    }

    @Override
    public void removeEffect() {
        if(paddle == null) {
            return;
        }

        // Trả về kích thước ban đầu
        paddle.setWidth(originalWidth);
        paddle.setHeight(originalHeight);
        paddle.setDx(originalSpeed);
    }
}

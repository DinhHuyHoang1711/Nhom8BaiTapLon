package PowerUp;

import arkanoid.*;

import static arkanoid.Game.PLAYFRAME_WIDTH;

/**
 * Mở rộng Paddle trong 10s
 */
public class PowerUpExpandPaddle extends PowerUp {
    private static final double SIZE_MULTIPLIER = 1.5;  // Hệ số mở rộng
    private final Paddle paddle;
    private final int originalWidth;    // Chiều rộng ban đầu

    public PowerUpExpandPaddle(int x, int y, Paddle paddle) {
        super(x, y, 40, 40, "img/powerup/expand_paddle.png", PowerUpType.EXPAND_PADDLE, 10000);
        this.paddle = paddle;
        this.originalWidth = paddle.getWidth(); // Lưu chiều rộng ban đầu
    }

    @Override
    public void applyEffect() {
        if (paddle == null) {
            return;
        }

        // Tăng kích thước
        int newWidth = (int) (originalWidth * SIZE_MULTIPLIER);
        paddle.setWidth(newWidth);

        // check không vượt quá bản đồ
        if (paddle.getX() + newWidth > PLAYFRAME_WIDTH) {
            paddle.setX(PLAYFRAME_WIDTH - newWidth);
        }
    }


    @Override
    public void removeEffect() {
        if (paddle == null) {
            return;
        }
        paddle.setWidth(originalWidth);     // Phục hồi chiều rộng ban đầu
    }
}
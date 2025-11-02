package PowerUp;

import arkanoid.*;

import static arkanoid.Game.PLAYFRAME_WIDTH;

public class PowerUpExpandPaddle extends PowerUp {
    private static final double SIZE_MULTIPLIER = 1.5;
    private final Paddle paddle;
    private final int originalWidth;

    public PowerUpExpandPaddle(int x, int y, Paddle paddle) {
        super(x, y, 40, 40, "img/powerup/expand_paddle.png", PowerUpType.EXPAND_PADDLE, 10000);
        this.paddle = paddle;
        this.originalWidth = paddle.getWidth();
    }

    @Override
    public void applyEffect() {
        if (paddle == null) {
            return;
        }

        int newWidth = (int) (originalWidth * SIZE_MULTIPLIER);
        paddle.setWidth(newWidth);

        if (paddle.getX() + newWidth > PLAYFRAME_WIDTH) {
            paddle.setX(PLAYFRAME_WIDTH - newWidth);
        }
    }


    @Override
    public void removeEffect() {
        if (paddle == null) {
            return;
        }
        paddle.setWidth(originalWidth);
    }
}
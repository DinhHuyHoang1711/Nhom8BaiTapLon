package PowerUp;

import arkanoid.Paddle;

public class PowerUpSlowPaddle extends PowerUp {
    private static final int PENALTY_SPEED = 5;
    private static final int MIN_SPEED = 5;
    private static final double SIZE_MULTIPLIER = 1.5;
    private final Paddle paddle;
    private final int originalWidth;
    private final int originalSpeed;

    public PowerUpSlowPaddle(int x, int y, Paddle paddle) {
        super(x, y ,40, 40, "img/powerup/slow_paddle.png", PowerUpType.SLOW_PADDLE, 6000);
        this.paddle = paddle;
        this.originalWidth = paddle.getWidth();
        this.originalSpeed = paddle.getDx();
    }


    @Override
    public void applyEffect() {
        if(paddle == null) {
            return;
        }

        int newSpeed = Math.max(originalSpeed - PENALTY_SPEED, MIN_SPEED);
        int newWidth = (int) (originalWidth / SIZE_MULTIPLIER);

        paddle.setWidth(newWidth);
        paddle.setDx(newSpeed);

    }

    @Override
    public void removeEffect() {
        if(paddle == null) {
            return;
        }

        paddle.setWidth(originalWidth);
        paddle.setDx(originalSpeed);
    }
}

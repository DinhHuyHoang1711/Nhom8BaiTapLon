package PowerUp;

import arkanoid.*;

public class PowerUpIncreaseDamage extends PowerUp {
    private final OwnedManager ownedManager;
    private static final int BONUS_DAMAGE = 50;
    private static final double SIZE_MULTIPLIER = 1.5;

    private int currentDamage;
    private int previousWidth;
    private int previousHeight;

    public PowerUpIncreaseDamage(int x, int y, OwnedManager ownedManager) {
        super(x, y, 40, 40, "img/button/2.png", PowerUp.PowerUpType.INCREASE_DAMAGE, 8000);
        this.ownedManager = ownedManager;
    }

    @Override
    public void applyEffect() {
        Ball ball = ownedManager.getCurrentBall();
        if (ball == null) return; // tránh null pointer

        // Lưu lại thông tin gốc
        currentDamage = ball.getBaseDamage();
        previousWidth = ball.getWidth();
        previousHeight = ball.getHeight();

        ball.setBaseDamage(currentDamage + BONUS_DAMAGE);

        int newWidth = (int) (previousWidth * SIZE_MULTIPLIER);
        int newHeight = (int) (previousHeight * SIZE_MULTIPLIER);
        ball.setWidth(newWidth);
        ball.setHeight(newHeight);
    }

    @Override
    public void removeEffect() {
        Ball ball = ownedManager.getCurrentBall();
        if (ball == null) return;

        ball.setBaseDamage(currentDamage);
        ball.setWidth(previousWidth);
        ball.setHeight(previousHeight);
    }

}

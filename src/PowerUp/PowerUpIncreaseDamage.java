package PowerUp;

import arkanoid.*;

/**
 * Tăng sát thương.
 */
public class PowerUpIncreaseDamage extends PowerUp {
    private final OwnedManager ownedManager;    // Danh sách bóng
    private static final int BONUS_DAMAGE = 50;     // Sát thương tăng thêm
    private static final double SIZE_MULTIPLIER = 1.5;  // hệ số tăng kích thước

    private int currentDamage;  // Sát thương hiện tại
    private int previousWidth;  // Chiều rộng ban đầu
    private int previousHeight; // Chiều dài ban đầu

    // Constructor
    public PowerUpIncreaseDamage(int x, int y, OwnedManager ownedManager) {
        super(x, y, 40, 40, "img/powerup/increase_damage.png", PowerUpType.INCREASE_DAMAGE, 8000);
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

        ball.setBaseDamage(currentDamage + BONUS_DAMAGE);   // Tăng sát thương khi bắt được

        // Cập nhật kích thước
        int newWidth = (int) (previousWidth * SIZE_MULTIPLIER);
        int newHeight = (int) (previousHeight * SIZE_MULTIPLIER);
        ball.setWidth(newWidth);
        ball.setHeight(newHeight);
    }

    @Override
    public void removeEffect() {
        Ball ball = ownedManager.getCurrentBall();
        if (ball == null) return;

        // Trả về sát thương và kích thước ban đầu khi kết thúc hiệu ứng
        ball.setBaseDamage(currentDamage);
        ball.setWidth(previousWidth);
        ball.setHeight(previousHeight);
    }

}

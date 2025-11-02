package PowerUp;

import arkanoid.Game;
import java.awt.*;
import javax.swing.*;
import arkanoid.*;

/**
 * Tăng mạng
 */
public class PowerUpExtraHeart extends PowerUp {
    private static final int MAX_HEARTS = 3;    // Mạng tối đa

    // Constructor
    public PowerUpExtraHeart(int x, int y) {
        super(x, y, 40, 40, "img/powerup/heartPowerup.png", PowerUpType.EXTRA_HEART, 0);
    }

    @Override
    public void applyEffect() {
        Game game = getActiveGame();
        if (game.currentHeart >= MAX_HEARTS) return;

        game.currentHeart++;        // Tăng mạng
        updateHeartIcon(game);
    }

    @Override
    public void removeEffect() {
        //hello.
    }

    // Cập nhật hiển thị
    private void updateHeartIcon(Game game) {
        if (game.heart.size() > game.currentHeart - 1) {
            ImageIcon red = new ImageIcon("img/heart/redheart.png");
            Image scaled = red.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            game.heart.get(game.currentHeart - 1).setIcon(new ImageIcon(scaled));
        }
    }

    // Tìm đối tượng game để áp dụng hiệu ứng
    private Game getActiveGame() {
        for (Frame f : Frame.getFrames()) {
            if (f instanceof Game && f.isDisplayable()) {
                return (Game) f;
            }
        }
        return null;
    }
}
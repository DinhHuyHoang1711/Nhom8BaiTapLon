package PowerUp;

import arkanoid.Game;
import java.awt.*;
import javax.swing.*;
import arkanoid.*;

public class PowerUpExtraHeart extends PowerUp {
    private static final int MAX_HEARTS = 3;

    public PowerUpExtraHeart(int x, int y) {
        super(x, y, 40, 40, "img/button/4.png", PowerUp.PowerUpType.EXTRA_HEART, 0);
    }

    @Override
    public void applyEffect() {
        Game game = getActiveGame();
        if (game.currentHeart >= MAX_HEARTS) return;

        game.currentHeart++;
        updateHeartIcon(game);
    }

    @Override
    public void removeEffect() {
        //hello.
    }

    private void updateHeartIcon(Game game) {
        if (game.heart.size() > game.currentHeart - 1) {
            ImageIcon red = new ImageIcon("img/heart/redheart.png");
            Image scaled = red.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            game.heart.get(game.currentHeart - 1).setIcon(new ImageIcon(scaled));
        }
    }

    private Game getActiveGame() {
        for (Frame f : Frame.getFrames()) {
            if (f instanceof Game && f.isDisplayable()) {
                return (Game) f;
            }
        }
        return null;
    }
}
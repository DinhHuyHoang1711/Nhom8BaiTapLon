package PowerUp;

import arkanoid.Sound;
import javax.swing.Timer;
import java.util.HashMap;
import java.util.Map;
import arkanoid.Game;

public class PowerUpManager {
    private final Game game;
    private final Map<PowerUp, Timer> activeTimers = new HashMap<>();

    public PowerUpManager(Game game) {
        this.game = game;
    }

    public void apply(PowerUp pu) {
        if (pu == null) return;

        pu.applyEffect();

        new Sound("sound/click.wav").play();

        if (pu.getDuration() > 0) {
            Timer timer = new Timer((int) pu.getDuration(), ev -> pu.removeEffect());
            timer.setRepeats(false);
            timer.start();
            activeTimers.put(pu, timer);
        }
    }

    public void clear() {
        activeTimers.values().forEach(Timer::stop);
        activeTimers.clear();
    }
}
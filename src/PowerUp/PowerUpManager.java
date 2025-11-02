package PowerUp;

import arkanoid.Sound;
import javax.swing.Timer;
import java.util.HashMap;
import java.util.Map;
import arkanoid.*;

/**
 * Quản lý các Power Up trong game.
 */
public class PowerUpManager {
    // Tham chiếu đến game hiện tại
    private final Game game;

    // Lưu các hiệu ứng và thời gian tương ứng để hủy khi hết thời gian
    private final Map<PowerUp, Timer> activeTimers = new HashMap<>();

    // ConSstructor
    public PowerUpManager(Game game) {
        this.game = game;
    }

    public void apply(PowerUp pu) {
        if (pu == null) return;

        // Áp dụng hiệu ứng
        pu.applyEffect();

        // Âm thanh khi bắt được
        new Sound("sound/click.wav").play();

        // Nếu là hiệu ứng tác dụng tạm thời, tạo Timer để tự động gọi RemoveEffect
        if (pu.getDuration() > 0) {
            Timer timer = new Timer((int) pu.getDuration(), ev -> pu.removeEffect());
            timer.setRepeats(false);    // Chỉ chạy 1 lần
            timer.start();  // Thời gian tính từ lúc chạy

            // Lưu power Up và Timer để quản lý
            activeTimers.put(pu, timer);
        }
    }
}
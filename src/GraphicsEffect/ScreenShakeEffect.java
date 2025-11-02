package GraphicsEffect;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

// Rung màn hình
public class ScreenShakeEffect {

    public static void shake(Window window, int duration, int strength) {
        int delay = 20 ;    // Chu kì (ms)
        ShakeAction shake = new ShakeAction(window, delay, duration, strength);
        shake.start();  // Bắt đầu rung
    }
}

package GraphicsEffect;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

/**
 * Lớp tạo rung.
 */
public class ScreenShakeEffect {

    public static void shake(Window window, int duration, int strength) {
        int delay = 20 ;    // tần số rung
        ShakeAction shake = new ShakeAction(window, delay, duration, strength);
        shake.start();  // bắt đầu rung
    }
}

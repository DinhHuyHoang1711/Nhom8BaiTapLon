package GraphicsEffect;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

public class ScreenShakeEffect {

    public static void shake(Window window, int duration, int strength) {
        int delay = 20 ;
        ShakeAction shake = new ShakeAction(window, delay, duration, strength);
        shake.start();
    }
}

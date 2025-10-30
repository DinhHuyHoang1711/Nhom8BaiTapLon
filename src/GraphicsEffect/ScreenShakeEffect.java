package GraphicsEffect;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

public class ScreenShakeEffect {
    public static void shake (Window window, int duration, int strength) {
        int currentPosOfX = window.getX();
        int currentPosOfY = window.getY();

        int delay = 20;
        int totalTimes = duration/delay;
        final int total = totalTimes;

        class Counter {
            int value = total;
        }
        Counter counter = new Counter();

        final Timer[] timer = new Timer[1];

        timer[0] = new Timer(delay, e ->{
            if(counter.value > 0){
                counter.value--;

                int offsetX =(int) (Math.random() * strength - strength /2);
                int offsetY = (int)(Math.random() * strength - strength /2);

                window.setLocation(currentPosOfX + offsetX, currentPosOfY + offsetY);
            }else {
                timer[0].stop();
                window.setLocation(currentPosOfX,currentPosOfY);
            }
        });
        timer[0].start();
    }
}

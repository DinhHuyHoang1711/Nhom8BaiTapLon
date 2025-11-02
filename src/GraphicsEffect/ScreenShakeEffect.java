package GraphicsEffect;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

/**
 * Lớp ScreenShakeEffect chịu trách nhiệm tạo hiệu ứng rung màn hình (screen shake)
 * cho một cửa sổ (Window) trong một khoảng thời gian nhất định.
 */
public class ScreenShakeEffect {

    /**
     * Kích hoạt hiệu ứng rung màn hình trên một cửa sổ cụ thể.
     */
    public static void shake(Window window, int duration, int strength) {
        // Khoảng thời gian giữa các lần cập nhật vị trí
        int delay = 20 ;

        // Tạo đối tượng ShakeAction chịu trách nhiệm cập nhật vị trí cửa sổ liên tục
        ShakeAction shake = new ShakeAction(window, delay, duration, strength);

        // Bắt đầu chạy hiệu ứng
        shake.start();
    }
}

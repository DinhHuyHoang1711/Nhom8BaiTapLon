package GraphicsEffect;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

<<<<<<< HEAD
// Rung màn hình
=======
/**
 * Lớp ScreenShakeEffect chịu trách nhiệm tạo hiệu ứng rung màn hình (screen shake)
 * cho một cửa sổ (Window) trong một khoảng thời gian nhất định.
 */
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
public class ScreenShakeEffect {

    /**
     * Kích hoạt hiệu ứng rung màn hình trên một cửa sổ cụ thể.
     */
    public static void shake(Window window, int duration, int strength) {
<<<<<<< HEAD
        int delay = 20 ;    // Chu kì (ms)
        ShakeAction shake = new ShakeAction(window, delay, duration, strength);
        shake.start();  // Bắt đầu rung
=======
        // Khoảng thời gian giữa các lần cập nhật vị trí
        int delay = 20 ;

        // Tạo đối tượng ShakeAction chịu trách nhiệm cập nhật vị trí cửa sổ liên tục
        ShakeAction shake = new ShakeAction(window, delay, duration, strength);

        // Bắt đầu chạy hiệu ứng
        shake.start();
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
    }
}

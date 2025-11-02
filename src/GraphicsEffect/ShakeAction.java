package GraphicsEffect;

import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Lớp ShakeAction chịu trách nhiệm tạo hiệu ứng rung cho một cửa sổ (Window)
 * bằng cách dịch chuyển vị trí của nó một khoảng ngẫu nhiên trong thời gian ngắn.
 */
public class ShakeAction implements ActionListener {

    /**
     * Cửa sổ sẽ bị tác động hiệu ứng rung.
     */
    private final Window window;

    /**
     * Bộ hẹn giờ để cập nhật hiệu ứng theo mỗi chu kỳ (delay ms).
     */
    private final Timer timer;

    /**
     * Thời gian (ms) giữa các lần rung
     */
    private final int delay;

    /**
     * Vị trí ban đầu theo trục X của cửa sổ
     */
    private final int currentOfPosX;

    /**
     * Vị trí ban đầu theo trục Y của cửa sổ.
     */
    private final int currentOfPosY;

    /**
     * Độ mạnh của rung
     */
    private final int strength;

    /**
     * Số lần cập nhật còn lại trước khi dừng hiệu ứng.
     */
    private int remaining;

    /**
     * Khởi tạo hiệu ứng rung màn hình.
     */
    public ShakeAction(Window window, int delay, int duration, int strength) {
        this.window = window;
        this.delay = delay;
        this.strength = strength;

        // Lưu vị trí ban đầu của cửa sổ để khôi phục
        this.currentOfPosX = window.getX();
        this.currentOfPosY = window.getY();
        ;

        // Số chu kỳ rung = duration / delay
        this.remaining = duration / delay;

        // Timer sẽ gọi hàm actionPerformed() mỗi delay ms
        this.timer = new Timer(delay, this);
    }

    /**
     * Bắt đầu hiệu ứng rung màn hình.
     */
    public void start() {
        timer.start();
    }

    /**
     * Hàm này được gọi tự động mỗi khi Timer "tick".
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (remaining > 0) {
            remaining--;

            // Tạo độ lệch ngẫu nhiên trong khoảng [-strength/2, +strength/2]
            int newPosX = (int) (Math.random() * strength - strength / 2);
            int newPosY = (int) (Math.random() * strength - strength / 2);

            // Cập nhật lại vị trí cửa sổ so với vị trí ban đầu
            window.setLocation(currentOfPosX + newPosX, currentOfPosY + newPosY);
        }
        // Hết thời gian rung → dừng timer và trả cửa sổ về đúng vị trí
        else {
            timer.stop();
            window.setLocation(currentOfPosX, currentOfPosY);
        }
    }
}

package GachaMachine;

import arkanoid.Sound;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Lớp MyTimerListener chịu trách nhiệm xử lý sự kiện mỗi khi Timer kích hoạt (từng "tick").
 * Mục đích chính: điều khiển hiệu ứng quay của vòng quay Gacha.
 */
public class MyTimerListener implements ActionListener {
    /**
     * Biến đếm số lần Timer đã kích hoạt, giúp xác định khi nào nên dừng hiệu ứng quay
     */
    private int counter = 0;

    /**
     * Tham chiếu đến đối tượng SpinAnimation để điều khiển giao diện vòng quay
     */
    private SpinAnimation spinAnimation;

    /**
     * Âm thanh quay Gacha phát mỗi khi chuyển sang ô tiếp theo
     */
    private Sound spinsound = new Sound("sound/gachaSound.wav");

    /**
     * Hàm khởi tạo (Constructor) nhận vào SpinAnimation để tương tác với vòng quay.
     *
     * @param spinAnimation đối tượng dùng để điều khiển và cập nhật hiệu ứng quay
     */
    public MyTimerListener(SpinAnimation spinAnimation) {
        this.spinAnimation = spinAnimation;
    }

    /**
     * Hàm được gọi tự động mỗi khi Timer "tick".
     * Dùng để cập nhật hiệu ứng quay, phát âm thanh và kiểm tra điều kiện dừng.
     *
     * @param e sự kiện hành động (ActionEvent) được Timer gửi tới
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Tăng highlightIndex rồi yêu cầu panel vẽ lại
        spinAnimation.highlightNext();

        // Dừng âm thanh trước (nếu đang phát) để tránh chồng lặp
        spinsound.stop();

        // Phát lại âm thanh cho mỗi lần chuyển ô
        spinsound.play();

        // Tăng biến đếm để biết đã quay được bao nhiêu bước
        counter++;

        // Kiểm tra điều kiện dừng
        if (counter > spinAnimation.getNumTiles() * 5
                && spinAnimation.isStopPosition()) {
            spinAnimation.stopSpin();
        }
    }
}


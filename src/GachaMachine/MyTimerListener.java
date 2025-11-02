package GachaMachine;

import arkanoid.Sound;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MyTimerListener implements ActionListener {
    private int counter = 0;
    private SpinAnimation spinAnimation; // Tham chiếu đến đối tượng quay
    private Sound spinsound = new Sound("sound/gachaSound.wav");   // Âm thanh khi quay qua từng ô

    // Khởi tạo listener với đối tượng SpinAnimation liên kết
    public MyTimerListener(SpinAnimation spinAnimation) {
        this.spinAnimation = spinAnimation;
    }

    // Được goij mỗi khi tick (1 chu kì)
    @Override
    public void actionPerformed(ActionEvent e) {
        // Tăng highlightIndex rồi yêu cầu panel vẽ lại
        spinAnimation.highlightNext();
        spinsound.stop();
        spinsound.play();

        counter++;

        // Kiểm tra điều kiện dừng
        if (counter > spinAnimation.getNumTiles() * 5
                && spinAnimation.isStopPosition()) {
            spinAnimation.stopSpin();  // Dừng
        }
    }
}


package GachaMachine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MyTimerListener implements ActionListener {
    private int counter = 0;
    private SpinAnimation spinAnimation;

    public MyTimerListener(SpinAnimation spinAnimation) {
        this.spinAnimation = spinAnimation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Tăng highlightIndex rồi yêu cầu panel vẽ lại
        spinAnimation.highlightNext();

        counter++;

        // Kiểm tra điều kiện dừng
        if (counter > spinAnimation.getNumTiles() * 5
                && spinAnimation.isStopPosition()) {
            spinAnimation.stopSpin();
        }
    }
}


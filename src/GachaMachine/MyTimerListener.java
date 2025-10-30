package GachaMachine;

import arkanoid.Sound;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MyTimerListener implements ActionListener {
    private int counter = 0;
    private SpinAnimation spinAnimation;
    private Sound spinsound = new Sound("sound/click.wav");

    public MyTimerListener(SpinAnimation spinAnimation) {
        this.spinAnimation = spinAnimation;
    }

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
            spinAnimation.stopSpin();
        }
    }
}


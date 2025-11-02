package GraphicsEffect;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShakeAction implements ActionListener {
    private final Window window;    // Cửa số rung
    private final Timer timer;  // Bộ đếm thơif gian rung
    private final int delay;     // chu kì

    // Vị trí x, y gốc
    private final int currentOfPosX;
    private final int currentOfPosY;
    private final int strength; // Biên độ rung
    private int remaining ; // Số lần cập nhật còn lại

    /**
     * Constructor.
     */
    public ShakeAction(Window window, int delay ,int duration, int strength){
        this.window = window;
        this.delay = delay;
        this.strength = strength;
        this.currentOfPosX = window.getX();
        this.currentOfPosY = window.getY();;
        this.remaining = duration / delay;

        this.timer = new Timer(delay, this);
    }

    /**
     * goi den de bat dau rung.
     */
    public void start(){
        timer.start();
    }

    /**
     * Nhan hanh dong va thuc hien rung cho toi khi gap else.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(remaining > 0) {
            remaining--;
            int newPosX = (int)(Math.random() * strength - strength/2);
            int newPosY = (int)(Math.random() * strength - strength/2);

            // di chuyển đến vị trí lêch
            window.setLocation(currentOfPosX + newPosX, currentOfPosY + newPosY);
        }else{
            timer.stop();
            window.setLocation(currentOfPosX, currentOfPosY);   // Trả vị trí ban đầu
        }
    }
}

package GraphicsEffect;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShakeAction implements ActionListener {
    private final Window window;
    private final Timer timer;
    private final int delay;
    private final int currentOfPosX;
    private final int currentOfPosY;
    private final int strength;
    private int remaining ;

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

            window.setLocation(currentOfPosX + newPosX, currentOfPosY + newPosY);
        }else{
            timer.stop();
            window.setLocation(currentOfPosX, currentOfPosY);
        }
    }
}

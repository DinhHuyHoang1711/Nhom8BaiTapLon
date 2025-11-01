package arkanoid;

import javax.swing.*;
import java.awt.*;

public class LaserBeam extends JPanel {
    private int width = 10;
    private int height;
    private Color mainColor = new Color(255, 215, 0);
    private Color glowColor = new Color(255, 165, 0, 120);

    public LaserBeam(int x, int y, int width, int height) {
        this.height = height;
        this.width = width;
        setBounds(x, y - height, width, height);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Tia laze
        g2d.setColor(mainColor);
        g2d.fillRect(0, 0, width, height);

        //Hieu ung
        g2d.setColor(glowColor);
        g2d.fillRect(-5, 0, width + 10, height);

        g2d.dispose();
    }
}
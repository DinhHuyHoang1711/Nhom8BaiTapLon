package arkanoid;

import javax.swing.*;
import java.awt.*;

// Item Laser
public class LaserBeam extends JPanel {

    // Kích thước (tầm hoạt động)
    private int width = 10;
    private int height;

    // Màu
    private Color mainColor = new Color(255, 215, 0);
    private Color glowColor = new Color(255, 165, 0, 120);

    // Constructor khởi tạo vị trí
    public LaserBeam(int x, int y, int width, int height) {
        this.height = height;
        this.width = width;
        setBounds(x, y - height, width, height);
        setOpaque(false);
    }

    // Vẽ tia laser
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Tia laser
        g2d.setColor(mainColor);
        g2d.fillRect(0, 0, width, height);

        //Hieu ung
        g2d.setColor(glowColor);
        g2d.fillRect(-5, 0, width + 10, height);

        // Giải phóng tài nguyên
        g2d.dispose();
    }
}
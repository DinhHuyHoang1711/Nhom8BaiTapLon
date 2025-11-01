package arkanoid;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class ArtifactSlot extends JPanel {
    private Image image;
    private int cooldownRemaining = 0;

    public ArtifactSlot(Image image) {
        this.image = image;
        setOpaque(false);
        setPreferredSize(new Dimension(60, 60));
    }

    public void setImage(Image img) {
        this.image = img;
        repaint();
    }

    public void setCooldown(int seconds) {
        this.cooldownRemaining = seconds;
        repaint();
    }

    public void tickCooldown() {
        if (cooldownRemaining > 0) {
            cooldownRemaining--;
            repaint();
        }
    }

    public boolean isCoolingDown() {
        return cooldownRemaining > 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        //Ve nen xam nhat co bo goc
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRoundRect(0, 0, w, h, 10, 10);

        //Ve vien xam dam
        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(1, 1, w - 3, h - 3, 10, 10);

        //ve hinh anh item
        if (image != null) {
            int padding = 3;
            g2d.drawImage(image, padding, padding, w - 2 * padding, h - 2 * padding, this);
        }

        //Ve chu E de con biet an nut E
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.setColor(new Color(50, 50, 50));
        g2d.drawString("E", 1, 12);

        //dang cooldown thi phu mau den
        if (cooldownRemaining > 0) {
            // Lớp mờ đen phủ lên
            g2d.setColor(new Color(0, 0, 0, 140));
            g2d.fillRoundRect(0, 0, w, h, 10, 10);

            // Hiển thị số cooldown
            g2d.setFont(new Font("Adobe Garamond Pro", Font.BOLD, 24));
            g2d.setColor(Color.WHITE);
            String text = String.valueOf(cooldownRemaining);
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (w - fm.stringWidth(text)) / 2;
            int textY = (h + fm.getAscent()) / 2 - 5;
            g2d.drawString(text, textX, textY);
        }

        g2d.dispose();
    }
}
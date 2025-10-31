package arkanoid;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class FireballLayer extends JComponent {
    private BufferedImage fbImg;
    private List<? extends arkanoid.GameObject> listRef; // tham chiếu danh sách từ Boss

    public FireballLayer(int w, int h) {
        setOpaque(false);
        setBounds(0, 0, w, h);
        try {
            BufferedImage raw = ImageIO.read(new File("img/Boss/FireBall.png"));
            // pre-scale và chuyển về ảnh compatible cho GPU
            BufferedImage scaled = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = scaled.createGraphics();
            g2.drawImage(raw, 0, 0, 100, 100, null);
            g2.dispose();
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration();
            fbImg = gc.createCompatibleImage(100, 100, Transparency.TRANSLUCENT);
            g2 = fbImg.createGraphics();
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();
        } catch (Exception e) {
            System.err.println("Load fireball image failed: " + e.getMessage());
        }
    }

    public void setProjectiles(List<? extends arkanoid.GameObject> ref) {
        this.listRef = ref;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fbImg == null || listRef == null) return;
        Graphics2D g2 = (Graphics2D) g;
        // không bật interpolation, vẽ tại tọa độ nguyên
        for (int i = 0, n = listRef.size(); i < n; i++) {
            arkanoid.GameObject f = listRef.get(i);
            g2.drawImage(fbImg, f.getX(), f.getY(), 100, 100, null);
        }
    }
}

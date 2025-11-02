package arkanoid;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Lớp ShurikenLayer vẽ các phi tiêu do Boss tạo ra
public class ShurikenLayer extends JComponent {
    private final Image shurikenSprite; // ảnh Shuriken
    private Boss boss; // source of shurikens

    // Constructor
    public ShurikenLayer(Image shurikenSprite) {
        this.shurikenSprite = shurikenSprite;
        setOpaque(false);
    }

    // Liên kết với Boss
    public void bindBoss(Boss b) {
        this.boss = b;
    }

    /** Tháo liên kết và dọn tài nguyên. Gọi khi boss chết hoặc rời màn. */
    public void unbind() {
        this.boss = null;
        repaint();
    }

    // Vẽ danh sách Shuriken
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (boss == null) return;
        List<ShurikenWind> list = boss.getShurikens(); // Lấy danh sách
        if (list == null) return;

        // Tạo bản sao 2D để vẽ
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Duyệt danh sách shuriken đang tồn tại
        for (int i = 0; i < list.size(); i++) {
            ShurikenWind s = list.get(i);
            if (s.isDead()) continue;
            s.paintRotating(g2d, shurikenSprite);
        }

        // Giải phóng tài nguyên
        g2d.dispose();
    }
}

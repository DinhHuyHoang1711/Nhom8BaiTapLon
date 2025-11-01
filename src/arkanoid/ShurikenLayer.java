package arkanoid;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShurikenLayer extends JComponent {
    private final Image shurikenSprite;
    private Boss boss; // source of shurikens

    public ShurikenLayer(Image shurikenSprite) {
        this.shurikenSprite = shurikenSprite;
        setOpaque(false);
    }

    public void bindBoss(Boss b) {
        this.boss = b;
    }

    /** Tháo liên kết và dọn tài nguyên. Gọi khi boss chết hoặc rời màn. */
    public void unbind() {
        this.boss = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (boss == null) return;
        List<ShurikenWind> list = boss.getShurikens();
        if (list == null) return;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (int i = 0; i < list.size(); i++) {
            ShurikenWind s = list.get(i);
            if (s.isDead()) continue;
            s.paintRotating(g2d, shurikenSprite);
        }
        g2d.dispose();
    }
}

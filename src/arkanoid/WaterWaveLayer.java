package arkanoid;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class WaterWaveLayer extends JComponent {
    private List<? extends GameObject> waves = Collections.emptyList();
    private Image sprite;                       // sprite chung (khuyến nghị)
    private final Map<String, Image> cache = new HashMap<>(); // cache nếu vẽ theo imagePath

    public WaterWaveLayer(Image sprite) {
        this.sprite = sprite;
        setOpaque(false);
    }

    /** Đổi sprite chung nếu cần. */
    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    /** Cập nhật danh sách wave để vẽ. Gọi mỗi tick trước repaint. */
    public void setWaves(List<? extends GameObject> waves) {
        this.waves = (waves == null) ? Collections.emptyList() : waves;
    }

    /** Xoá danh sách wave đang hiển thị. */
    public void clear() {
        this.waves = Collections.emptyList();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (waves == null || waves.isEmpty()) return;

        // Ưu tiên vẽ bằng sprite chung (nhanh nhất)
        if (sprite != null) {
            for (GameObject w : waves) {
                g.drawImage(sprite, w.getX(), w.getY(), w.getWidth(), w.getHeight(), null);
            }
            return;
        }

        // Fallback: vẽ theo imagePath của từng GameObject (có cache)
        for (GameObject w : waves) {
            String path = w.getImagePath();
            if (path == null) continue;
            Image img = cache.computeIfAbsent(path, p -> new ImageIcon(p).getImage());
            g.drawImage(img, w.getX(), w.getY(), w.getWidth(), w.getHeight(), null);
        }
    }
}

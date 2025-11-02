package arkanoid;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class FireballLayer extends JComponent {

    /**
     * Ảnh sprite (đã được scale và GPU optimize) của quả cầu lửa.
     */
    private BufferedImage fbImg;

    /**
     * Danh sách tham chiếu đến các đối tượng đạn lửa (thường do Boss tạo ra).
     */
    private List<? extends arkanoid.GameObject> listRef; // tham chiếu danh sách từ Boss

    /**
     * Khởi tạo một lớp hiển thị hiệu ứng cầu lửa với kích thước xác định.
     * <p>
     * Trong quá trình khởi tạo, ảnh {@code FireBall.png} sẽ được tải, scale trước
     * và chuyển sang dạng ảnh tương thích GPU để tăng tốc độ vẽ.
     *
     * @param w Chiều rộng của vùng vẽ
     * @param h Chiều cao của vùng vẽ
     */
    public FireballLayer(int w, int h) {
        // Thiết lập không vẽ nền để có thể phủ lên các lớp khác
        setOpaque(false);
        setBounds(0, 0, w, h);
        try {
            // === Tải ảnh gốc ===
            BufferedImage raw = ImageIO.read(new File("img/Boss/FireBall.png"));

            // pre-scale và chuyển về ảnh compatible cho GPU
            BufferedImage scaled = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = scaled.createGraphics();
            g2.drawImage(raw, 0, 0, 100, 100, null);
            g2.dispose();

            // Tối ưu hóa ảnh để tương thích GPU (VRAM-friendly)
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

    /**
     * Cập nhật danh sách các viên cầu lửa cần được vẽ trên màn hình.
     *
     * @param ref Danh sách {@link GameObject} đại diện cho các viên cầu lửa
     */
    public void setProjectiles(List<? extends arkanoid.GameObject> ref) {
        this.listRef = ref;
    }

    /**
     * Vẽ lại toàn bộ các viên cầu lửa trên màn hình.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Nếu chưa có ảnh hoặc danh sách, không vẽ gì
        if (fbImg == null || listRef == null) return;

        Graphics2D g2 = (Graphics2D) g;

        // không bật interpolation, vẽ tại tọa độ nguyên
        for (int i = 0, n = listRef.size(); i < n; i++) {
            arkanoid.GameObject f = listRef.get(i);
            g2.drawImage(fbImg, f.getX(), f.getY(), 100, 100, null);
        }
    }
}

package arkanoid;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class ArtifactSlot extends JPanel {
    /**
     * Ảnh đại diện cho vật phẩm (icon)
     */
    private Image image;
    /**
     * Số giây hồi chiêu còn lại, 0 nếu đã sẵn sàng dùng
     */
    private int cooldownRemaining = 0;

    /**
     * Khởi tạo một ArtifactSlot với hình ảnh ban đầu.
     */
    public ArtifactSlot(Image image) {
        this.image = image;
        setOpaque(false);
        setPreferredSize(new Dimension(60, 60));
    }

    /**
     * Thay đổi hình ảnh hiển thị của ô.
     */
    public void setImage(Image img) {
        this.image = img;
        repaint();
    }

    /**
     * Cài đặt thời gian hồi chiêu (theo giây).
     */
    public void setCooldown(int seconds) {
        this.cooldownRemaining = seconds;
        repaint();
    }

    /**
     * Giảm cooldown mỗi 1 giây (được gọi ở vòng lặp game hoặc Timer ngoài).
     */
    public void tickCooldown() {
        if (cooldownRemaining > 0) {
            cooldownRemaining--;
            repaint();
        }
    }

    /**
     * Kiểm tra slot có đang trong trạng thái hồi chiêu hay không.
     *
     * @return true nếu đang cooldown, false nếu đã sẵn sàng dùng
     */
    public boolean isCoolingDown() {
        return cooldownRemaining > 0;
    }

    /**
     * Ghi đè phương thức vẽ của JPanel để:
     * - Vẽ khung, nền, ảnh item.
     * - Vẽ ký tự "E".
     * - Nếu cooldown -> phủ màu đen và hiển thị số giây.
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(); // Chiều rộng slot
        int h = getHeight(); // Chiều cao slot

        // 1. Vẽ nền xám nhạt có bo góc
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRoundRect(0, 0, w, h, 10, 10);

        // 2. Vẽ đường viền xám đậm
        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(1, 1, w - 3, h - 3, 10, 10);

        // 3. Vẽ hình ảnh vật phẩm vào giữa slot
        if (image != null) {
            int padding = 3; // khoảng cách với viền
            g2d.drawImage(image, padding, padding, w - 2 * padding, h - 2 * padding, this);
        }

        // 4. Vẽ ký tự "E" để nhắc phím kích hoạt
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.setColor(new Color(50, 50, 50));
        g2d.drawString("E", 1, 12);

        // 5. Nếu đang cooldown -> phủ lớp mờ + hiển thị số giây còn lại
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

        g2d.dispose();// Giải phóng tài nguyên
    }
}
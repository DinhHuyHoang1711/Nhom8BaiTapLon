package arkanoid;

import javax.swing.*;
import java.awt.event.*;

import arkanoid.Sound;

/**
 * Lớp tiện ích quản lý việc tạo các JButton bằng hình ảnh
 * Dùng chung cho Menu, MapMenu, v.v...
 */
public class ButtonManager {

    /**
     * Tạo nút hình ảnh
     *
     * @param imagePath đường dẫn ảnh chính
     * @param hoverPath đường dẫn ảnh khi hover
     * @param x         tọa độ X
     * @param y         tọa độ Y
     * @param click     âm thanh click
     * @param action    hành động khi nhấn nút
     * @return JButton đã cấu hình
     */
    public static JButton createImageButton(String imagePath, String hoverPath,
                                            int x, int y, Sound click, ActionListener action) {

        ImageIcon icon = new ImageIcon(imagePath);
        JButton button = new JButton(icon);
        button.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());

        // loại bỏ viền và nền
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        // hover
        if (hoverPath != null) {
            ImageIcon hoverIcon = new ImageIcon(hoverPath);
            button.addMouseListener(new MouseAdapter() {

                /**
                 * Sự kiện khi chuột di chuyển vào vùng nút.
                 *
                 * @param e Đối tượng {@link MouseEvent} chứa thông tin chuột
                 */
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setIcon(hoverIcon);
                }

                /**
                 * Sự kiện khi chuột rời khỏi vùng nút.
                 *
                 * @param e Đối tượng {@link MouseEvent} chứa thông tin chuột
                 */
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setIcon(icon);
                }
            });
        }

        // click
        button.addActionListener(e -> {
            // Phát âm thanh click nếu có
            if (click != null) click.play();

            // Gọi hành động do người dùng chỉ định
            action.actionPerformed(e);
        });

        return button;
    }
}

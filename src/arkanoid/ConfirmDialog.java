package arkanoid;

import javax.swing.*;
import java.awt.*;

public class ConfirmDialog extends JFrame {

    /**
     * Chiều rộng mặc định của hộp thoại xác nhận.
     */
    public static final int CONFIRM_WIDTH = 450;

    /**
     * Chiều cao mặc định của hộp thoại xác nhận.
     */
    public static final int CONFIRM_HEIGHT = 200;

    /**
     * Âm thanh phát khi người dùng nhấn nút trong hộp thoại.
     */
    private final Sound clickSound;

    /**
     * Ảnh nền của hộp thoại xác nhận.
     */
    private final Image confirmImg;

    /**
     * Quả bóng được người chơi chọn để xác nhận.
     */
    private final Ball selectedBall;

    /**
     * Quản lý thông tin vật phẩm mà người chơi sở hữu, bao gồm bóng hiện tại.
     */
    private final OwnedManager ownedManager;

    /**
     * Khởi tạo một hộp thoại xác nhận cho phép người chơi quyết định có chọn quả bóng mới hay không.
     *
     * @param ownedManager Đối tượng {@link OwnedManager} quản lý bóng hiện tại của người chơi
     * @param selectedBall Quả bóng mà người chơi vừa chọn để xác nhận
     */
    public ConfirmDialog(OwnedManager ownedManager, Ball selectedBall) {
        this.ownedManager = ownedManager;
        this.selectedBall = selectedBall;

        // === Cấu hình cửa sổ ===
        setTitle("Bạn có chắc muốn chọn bóng này không?");
        setSize(CONFIRM_WIDTH, CONFIRM_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setAlwaysOnTop(true);

        // === Tải âm thanh click và ảnh nền ===
        clickSound = new Sound("sound/click.wav");
        confirmImg = new ImageIcon("img/confirm.png").getImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(confirmImg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Hiển thị hình bóng đang chọn
        ImageIcon ballIcon = new ImageIcon(selectedBall.getImagePath());
        Image scaled = ballIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel ballLabel = new JLabel(new ImageIcon(scaled));
        ballLabel.setBounds((CONFIRM_WIDTH - 100) / 2, 20, 80, 80);
        panel.add(ballLabel);

        // Nút YES → cập nhật currentBall và mở lại BallUI
        panel.add(ButtonManager.createImageButton(
                "img/Yes_button.png", null, 60, 100, clickSound,
                e -> {
                    ownedManager.setCurrentBall(selectedBall);
                    dispose(); // đóng ConfirmDialog

                }
        ));

        // Nút NO → chỉ đóng hộp thoại
        panel.add(ButtonManager.createImageButton(
                "img/No_button.png", null, 300, 100, clickSound, e -> dispose()
        ));

        // === Gán panel vào nội dung chính của frame ===
        setContentPane(panel);
        setVisible(true);
    }
}

package arkanoid;

import javax.swing.*;
import java.awt.*;
import arkanoid.Sound;
import arkanoid.OwnedManager;
import arkanoid.Ball;
import arkanoid.Artifact;
import arkanoid.ButtonManager;

public class ConfirmDialog extends JFrame {

    public static final int CONFIRM_WIDTH = 450;
    public static final int CONFIRM_HEIGHT = 200;

    private final Sound clickSound;
    private final Image confirmImg;
    private final Ball selectedBall;
    private final OwnedManager ownedManager;

    public ConfirmDialog(OwnedManager ownedManager, Ball selectedBall) {
        this.ownedManager = ownedManager;
        this.selectedBall = selectedBall;

        setTitle("Bạn có chắc muốn chọn bóng này không?");
        setSize(CONFIRM_WIDTH, CONFIRM_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setAlwaysOnTop(true);

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

        setContentPane(panel);
        setVisible(true);
    }
}

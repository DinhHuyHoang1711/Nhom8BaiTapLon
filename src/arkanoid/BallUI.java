package arkanoid;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import arkanoid.*;

import static arkanoid.GameObject.GAME_HEIGHT;
import static arkanoid.GameObject.GAME_WIDTH;

/**
 * Giao diện quản lý bộ sưu tập bóng của người chơi.
 * Hiển thị các bóng sở hữu, cho phép trang bị bóng mới và xem bóng hiện tại.
 */
public class BallUI extends JFrame {

    /**
     * Quản lý các bóng người chơi sở hữu
     */
    private final OwnedManager ownedManager;

    /**
     * Âm thanh khi nhấn nút
     */
    private Sound clickSound;

    /**
     * Hình nền danh sách bóng
     */
    private Image ballListImg;

    /**
     * Danh sách tất cả các bóng
     */
    private final List<Ball> ballList;

    /**
     * Nhãn hiển thị bóng đang trang bị
     */
    private JLabel currentBallLabel;

    /**
     * Tham chiếu đến màn hình BaloUI cha
     */
    private BaloUI parentBalo;

    /**
     * Kích thước hiển thị icon bóng
     */
    private static final int BALL_SIZE = 100;

    /**
     * Constructor tạo giao diện BallUI.
     *
     * @param parentBalo   màn hình cha BaloUI
     * @param ownedManager quản lý bóng sở hữu
     * @param ballList     danh sách tất cả các bóng
     */
    public BallUI(BaloUI parentBalo, OwnedManager ownedManager, List<Ball> ballList) {
        this.parentBalo = parentBalo;
        this.ownedManager = ownedManager;
        this.ballList = ballList;
        initUI();
    }

    /**
     * Khởi tạo giao diện chính, bao gồm:
     * - Thiết lập cửa sổ
     * - Tải âm thanh và hình nền
     * - Hiển thị nút Back
     * - Hiển thị bóng hiện tại và danh sách bóng
     */
    private void initUI() {
        setTitle("Bộ Sưu Tập Bóng");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        clickSound = new Sound("sound/click.wav");
        ballListImg = new ImageIcon("img/list.jpg").getImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ballListImg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Nút Back
        JButton back = ButtonManager.createImageButton(
                "img/back_button.png", "img/back_hover.png",
                10, 550, clickSound, e -> {
                    this.dispose();
                    parentBalo.setVisible(true);
                });
        panel.add(back);

        setContentPane(panel);
        setVisible(true);

        showCurrentBall(panel);
        loadBallsToPanel(panel);
    }

    /**
     * Hiển thị bóng hiện tại đang được trang bị.
     *
     * @param panel panel chính để thêm nhãn bóng
     */
    private void showCurrentBall(JPanel panel) {
        Ball currentBall = ownedManager.getCurrentBall();
        ImageIcon icon = new ImageIcon(currentBall.getImagePath());
        Image currentBallImg = icon.getImage().getScaledInstance(BALL_SIZE * 2, BALL_SIZE * 2, Image.SCALE_SMOOTH);

        currentBallLabel = new JLabel(new ImageIcon(currentBallImg));
        currentBallLabel.setBounds(GAME_WIDTH / 2 - BALL_SIZE, 200, BALL_SIZE * 2, BALL_SIZE * 2);
        panel.add(currentBallLabel);
    }

    /**
     * Hiển thị tất cả bóng trong danh sách dưới dạng các nút bấm.
     * Người chơi có thể chọn để trang bị bóng.
     *
     * @param panel panel chính để thêm các nút bóng
     */
    private void loadBallsToPanel(JPanel panel) {
        List<Ball> balls = ballList;
        int startX = 320;
        int startY = 450;
        int gap = 15;
        int count = 0;

        for (Ball ball : balls) {
            ImageIcon icon = new ImageIcon(ball.getImagePath());
            Image img = icon.getImage().getScaledInstance(BALL_SIZE, BALL_SIZE, Image.SCALE_SMOOTH);
            JButton ballButton = new JButton(new ImageIcon(img));

            int x = startX + (BALL_SIZE + gap) * (count % 8);
            int y = startY + (count / 8) * (BALL_SIZE + gap);

            ballButton.setBounds(x, y, BALL_SIZE, BALL_SIZE);
            ballButton.setBorderPainted(false);
            ballButton.setContentAreaFilled(false);
            ballButton.setFocusPainted(false);
            ballButton.setOpaque(false);

            ballButton.addActionListener(e -> {
                clickSound.play();
                int choice = JOptionPane.showConfirmDialog(this,
                        "Trang bị bóng này?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    ownedManager.setCurrentBall(ball);
                    JOptionPane.showMessageDialog(this, "Đã trang bị bóng mới!");

                    // Cập nhật hình ảnh bóng mới
                    ImageIcon newIcon = new ImageIcon(ball.getImagePath());
                    Image newImg = newIcon.getImage().getScaledInstance(BALL_SIZE * 2, BALL_SIZE * 2, Image.SCALE_SMOOTH);
                    currentBallLabel.setIcon(new ImageIcon(newImg));
                    panel.repaint();
                }
            });

            panel.add(ballButton);
            count++;
        }

        panel.repaint();
    }

    /**
     * Hàm main để chạy thử giao diện BallUI.
     *
     * @param args tham số dòng lệnh
     */
    public static void main(String[] args) {
        OwnedManager ownedManager = new OwnedManager();
        new BallUI(null, ownedManager, ownedManager.getBalls());
    }
}

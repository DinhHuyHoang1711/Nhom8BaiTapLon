import javax.swing.*;
import java.awt.*;
import java.util.List;
import arkanoid.Sound;
import arkanoid.ButtonManager;
import arkanoid.Ball;
import arkanoid.OwnedManager;
import arkanoid.ConfirmDialog;

import static arkanoid.GameObject.GAME_HEIGHT;
import static arkanoid.GameObject.GAME_WIDTH;

public class BallUI extends JFrame {

    private final OwnedManager ownedManager;
    private Sound clickSound;
    private Image ballListImg;
    private final List<Ball> ballList;

    private static final int BALL_SIZE = 100;

    public BallUI(OwnedManager ownedManager, List<Ball> ballList) {
        this.ownedManager = ownedManager;
        this.ballList = ballList;
        initUI();
    }

    private void initUI() {
        setTitle("Bộ Sưu Tập Bóng");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        clickSound = new Sound("sound/click.wav");
        ballListImg = new ImageIcon("img/ball_List.png").getImage();

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
                    new BaloUI(ownedManager);
                });
        panel.add(back);

        setContentPane(panel);
        setVisible(true);

        showCurrentBall(panel);
        loadBallsToPanel(panel);
    }

    private void showCurrentBall(JPanel panel) {
        Ball currentBall = ownedManager.getCurrentBall();
        ImageIcon icon = new ImageIcon(currentBall.getImagePath());
        Image currentBallImg = icon.getImage().getScaledInstance(BALL_SIZE * 2, BALL_SIZE * 2, Image.SCALE_SMOOTH);

        JLabel currentBallLabel = new JLabel(new ImageIcon(currentBallImg));
        currentBallLabel.setBounds(GAME_WIDTH / 2 - BALL_SIZE, 250, BALL_SIZE * 2, BALL_SIZE * 2);
        panel.add(currentBallLabel);
    }

    private void loadBallsToPanel(JPanel panel) {
        List<Ball> balls = ballList;
        int startX = 300;
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
                new ConfirmDialog(ownedManager, ball);
            });

            panel.add(ballButton);
            count++;
        }

        panel.repaint();
    }

    public static void main(String[] args) {
        OwnedManager ownedManager = new OwnedManager();
        new BallUI(ownedManager, ownedManager.getBalls());
    }
}

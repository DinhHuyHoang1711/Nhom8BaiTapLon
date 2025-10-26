import javax.swing.*;
import java.awt.*;
import arkanoid.Sound;
import arkanoid.ButtonManager;
import arkanoid.OwnedManager;

import static arkanoid.GameObject.GAME_HEIGHT;
import static arkanoid.GameObject.GAME_WIDTH;

public class BaloUI extends JFrame {

    private Sound clickSound;
    private Image baloImg;
    private OwnedManager ownedManager;

    public BaloUI(OwnedManager ownedManager) {
        this.ownedManager = ownedManager;

        setTitle("Vật phầm đang sở hữu");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        clickSound = new Sound("sound/click.wav");
        baloImg = new ImageIcon("img/balo.jpg").getImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(baloImg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        panel.add(ButtonManager.createImageButton("img/balls.png", "img/balls_hover.png",
                600, 200, clickSound, e -> openBallList()));
        panel.add(ButtonManager.createImageButton("img/artifacts.png", "img/artifacts_hover.png",
                320, 500, clickSound, e -> openArtifactList()));

        // Nút Back
        JButton back = ButtonManager.createImageButton(
                "img/back_button.png", "img/back_hover.png",
                10, 550, clickSound, e -> {
                    this.dispose();
                    new MapMenu();
                });
        panel.add(back);

        setContentPane(panel);
        setVisible(true);
    }

    private void openBallList() {
        this.dispose();
        new BallUI(ownedManager, ownedManager.getBalls());
    }

    private void openArtifactList() {

    }

    public static void main(String[] args) {
        OwnedManager ownedManager = new OwnedManager();

        new BaloUI(ownedManager);
    }
}

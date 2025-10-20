import javax.swing.*;
import java.awt.*;
import arkanoid.Sound;
import arkanoid.ButtonManager;
import static arkanoid.GameObject.GAME_HEIGHT;
import static arkanoid.GameObject.GAME_WIDTH;

public class Menu extends JFrame {

    private Sound click;
    private Sound backgroundMusic;
    private Image backgroundImage;
    private Image arkanoidImage;

    public Menu() {
        setTitle("ARKANOID");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Sound
        click = new Sound("sound/click.wav");
        backgroundMusic = new Sound("sound/backgroundMusic.wav");
        backgroundMusic.loop(); //phat lien tuc

        backgroundImage = new ImageIcon("img/background_menu.png").getImage();
        arkanoidImage = new ImageIcon("img/arkanoid.png").getImage();

        //panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                g.drawImage(arkanoidImage, 300, 100, this);
            }
        };
        backgroundPanel.setLayout(null);

        // Nút Start
        JButton startBtn = ButtonManager.createImageButton(
                "img/start_button.png", "img/start_hover.png",
                450, 350, click, e -> startGame());
        backgroundPanel.add(startBtn);

        // Nút Exit
        JButton exitBtn = ButtonManager.createImageButton(
                "img/exit_button.png", "img/exit_hover.png",
                455, 500, click, e -> exitGame());
        backgroundPanel.add(exitBtn);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    // click start
    private void startGame() {
        this.dispose();
        new MapMenu();
    }

    // click exit
    private void exitGame() {
        click.play();
        System.exit(0);
    }

    // render
    public static void main(String[] args) {
        new Menu();
    }
}

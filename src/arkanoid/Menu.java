import javax.swing.*;
import java.awt.*;
import arkanoid.*;
import static arkanoid.GameObject.GAME_HEIGHT;
import static arkanoid.GameObject.GAME_WIDTH;

public class Menu extends JFrame {

    private Sound clickSound;
    //private Sound backgroundMusic;

    public Menu() {
        setTitle("ARKANOID");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // Âm thanh
        clickSound = new Sound("sound/click.wav");
        //backgroundMusic = new Sound("sound/backgroundMusic.wav");
        //backgroundMusic.loop();

        // Ảnh nền
        GameObject backgroundImg = new GameObject(0, 0, GAME_WIDTH, GAME_HEIGHT,
                0, 0, "img/background_menu.png");
        ObjectPrinter background = new ObjectPrinter(backgroundImg);
        background.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // Nút Start
        JButton startBtn = ButtonManager.createImageButton(
                "img/start_button.png", "img/start_hover.png",
                450, 350, clickSound, e -> startGame());

        // Nút Exit
        JButton exitBtn = ButtonManager.createImageButton(
                "img/exit_button.png", "img/exit_hover.png",
                455, 500, clickSound, e -> exitGame());

        // Panel chính
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);

        layeredPane.add(background, Integer.valueOf(0));
        layeredPane.add(startBtn, Integer.valueOf(1));
        layeredPane.add(exitBtn, Integer.valueOf(1));

        setContentPane(layeredPane);
        setVisible(true);
    }

    private void startGame() {
        this.dispose();
        new MapMenu();
    }

    private void exitGame() {
        clickSound.play();
        System.exit(0);
    }

    public static void main(String[] args) {
        new Menu();
    }
}

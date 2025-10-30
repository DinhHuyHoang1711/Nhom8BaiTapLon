import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import GachaMachine.DiamondSquareGacha;
import arkanoid.Sound;
import arkanoid.ButtonManager;
import arkanoid.Paddle;
import arkanoid.Ball;
import arkanoid.Brick;

import static arkanoid.GameObject.*;

public class MapMenu extends JFrame {

    //nhac nen mac dinh
    public static final Sound backgroundMusic = new Sound("sound/backgroundMusic.wav");

    private Sound click;
    private Image mapImage;

    //paddle hien tai dang trang bi
    private Paddle currentPaddle = new Paddle(PLAYFRAME_WIDTH / 2 - 60, 600, 120, 20,
            15, 0, "img/paddle/paddlevip.png");

    //bong hien tai dang trang bi
    private Ball currentBall = new Ball(PLAYFRAME_WIDTH / 2 - 30, GAME_HEIGHT - 120, Ball.BALL_SIZE, 6,
            -8, "img/ball/bongnguhanh.png", 25);


    private String currentGameScene = "img/Beach.jpg";

    //chuan bi bircks
    private ArrayList <Brick> lv1Bricks = new ArrayList<>();
    
    public MapMenu() {

        lv1Bricks.clear();
        lv1Bricks.addAll(Brick.buildLevel4Bricks());

        setTitle("SELECT MAP");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //khoi tao am thanh
        click = new Sound("sound/click.wav");
        //phat nhac nen
        backgroundMusic.loop();
        //khoi tao ban do the gioi
        mapImage = new ImageIcon("img/background.jpg").getImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Thêm các nút level
        panel.add(ButtonManager.createImageButton("img/button/1.png", null,
                200, 300, click, e -> openLevel(1)));
        panel.add(ButtonManager.createImageButton("img/button/2.png", null,
                450, 280, click, e -> openLevel(2)));
        panel.add(ButtonManager.createImageButton("img/button/3.png", null,
                700, 260, click, e -> openLevel(3)));
        panel.add(ButtonManager.createImageButton("img/button/4.png", null,
                950, 330, click, e -> openLevel(4)));

        // Nút Back
        JButton back = ButtonManager.createImageButton(
                "img/back_button.png", null,
                0, 550, click, e -> {
                    this.dispose();
                    new Menu();
                });
        panel.add(back);

        // Nút Gacha
        JButton gacha = ButtonManager.createImageButton(
                "img/gacha_button.png", "img/gacha_hover.png",
                1075, 550, click, e -> {
                    //JOptionPane.showMessageDialog(this, "Comming soon");
                    //this.dispose();
                    JFrame frame = new JFrame("Vòng Quay 12 Ô - Hình Thoi");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.getContentPane().add(new DiamondSquareGacha());
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
        );
        panel.add(gacha);

        // Nút Balo
        JButton balo = ButtonManager.createImageButton(
                "img/balo_button.png", "img/balo_hover.png",
                975, 550, click, e -> {
                    JFrame frame = new JFrame("Đồ đang sở hữu");
                    new BaloUI(new arkanoid.OwnedManager());
                }
        );
        panel.add(balo);

        setContentPane(panel);
        setVisible(true);
    }

    private void openLevel(int level) {
        //vao man choi thi tat bgm di
        backgroundMusic.stop();

        //copy current ball va paddle de chung no khong bi thay doi sau game()
        Paddle paddleCopy = new Paddle(currentPaddle);
        Ball ballCopy = new Ball(currentBall);

        new Game(paddleCopy, ballCopy, "levels/level1.txt" ,
                currentGameScene);
        //thoat khoi man choi thi bat
        //Game da implement window listener roi nen khi dong game, bgm tu phat lai
    }

    public static void main(String[] args) {
        new MapMenu();
    }
}

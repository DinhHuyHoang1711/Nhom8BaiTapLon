package arkanoid;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import GachaMachine.DiamondSquareGacha;
import GachaMachine.Item;

import static arkanoid.Game.*;

public class MapMenu extends JFrame {

    //nhac nen mac dinh
    public static final Sound backgroundMusic = new Sound("sound/backgroundMusic.wav");

    private Sound click;
    private Image mapImage;
    protected static final int TOTAL_LEVEL = 20;

    //Nhung gi nguoi choi so huu se nam trong lop nay
    private OwnedManager ownedManager = new OwnedManager();

    //paddle hien tai dang trang bi
    private Paddle currentPaddle = new Paddle(PLAYFRAME_WIDTH / 2 - 60, 600, 120, 20,
            15, 0, "img/paddle/paddlevip.png");

    //bong hien tai dang trang bi chinh la ownedManager.getCurrentBall()
    //item hien tai dang trang bi chinh la ownedManager.getCurrentItem()

    private String currentGameScene = "img/Beach.jpg";

    private java.util.List<Boolean> levelStatus = new java.util.ArrayList<>(java.util.Collections.nCopies(TOTAL_LEVEL + 1, false));

    //chuan bi bircks
    private ArrayList <Brick> lv1Bricks = new ArrayList<>();

    public MapMenu() {

        lv1Bricks.clear();
        lv1Bricks.addAll(Brick.buildLevel4Bricks());

        setTitle("SELECT MAP");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JButton exit = ButtonManager.createImageButton(
                "img/exit.png","img/exithover.png" ,
                10, 570, click, e -> {
                    this.dispose();
                    System.exit(0);
                });
        panel.add(exit);

        // Nút Gacha
        JButton gacha = ButtonManager.createImageButton(
                "img/gacha_button.png", "img/gacha_hover.png",
                1075, 550, click, e -> {
                    //JOptionPane.showMessageDialog(this, "Comming soon");
                    //this.dispose();
                    backgroundMusic.stop();
                    Sound gachaBgm = new Sound("sound/gachaBgm.wav");
                    gachaBgm.loop();
                    JFrame frame = new JFrame("Vòng Quay 12 Ô - Hình Thoi");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.getContentPane().add(new DiamondSquareGacha());
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    //sau khi tat gacha nhac nen se duoc bat lai
                    frame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            gachaBgm.close();
                            backgroundMusic.play();
                        }
                    });
                }
        );
        panel.add(gacha);

        // Nút Balo
        JButton balo = ButtonManager.createImageButton(
                "img/balo_button.png", "img/balo_hover.png",
                975, 550, click, e -> {
                    JFrame frame = new JFrame("Đồ đang sở hữu");
                    this.setVisible(false);
                    new BaloUI(this, ownedManager);
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
        Ball ballCopy = new Ball(ownedManager.getCurrentBall());
        Item itemCopy = new Item(ownedManager.getCurrentItem());

        String leveltxt = "levels/level" + level + ".txt";
        Boss boss = Boss.makeBossForLevel(level);

        new Game(paddleCopy,
                ballCopy,
                itemCopy,
                leveltxt,
                currentGameScene,
                level,
                levelStatus,
                boss,
                this);
        //thoat khoi man choi thi bat
        //Game da implement window listener roi nen khi dong game, bgm tu phat lai
    }

    public static void main(String[] args) {
        new MapMenu();
    }
}

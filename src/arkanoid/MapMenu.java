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
    protected static final int TOTAL_LEVEL = 15;

    //Nhung gi nguoi choi so huu se nam trong lop nay
    private OwnedManager ownedManager = new OwnedManager();

    //paddle hien tai dang trang bi
    private Paddle currentPaddle = new Paddle(PLAYFRAME_WIDTH / 2 - 60, 600, 120, 20,
            15, 0, "img/paddle/paddlevip.png");

    //bong hien tai dang trang bi chinh la ownedManager.getCurrentBall()
    //item hien tai dang trang bi chinh la ownedManager.getCurrentItem()

    private java.util.List<Boolean> levelStatus = new java.util.ArrayList<>(java.util.Collections.nCopies(TOTAL_LEVEL, false));
    private ArrayList <JButton> levelButton = new ArrayList <> ();

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

        // Thêm các nút level
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                630, 340, click, e -> showLevelPreview(1)));
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                515, 315, click, e -> showLevelPreview(2)));
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                575, 400, click, e -> showLevelPreview(3)));
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                760, 496, click, e -> showLevelPreview(4)));
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                855, 527, click, e -> showLevelPreview(5)));
        levelButton.add(ButtonManager.createImageButton("img/button/3.png", null,
                806, 585, click, e -> showLevelPreview(6)));
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                450, 490, click, e -> showLevelPreview(7)));
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                315, 440, click, e -> showLevelPreview(8)));
        levelButton.add(ButtonManager.createImageButton("img/button/3.png", null,
                205, 461, click, e -> showLevelPreview(9)));
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                434, 154, click, e -> showLevelPreview(10)));
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                339, 123, click, e -> showLevelPreview(11)));
        levelButton.add(ButtonManager.createImageButton("img/button/3.png", null,
                402, 92, click, e -> showLevelPreview(12)));
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                904, 355, click, e -> showLevelPreview(13)));
        levelButton.add(ButtonManager.createImageButton("img/button/1.png", null,
                953, 419, click, e -> showLevelPreview(14)));
        levelButton.add(ButtonManager.createImageButton("img/button/3.png", null,
                997, 302, click, e -> showLevelPreview(15)));

        for(int i = 0; i < 15; i++) {
            panel.add(levelButton.get(i));
        }

        updateLevelStatus();

        setContentPane(panel);
        setVisible(true);
    }

    //Mo Popup gioi thieu ve level
    private void showLevelPreview(int level) {

        //Dung Jdialog nhe
        JDialog dialog = new JDialog(this, "Level " + level, true);
        dialog.setSize(1100, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setLayout(null);

        //Anh popup
        ImageIcon previewIcon = new ImageIcon("img/levelpreview/lv" + level + ".png");
        Image scaledImage = previewIcon.getImage().getScaledInstance(1100, 600, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
        backgroundLabel.setBounds(0, 0, 1100, 600);
        backgroundLabel.setLayout(null);

        // Nút Start và Cancel
        JButton startButton = new JButton("Start");
        JButton cancelButton = new JButton("Cancel");
        startButton.setBounds(890, 520, 80, 30);
        cancelButton.setBounds(990, 520, 80, 30);
        startButton.setFocusPainted(false);
        cancelButton.setFocusPainted(false);


        startButton.addActionListener(e -> {
            dialog.dispose();
            openLevel(level);
        });
        cancelButton.addActionListener(e -> dialog.dispose());


        backgroundLabel.add(startButton);
        backgroundLabel.add(cancelButton);
        dialog.add(backgroundLabel);

        dialog.setVisible(true);
    }

    private void openLevel(int level) {
        //vao man choi thi tat bgm di
        backgroundMusic.stop();

        //copy current ball va paddle de chung no khong bi thay doi sau game()
        Paddle paddleCopy = new Paddle(currentPaddle);
        Ball ballCopy = new Ball(ownedManager.getCurrentBall());
        Item itemCopy = new Item(ownedManager.getCurrentItem());

        String leveltxt = "levels/level" + level + ".txt";
        String scene = "img/levelbackground/lv" + level + ".png";
        Boss boss = Boss.makeBossForLevel(level);

        new Game(paddleCopy,
                ballCopy,
                itemCopy,
                leveltxt,
                scene,
                level,
                levelStatus,
                boss,
                this);
        //thoat khoi man choi thi bat
        //Game da implement window listener roi nen khi dong game, bgm tu phat lai
    }

    public void updateLevelStatus() {
        for(int i = 0; i < TOTAL_LEVEL; i++) {
            if(levelStatus.get(i) == true) {
                levelButton.get(i).setIcon(new ImageIcon("img/button/2.png"));
            }
        }
    }

    public static void main(String[] args) {
        new MapMenu();
    }
}

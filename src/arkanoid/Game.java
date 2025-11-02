package arkanoid;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import GraphicsEffect.ScreenShakeEffect;
import arkanoid.ArtifactSlot;
import arkanoid.LaserBeam;

import GachaMachine.Item;

import MoneyCollected.Coin;
import arkanoid.*;

import PowerUp.*;

public class Game extends JFrame implements ActionListener, KeyListener, WindowListener {

    //Thông số khung hình
    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 700;
    public static final int PLAYFRAME_WIDTH = 800;
    public static final int PLAYFRAME_HEIGHT = 700;
    private static final int TICK_MS = 33;

    //Cửa sổ cha
    MapMenu parentMenu;

    //Âm thanh màn chơi
    private Sound bgm = new Sound("sound/CombatSound.wav");
    Sound bossSound = new Sound("sound/bossSound.wav");

    //Số máu trong trò chơi
    public int currentHeart;
    public ArrayList<JLabel> heart = new ArrayList<>();

    //Trạng thái pause hay chưa
    private boolean isPause = false;
    private JButton pauseButton;

    //Nút đầu hàng
    private JButton surrenderButton;

    //paddle
    private final Paddle paddle;

    //ball
    private final Ball ball;
    private int currentDamage;

    //Kiểm tra xem bóng đã được phóng đi hay chưa.
    private boolean isBallActive = false;

    //dx, dy mặc định của ball
    private final int initDx;
    private final int initDy;

    //item của người chơi
    private final Item item;

    // Coin
    private Coin amount = new Coin();

    //bricks
    private int totalBrick; // so gach 1 man choi
    private int registeredBricks = 0;
    private final List<Brick> bricks = new ArrayList<>();
    private final List<Brick> removed = new ArrayList<>();
    private static final int BRICK_W = 80;
    private static final int BRICK_H = 30;
    private static final int STEP_X = 80;
    private static final int STEP_Y = 30;
    private static final int PLAY_LEFT = 0;
    private static final double COIN_DROP_CHANCE = 0.7;
    private static final double POWER_UP_DROP_CHANCE = 0.3;

    // Công cụ hỗ trợ in hình ảnh
    private final ObjectPrinter paddlePrinter = new ObjectPrinter();
    private final ObjectPrinter ballPrinter = new ObjectPrinter();
    private final JLayeredPane layers = new JLayeredPane();
    private final Map<Brick, ObjectPrinter> brickPrinters = new HashMap<>();
    private final String gameScene;

    //timer
    private final Timer timer = new Timer(TICK_MS, this);

    // Trạng thái (fix delay)
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private final Rectangle tempPuRect = new Rectangle();
    private final Rectangle tempPaddleRect = new Rectangle();

    // Nhãn hiển thị thông tin paddle
    private JLabel paddleTitle;
    private JLabel paddleWidthLabel;
    private JLabel paddleDxLabel;

    // Nhãn hiển thị thông tin Ball
    private JLabel ballTitle;
    private JLabel ballElementLabel;
    private JLabel ballDxLabel;
    private JLabel ballDyLabel;
    private JLabel ballDamageLabel;

    // Nhãn hiển thị thông tin artifact
    private JLabel artifactTitle;

    //Slot hiển thị artifact có cooldown
    private ArtifactSlot artifactSlot;

    //Timer đếm cooldown của artifact
    private Timer artifactTimer;

    //Trạng thái cooldown của artifact
    private boolean isCoolingDown = false;

    //Trạng thái không thể bị phá (từ artifact)
    private boolean unbreakable = false;

    //Nhãn hiển thị thông tin Coin
    private JLabel coinTitle;
    private JLabel amountLabel;

    //  Quản lý PowerUp trong game
    private final PowerUpManager powerUpManager;
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final Map<PowerUp, ObjectPrinter> powerUpPrinters = new HashMap<>();

    // ===== Boss + Fire Rain =====
    private Boss boss = null;                         // chỉ spawn 1 lần ở cuối màn
    private boolean bossSpawned = false;
    private final ObjectPrinter1 bossPrinter = new ObjectPrinter1();
    private final FireballLayer fireballLayer;              // 1 layer vẽ tất cả fireball
    private final Rectangle tmpR2 = new Rectangle();
    private int bossSpawnGrace = 0; // số tick miễn va chạm ngay sau khi spawn

    // ===== Water Waves =====
    private final List<Brick> waveBricks = new ArrayList<>();
    private final Image waveSprite = new ImageIcon("img/Boss/WaterWave.png").getImage();
    private final WaterWaveLayer waterLayer = new WaterWaveLayer(waveSprite);

    // ===== Wind Shuriken =====
    private final Image shurikenSprite = new ImageIcon("img/Boss/WindShuriken.png").getImage();
    private final ShurikenLayer shurikenLayer = new ShurikenLayer(shurikenSprite);

    // Tick counter cho scheduler
    private int tickCounter = 0;

    // ==== Levels ====
    private int currentLevel;
    private List<Boolean> levelStatus;
    private final Boss bossForLevel; // null = level này không có boss

    /**
     * Khởi tạo một đối tượng Game mới, bao gồm toàn bộ thành phần gameplay và UI.
     *
     * @param currentPaddle paddle mà người chơi đang chọn
     * @param currentBall bóng mà người chơi đang dùng
     * @param currentItem vật phẩm (artifact) của người chơi
     * @param coin đối tượng quản lý coin của người chơi
     * @param level tên file cấu hình màn chơi (dùng để sinh gạch)
     * @param currentGameScene đường dẫn hình nền của màn chơi
     * @param currentLevel số thứ tự màn chơi hiện tại
     * @param levelStatus danh sách trạng thái hoàn thành của các màn
     * @param bossForLevel boss của màn chơi (có thể null)
     * @param parentMenu cửa sổ cha (MapMenu)
     */
    public Game(Paddle currentPaddle, Ball currentBall, Item currentItem, Coin coin, String level, String currentGameScene,
                int currentLevel, List<Boolean> levelStatus, Boss bossForLevel, MapMenu parentMenu) {
        super("Arkanoid (Ball + Brick)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        //an man hinh cha di
        //cua so cha
        this.parentMenu = parentMenu;
        this.parentMenu.setVisible(false);

        layers.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        layers.setLayout(null);
        layers.setOpaque(true);
        layers.setBackground(Color.BLACK);
        setContentPane(layers);

        configurePrinter(paddlePrinter);
        configurePrinter(ballPrinter);

        //phat nhac
        bgm.loop();

        //set pausebutton
        pauseButton = new JButton(new ImageIcon(new ImageIcon("img/pauseButton.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        pauseButton.setRolloverIcon(new ImageIcon(new ImageIcon("img/pauseHover.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        pauseButton.setBounds(1080, 10, 40, 40);
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusPainted(false);
        pauseButton.setOpaque(false);
        pauseButton.addActionListener(e -> togglePause());
        layers.add(pauseButton, Integer.valueOf(5));

        //set nut dau hang
        surrenderButton = new JButton(new ImageIcon(new ImageIcon("img/whiteFlag.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        surrenderButton.setRolloverIcon(new ImageIcon(new ImageIcon("img/whiteFlagHover.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        surrenderButton.setBounds(1130, 10, 40, 40);
        surrenderButton.setBorderPainted(false);
        surrenderButton.setContentAreaFilled(false);
        surrenderButton.setFocusPainted(false);
        surrenderButton.setOpaque(false);
        surrenderButton.addActionListener(e -> surrender());
        layers.add(surrenderButton, Integer.valueOf(5));

        //set currentheart
        currentHeart = 3;

        ImageIcon icon = new ImageIcon("img/heart/redheart.png");
        Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        for (int i = 0; i < 3; i++) {
            JLabel heartLabel = new JLabel(new ImageIcon(scaled));
            heartLabel.setBounds(840 + i * 45, 120, 40, 40);
            this.heart.add(heartLabel);
            layers.add(heartLabel, Integer.valueOf(3));
        }

        //level
        this.currentLevel = currentLevel;
        this.levelStatus = levelStatus;
        this.bossForLevel = bossForLevel;

        //paddle
        paddle = currentPaddle;

        //ball
        ball = currentBall;
        initDx = ball.getDx();
        initDy = ball.getDy();
        currentDamage = ball.getBaseDamage();

        paddlePrinter.setGameObject(paddle);
        ballPrinter.setGameObject(ball);

        //item
        item = currentItem;

        //coin
        this.amount = coin;

        powerUpManager = new PowerUpManager(this);

        layers.add(paddlePrinter, Integer.valueOf(9));
        layers.add(ballPrinter, Integer.valueOf(10));

        int[][] grid;
        try {
            grid = Brick.readGrid(level);
        } catch (IOException e) {
            throw new RuntimeException("load grid fail", e);
        }
        int rows = grid.length;
        int cols = grid[0].length;
        int totalW = BRICK_W + (cols - 1) * STEP_X;
        int originX = PLAY_LEFT + (PLAYFRAME_WIDTH - totalW) / 2;
        int originY = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int id = grid[r][c];
                if (id == 0) continue;
                int x = originX + c * STEP_X;
                int y = originY + r * STEP_Y;
                Brick b = Brick.createBrickFromId(id, x, y);
                if (b == null) continue;
                bricks.add(b);
                ObjectPrinter bp = new ObjectPrinter();
                configurePrinter(bp);
                bp.setGameObject(b);
                brickPrinters.put(b, bp);
                layers.add(bp, Integer.valueOf(8));
            }
        }
        totalBrick = bricks.size();

        //game scene
        gameScene = currentGameScene;

        //ve boi canh man choi
        ImageIcon icon1 = new ImageIcon(gameScene);
        Image scaled1 = icon1.getImage().getScaledInstance(GAME_WIDTH, GAME_HEIGHT, Image.SCALE_SMOOTH);
        JLabel bg1 = new JLabel(new ImageIcon(scaled1));
        bg1.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
        layers.add(bg1, Integer.valueOf(0));

        //ve thanh stat bar
        ImageIcon icon2 = new ImageIcon("img/statbar.png");
        Image scaled2 = icon2.getImage().getScaledInstance(400,700, Image.SCALE_SMOOTH);
        JLabel bg2 = new JLabel(new ImageIcon(scaled2));
        bg2.setBounds(800, 0, 400, 700);
        layers.add(bg2, Integer.valueOf(1));

        //ve cac thong so tren stat bar

        // --- PADDLE INFO ---
        paddleTitle = new JLabel("Paddle");
        paddleTitle.setBounds(840, 175, 200, 25);
        paddleTitle.setFont(new Font("Adobe Garamond Pro", Font.BOLD, 25));
        paddleTitle.setForeground(Color.YELLOW);
        layers.add(paddleTitle, Integer.valueOf(2));

        paddleWidthLabel = new JLabel("Width: " + paddle.getWidth());
        paddleWidthLabel.setBounds(840, 205, 200, 15);
        paddleWidthLabel.setFont(new Font("Adobe Garamond Pro", Font.PLAIN, 15));
        paddleWidthLabel.setForeground(Color.WHITE);
        layers.add(paddleWidthLabel, Integer.valueOf(2));

        paddleDxLabel = new JLabel("Speed: " + paddle.getDx());
        paddleDxLabel.setBounds(840, 225, 200, 15);
        paddleDxLabel.setFont(new Font("Adobe Garamond Pro", Font.PLAIN, 15));
        paddleDxLabel.setForeground(Color.WHITE);
        layers.add(paddleDxLabel, Integer.valueOf(2));

        // --- BALL INFO ---
        ballTitle = new JLabel("Ball");
        ballTitle.setBounds(840, 260, 200, 25);
        ballTitle.setFont(new Font("Adobe Garamond Pro", Font.BOLD, 25));
        ballTitle.setForeground(Color.YELLOW);
        layers.add(ballTitle, Integer.valueOf(2));

        ballElementLabel = new JLabel("Element: " + ball.getElement());
        ballElementLabel.setBounds(840, 290, 200, 15);
        ballElementLabel.setFont(new Font("Adobe Garamond Pro", Font.PLAIN, 15));
        ballElementLabel.setForeground(Color.WHITE);
        layers.add(ballElementLabel, Integer.valueOf(2));

        ballDxLabel = new JLabel("Dx: " + ball.getDx());
        ballDxLabel.setBounds(840, 310, 200, 15);
        ballDxLabel.setFont(new Font("Adobe Garamond Pro", Font.PLAIN, 15));
        ballDxLabel.setForeground(Color.WHITE);
        layers.add(ballDxLabel, Integer.valueOf(2));

        ballDyLabel = new JLabel("Dy: " + ball.getDy());
        ballDyLabel.setBounds(840, 330, 200, 15);
        ballDyLabel.setFont(new Font("Adobe Garamond Pro", Font.PLAIN, 15));
        ballDyLabel.setForeground(Color.WHITE);
        layers.add(ballDyLabel, Integer.valueOf(2));

        ballDamageLabel = new JLabel("Damage: " + ball.getBaseDamage());
        ballDamageLabel.setBounds(840, 350, 200, 15);
        ballDamageLabel.setFont(new Font("Adobe Garamond Pro", Font.PLAIN, 15));
        ballDamageLabel.setForeground(Color.WHITE);
        layers.add(ballDamageLabel, Integer.valueOf(2));

        // --- ARTIFACT INFO ---
        artifactTitle = new JLabel("Artifact");
        artifactTitle.setBounds(840, 385, 200, 25); // vi tri va kich thuoc
        artifactTitle.setFont(new Font("Adobe Garamond Pro", 1, 25));
        artifactTitle.setForeground(Color.YELLOW); // mau
        layers.add(artifactTitle, Integer.valueOf(2));

        //hien thi hinh anh artifact co the cooldown nhu lien quan
        artifactSlot = new ArtifactSlot(item.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        artifactSlot.setBounds(840, 420, 50, 50);
        layers.add(artifactSlot, Integer.valueOf(2));

        // COIN INFO
        coinTitle = new JLabel("Coin");
        coinTitle.setBounds(840, 485, 200, 25); // vi tri va kich thuoc
        coinTitle.setFont(new Font("Adobe Garamond Pro", 1, 25));
        coinTitle.setForeground(Color.YELLOW); // mau
        layers.add(coinTitle, Integer.valueOf(2));

        amountLabel = new JLabel("Amount: " + amount.getAmount());
        amountLabel.setBounds(840, 515, 200, 15);
        amountLabel.setFont(new Font("Adobe Garamond Pro", Font.PLAIN, 15));
        amountLabel.setForeground(Color.WHITE);
        layers.add(amountLabel, Integer.valueOf(2));

        // --- FIRE BALL ---
        fireballLayer = new FireballLayer(PLAYFRAME_WIDTH, PLAYFRAME_HEIGHT);
        layers.add(fireballLayer, Integer.valueOf(7));

        // --- WATER WAVE ---
        waterLayer.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
        layers.add(waterLayer, Integer.valueOf(9));

        setVisible(true);

        //them keylistender
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        //them windowlistener
        addWindowListener(this);

        timer.start();
    }

    /**
     * Chuyển đổi trạng thái tạm dừng của trò chơi.
     * <p>
     * Nếu trò chơi đang tạm dừng thì tiếp tục; ngược lại, dừng lại.
     * Cập nhật biểu tượng của nút Pause tương ứng với trạng thái hiện tại.
     */
    private void togglePause() {
        if (isPause) {
            // luc nay dang pause, an vao de tiep tuc game
            pauseButton.setIcon(new ImageIcon(new ImageIcon("img/pauseButton.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            pauseButton.setRolloverIcon(new ImageIcon(new ImageIcon("img/pauseHover.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            timer.start();
            artifactTimer.start();
            isPause = false;
            this.requestFocusInWindow();
        } else {
            // game dang dien ra thi tam dung
            pauseButton.setIcon(new ImageIcon(new ImageIcon("img/pauseButton1.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            pauseButton.setRolloverIcon(new ImageIcon(new ImageIcon("img/pauseHover1.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            timer.stop();
            artifactTimer.stop();
            isPause = true;
        }
    }

    /**
     * Kích hoạt hành động đầu hàng của người chơi.
     * <p>
     * Dừng trò chơi, tắt nhạc nền và âm thanh boss, hiển thị thông báo đầu hàng
     * và quay trở lại menu bản đồ cha.
     */
    private void surrender() {
        timer.stop();
        bgm.close();
        bossSound.close();
        JOptionPane.showMessageDialog(this, "Chua j da dau hang roi ga vcl");
        this.dispose();
        parentMenu.setVisible(true);
    }

    /**
     * Cấu hình một {@link ObjectPrinter} để hiển thị đối tượng trong khung trò chơi.
     *
     * @param p đối tượng {@link ObjectPrinter} cần cấu hình
     */
    private void configurePrinter(ObjectPrinter p) {
        p.setOpaque(false);
        p.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    /**
     * Cấu hình một {@link ObjectPrinter1} để hiển thị đối tượng trong khung trò chơi.
     *
     * @param p đối tượng {@link ObjectPrinter1} cần cấu hình
     */
    private void configurePrinter1(ObjectPrinter1 p) {
        p.setOpaque(false);
        p.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    /**
     * Kiểm tra trạng thái sống của boss.
     *
     * @return {@code true} nếu boss đã bị tiêu diệt hoặc chưa tồn tại; {@code false} nếu boss còn sống
     */
    private boolean isBossDead() {
        return boss == null;
    }

    /**
     * Cập nhật hiển thị trái tim trên giao diện dựa theo số mạng hiện tại của người chơi.
     * <p>
     * Những trái tim tương ứng với số mạng còn lại sẽ hiển thị màu đỏ,
     * các trái tim đã mất hiển thị màu xám.
     */
    private void updateHeartDisplay() {
        for (int i = 0; i < heart.size(); i++) {
            String path = (i < currentHeart)
                    ? "img/heart/redheart.png"   // có máu
                    : "img/heart/grayheart.png"; // hết máu
            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            heart.get(i).setIcon(new ImageIcon(scaled));
        }
        layers.repaint();
    }

    /**
     * Hàm chính điều khiển toàn bộ vòng lặp logic của trò chơi, được gọi mỗi tick bởi {@link javax.swing.Timer}.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        tickCounter++;
        if (bricks.isEmpty()) {
            if (bossForLevel == null) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Yee thang roi");
                amount.add(500);
                levelStatus.set(currentLevel - 1, true);
                bgm.close();
                bossSound.close();
                parentMenu.updateLevelStatus();
                this.dispose();
                parentMenu.setVisible(true);
                return;
            }
            if (!bossSpawned) {
                boss = bossForLevel;                 // dùng bossForLevel
                boss.setX(PLAYFRAME_WIDTH / 2 - boss.getWidth() / 2);
                boss.setY(120);
                boss.setDx(3);
                boss.setDy(2);

                configurePrinter1(bossPrinter);
                bossPrinter.setGameObject(boss);
                layers.add(bossPrinter, Integer.valueOf(11));
                bossSpawned = true;
                bossSpawnGrace = 12;

                ScreenShakeEffect.shake(this,  1000, 8);
                bossSound.play();

                if ("fire".equals(boss.getElement())) {
                    boss.activateFireRain();             // kích hoạt skill một lần
                    boss.activateFireRain();
                } else if ("wind".equals(boss.getElement())) {
                    shurikenLayer.bindBoss(boss);
                    shurikenLayer.setBounds(0, 0, PLAYFRAME_WIDTH, PLAYFRAME_HEIGHT);
                    layers.add(shurikenLayer, Integer.valueOf(10));
                } else if ("water".equals(boss.getElement())) {
                    waterLayer.setWaves(boss.getWaves());
                    boss.maybeActivateWaveRain(PLAYFRAME_WIDTH, /*minW*/200, /*maxW*/100);
                } else if ("earth".equals(boss.getElement())) {
                    boss.maybeActivateEarthBulwark(bricks, PLAYFRAME_WIDTH, BRICK_W, BRICK_H);
                }
            } else if (isBossDead()) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Yee thang roi");
                amount.add(500);
                bgm.close();
                bossSound.close();
                levelStatus.set(currentLevel - 1, true);
                parentMenu.updateLevelStatus();
                this.dispose();
                parentMenu.setVisible(true);
                return;
            }
        }

        if (currentHeart <= 0) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game over!, qua ngu");
            bgm.close();
            bossSound.close();
            this.dispose();
            parentMenu.setVisible(true);
            return;
        }

        //Tức là chưa thua
        if (leftPressed) {
            paddle.setX(Math.max(paddle.getX() - paddle.getDx(), 0));
            paddle.setMovingLeft();
        } else {
            if (rightPressed) {
                paddle.setX(Math.min(paddle.getX() + paddle.getDx(), PLAYFRAME_WIDTH - paddle.getWidth()));
                paddle.setMovingRight();
            } else {
                paddle.setDefaultMoving();
            }
        }

        // Wind shuriken scheduler
        if (boss != null && "wind".equals(boss.getElement())) {
            // ~1.5s một lần nếu đang còn sống
            if ((tickCounter % 90) == 0) {
                boss.activateWindSeek(paddle);
            }
        }

        // Cập nhật bóng nếu đã bắn ra rồi hoặc nếu bóng chưa bắn ra thì bóng sé dính vào paddle
        if(isBallActive == true) {
            ball.step(new Rectangle(0, 0, PLAYFRAME_WIDTH, GAME_HEIGHT));
            ball.collideWithPaddle(paddle);
        } else {
            ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
            ball.setY(paddle.getY() - ball.getHeight());
            ball.setDx(0);
            ball.setDy(0);
        }

        // Mất mạng
        if (ball.getY() > GAME_HEIGHT) {
            if(unbreakable == false) {
                currentHeart--;

                //cho bong ve lai giua paddle
                isBallActive = false;
                ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
                ball.setY(paddle.getY() - ball.getHeight());
                ball.setDx(0);
                ball.setDy(0);

                if (currentHeart >= 0) {
                    ImageIcon icon = new ImageIcon("img/heart/grayheart.png");
                    Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    heart.get(currentHeart).setIcon(new ImageIcon(scaled));
                }
            } else {
                ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
                ball.setY(paddle.getY() - ball.getHeight());
                ball.setDx(0);
                ball.setDy(0);

            }
        }

        // Va chạm với gạch
        for (Brick b : bricks) {
            if (ball.collide(b)) {
                if (b.isDestroyed()) {
                    removed.add(b);

                    // 30% chance drop PowerUp
                    PowerUp pu = null;
                    if (Math.random() < POWER_UP_DROP_CHANCE) {
                        double rand = Math.random();
                        if(rand < 0.2) {
                            pu = new PowerUpIncreaseDamage(b.getX() + 20, b.getY() + 10, new OwnedManager(ball));
                        }
                        else if(rand < 0.4) {
                            pu = new PowerUpExtraHeart(b.getX() + 20, b.getY() + 10);
                        }
                        else if(rand < 0.6){
                            pu = new PowerUpExpandPaddle(b.getX() + 20, b.getY() + 10, paddle);
                        }
                        else if (rand < 0.8) {
                            pu = new PowerUpSlowPaddle(b.getX() + 20, b.getY() + 10, paddle);
                        }
                        else {
                            pu = new CoinBonus(b.getX() + 20, b.getY() + 10, amount);
                        }
                    }
                    if (pu != null) {
                        powerUps.add(pu);
                        ObjectPrinter pup = new ObjectPrinter();
                        configurePrinter(pup);
                        pup.setGameObject(pu);
                        powerUpPrinters.put(pu, pup);
                        layers.add(pup, Integer.valueOf(7));
                    }
                } else {
                    ObjectPrinter p = brickPrinters.get(b);
                    if (p != null) p.startFlash();
                }
                break; // Chỉ va chạm 1 gạch/lần
            }
        }

        // Xóa gạch bị phá
        for (Brick b : removed) {
            bricks.remove(b);
            ObjectPrinter p = brickPrinters.remove(b);
            if (p != null) layers.remove(p);
        }
        removed.clear();

        // Cập nhật PowerUp
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp pu = powerUps.get(i);
            pu.setY(pu.getY() + 3);

            tempPuRect.setBounds(pu.getX(), pu.getY(), pu.getWidth(), pu.getHeight());
            tempPaddleRect.setBounds(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

            if (tempPuRect.intersects(tempPaddleRect)) {
                powerUpManager.apply(pu);
                removePowerUp(pu);
                powerUps.remove(i);
            } else if (pu.isOutOfBounds()) {
                removePowerUp(pu);
                powerUps.remove(i);
            } else {
                ObjectPrinter p = powerUpPrinters.get(pu);
                if (p != null) p.setGameObject(pu);
            }
        }

        // Cập nhật hiển thị
        paddlePrinter.setGameObject(paddle);
        ballPrinter.setGameObject(ball);

        paddleWidthLabel.setText("Width: " + paddle.getWidth());
        paddleDxLabel.setText("Speed: " + paddle.getDx());
        ballElementLabel.setText("Element: " + ball.getElement());
        ballDxLabel.setText("Dx: " + ball.getDx());
        ballDyLabel.setText("Dy: " + ball.getDy());
        ballDamageLabel.setText("Damage: " + ball.getBaseDamage());
        amountLabel.setText("Amount : " + amount.getAmount());

        // ===== Boss logic =====
        if (bossSpawned && boss != null) {
            updateBossMovementAndCollision();
            updateBossSkills();
            registerNewBricks();
        }

        layers.revalidate();
        layers.repaint();
    }

    // === Boss: movement + va chạm bóng ===
    private void updateBossMovementAndCollision() {
        // 1) Cập nhật animation nội bộ nếu có
        boss.tick();

        // 2) Biên di chuyển
        final int maxX = PLAYFRAME_WIDTH - boss.getWidth();
        final int TOP_Y = 80;          // tránh HUD/gạch
        final int BOTTOM_Y = 320;      // không lấn vùng paddle
        final int maxY = BOTTOM_Y - boss.getHeight();

        // 3) Tính bước tiếp theo
        int nx = boss.getX() + boss.getDx();
        int ny = boss.getY() + boss.getDy();

        // 4) Bật nảy theo X
        if (nx < 0) {
            nx = 0;
            boss.setDx(Math.abs(boss.getDx()));
        } else if (nx > maxX) {
            nx = maxX;
            boss.setDx(-Math.abs(boss.getDx()));
        }

        // 5) Bật nảy theo Y → tạo zigzag
        if (ny < TOP_Y) {
            ny = TOP_Y;
            boss.setDy(Math.abs(boss.getDy()));
        } else if (ny > maxY) {
            ny = maxY;
            boss.setDy(-Math.abs(boss.getDy()));
        }

        // 6) Ghi toạ độ mới và render
        boss.setX(nx);
        boss.setY(ny);
        bossPrinter.setGameObject(boss);

        // 7) Miễn va chạm trong grace period
        if (bossSpawnGrace > 0) {
            bossSpawnGrace--;
            return;
        }

        // 8) Va chạm bóng–boss
        if (ball.collideWithBoss(boss)) {
            boolean canFlash = !("earth".equals(boss.getElement()) && boss.isInvulnerable());
            if (canFlash) {
                try { bossPrinter.startFlash(); } catch (Throwable ignore) {}
            }
            if (boss.isDead()) {
                cleanupBossSkills();
                layers.remove(bossPrinter);
                boss = null;
                layers.revalidate();
                layers.repaint();
            }
        }
    }

    /**
     * Cập nhật và kích hoạt các kỹ năng của boss dựa trên nguyên tố hiện tại.
     * <p>
     * - {@code fire}: kích hoạt mưa lửa, cập nhật đường đạn và xử lý va chạm với paddle. <br>
     * - {@code water}: sinh sóng nước, cập nhật rơi và kiểm tra va chạm. <br>
     * - {@code earth}: tạo lá chắn hoặc hiệu ứng rung đất ảnh hưởng đến gạch. <br>
     * - {@code wind}: tạo phi tiêu di chuyển và va chạm với paddle. <br>
     * <p>
     * Mỗi loại nguyên tố được cập nhật và vẽ trên layer riêng tương ứng.
     */
    private void updateBossSkills() {
        if (boss == null) return;

        if ("fire".equals(boss.getElement())) {
            maybeActivateFireRain();
            boss.tickSkill(PLAYFRAME_WIDTH, PLAYFRAME_HEIGHT);

            // render projectile trên layer riêng
            fireballLayer.setProjectiles(boss.getFireballs());
            fireballLayer.repaint();

            // xử lý va chạm fireball với paddle
            handleFireballHitsPaddle();
        } else if ("water".equals(boss.getElement())) {
            // spawn theo cooldown nằm trong Boss
            boss.maybeActivateWaveRain(PLAYFRAME_WIDTH, 200, 100);

            // cập nhật rơi
            boss.tickWaves(PLAYFRAME_WIDTH, PLAYFRAME_HEIGHT);

            // vẽ bằng WaterWaveLayer
            waterLayer.setWaves(boss.getWaves());
            waterLayer.repaint();

            // va chạm
            handleWaterWaveCollisions();
        } else if ("earth".equals(boss.getElement())) {
            boss.tickEarth(bricks);
            if ((System.nanoTime() & 63) == 0) { // nhẹ, không phụ thuộc tick đếm ngoài
                boss.maybeActivateEarthBulwark(bricks, PLAYFRAME_WIDTH, BRICK_W, BRICK_H);
            }
        } else {
            boss.tickWindShurikens(PLAYFRAME_WIDTH, PLAYFRAME_HEIGHT, paddle);
            shurikenLayer.repaint();
            handleShurikenCollisions();
        }

        // nếu có element khác thì thêm các nhánh else-if tại đây
    }

    /**
     * Dọn dẹp toàn bộ hiệu ứng và kỹ năng còn tồn tại của boss sau khi bị tiêu diệt.
     * <p>
     * Xóa các projectiles (Fireball), sóng nước (WaterWave), và phi tiêu (Wind Shuriken)
     * khỏi các layer tương ứng để tránh rò rỉ tham chiếu hoặc vẽ dư thừa.
     */
    private void cleanupBossSkills() {
        // Fire
        fireballLayer.setProjectiles(Collections.emptyList());
        fireballLayer.repaint();

        // Water
        if (boss != null) {
            waterLayer.setWaves(Collections.emptyList());
            waterLayer.repaint();
        }

        // Wind
        if (shurikenLayer.getParent() != null) {
            shurikenLayer.unbind(); // viết hàm này để bỏ tham chiếu boss, nếu chưa có thì bỏ qua
            layers.remove(shurikenLayer);
        }
    }

    /**
     * Kích hoạt kỹ năng mưa lửa của boss với xác suất ngẫu nhiên.
     * <p>
     * Hàm được gọi định kỳ trong vòng lặp cập nhật để tạo hiệu ứng tấn công không liên tục.
     * Xác suất mặc định là 1%.
     */
    private void maybeActivateFireRain() {
        // kích hoạt ngẫu nhiên
        if (Math.random() < 0.01) boss.activateFireRain();
    }

    /**
     * Xử lý va chạm giữa các quả cầu lửa (Fireball) và paddle của người chơi.
     * <p>
     * Khi va chạm xảy ra:
     * <ul>
     *   <li>Giảm số lượng tim nếu người chơi không ở trạng thái bất tử.</li>
     *   <li>Xóa quả cầu lửa khỏi danh sách projectiles.</li>
     *   <li>Kích hoạt hiệu ứng nhấp nháy cho paddle để báo hiệu trúng đòn.</li>
     * </ul>
     * Hàm duy trì danh sách projectiles an toàn bằng cách hoán đổi phần tử cuối.
     */
    private void handleFireballHitsPaddle() {
        List<? extends GameObject> fbs = boss.getFireballs();
        for (int i = 0; i < fbs.size(); ) {
            GameObject fb = fbs.get(i);

            boolean hit = paddle.collideFireball(fb, 0.5f);
            if (hit) {
                if (currentHeart > 0 && unbreakable == false) {
                    currentHeart--;
                    ImageIcon iconH = new ImageIcon("img/heart/grayheart.png");
                    Image scaledH = iconH.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    heart.get(currentHeart).setIcon(new ImageIcon(scaledH));
                }
                List list = (List) fbs;
                int last = list.size() - 1;
                list.set(i, list.get(last));
                list.remove(last);
                try {
                    paddlePrinter.startFlash();
                } catch (Throwable ignore) {
                }
            } else {
                i++;
            }
        }
    }

    // cấu hình
    private static final int WAVE_INSET_X = 8;
    private static final int WAVE_INSET_Y = 6;
    private static final int MIN_PEN = 2;
    private static final boolean WAVE_ONE_WAY = false; // true: chỉ chặn từ trên xuống

    /**
     * Xử lý va chạm giữa các đợt sóng nước (WaterWave) và paddle của người chơi.
     * <p>
     * Khi xảy ra va chạm:
     * <ul>
     *   <li>Người chơi mất một tim nếu không ở trạng thái bất tử.</li>
     *   <li>Sóng bị loại khỏi danh sách sóng đang hoạt động.</li>
     *   <li>Kích hoạt hiệu ứng nhấp nháy (flash) cho paddle.</li>
     * </ul>
     * Hàm đồng thời thu nhỏ hitbox của sóng bằng giá trị WAVE_INSET để va chạm chính xác hơn.
     */
    private void handleWaterWaveCollisions() {
        List<? extends GameObject> ws = boss.getWaves();

        for (int i = 0; i < ws.size();) {
            GameObject w = ws.get(i);

            // hitbox đã co
            final int wx  = w.getX() + WAVE_INSET_X;
            final int wy  = w.getY() + WAVE_INSET_Y;
            final int ww0 = w.getWidth()  - 2*WAVE_INSET_X;
            final int wh0 = w.getHeight() - 2*WAVE_INSET_Y;
            if (ww0 <= 0 || wh0 <= 0) { i++; continue; }
            final int ww = ww0, wh = wh0;
            final int wright = wx + ww, wbot = wy + wh;

            // paddle ↔ wave
            if (paddle.collideWave(w, WAVE_INSET_X, WAVE_INSET_Y)) {
                if (currentHeart > 0 && !unbreakable) {
                    currentHeart--;
                    ImageIcon iconH = new ImageIcon("img/heart/grayheart.png");
                    Image scaledH = iconH.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    heart.get(currentHeart).setIcon(new ImageIcon(scaledH));
                }
                @SuppressWarnings("rawtypes")
                List list = (List) ws;
                int last = list.size() - 1;
                list.set(i, list.get(last));
                list.remove(last);
                try { paddlePrinter.startFlash(); } catch (Throwable ignore) {}
                continue;
            }

            i++;
        }
    }

    /**
     * Xử lý va chạm giữa phi tiêu gió (Shuriken) của boss và paddle.
     * <p>
     * Khi va chạm xảy ra:
     * <ul>
     *   <li>Trừ tim người chơi nếu không bất tử.</li>
     *   <li>Paddle có thể bị hất lùi (knockback) thông qua logic nội bộ.</li>
     *   <li>Phi tiêu bị xóa khỏi danh sách khi đánh trúng hoặc rời khỏi màn chơi.</li>
     * </ul>
     * Đồng thời kích hoạt hiệu ứng nhấp nháy để báo hiệu trúng đòn.
     */
    private void handleShurikenCollisions() {
        if (boss == null) return;

        List<ShurikenWind> ss = boss.getShurikens();
        for (int i = 0; i < ss.size(); ) {
            ShurikenWind s = ss.get(i);

            // Paddle tự xử lý knockback + markDead; Game chỉ trừ tim nếu có va chạm
            boolean hit = paddle.collideWithShuriken(s, PLAYFRAME_WIDTH);
            if (hit) {
                if (currentHeart > 0 && !unbreakable) {
                    currentHeart--;
                    updateHeartDisplay();  // dùng hàm sẵn có để đổi icon tim
                }
                try { paddlePrinter.startFlash(); } catch (Throwable ignore) {}
            }

            // swap-remove nếu đã chết
            if (s.isDead()) {
                @SuppressWarnings({"rawtypes","unchecked"})
                List list = (List) ss;
                int last = list.size() - 1;
                list.set(i, list.get(last));
                list.remove(last);
            } else {
                i++;
            }
        }
    }

    /**
     * Đăng ký các viên gạch mới được thêm vào danh sách
     */
    private void registerNewBricks() {
        for (int i = registeredBricks; i < bricks.size(); i++) {
            Brick b = bricks.get(i);
            if (!brickPrinters.containsKey(b)) {
                ObjectPrinter bp = new ObjectPrinter();
                configurePrinter(bp);
                bp.setGameObject(b);                // nạp ảnh 1 lần
                brickPrinters.put(b, bp);
                layers.add(bp, Integer.valueOf(8)); // cùng layer với bricks ban đầu
            }
        }
        // Đồng bộ lại chỉ số đã đăng ký
        registeredBricks = bricks.size();
    }

    /**
     * Xóa vật phẩm tăng cường (PowerUp) ra khỏi màn chơi.
     */
    private void removePowerUp(PowerUp pu) {
        ObjectPrinter printer = powerUpPrinters.remove(pu);
        if (printer != null) layers.remove(printer);
    }

    /**
     * Kích hoạt kỹ năng **Sword Artifact**: tăng 100 sát thương cho bóng trong 5 giây.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh hiệu ứng kích hoạt.</li>
     *   <li>Tăng tạm thời sát thương cơ bản của bóng thêm 100.</li>
     *   <li>Hiệu lực kéo dài 5 giây, sau đó sát thương được khôi phục về giá trị ban đầu.</li>
     *   <li>Thời gian hồi chiêu: 20 giây.</li>
     * </ul>
     * Nếu đang trong thời gian hồi chiêu, kỹ năng sẽ không được kích hoạt.
     */
    public void Sword() {
        if(isCoolingDown) {
            return;
        }

        Sound effect = new Sound("sound/activate.wav");
        effect.play();

        isCoolingDown = true;

        ball.setBaseDamage(ball.getBaseDamage() + 100);
        ballPrinter.setGameObject(ball);

        layers.revalidate();
        layers.repaint();

        Timer durationMs = new Timer(5000, e ->{
            ball.setBaseDamage(currentDamage);
            ballPrinter.setGameObject(ball);

            layers.revalidate();
            layers.repaint();
            ((Timer) e.getSource()).stop();
        });
        durationMs.setRepeats(false);
        durationMs.start();

        Timer cooldown = new Timer(20000, e->{
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    /**
     * Kích hoạt kỹ năng **Bow Artifact**: tăng 70 sát thương trong 4 giây.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh hiệu ứng kích hoạt.</li>
     *   <li>Tăng sát thương cơ bản của bóng thêm 70 điểm.</li>
     *   <li>Hiệu lực kéo dài 4 giây, sau đó quay về giá trị cũ.</li>
     *   <li>Thời gian hồi chiêu: 10 giây.</li>
     * </ul>
     * Nếu đang trong trạng thái hồi chiêu, kỹ năng sẽ không được thực thi.
     */
    public void Bow() {
        if(isCoolingDown) {
            return;
        }

        Sound effect = new Sound("sound/activate.wav");
        effect.play();

        isCoolingDown = true;

        ball.setBaseDamage(ball.getBaseDamage() + 70);
        ballPrinter.setGameObject(ball);

        layers.revalidate();
        layers.repaint();

        Timer durationMs = new Timer(4000, e ->{
            ball.setBaseDamage(currentDamage);
            ballPrinter.setGameObject(ball);

            layers.revalidate();
            layers.repaint();
            ((Timer) e.getSource()).stop();
        });
        durationMs.setRepeats(false);
        durationMs.start();

        Timer cooldown = new Timer(10000, e->{
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    /**
     * Kích hoạt kỹ năng **Heart Artifact**: hồi 1 tim (nếu chưa đầy) và khởi động hồi chiêu 30 giây.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh hiệu ứng kích hoạt.</li>
     *   <li>Tăng số tim hiện tại thêm 1, tối đa 3 tim.</li>
     *   <li>Hiệu lực tức thời (không kéo dài theo thời gian).</li>
     *   <li>Thời gian hồi chiêu: 30 giây.</li>
     * </ul>
     * Nếu người chơi đã đủ 3 tim hoặc đang trong thời gian hồi chiêu, kỹ năng không có hiệu lực.
     */
    public void Heart(){
        if(isCoolingDown) {
            return ;
        }

        Sound effect = new Sound("sound/activate.wav");
        effect.play();

        if(currentHeart == 3) {
            return;
        }

        isCoolingDown = true;

        ++currentHeart;
        updateHeartDisplay();

        Timer coolDown = new Timer(30000, e ->{
            effect.close();
            isCoolingDown = false;
            ((Timer)e.getSource()).stop();
        });
        coolDown.setRepeats(false);
        coolDown.start();
    }

    /**
     * Kích hoạt kỹ năng **Meat Artifact**: hồi ngẫu nhiên 1 đến 3 tim và bắt đầu hồi chiêu 60 giây.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh kích hoạt.</li>
     *   <li>Tăng số tim hiện tại thêm ngẫu nhiên từ 1–3, tối đa 3 tim.</li>
     *   <li>Thời gian hồi chiêu: 60 giây.</li>
     * </ul>
     * Nếu đã đủ tim hoặc đang trong thời gian hồi chiêu, kỹ năng không được thi triển.
     */
    public void Meat() {
        if(isCoolingDown) {
            return ;
        }

        Sound effect = new Sound("sound/activate.wav");
        effect.play();

        if(currentHeart == 3) {
            return ;
        }

        isCoolingDown = true;

        currentHeart += (int)(Math.random() * 3) + 1;
        if(currentHeart > 3) {
            currentHeart = 3;
        }
        updateHeartDisplay();

        Timer coolDown = new Timer(60000, e-> {
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        coolDown.setRepeats(false);
        coolDown.start();
    }

    /**
     * Kích hoạt kỹ năng **Brick Artifact**: tăng kích thước paddle thêm 20 đơn vị trong 10 giây.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh kích hoạt.</li>
     *   <li>Tăng chiều rộng của paddle thêm 20 đơn vị.</li>
     *   <li>Sau 10 giây, paddle trở lại kích thước ban đầu.</li>
     *   <li>Thời gian hồi chiêu: 40 giây.</li>
     * </ul>
     * Nếu đang trong thời gian hồi chiêu, kỹ năng sẽ không được kích hoạt.
     */
    public void Brick() {
        if(isCoolingDown) {
            return;
        }

        Sound effect = new Sound("sound/activate.wav");
        effect.play();

        isCoolingDown = true;

        int oldWidth = paddle.getWidth();
        paddle.setWidth(paddle.getWidth() + 20);
        paddlePrinter.setGameObject(paddle);
        layers.revalidate();
        layers.repaint();

        Timer durationMs = new Timer(10000, e ->{
            paddle.setWidth(oldWidth);
            paddlePrinter.setGameObject(paddle);
            layers.revalidate();
            layers.repaint();
            ((Timer) e.getSource()).stop();
        });
        durationMs.setRepeats(false);
        durationMs.start();

        Timer cooldown = new Timer(40000, e->{
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    /**
     * Kích hoạt kỹ năng **Boom Artifact**: gây 100 sát thương cho toàn bộ gạch trên màn hình.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh nổ và hiệu ứng rung màn hình.</li>
     *   <li>Mỗi viên gạch nhận 100 sát thương, gạch bị phá sẽ bị loại bỏ khỏi danh sách.</li>
     *   <li>Nếu có boss đang xuất hiện, boss cũng chịu 100 sát thương (trừ khi ở trạng thái miễn nhiễm).</li>
     *   <li>Thời gian hồi chiêu: 60 giây.</li>
     * </ul>
     * Nếu đang trong thời gian hồi chiêu, kỹ năng sẽ không được kích hoạt.
     */
    public void Boom() {
        if(isCoolingDown) {
            return;
        }

        Sound effect = new Sound("sound/boom.wav");
        effect.play();
        ScreenShakeEffect.shake(this, 400, 6);

        isCoolingDown = true;
        int boomDamage = 100;

        for(Brick b : bricks) {
            b.takeHit(boomDamage);
            if (b.isDestroyed()) {
                removed.add(b);

            } else {
                ObjectPrinter p = brickPrinters.get(b);
                if (p != null) p.startFlash();
            }
        }

        for (Brick b : removed) {
            bricks.remove(b);
            ObjectPrinter p = brickPrinters.remove(b);
            if (p != null) layers.remove(p);
        }

        //Nếu có boss
        if(bossForLevel != null && bossSpawned) {
            boolean canFlash = !("earth".equals(boss.getElement()) && boss.isInvulnerable());
            if (canFlash) {
                try {
                    boss.takeDamage(boomDamage);
                    bossPrinter.startFlash();
                } catch (Throwable ignore) {
                }
            }
            if (boss.isDead()) {
                cleanupBossSkills();
                layers.remove(bossPrinter);
                boss = null;
                layers.revalidate();
                layers.repaint();
            }

        }

        removed.clear();
        layers.revalidate();
        layers.repaint();

        Timer cooldown = new Timer(60000, e->{
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    /**
     * Kích hoạt kỹ năng **Fire Artifact**: tiêu hao 1 tim để tăng 300 sát thương trong 10 giây.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh hiệu ứng kích hoạt.</li>
     *   <li>Tăng sát thương bóng thêm 300 điểm trong 10 giây.</li>
     *   <li>Giảm 1 tim hiện tại của người chơi.</li>
     *   <li>Thời gian hồi chiêu: 90 giây.</li>
     * </ul>
     * Nếu đang hồi chiêu, kỹ năng sẽ không kích hoạt.
     */
    public void Fire() {
        if(isCoolingDown) {
            return;
        }

        Sound effect = new Sound("sound/activate.wav");
        effect.play();

        isCoolingDown = true;

        ball.setBaseDamage(ball.getBaseDamage() + 300);
        currentHeart--;
        ballPrinter.setGameObject(ball);
        updateHeartDisplay();

        layers.revalidate();
        layers.repaint();

        Timer durationMs = new Timer(10000, e ->{
            ball.setBaseDamage(currentDamage);
            ballPrinter.setGameObject(ball);

            layers.revalidate();
            layers.repaint();
            ((Timer) e.getSource()).stop();
        });
        durationMs.setRepeats(false);
        durationMs.start();

        Timer cooldown = new Timer(90000, e->{
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    /**
     * Kích hoạt kỹ năng **Helmet Artifact**: giúp người chơi miễn nhiễm sát thương trong 5 giây.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh kích hoạt.</li>
     *   <li>Người chơi trở nên không thể bị tổn thương trong 5 giây (unbreakable = true).</li>
     *   <li>Hết 5 giây, hiệu ứng kết thúc.</li>
     *   <li>Thời gian hồi chiêu: 70 giây.</li>
     * </ul>
     * Nếu đang hồi chiêu, kỹ năng sẽ không hoạt động.
     */
    public void Helmet() {
        if(isCoolingDown) {
            return;
        }

        Sound effect = new Sound("sound/activate.wav");
        effect.play();

        isCoolingDown = true;

        unbreakable = true;

        layers.revalidate();
        layers.repaint();

        Timer durationMs = new Timer(5000, e ->{
            unbreakable = false;

            layers.revalidate();
            layers.repaint();
            ((Timer) e.getSource()).stop();
        });
        durationMs.setRepeats(false);
        durationMs.start();

        Timer cooldown = new Timer(70000, e->{
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    /**
     * Kích hoạt kỹ năng **Diamond Artifact**: tăng vĩnh viễn 50 sát thương cho bóng.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh kích hoạt.</li>
     *   <li>Tăng sát thương cơ bản của bóng thêm 50 điểm, hiệu ứng vĩnh viễn.</li>
     *   <li>Thời gian hồi chiêu: 60 giây.</li>
     * </ul>
     * Nếu đang hồi chiêu, kỹ năng sẽ không được thi triển.
     */
    public void Diamond() {
        if(isCoolingDown) {
            return;
        }

        Sound effect = new Sound("sound/activate.wav");
        effect.play();

        isCoolingDown = true;

        ball.setBaseDamage(ball.getBaseDamage() + 50);
        currentDamage += 50;

        layers.revalidate();
        layers.repaint();

        Timer cooldown = new Timer(60000, e->{
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    /**
     * Kích hoạt kỹ năng **Chest Artifact**: nhận ngẫu nhiên từ 1–300 xu.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh kích hoạt.</li>
     *   <li>Thêm số tiền ngẫu nhiên (1–300) vào ví của người chơi.</li>
     *   <li>Thời gian hồi chiêu: 200 giây.</li>
     * </ul>
     * Nếu đang trong thời gian hồi chiêu, kỹ năng sẽ không hoạt động.
     */
    public void Chest() {
        if(isCoolingDown) {
            return;
        }

        Sound effect = new Sound("sound/activate.wav");
        effect.play();

        isCoolingDown = true;
        amount.add((int)(Math.random() * 300) + 1);

        Timer cooldown = new Timer(200000, e->{
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    /**
     * Kích hoạt kỹ năng **Clock Artifact**: giảm tốc độ bóng xuống một nửa trong 30 giây.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh kích hoạt.</li>
     *   <li>Giảm tốc độ di chuyển của bóng còn 1/2 ban đầu.</li>
     *   <li>Thời gian hồi chiêu: 30 giây.</li>
     * </ul>
     */
    public void Clock() {
        if(isCoolingDown) {
            return;
        }

        Sound effect = new Sound("sound/activate.wav");
        effect.play();

        isCoolingDown = true;
        int oldDx = ball.getDx();
        int oldDy = ball.getDy();

        ball.setDx(Math.abs(oldDx / 2));
        ball.setDy(Math.abs(oldDy / 2));

        Timer timer = new Timer(30000, e ->{
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }
    /**
     * Kích hoạt kỹ năng **Lightning Artifact**: bắn tia sét thẳng đứng gây 30.000 sát thương cho tất cả gạch trên đường bắn.
     * <p>
     * Khi kích hoạt:
     * <ul>
     *   <li>Phát âm thanh sét và hiệu ứng rung màn hình.</li>
     *   <li>Tạo tia laser dọc từ vị trí paddle hiện tại, gây 30.000 sát thương cho tất cả gạch bị chạm.</li>
     *   <li>Nếu có boss trong vùng ảnh hưởng, boss cũng nhận sát thương tương ứng.</li>
     *   <li>Hiệu ứng tồn tại trong 0.2 giây, sau đó biến mất.</li>
     *   <li>Thời gian hồi chiêu: 10 giây.</li>
     * </ul>
     * Nếu đang hồi chiêu, kỹ năng sẽ không kích hoạt.
     */
    public void Lightning() {
        if (isCoolingDown) {
            return;
        }

        Sound effect = new Sound("sound/lightning.wav");
        effect.play();
        ScreenShakeEffect.shake(this,  300, 6);

        isCoolingDown = true;

        //Vi tri cua laser phu thuoc vao vi tri paddle hien tai do
        int laserDamage = 30000;
        int laserWidth = 10;
        int laserHeight = GAME_HEIGHT;
        int laserX = paddle.getX() + paddle.getWidth() / 2 - laserWidth / 2;
        int laserY = paddle.getY();

        LaserBeam laser = new LaserBeam(laserX, laserY, laserWidth, laserHeight);
        Rectangle laserRect = new Rectangle(paddle.getX() + paddle.getWidth() / 2 - laserWidth / 2,
                0, 10, paddle.getY());
        for(Brick b : bricks) {
            Rectangle bRect = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            if (laserRect.intersects(bRect)) {
                b.takeHit(laserDamage);
                if (b.isDestroyed()) {
                    removed.add(b);

                } else {
                    ObjectPrinter p = brickPrinters.get(b);
                    if (p != null) p.startFlash();
                }
            }
        }

        for (Brick b : removed) {
            bricks.remove(b);
            ObjectPrinter p = brickPrinters.remove(b);
            if (p != null) layers.remove(p);
        }
        removed.clear();

        //Neu co boss
        if(bossForLevel != null && bossSpawned) {
            Rectangle bossRect = new Rectangle(boss.getX(), boss.getY(), boss.getWidth(), boss.getHeight());
            if (laserRect.intersects(bossRect)) {
                boolean canFlash = !("earth".equals(boss.getElement()) && boss.isInvulnerable());
                if (canFlash) {
                    try {
                        boss.takeDamage(laserDamage);
                        bossPrinter.startFlash();
                    } catch (Throwable ignore) {
                    }
                }
                if (boss.isDead()) {
                    cleanupBossSkills();
                    layers.remove(bossPrinter);
                    boss = null;
                    layers.revalidate();
                    layers.repaint();
                }
            }
        }

        layers.add(laser, Integer.valueOf(13));
        layers.revalidate();
        layers.repaint();

        Timer durationMs = new Timer(200, e ->{
            layers.remove(laser);
            layers.revalidate();
            layers.repaint();
            ((Timer) e.getSource()).stop();
        });
        durationMs.setRepeats(false);
        durationMs.start();

        Timer cooldown = new Timer(10000, e->{
            effect.close();
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    /**
     * Bắt đầu đếm thời gian hồi chiêu cho Artifact hiện tại.
     */
    private void startArtifactCooldownTimer() {
        if (artifactTimer != null && artifactTimer.isRunning()) {
            artifactTimer.stop();
        }

        artifactTimer = new Timer(1000, e -> {
            artifactSlot.tickCooldown();
            if (!artifactSlot.isCoolingDown()) {
                ((Timer) e.getSource()).stop();
            }
        });
        artifactTimer.start();
    }


    /**
     * Xử lý sự kiện bàn phím khi người chơi nhấn phím.
     * <p>
     * Bao gồm:
     * <ul>
     *   <li><b>← / →</b>: di chuyển paddle, đảm bảo chỉ một hướng được nhấn cùng lúc.</li>
     *   <li><b>P</b>: tạm dừng (pause) hoặc tiếp tục trò chơi.</li>
     *   <li><b>F8</b>: (chế độ DEV) xóa toàn bộ gạch để kích hoạt logic boss.</li>
     *   <li><b>E</b>: kích hoạt Artifact hiện tại (nếu không trong thời gian hồi chiêu hoặc tạm dừng).</li>
     *   <li><b>SPACE</b>: bắn bóng khi đang dính vào paddle.</li>
     * </ul>
     * Các phím khác sẽ bị bỏ qua.
     *
     * @param e sự kiện phím được nhấn
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
            rightPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
            leftPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            togglePause();
        }
        // DEBUG: F8 -> dọn sạch gạch để kích hoạt boss logic
        if (e.getKeyCode() == KeyEvent.VK_F8) {
            // remove printers khỏi UI và map
            for (Brick b : new ArrayList<>(bricks)) {
                ObjectPrinter p = brickPrinters.remove(b);
                if (p != null) layers.remove(p);
            }
            bricks.clear();
            removed.clear();      // tránh double-remove ở tick sau
            totalBrick = 0;

            layers.revalidate();  // vì vừa remove components
            layers.repaint();

            System.out.println("[DEV] Cleared all bricks");
        }
        //Ấn E là kích hoạt artifact
        if(e.getKeyCode() == KeyEvent.VK_E) {
            if (artifactSlot.isCoolingDown()) {
                return;
            }
            if (isPause) {
                return;
            }
            if (isBallActive == false) {
                return;
            }
            int cooldownSeconds = item.getCooldown() / 1000;
            artifactSlot.setCooldown(cooldownSeconds);

            //Bắt đầu đếm thời gian hồi chiêu
            startArtifactCooldownTimer();

            //Đưa vào tên artifact mà có hàm chức năng tương ứng
            String artifactName = item.getName();
            switch (artifactName) {
                case "Heart":
                    Heart();
                    break;
                case "Sword":
                    Sword();
                    break;
                case "Bow":
                    Bow();
                    break;
                case "Boom":
                    Boom();
                    break;
                case "Helmet":
                    Helmet();
                    break;
                case "Diamond":
                    Diamond();
                    break;
                case "Fire":
                    Fire();
                    break;
                case "Clock":
                    Clock();
                    break;
                case "Treasure Chest":
                    Chest();
                    break;
                case "Lightning":
                    Lightning();
                    break;
                case "Brick":
                    Brick();
                    break;
                case "Meat":
                    Meat();
                    break;
                default:
                    break;
            }
        }
        //Khi bóng dính vào paddle có thể ấn space để bắn
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(isPause) {
                return; // pause thì không làm gì cả
            }
            if(isBallActive == true) {
                return;
            }
            else {
                isBallActive = true;
                ball.setDx(0);
                ball.setDy(-10);
            }
        }
    }

    /**
     * Xử lý khi người chơi thả phím.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;
            rightPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;
            leftPressed = false;
        }
    }

    /**
     * Xử lý sự kiện khi người chơi gõ phím (key typed).
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
            rightPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
            leftPressed = false;
        }
    }

    /**
     * Khi cửa sổ trò chơi bị đóng:
     * <ul>
     *   <li>Dừng nhạc nền của trò chơi (bgm).</li>
     *   <li>Phát lại nhạc nền của menu chính.</li>
     *   <li>Hiển thị lại menu cha (parentMenu).</li>
     * </ul>
     *
     * @param e sự kiện cửa sổ
     */
    @Override
    public void windowClosed(WindowEvent e) {
        bgm.close();
        MapMenu.backgroundMusic.loop(); // bật lại nhạc menu
        parentMenu.setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}


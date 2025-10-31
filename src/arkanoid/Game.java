package arkanoid;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import arkanoid.*;

import PowerUp.*;

public class Game extends JFrame implements ActionListener, KeyListener, WindowListener {

    //Thong so khung hinh
    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 700;
    public static final int PLAYFRAME_WIDTH = 800;
    public static final int PLAYFRAME_HEIGHT = 700;
    private static final int TICK_MS = 16;

    //Am thanh man choi
    private Sound bgm = new Sound("sound/CombatSound.wav");

    //heart, so mau trong tro choi
    public int currentHeart;
    public ArrayList<JLabel> heart = new ArrayList<>();

    //Trang thai pause hay chua
    private boolean isPause = false;
    private JButton pauseButton;
    //
    //Nut dau hang
    private JButton surrenderButton;

    //paddle
    private final Paddle paddle;

    //ball
    private final Ball ball;

    //dx, dy mac dinh cua ball
    private final int initDx;
    private final int initDy;

    //bricks
    private int totalBrick; // so gach 1 man choi
    private final List<Brick> bricks = new ArrayList<>();
    private final List<Brick> removed = new ArrayList<>();
    private static final int BRICK_W = 80;
    private static final int BRICK_H = 30;
    private static final int STEP_X = 80;
    private static final int STEP_Y = 30;
    private static final int PLAY_LEFT = 0;

    //cong cu ho tro in hinh anh
    private final ObjectPrinter paddlePrinter = new ObjectPrinter();
    private final ObjectPrinter ballPrinter = new ObjectPrinter();
    private final JLayeredPane layers = new JLayeredPane();
    private final Map<Brick, ObjectPrinter> brickPrinters = new HashMap<>();
    private final String gameScene;
    //timer
    private final javax.swing.Timer timer = new javax.swing.Timer(TICK_MS, this);

    // trang thai (fix delay)
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private final Rectangle tempPuRect = new Rectangle();
    private final Rectangle tempPaddleRect = new Rectangle();

    //Info hien thi tren stat bar

    // stat cua paddle
    private JLabel paddleTitle;
    private JLabel paddleWidthLabel;
    private JLabel paddleDxLabel;

    // stat ball
    private JLabel ballTitle;
    private JLabel ballElementLabel;
    private JLabel ballDxLabel;
    private JLabel ballDyLabel;
    private JLabel ballDamageLabel;

    // stat artifact
    private JLabel artifactTitle;
    private boolean isCoolingDown = false;
    private boolean unbreakable = false;

    // PowerUp
    private final PowerUpManager powerUpManager;
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final Map<PowerUp, ObjectPrinter> powerUpPrinters = new HashMap<>();

    // ===== Boss + Fire Rain =====
    private Boss boss = null;                         // chỉ spawn 1 lần ở cuối màn
    private boolean bossSpawned = false;
    private final ObjectPrinter1 bossPrinter = new ObjectPrinter1();
    private final arkanoid.FireballLayer fireballLayer;              // 1 layer vẽ tất cả fireball
    private final Rectangle tmpR2 = new Rectangle();
    private int bossSpawnGrace = 0; // số tick miễn va chạm ngay sau khi spawn

    // ==== Levels ====
    private final int currentLevel;
    private final java.util.List<Boolean> levelStatus;
    private final Boss bossForLevel; // null = level này không có boss

    public Game(Paddle currentPaddle, Ball currentBall, String level, String currentGameScene,
                int currentLevel, java.util.List<Boolean> levelStatus, Boss bossForLevel) {
        super("Arkanoid (Ball + Brick)");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

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

        paddlePrinter.setGameObject(paddle);
        ballPrinter.setGameObject(ball);

        powerUpManager = new PowerUpManager(this);

        layers.add(paddlePrinter, Integer.valueOf(9));
        layers.add(ballPrinter, Integer.valueOf(10));

        //bricks
        /*
        for(int i = 0; i < currentBricks.size(); i++) {

            Brick b = new Brick();
            b = currentBricks.get(i);
            bricks.add(b);

            ObjectPrinter bp = new ObjectPrinter();
            configurePrinter(bp);

            bp.setGameObject(b);
            brickPrinters.put(b, bp);
            layers.add(bp, Integer.valueOf(8));
        }
         */
        int[][] grid;
        try {
            grid = Brick.readGrid(level);
        } catch (java.io.IOException e) {
            throw new RuntimeException("load grid fail", e);
        }
        int rows = grid.length;
        int cols = grid[0].length;
        int totalW = BRICK_W + (cols - 1) * STEP_X;
        int originX = PLAY_LEFT + (PLAYFRAME_WIDTH - totalW) / 2;
        int originY = 80;

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
        artifactTitle.setFont(new java.awt.Font("Adobe Garamond Pro", 1, 25));
        artifactTitle.setForeground(java.awt.Color.YELLOW); // mau
        layers.add(artifactTitle, Integer.valueOf(2));

        // --- FIRE BALL ---
        fireballLayer = new FireballLayer(PLAYFRAME_WIDTH, PLAYFRAME_HEIGHT);
        layers.add(fireballLayer, Integer.valueOf(7));

        setVisible(true);

        //them keylistender
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        //them windowlistener
        addWindowListener(this);

        timer.start();
    }

    private void togglePause() {
        if (isPause) {
            // luc nay dang pause, an vao de tiep tuc game
            pauseButton.setIcon(new ImageIcon(new ImageIcon("img/pauseButton.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            pauseButton.setRolloverIcon(new ImageIcon(new ImageIcon("img/pauseHover.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            timer.start();
            isPause = false;
            this.requestFocusInWindow();
        } else {
            // game dang dien ra thi tam dung
            pauseButton.setIcon(new ImageIcon(new ImageIcon("img/pauseButton1.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            pauseButton.setRolloverIcon(new ImageIcon(new ImageIcon("img/pauseHover1.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            timer.stop();
            isPause = true;
        }
    }

    private void surrender() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Chua j da dau hang roi ga vcl");
        this.dispose();
    }

    private void configurePrinter(ObjectPrinter p) {
        p.setOpaque(false);
        p.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    private void configurePrinter1(ObjectPrinter1 p) {
        p.setOpaque(false);
        p.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    private boolean isBossDead() {
        return boss == null;
    }

    // Cập nhật giao diện tim dựa vào currentHeart
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


    //Khi kích hoạt hiệu ứng tim sẽ +1 tim và hồi trong khoảng 30s
    public void Heart(){
        if(isCoolingDown) {
            return ;
        }

        if(currentHeart == 3) {
            return;
        }

        isCoolingDown = true;

        ++currentHeart;
        updateHeartDisplay();

        Timer coolDown = new Timer(30000, e ->{
            isCoolingDown = false;
            ((Timer)e.getSource()).stop();
        });
        coolDown.setRepeats(false);
        coolDown.start();
    }

    // Khi kích hoạt sẽ nhận được 1 -> 3 trái tym mới , hồi chiêu 60s
    public void RandomHeart() {
        if(isCoolingDown) {
            return ;
        }

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
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        coolDown.setRepeats(false);
        coolDown.start();
    }

    // khi kích hoạt sẽ tăng kích thước của paddle lên 20 hồi chiêu trong 40s
    public void ExpandPaddle() {
        if(isCoolingDown) {
            return;
        }

        isCoolingDown = true;

        int oldWidth = paddle.getWidth();
        int oldHeight = paddle.getHeight();
        paddle.setWidth(paddle.getWidth() + 20);
        paddle.setHeight(paddle.getHeight() + 20);
        paddlePrinter.setGameObject(paddle);
        layers.revalidate();
        layers.repaint();

        Timer durationMs = new Timer(30000, e ->{
            paddle.setWidth(oldWidth);
            paddle.setHeight(oldHeight);
            paddlePrinter.setGameObject(paddle);
            layers.revalidate();
            layers.repaint();
            ((Timer) e.getSource()).stop();
        });
        durationMs.setRepeats(false);
        durationMs.start();

        Timer cooldown = new Timer(40000, e->{
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    // khi kích hoạt sẽ gây 100 sát thương cho toàn bộ gạch, cooldown 60s
    public void Boom() {
        if(isCoolingDown) {
            return;
        }

        isCoolingDown = true;

        for(Brick b : bricks) {
            b.takeHit(100);
        }

        Timer cooldown = new Timer(60000, e->{
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    // khi kích hoạt -1 máu, +300 sát thương trong 10 giây, cooldown 90s
    public void Fire() {
        if(isCoolingDown) {
            return;
        }

        isCoolingDown = true;

        int oldDamage = ball.getBaseDamage();
        ball.setBaseDamage(oldDamage + 300);
        currentHeart--;
        ballPrinter.setGameObject(ball);

        layers.revalidate();
        layers.repaint();

        Timer durationMs = new Timer(10000, e ->{
            ball.setBaseDamage(oldDamage);
            ballPrinter.setGameObject(ball);

            layers.revalidate();
            layers.repaint();
            ((Timer) e.getSource()).stop();
        });
        durationMs.setRepeats(false);
        durationMs.start();

        Timer cooldown = new Timer(90000, e->{
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    // khi kích hoạt miễn nhiễm sát thương trong 5s, cooldown 70s
    public void Helmet() {
        if(isCoolingDown) {
            return;
        }

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
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }

    // khi kích hoạt tăng vĩnh viễn 50 sát thương, 60s hồi chiêu
    public void Diamond() {
        if(isCoolingDown) {
            return;
        }

        isCoolingDown = true;

        ball.setBaseDamage(ball.getBaseDamage() + 50);

        layers.revalidate();
        layers.repaint();

        Timer cooldown = new Timer(60, e->{
            isCoolingDown = false;
            ((Timer) e.getSource()).stop();
        });
        cooldown.setRepeats(false);
        cooldown.start();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (bricks.isEmpty()) {
            if (bossForLevel == null) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Yee thang roi");
                this.dispose();
                return;
            }
            if (!bossSpawned) {
                boss = bossForLevel;                 // dùng bossForLevel
                boss.setX(PLAYFRAME_WIDTH / 2 - boss.getWidth() / 2);
                boss.setY(120);
                boss.setDx(3);
                boss.setDy(0);

                configurePrinter1(bossPrinter);
                bossPrinter.setGameObject(boss);
                layers.add(bossPrinter, Integer.valueOf(11));
                bossSpawned = true;
                bossSpawnGrace = 12;

                boss.activateFireRain();             // kích hoạt skill một lần
            } else if (isBossDead()) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Yee thang roi");
                this.dispose();
                return;
            }
        }

        if (currentHeart <= 0) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game over!, qua ngu");
            this.dispose();
            return;
        }

        //tuc la chua thua
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

        // Cập nhật bóng
        ball.step(new Rectangle(0, 0, PLAYFRAME_WIDTH, GAME_HEIGHT));
        ball.collideWithPaddle(paddle);

        // Mất mạng
        if (ball.getY() > GAME_HEIGHT) {
            currentHeart--;
            ball.setX(PLAYFRAME_WIDTH / 2 - ball.getWidth() / 2);
            ball.setY(GAME_HEIGHT - 120);
            ball.setDx(initDx);
            ball.setDy(initDy);

            if (currentHeart >= 0) {
                ImageIcon icon = new ImageIcon("img/heart/grayheart.png");
                Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                heart.get(currentHeart).setIcon(new ImageIcon(scaled));
            }
        }

        // Va chạm với gạch
        for (Brick b : bricks) {
            if (ball.collide(b)) {
                if (b.isDestroyed()) {
                    removed.add(b);

                    // 30% chance drop PowerUp
                    if (Math.random() < 0.3) {
                        PowerUp pu;

                        if(Math.random() < 0.3) {
                            pu = new PowerUpIncreaseDamage(b.getX() + 20, b.getY() + 10, new OwnedManager(ball));
                        }
                        else if(Math.random() < 0.4) {
                            pu = new PowerUpExtraHeart(b.getX() + 20, b.getY() + 10);
                        }
                        else {
                            pu = new PowerUpExpandPaddle(b.getX() + 20, b.getY() + 10, paddle);
                        }

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

        // ===== Boss logic =====
        if (bossSpawned && boss != null) {
            updateBossMovementAndCollision();
            updateBossSkills();
        }

        layers.revalidate();
        layers.repaint();
    }

    // === Boss: movement + va chạm bóng ===
    private void updateBossMovementAndCollision() {
        // di chuyển L-R trong playframe
        boss.tick();
        int nx = boss.getX() + boss.getDx();
        if (nx < 0) {
            nx = 0;
            boss.setDx(Math.abs(boss.getDx()));
        } else if (nx > PLAYFRAME_WIDTH - boss.getWidth()) {
            nx = PLAYFRAME_WIDTH - boss.getWidth();
            boss.setDx(-Math.abs(boss.getDx()));
        }
        boss.setX(nx);
        bossPrinter.setGameObject(boss);

        // bật lại bóng khi chạm boss
        if (bossSpawnGrace > 0) {
            bossSpawnGrace--;
            return;
        }
        if (ball.collideWithBoss(boss)) {
            try {
                bossPrinter.startFlash();
            } catch (Throwable ignore) {
            }
            if (boss.isDead()) {
                layers.remove(bossPrinter);
                boss = null; // tick sau isBossDead() = true
                layers.revalidate();
                layers.repaint();
            }
        }
    }

    // === Boss: kỹ năng theo element ===
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
        }

        // nếu có element khác thì thêm các nhánh else-if tại đây
    }

    private void maybeActivateFireRain() {
        // kích hoạt ngẫu nhiên
        if (Math.random() < 0.01) boss.activateFireRain();
    }

    private void handleFireballHitsPaddle() {
        List<? extends GameObject> fbs = boss.getFireballs();
        for (int i = 0; i < fbs.size(); ) {
            GameObject fb = fbs.get(i);

            boolean hit = paddle.collideFireball(fb, 0.5f);
            if (hit) {
                if (currentHeart > 0) {
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


    private void removePowerUp(PowerUp pu) {
        ObjectPrinter printer = powerUpPrinters.remove(pu);
        if (printer != null) layers.remove(printer);
    }


    //Tranh truong hop an cung luc ca left ca right, chi 1 trong 2 cai dc true
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

    }

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

    //implements cac phuong thuoc cua interface windowlistener
    //tat man cua so game thi bat bgm
    @Override
    public void windowClosed(WindowEvent e) {
        bgm.close();
        MapMenu.backgroundMusic.loop(); // bật lại nhạc menu
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


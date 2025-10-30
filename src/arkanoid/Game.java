package arkanoid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import arkanoid.*;

public class Game extends JFrame implements ActionListener, KeyListener, WindowListener {

    //Thong so khung hinh
    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 700;
    public static final int PLAYFRAME_WIDTH = 800;
    public static final int PLAYFRAME_HEIGHT = 700;
    private static final int TICK_MS = 16;

    //Am thanh man choi
    private Sound bgm = new Sound("sound/CombatSound.wav");
    //heart
    private int currentHeart;
    private ArrayList <JLabel> heart = new ArrayList<>();

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
    private static final int STEP_X  = 80;
    private static final int STEP_Y  = 30;
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


    public Game(Paddle currentPaddle, Ball currentBall, String level,
                String currentGameScene) {
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

        //paddle
        paddle = currentPaddle;

        //ball
        ball = currentBall;
        initDx = ball.getDx();
        initDy = ball.getDy();

        paddlePrinter.setGameObject(paddle);
        ballPrinter.setGameObject(ball);

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
            grid = arkanoid.Brick.readGrid(level);
        } catch (java.io.IOException e) {
            throw new RuntimeException("load grid fail", e);
        }
        int rows = grid.length;
        int cols = grid[0].length;
        int totalW  = BRICK_W + (cols - 1) * STEP_X;
        int originX = PLAY_LEFT + (PLAYFRAME_WIDTH - totalW) / 2;
        int originY = 80;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int id = grid[r][c];
                if (id == 0) continue;
                int x = originX + c * STEP_X;
                int y = originY + r * STEP_Y;
                Brick b = arkanoid.Brick.createBrickFromId(id, x, y);
                if (b == null) continue;
                bricks.add(b);
                arkanoid.ObjectPrinter bp = new arkanoid.ObjectPrinter();
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
        paddleTitle.setFont(new Font("Arial", Font.BOLD, 25));
        paddleTitle.setForeground(Color.YELLOW);
        layers.add(paddleTitle, Integer.valueOf(2));

        paddleWidthLabel = new JLabel("Width: " + paddle.getWidth());
        paddleWidthLabel.setBounds(840, 205, 200, 15);
        paddleWidthLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        paddleWidthLabel.setForeground(Color.WHITE);
        layers.add(paddleWidthLabel, Integer.valueOf(2));

        paddleDxLabel = new JLabel("Speed: " + paddle.getDx());
        paddleDxLabel.setBounds(840, 225, 200, 15);
        paddleDxLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        paddleDxLabel.setForeground(Color.WHITE);
        layers.add(paddleDxLabel, Integer.valueOf(2));

        // --- BALL INFO ---
        ballTitle = new JLabel("Ball");
        ballTitle.setBounds(840, 260, 200, 25);
        ballTitle.setFont(new Font("Arial", Font.BOLD, 25));
        ballTitle.setForeground(Color.YELLOW);
        layers.add(ballTitle, Integer.valueOf(2));

        ballElementLabel = new JLabel("Element: " + ball.getElement());
        ballElementLabel.setBounds(840, 290, 200, 15);
        ballElementLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        ballElementLabel.setForeground(Color.WHITE);
        layers.add(ballElementLabel, Integer.valueOf(2));

        ballDxLabel = new JLabel("Dx: " + ball.getDx());
        ballDxLabel.setBounds(840, 310, 200, 15);
        ballDxLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        ballDxLabel.setForeground(Color.WHITE);
        layers.add(ballDxLabel, Integer.valueOf(2));

        ballDyLabel = new JLabel("Dy: " + ball.getDy());
        ballDyLabel.setBounds(840, 330, 200, 15);
        ballDyLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        ballDyLabel.setForeground(Color.WHITE);
        layers.add(ballDyLabel, Integer.valueOf(2));

        ballDamageLabel = new JLabel("Damage: " + ball.getBaseDamage());
        ballDamageLabel.setBounds(840, 350, 200, 15);
        ballDamageLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        ballDamageLabel.setForeground(Color.WHITE);
        layers.add(ballDamageLabel, Integer.valueOf(2));

        // --- ARTIFACT INFO ---
        artifactTitle = new JLabel("Artifact");
        artifactTitle.setBounds(840, 385, 200, 25); // vi tri va kich thuoc
        artifactTitle.setFont(new java.awt.Font("Arial", 1, 25));
        artifactTitle.setForeground(java.awt.Color.YELLOW); // mau
        layers.add(artifactTitle, Integer.valueOf(2));


        setVisible(true);

        //them keylistender
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        //them windowlistener
        addWindowListener(this);

        timer.start();
    }

    private void configurePrinter(ObjectPrinter p) {
        p.setOpaque(false);
        p.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (removed.size() == totalBrick) { // tuc la thang roi
            timer.stop();
            JOptionPane.showMessageDialog(this, "Yee thang roi");
            this.dispose();
        } else {
            if (currentHeart > 0) {//tuc la chua thua
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

                ball.step(new Rectangle(0, 0, PLAYFRAME_WIDTH, GAME_HEIGHT));
                ball.collideWithPaddle(paddle);

                if (ball.getY() > GAME_HEIGHT) {
                    //giam 1 heart
                    currentHeart--;

                    //dua bong ve lai vi tri ban dau
                    ball.setX(PLAYFRAME_WIDTH / 2 - ball.getWidth() / 2);
                    ball.setY(GAME_HEIGHT - 120);
                    ball.setDx(initDx);
                    ball.setDy(initDy);

                    //sua hien thi tim
                    ImageIcon icon = new ImageIcon("img/heart/grayheart.png");
                    Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    ImageIcon grayHeart = new ImageIcon(scaled);
                    heart.get(currentHeart).setIcon(grayHeart);
                }


                for (Brick b : bricks) {
                    if (ball.collide(b)) {
                        if (b.isDestroyed()) {
                            removed.add(b);
                        } else {
                            ObjectPrinter p = brickPrinters.get(b);
                            if (p != null) p.startFlash();
                        }
                        //bóng chỉ va chạm 1 gạch 1 lần
                        // từ từ nhé, chỗ này sẽ phải bỏ, nếu nhận được buff đi xuyên.
                        break;
                    }
                }

                if (!removed.isEmpty()) {
                    for (Brick b : removed) {
                        bricks.remove(b);
                        ObjectPrinter p = brickPrinters.remove(b);
                        if (p != null) layers.remove(p);
                    }
                }

                paddlePrinter.setGameObject(paddle);
                ballPrinter.setGameObject(ball);

                // update stat bar
                paddleWidthLabel.setText("Width: " + paddle.getWidth());
                paddleDxLabel.setText("Speed: " + paddle.getDx());

                ballElementLabel.setText("Element: " + ball.getElement());
                ballDxLabel.setText("Dx: " + ball.getDx());
                ballDyLabel.setText("Dy: " + ball.getDy());
                ballDamageLabel.setText("Damage: " + ball.getBaseDamage());

                layers.revalidate();
                layers.repaint();
            } else {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game over!, qua ngu");
                this.dispose();
            }
        }
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import arkanoid.ObjectPrinter;

public class Game extends JFrame implements ActionListener, KeyListener {

    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 700;
    public static final int PLAYFRAME_WIDTH = 800;
    public static final int PLAYFRAME_HEIGHT = 700;
    private static final int TICK_MS = 16;

    //private final Paddle paddle = new Paddle(500, 500, 120, 20,
    //        15, 0, "img/paddle/paddlevip.png");

    //paddle
    private final Paddle paddle;

    //ball
    private final Ball ball;

    //dx, dy mac dinh cua ball
    private final int initDx;
    private final int initDy;

    //bricks
    private final List<Brick> bricks = new ArrayList<>();

    private final ObjectPrinter paddlePrinter = new ObjectPrinter();
    private final ObjectPrinter ballPrinter = new ObjectPrinter();
    private final JLayeredPane layers = new JLayeredPane();
    private final Map<Brick, ObjectPrinter> brickPrinters = new HashMap<>();
    private final String gameScene;

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

    // stat artiface
    private JLabel artifactTitle;


    public Game(Paddle currentPaddle, Ball currentBall, ArrayList <Brick> currentBricks,
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

        configurePrinter(paddlePrinter);
        configurePrinter(ballPrinter);

        //String BALL_IMG = "img/ball/bongnguhanh.png";
        //String BRICK_IMG = "img/brick/red_brick_cropped.png";

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

        /*int rows = 6;
        int cols = 10;
        int brickW = 90;
        int brickH = 50;
        int marginY = 40;
        int gap = 2;
        int startX = (GAME_WIDTH - (cols * brickW + (cols - 1) * gap)) / 2;
        int startY = marginY;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = startX + c * (brickW + gap);
                int y = startY + r * (brickH + gap);
                Brick b = new Brick(x, y, brickW, brickH, 50, BRICK_IMG);
                bricks.add(b);

                ObjectPrinter bp = new ObjectPrinter();
                configurePrinter(bp);
                bp.setGameObject(b);
                brickPrinters.put(b, bp);
                layers.add(bp, Integer.valueOf(5));
            }
        }
         */

        //bricks
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

        setContentPane(layers);

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

        /*
        JLabel text = new JLabel("Paddle");
        text.setBounds(840, 175, 200, 25); // vi tri va kich thuoc
        text.setFont(new java.awt.Font("Arial", 1, 25));
        text.setForeground(java.awt.Color.YELLOW); // mau
        layers.add(text, Integer.valueOf(2));

        text = new JLabel("Width: " + paddle.getWidth());
        text.setBounds(840, 205, 200, 15); // vi tri va kich thuoc
        text.setFont(new java.awt.Font("Arial", 0, 15));
        text.setForeground(java.awt.Color.WHITE); // mau
        layers.add(text, Integer.valueOf(2));

        text = new JLabel("Dx: " + paddle.getDx());
        text.setBounds(840, 225, 200, 15); // vi tri va kich thuoc
        text.setFont(new java.awt.Font("Arial", 0, 15));
        text.setForeground(java.awt.Color.WHITE); // mau
        layers.add(text, Integer.valueOf(2));

        text = new JLabel("Ball");
        text.setBounds(840, 260, 200, 25); // vi tri va kich thuoc
        text.setFont(new java.awt.Font("Arial", 1, 25));
        text.setForeground(java.awt.Color.YELLOW); // mau
        layers.add(text, Integer.valueOf(2));

        text = new JLabel("Element: " + ball.getElement());
        text.setBounds(840, 290, 200, 15); // vi tri va kich thuoc
        text.setFont(new java.awt.Font("Arial", 0, 15));
        text.setForeground(java.awt.Color.WHITE); // mau
        layers.add(text, Integer.valueOf(2));

        text = new JLabel("Dx: " + ball.getDx());
        text.setBounds(840, 310, 200, 15); // vi tri va kich thuoc
        text.setFont(new java.awt.Font("Arial", 0, 15));
        text.setForeground(java.awt.Color.WHITE); // mau
        layers.add(text, Integer.valueOf(2));

        text = new JLabel("Dy: " + ball.getDy());
        text.setBounds(840, 330, 200, 15); // vi tri va kich thuoc
        text.setFont(new java.awt.Font("Arial", 0, 15));
        text.setForeground(java.awt.Color.WHITE); // mau
        layers.add(text, Integer.valueOf(2));

        text = new JLabel("Damage: " + ball.getBaseDamage());
        text.setBounds(840, 350, 200, 15); // vi tri va kich thuoc
        text.setFont(new java.awt.Font("Arial", 0, 15));
        text.setForeground(java.awt.Color.WHITE); // mau
        layers.add(text, Integer.valueOf(2));

        text = new JLabel("Artifact");
        text.setBounds(840, 385, 200, 25); // vi tri va kich thuoc
        text.setFont(new java.awt.Font("Arial", 1, 25));
        text.setForeground(java.awt.Color.YELLOW); // mau
        layers.add(text, Integer.valueOf(2));

         */

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

        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        timer.start();
    }

    private void configurePrinter(ObjectPrinter p) {
        p.setOpaque(false);
        p.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
            ball.setX(PLAYFRAME_WIDTH / 2 - ball.getWidth() / 2);
            ball.setY(GAME_HEIGHT - 120);
            ball.setDx(initDx);
            ball.setDy(initDy);
        }

        List<Brick> removed = new ArrayList<>();
        for (Brick b : bricks) {
            if (ball.collide(b) && b.isDestroyed()) {
                removed.add(b);
            }
        }

        if (!removed.isEmpty()) {
            for (Brick b : removed) {
                bricks.remove(b);
                ObjectPrinter p = brickPrinters.remove(b);
                if (p != null) layers.remove(p);
            }

            layers.revalidate();
            layers.repaint();
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
    }

    /*
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            //player.x -= player.dx;
            paddle.setX(Math.max(paddle.getX() - paddle.getDx(), 0));
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.setX(Math.min(paddle.getX() + paddle.getDx(), GAME_WIDTH - paddle.getWidth()));
        }
    }
     */
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
}

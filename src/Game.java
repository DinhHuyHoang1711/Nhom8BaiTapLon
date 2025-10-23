import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Game extends JFrame implements ActionListener, KeyListener {

    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 700;
    private static final int TICK_MS = 16;

    private final Paddle paddle = new Paddle(500, 500, 120, 40,
            10, 0, "img/paddle/paddlevip.png");

    private final Ball ball;

    private final List<Brick> bricks = new ArrayList<>();

    private final ObjectPrinter paddlePrinter = new ObjectPrinter();
    private final ObjectPrinter ballPrinter = new ObjectPrinter();
    private final JLayeredPane layers = new JLayeredPane();
    private final Map<Brick, ObjectPrinter> brickPrinters = new HashMap<>();

    private final javax.swing.Timer timer = new javax.swing.Timer(TICK_MS, this);

    // trang thai (fix delay)
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public Game() {
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

        String BALL_IMG = "img/ball/bongnguhanh.png";
        String BRICK_IMG = "img/brick/red_brick_cropped.png";

        ball = new Ball(GAME_WIDTH / 2 - 30, GAME_HEIGHT - 120, 41, 6, -8, BALL_IMG);
        paddlePrinter.setGameObject(paddle);
        ballPrinter.setGameObject(ball);

        layers.add(paddlePrinter, Integer.valueOf(9));
        layers.add(ballPrinter, Integer.valueOf(10));

        int rows = 6;
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
                Brick b = new Brick(x, y, brickW, brickH, 1, BRICK_IMG);
                bricks.add(b);

                ObjectPrinter bp = new ObjectPrinter();
                configurePrinter(bp);
                bp.setGameObject(b);
                brickPrinters.put(b, bp);
                layers.add(bp, Integer.valueOf(5));
            }
        }

        setContentPane(layers);

        ImageIcon icon = new ImageIcon("img/Beach.jpg");
        Image scaled = icon.getImage().getScaledInstance(GAME_WIDTH, GAME_HEIGHT, Image.SCALE_SMOOTH);
        JLabel bg = new JLabel(new ImageIcon(scaled));
        bg.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
        layers.add(bg, Integer.valueOf(0));

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
        }
        if (rightPressed) {
            paddle.setX(Math.min(paddle.getX() + paddle.getDx(), GAME_WIDTH - paddle.getWidth()));
        }

        ball.step(new Rectangle(0, 0, GAME_WIDTH, GAME_HEIGHT));
        ball.collideWithPaddle(paddle);

        if (ball.getY() > GAME_HEIGHT) {
            ball.setX(GAME_WIDTH / 2 - ball.getWidth() / 2);
            ball.setY(GAME_HEIGHT - 120);
            ball.setDx(6);
            ball.setDy(-8);
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
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
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
}
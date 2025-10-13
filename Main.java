import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


class Item {
    public int x, y, width, height, dx, dy;
}

class Renderer extends JPanel implements Runnable, KeyListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int BRICK_X = 10;
    public static final int BRICK_Y = 6;
    public static final int BRICK_WIDTH = 62;
    public static final int BRICK_HEIGHT = 34;

    private Item player;
    private Item ball;
    private Item[] bricks;
    private int amount = 0;
    private int destroyed = 0;
    private Thread gameThread;
    private Random rand = new Random();
    private boolean gameOver = false;
    private boolean gameWin = false;

    public void setup() {
        gameThread = new Thread(this);
        gameThread.start();

        player = new Item();
        player.width = 120;
        player.height = 15;
        player.x = WIDTH / 2 - player.width / 2;
        player.y =  500;
        player.dx = 15;

        ball = new Item();
        ball.width = 20;
        ball.height = 20;
        ball.x = WIDTH / 2 - ball.width / 2;
        ball.y =  HEIGHT / 2 - ball.height/2 - 40;
        ball.dx = -1;
        ball.dy = 3;

        bricks = new Item[BRICK_X * BRICK_Y];
        for (int i = 0; i < BRICK_X; i++) {
            for (int j = 0; j < BRICK_Y; j++) {
                bricks[amount] = new Item();
                bricks[amount].x = i * BRICK_WIDTH + 82;
                bricks[amount].y = j * BRICK_HEIGHT + 30;
                amount++;
            }
        }
    }

    private void logic() {
        if (!gameOver && !gameWin) {

            ball.x += ball.dx;
            for (int i = 0; i < amount; i++) {
                if (new Rectangle(bricks[i].x, bricks[i].y, BRICK_WIDTH, BRICK_HEIGHT).intersects(
                        new Rectangle(ball.x, ball.y, ball.width, ball.height)
                )) {
                    ball.dx *= -1;
                    bricks[i].x = -100;
                    destroyed++;
                }
            }
            ball.y += ball.dy;
            for (int i = 0; i < amount; i++) {
                if (new Rectangle(bricks[i].x, bricks[i].y, BRICK_WIDTH, BRICK_HEIGHT).intersects(
                        new Rectangle(ball.x, ball.y, ball.width, ball.height)
                )) {
                    ball.dy *= -1;
                    bricks[i].x = -100;
                    destroyed++;
                }
            }

            if (ball.x < 0 || ball.x > (WIDTH - ball.width)) {
                ball.dx *= -1;
            }

            if (ball.y < 0) {
                ball.dy *= -1;
            }

            if (ball.y > HEIGHT - ball.height) {
                gameOver = true;
            }

            if(destroyed == amount) {
                gameWin = true;
            }
            if (new Rectangle(player.x, player.y, player.width, player.height).intersects(
                    new Rectangle(ball.x, ball.y, ball.width, ball.height)
            )) {
                ball.dy = -Math.abs(ball.dy);
                ball.dx = ball.dx / Math.abs(ball.dx) * (rand.nextInt(4 - 2) + 2);

            }
        }
    }

    @Override
    public void run() {
        long time1 = System.nanoTime();
        long time2;
        double delta = 0.0;
        double ticks = 60.0;
        double secs = 1e9 / ticks;

        while (gameThread != null) {
            time2 = System.nanoTime();
            delta += (time2 - time1) / secs;
            time1 = time2;

            if(delta >= 1) {
                logic();
                repaint();
                delta--;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLUE);
        g.fillRect(player.x, player.y, player.width, player.height);

        g.setColor(Color.PINK);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);
        for(int i = 0; i < amount; i++) {
            g.setColor(Color.GREEN);
            g.fillRect(bricks[i].x, bricks[i].y, BRICK_WIDTH, BRICK_HEIGHT);
            g.setColor(Color.YELLOW);
            g.drawRect(bricks[i].x, bricks[i].y, BRICK_WIDTH + 1, BRICK_HEIGHT + 1);
        }

        if(gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.PLAIN, 60));
            g.drawString("GAME OVER", WIDTH / 2 - 200,  HEIGHT / 2);
        }
        if(gameWin) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 60));
            g.drawString("YOU WIN", WIDTH / 2 - 160,  HEIGHT / 2);
        }

        g.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && !gameWin) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) player.x -= player.dx;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.x += player.dx;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    public Renderer() {
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.setBackground(Color.YELLOW);
        this.addKeyListener(this);
        setup();
    }
}

class Window extends JFrame {
    public Window() {
        this.add(new Renderer());
        this.pack();
        this.setTitle("Arkanoid");
        this.setSize(Renderer.WIDTH, Renderer.HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}

public class Main {
    public static void main(String[] args) {
        new Window();
    }
}
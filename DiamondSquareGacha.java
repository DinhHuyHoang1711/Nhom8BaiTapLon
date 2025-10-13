import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class DiamondSquareGacha extends JPanel {
    private static final int TILE_SIZE = 80;
    private static final int NUM_TILES = 12;
    private static final int CENTER_X = 300;
    private static final int CENTER_Y = 250;

    private Point[] positions;
    private int highlightIndex = -1;
    private boolean spinning = false;
    private Timer spinTimer;
    private int stopIndex;
    private Random random = new Random();

    private JButton spinButton;
    private JLabel resultLabel;

    public DiamondSquareGacha() {
        setPreferredSize(new Dimension(600, 600));
        setLayout(null);
        setBackground(new Color(230, 240, 255));

        spinButton = new JButton("QUAY");
        spinButton.setFont(new Font("Arial", Font.BOLD, 18));
        spinButton.setBounds(240, 500, 120, 40);
        add(spinButton);

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setBounds(100, 450, 400, 40);
        add(resultLabel);

        createDiamondLayout();

        spinButton.addActionListener(e -> startSpin());
    }

    private void createDiamondLayout() {
        positions = new Point[NUM_TILES];
        int d = 90; // khoảng cách giữa các ô

        // Tạo vòng thoi 12 ô
        // 3 ô mỗi cạnh: trên phải, phải dưới, trái dưới, trái trên
        int[][] offsets = {
                {-3,0},{-2,1},{-1,2},
                {0,3},{1,2},{2,1},
                {3,0},{2,-1},{1,-2},
                {0,-3},{-1,-2},{-2,-1}
        };

        for (int i = 0; i < NUM_TILES; i++) {
            double angle = Math.PI / 4; // xoay 45°
            double dx = offsets[i ][0] * d / 1.4;
            double dy = offsets[i ][1] * d / 1.4;
            positions[i] = new Point((int) (CENTER_X + dx), (int) (CENTER_Y + dy));
        }
    }

    private void startSpin() {
        if (spinning) return;
        spinning = true;
        resultLabel.setText("");
        stopIndex = random.nextInt(NUM_TILES);

        spinTimer = new Timer(40, new ActionListener() {
            int counter = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                highlightIndex = (highlightIndex + 1) % NUM_TILES;
                repaint();
                counter++;

                if (counter > NUM_TILES * 10000 && highlightIndex == stopIndex) {
                    spinTimer.stop();
                    spinning = false;
                    showResult();
                }
            }
        });
        spinTimer.start();
    }

    private void showResult() {
        resultLabel.setText("🎉 Bạn trúng ô số " + (stopIndex + 1) + "!");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < NUM_TILES; i++) {
            Point p = positions[i];

            AffineTransform old = g2.getTransform();
            g2.translate(p.x, p.y);
            g2.rotate(Math.toRadians(45)); // xoay ô vuông thành hình thoi

            if (i == highlightIndex) {
                g2.setColor(new Color(255, 215, 0)); // vàng sáng
            } else {
                g2.setColor(new Color(150, 180, 255));
            }

            Rectangle2D rect = new Rectangle2D.Double(-TILE_SIZE / 2.0, -TILE_SIZE / 2.0, TILE_SIZE, TILE_SIZE);
            g2.fill(rect);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.draw(rect);

            // Vẽ số thứ tự
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString(String.valueOf(i + 1), -8, 7);

            g2.setTransform(old);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Vòng Quay 12 Ô - Hình Thoi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new DiamondSquareGacha());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

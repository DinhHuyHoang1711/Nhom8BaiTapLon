package GachaMachine;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * 🎮 Game Vòng Quay 12 Ô - Hình Thoi
 * Lớp chính quản lý giao diện & khởi động chương trình
 */
public class DiamondSquareGacha extends JPanel {
    private static final int TILE_SIZE = 80;
    private static final int NUM_TILES = 12;
    private static final int CENTER_X = 300;
    private static final int CENTER_Y = 250;

    private Point[] positions;
    private Image backgroundImage;

    private Item[] items;
    private SpinAnimation spinAnimation;
    private Inventory inventory;

    private JButton spinButton, bagButton;

    public DiamondSquareGacha() {
        backgroundImage = new ImageIcon("images/background.jpg").getImage();
        items = Item.loadItems(NUM_TILES);
        inventory = new Inventory(items);

        setPreferredSize(new Dimension(600, 600));
        setLayout(null);

        // Nút quay
        spinButton = new JButton("QUAY");
        spinButton.setFont(new Font("Arial", Font.BOLD, 18));
        spinButton.setBounds(240, 520, 120, 40);
        add(spinButton);

        // Nút túi đồ
        bagButton = new JButton(new ImageIcon("images/bag.png"));
        bagButton.setBounds(400, 400, 200, 200);
        bagButton.setToolTipText("Túi đồ");
        bagButton.setContentAreaFilled(false);
        bagButton.setBorderPainted(false);
        bagButton.addActionListener(e -> inventory.showInventory(this));
        add(bagButton);

        createDiamondLayout();

        spinAnimation = new SpinAnimation(this, NUM_TILES, items, inventory);
        spinButton.addActionListener(e -> spinAnimation.startSpin());
    }

    private void createDiamondLayout() {
        positions = new Point[NUM_TILES];
        int d = 90;
        int[][] offsets = {
                {-3,0},{-2,1},{-1,2},
                {0,3},{1,2},{2,1},
                {3,0},{2,-1},{1,-2},
                {0,-3},{-1,-2},{-2,-1}
        };
        for (int i = 0; i < NUM_TILES; i++) {
            double dx = offsets[i][0] * d / 1.4 ;
            double dy = offsets[i][1] * d / 1.4 ;
            positions[i] = new Point((int)(CENTER_X + dx), (int)(CENTER_Y + dy));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int highlight = spinAnimation.getHighlightIndex();
        for (int i = 0; i < NUM_TILES; i++) {
            Point p = positions[i];
            AffineTransform old = g2.getTransform();
            g2.translate(p.x, p.y);
            g2.rotate(Math.toRadians(45));

            g2.setColor(new Color(0, 0, 100, 100));
            g2.fillRect(-TILE_SIZE / 2, -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);

            Image img = items[i].getImage();
            AffineTransform at = new AffineTransform();
            at.translate(-TILE_SIZE / 2.0, -TILE_SIZE / 2.0);
            at.rotate(Math.toRadians(-45), TILE_SIZE / 2.0, TILE_SIZE / 2.0);
            at.scale((double) TILE_SIZE / img.getWidth(null), (double) TILE_SIZE / img.getHeight(null));
            g2.drawImage(img, at, null);

            if (i == highlight) {
                g2.setColor(new Color(255, 215, 0, 120));
                g2.fillRect(-TILE_SIZE / 2, -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
            }

            g2.setTransform(old);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("🎮 Vòng Quay 12 Ô - Hình Thoi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new DiamondSquareGacha());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

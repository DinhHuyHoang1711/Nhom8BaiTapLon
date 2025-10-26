package GachaMachine;

import MoneyCollected.Coin;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * 🎮 Game Vòng Quay 12 Ô - Hình Thoi
 */
public class DiamondSquareGacha extends JPanel {
    // Kích thước & cấu hình
    private static final int TILE_SIZE = 80;
    private static final int NUM_TILES = 12;
    private static final int CENTER_X = 300;
    private static final int CENTER_Y = 250;

    // Dữ liệu chính
    private Point[] tilePositions;
    private Image background;
    private Item[] items;
    private Inventory inventory;
    private SpinAnimation spinAnim;
    private Coin currentMoney;

    // Âm thanh & nút
    private arkanoid.Sound spinSound;
    private JLabel moneyLabel;
    private JPanel moneyPanel;
    private JButton spinBtn, bagBtn;

    public DiamondSquareGacha() {
        // Ảnh nền
        background = new ImageIcon("images/background.jpg").getImage();

        // Load tiền hiện có ở trên đầu
        currentMoney = new Coin();
        moneyPanel = new JPanel();
        moneyPanel.setLayout(null);
        moneyPanel.setBounds(380, 10,100,40);
        moneyPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE,2));
        moneyLabel = new JLabel( "" + currentMoney.getAmount(), SwingConstants.CENTER);
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setForeground(new Color(180, 120,0));
        moneyLabel.setBounds(0,0,100,40);
        moneyPanel.add(moneyLabel);
        this.add(moneyPanel);

        // Load vật phẩm & túi đồ
        items = Item.loadItems(NUM_TILES);
        inventory = new Inventory(items);

        // Cấu hình panel
        setPreferredSize(new Dimension(600, 600));
        setLayout(null);

        // Nút quay
        spinBtn = new JButton("QUAY");
        spinBtn.setFont(new Font("Arial", Font.BOLD, 18));
        spinBtn.setBounds(240, 520, 120, 40);
        add(spinBtn);

        // Nút túi đồ
        bagBtn = new JButton(new ImageIcon("images/bag.png"));
        bagBtn.setBounds(400, 400, 200, 200);
        bagBtn.setToolTipText("Túi đồ");
        bagBtn.setContentAreaFilled(false);
        bagBtn.setBorderPainted(false);
        bagBtn.addActionListener(e -> inventory.showInventory(this));
        add(bagBtn);

        // Tạo vị trí 12 ô
        makeTilePositions();

        // Khởi tạo vòng quay
        spinAnim = new SpinAnimation(this, NUM_TILES, items, inventory);

        // Khi nhấn nút “QUAY”
        spinBtn.addActionListener(e -> spinAnim.startSpin());
    }

    /** Tính toán vị trí 12 ô hình thoi quanh tâm */
    private void makeTilePositions() {
        tilePositions = new Point[NUM_TILES];
        int gap = 90;
        int[][] offsets = {
                {-3,0},{-2,1},{-1,2},
                {0,3},{1,2},{2,1},
                {3,0},{2,-1},{1,-2},
                {0,-3},{-1,-2},{-2,-1}
        };

        for (int i = 0; i < NUM_TILES; i++) {
            double dx = offsets[i][0] * gap / 1.4;
            double dy = offsets[i][1] * gap / 1.4;
            tilePositions[i] = new Point((int)(CENTER_X + dx), (int)(CENTER_Y + dy));
        }
    }

    /** Vẽ giao diện chính */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Nền
        if (background != null)
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int highlightIndex = spinAnim.getHighlightIndex();

        // Vẽ từng ô vật phẩm
        for (int i = 0; i < NUM_TILES; i++) {
            Point p = tilePositions[i];
            AffineTransform old = g2.getTransform();

            g2.translate(p.x, p.y);
            g2.rotate(Math.toRadians(45));

            // Nền ô
            g2.setColor(new Color(0, 0, 100, 100));
            g2.fillRect(-TILE_SIZE / 2, -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);

            // Ảnh vật phẩm
            Image img = items[i].getImage();
            AffineTransform at = new AffineTransform();
            at.translate(-TILE_SIZE / 2.0, -TILE_SIZE / 2.0);
            at.rotate(Math.toRadians(-45), TILE_SIZE / 2.0, TILE_SIZE / 2.0);
            at.scale((double) TILE_SIZE / img.getWidth(null),
                    (double) TILE_SIZE / img.getHeight(null));
            g2.drawImage(img, at, null);

            // Ô được highlight
            if (i == highlightIndex) {
                g2.setColor(new Color(255, 215, 0, 120));
                g2.fillRect(-TILE_SIZE / 2, -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
            }

            g2.setTransform(old);
        }
    }

    /** Chạy chương trình */
    public static void main(String[] args) {
        JFrame frame = new JFrame("🎮 Vòng Quay 12 Ô - Hình Thoi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new DiamondSquareGacha());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

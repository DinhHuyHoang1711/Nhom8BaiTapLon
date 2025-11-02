package GachaMachine;

import MoneyCollected.Coin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import arkanoid.OwnedManager;

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
    private double tileX[];
    private double tileY[];
    private Point[] tilePositions;
    private Image background;
    private Item[] items;
    private Inventory inventory;
    private SpinAnimation spinAnim;
    private Coin currentMoney;
    private OwnedManager ownedManager;

    // Giao diện & nút
    private JLabel moneyLabel;
    private JPanel moneyPanel;
    private JButton spinBtn, bagBtn;

    public DiamondSquareGacha(OwnedManager ownedManager) {
        this.ownedManager = ownedManager;
        this.currentMoney = ownedManager.getCurrentCoin();
        setupBackground();
        setupMoneyPanel();
        setupItemsAndInventory();
        setupPanel();
        setupButtons();
        setupSpinAnimation();
    }

    /**
     * Ảnh nền
     */
    private void setupBackground() {
        background = new ImageIcon("images/background.jpg").getImage();
    }

    /**
     * Khung hiển thị tiền
     */
    private void setupMoneyPanel() {
        moneyPanel = new JPanel();
        moneyPanel.setLayout(null);
        moneyPanel.setBounds(380, 10, 100, 40);
        moneyPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));

        moneyLabel = new JLabel("" + currentMoney.getAmount(), SwingConstants.CENTER);
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setForeground(new Color(180, 120, 0));
        moneyLabel.setBounds(0, 0, 100, 40);

        moneyPanel.add(moneyLabel);
        this.add(moneyPanel);
    }

    /**
     * cap nhat hien thi tien .
     */
    private void updateMoneyLabel() {
        moneyLabel.setText("" + currentMoney.getAmount());
    }


    /**
     * Load vật phẩm và túi đồ
     */
    private void setupItemsAndInventory() {
        items = Item.loadItems(NUM_TILES);
        inventory = new Inventory(items);
        makeTilePositions();
    }

    /**
     * Cấu hình panel chính
     */
    private void setupPanel() {
        setPreferredSize(new Dimension(600, 600));
        setLayout(null);
    }

    /**
     * Thiết lập các nút bấm
     */
    private void setupButtons() {
        //  Nút QUAY
        spinBtn = new JButton("QUAY");
        spinBtn.setFont(new Font("Arial", Font.BOLD, 18));
        spinBtn.setBounds(240, 520, 120, 40);
        spinBtn.addActionListener(new SpinButtonListener());
        add(spinBtn);

        // Nút TÚI ĐỒ
        bagBtn = new JButton(new ImageIcon("images/bag.png"));
        bagBtn.setBounds(400, 400, 200, 200);
        bagBtn.setToolTipText("Túi đồ");
        bagBtn.setContentAreaFilled(false);
        bagBtn.setBorderPainted(false);
        bagBtn.addActionListener(new BagButtonListener());
        add(bagBtn);
    }

    /**
     * Thiết lập hoạt ảnh quay
     */
    private void setupSpinAnimation() {
        spinAnim = new SpinAnimation(this, NUM_TILES, items, inventory, ownedManager);
    }

    /**
     * khi nhấn nút QUAY
     */
    private class SpinButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int cost = 1000;
            if (currentMoney.spend(cost)) {
                spinAnim.startSpin();
                updateMoneyLabel();
            } else {
                JOptionPane.showMessageDialog(DiamondSquareGacha.this,
                        "Không đủ coin để quay!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    /**
     * khi nhấn nút TÚI ĐỒ
     */
    private class BagButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            inventory.showInventory(DiamondSquareGacha.this);
        }
    }

    /**
     * Tính toán vị trí 12 ô hình thoi quanh tâm
     */
    private void makeTilePositions() {
        tileX = new double[NUM_TILES];
        tileY = new double[NUM_TILES];
        int gap = 90;
        int[][] offsets = {
                {-3, 0}, {-2, 1}, {-1, 2},
                {0, 3}, {1, 2}, {2, 1},
                {3, 0}, {2, -1}, {1, -2},
                {0, -3}, {-1, -2}, {-2, -1}
        };

        for (int i = 0; i < NUM_TILES; i++) {
            double dx = offsets[i][0] * gap / 1.59;
            double dy = offsets[i][1] * gap / 1.59;
            tileX[i] = CENTER_X + dx;
            tileY[i] = CENTER_Y + dy;
        }
    }

    /**
     * Vẽ giao diện chính
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null)
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int highlightIndex = spinAnim.getHighlightIndex();

        for (int i = 0; i < NUM_TILES; i++) {
            AffineTransform old = g2.getTransform();

            g2.translate(tileX[i], tileY[i]);
            g2.rotate(Math.toRadians(45));

            g2.setColor(new Color(0, 0, 100, 100));
            g2.fillRect(-TILE_SIZE / 2, -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);

            Image img = items[i].getImage();
            AffineTransform at = new AffineTransform();
            at.translate(-TILE_SIZE / 2.0, -TILE_SIZE / 2.0);
            at.rotate(Math.toRadians(-45), TILE_SIZE / 2.0, TILE_SIZE / 2.0);
            at.scale((double) TILE_SIZE / img.getWidth(null),
                    (double) TILE_SIZE / img.getHeight(null));
            g2.drawImage(img, at, null);

            if (i == highlightIndex) {
                g2.setColor(new Color(255, 215, 0, 120));
                g2.fillRect(-TILE_SIZE / 2, -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
            }

            g2.setTransform(old);
        }
    }

    /**
     * Chạy chương trình
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("🎮 Vòng Quay 12 Ô - Hình Thoi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new DiamondSquareGacha(new OwnedManager()));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

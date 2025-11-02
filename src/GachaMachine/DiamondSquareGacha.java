package GachaMachine;

import MoneyCollected.Coin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import arkanoid.OwnedManager;

public class DiamondSquareGacha extends JPanel {
    // Kích thước & cấu hình
    private static final int TILE_SIZE = 80; // Kích thước mỗi ô vật phẩm
    private static final int NUM_TILES = 12; // Tổng số ô trên vòng quay
    private static final int CENTER_X = 300; // Tọa độ tâm X của vòng quay
    private static final int CENTER_Y = 250; // Tọa độ tâm Y của vòng quay

    // Dữ liệu chính
    private double tileX[]; // Mảng lưu tọa độ X của từng ô
    private double tileY[]; // Mảng lưu tọa độ Y của từng ô
    private Image background; // Ảnh nền
    private Item[] items; // Danh sách vật phẩm trong vòng quay
    private Inventory inventory; // Túi đồ
    private SpinAnimation spinAnim; // Xử lý hoạt ảnh quay
    private Coin currentMoney; // Số tiền hiện có (liên kết với OwnedManager)
    private OwnedManager ownedManager; // Quản lý vật phẩm sở hữu & tiền chung

    // Giao diện & nút
    private JLabel moneyLabel; // Hiển thị số tiền
    private JPanel moneyPanel;  // Khung hiển thị tiền
    private JButton spinBtn, bagBtn; // Nút quay và túi đồ

    /**
     * Khởi tạo vòng quay – truyền OwnedManager để đồng bộ tiền & vật phẩm chung.
     */
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
     * Ảnh nền.
     */
    private void setupBackground() {
        background = new ImageIcon("images/background.jpg").getImage();
    }

    /**
     * Khung hiển thị tiền.
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
     * Cập nhật hiển thị tiền.
     */
    private void updateMoneyLabel() {
        moneyLabel.setText("" + currentMoney.getAmount());
    }


    /**
     * Load vật phẩm và túi đồ.
     */
    private void setupItemsAndInventory() {
        items = Item.loadItems(NUM_TILES);
        inventory = new Inventory(items);
        makeTilePositions();
    }

    /**
     * Cấu hình panel chính.
     */
    private void setupPanel() {
        setPreferredSize(new Dimension(600, 600));
        setLayout(null);
    }

    /**
     * Thiết lập các nút bấm.
     */
    private void setupButtons() {
<<<<<<< HEAD
        //  Nút QUAY
=======
        // Nút QUAY
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
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
     * Xử lý khi nhấn nút "QUAY".
     */
    private class SpinButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
<<<<<<< HEAD
            int cost = 1000;
            if (currentMoney.spend(cost)) {
                spinAnim.startSpin();
                updateMoneyLabel();
=======
            int cost = 1000; // Giá mỗi lượt quay
            if (currentMoney.spend(cost)) { // Trừ tiền thành công
                spinAnim.startSpin();
                updateMoneyLabel(); // Cập nhật tiền trên UI
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
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

        int gap = 90; // Khoảng cách giữa các ô

        // Các vị trí tương đối của 12 ô (theo dạng hình thoi xoay 45 độ)
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
     * Vẽ toàn bộ giao diện vòng quay:
     * - Ảnh nền
     * - 12 ô vật phẩm hình thoi
     * - Vật phẩm được xoay đúng hướng
     * - Hiệu ứng highlight khi đang quay trúng vị trí nào
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ ảnh nền nếu tồn tại
        if (background != null)
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Lấy index của ô đang sáng(khi quay)
        int highlightIndex = spinAnim.getHighlightIndex();

        // Vẽ từng ô
        for (int i = 0; i < NUM_TILES; i++) {
            AffineTransform old = g2.getTransform(); // Lưu lại transform hiện tại

            // Tịnh tiến đến vị trí ô và xoay để tạo hình thơi
            g2.translate(tileX[i], tileY[i]);
            g2.rotate(Math.toRadians(45));

            // Vẽ nền của ô
            g2.setColor(new Color(0, 0, 100, 100));
            g2.fillRect(-TILE_SIZE / 2, -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);

            // Lấy ảnh của item để vẽ
            Image img = items[i].getImage();

            // Vẽ ảnh nhưng xoay ngược lại 45 độ để ảnh đứng thẳng
            AffineTransform at = new AffineTransform();
            at.translate(-TILE_SIZE / 2.0, -TILE_SIZE / 2.0);
            at.rotate(Math.toRadians(-45), TILE_SIZE / 2.0, TILE_SIZE / 2.0);
            at.scale((double) TILE_SIZE / img.getWidth(null),
                    (double) TILE_SIZE / img.getHeight(null));
            g2.drawImage(img, at, null);

            // Nếu là ô được highlight
            if (i == highlightIndex) {
                g2.setColor(new Color(255, 215, 0, 120));
                g2.fillRect(-TILE_SIZE / 2, -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
            }

            g2.setTransform(old); // Khôi phục transform ban đầu
        }
    }

    /**
     * Chạy chương trình
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame(" Vòng Quay 12 Ô ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Truyền OwnedManager để đồng bộ tiền & vật phẩm giữa các phần khác của game
        frame.getContentPane().add(new DiamondSquareGacha(new OwnedManager()));
        frame.pack();
        frame.setLocationRelativeTo(null); // Căn giữa màn hình
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

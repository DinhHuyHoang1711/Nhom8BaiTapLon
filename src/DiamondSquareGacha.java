import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.ArrayList;


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

    private Image backgroundImage;
    private Image[] tileImages;
    private String[] itemNames; // 🌟 Tên từng item

    private JButton spinButton;
    private JLabel resultLabel;

    // túi đồ
    private java.util.List<String> inventory = new ArrayList<>();
    private JButton bagButton;


    public DiamondSquareGacha() {

        // --- Nạp ảnh nền ---
        backgroundImage = new ImageIcon("images/background.jpg").getImage();

        // --- Nạp ảnh cho 12 ô ---
        tileImages = new Image[NUM_TILES];
        for (int i = 0; i < NUM_TILES; i++) {
            tileImages[i] = new ImageIcon("images/test" + (i + 1) + "-removebg-preview.png").getImage();
        }

        // --- Đặt tên cho từng item ---
        itemNames = new String[]{
                "Kiếm Rồng", "Khiên Gió", "Vòng Lửa", "Mũ Phù Thủy",
                "Áo Giáp Bạc", "Dây Chuyền Sấm Sét", "Nhẫn Băng",
                "Gậy Phép", "Giày Bay", "Cánh Thiên Thần",
                "Vương Miện", "Mắt Rồng"
        };

        setPreferredSize(new Dimension(600, 600));
        setLayout(null);
        setBackground(new Color(230, 240, 255));

        spinButton = new JButton("QUAY");
        spinButton.setFont(new Font("Arial", Font.BOLD, 18));
        spinButton.setBounds(240, 500, 120, 40);
        add(spinButton);

        // --- Nút túi đồ ---
        bagButton = new JButton(new ImageIcon("images/bag.png")); // ảnh túi đồ
        bagButton.setBounds(400, 400, 200, 200);
        bagButton.setToolTipText("Túi đồ");
        bagButton.setContentAreaFilled(false); // nền trong suốt
        bagButton.setBorderPainted(false);
        bagButton.setFocusPainted(false);

        bagButton.addActionListener(e -> showInventory());

        add(bagButton);

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

                if (counter > NUM_TILES * 5 && highlightIndex == stopIndex) {
                    spinTimer.stop();
                    spinning = false;
                    showResult();
                }
            }
        });
        spinTimer.start();
    }

    private void showResult() {
        // Lấy ảnh và tên item trúng thưởng
        Image img = tileImages[stopIndex];
        String itemName = itemNames[stopIndex];

        inventory.add(itemName); // 🌟 Lưu item vào túi đồ

        // Tạo cửa sổ nhỏ hiện kết quả
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Kết quả", true);
        dialog.setSize(200, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);

        // Panel hiển thị ảnh + tên
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth();
                int h = getHeight() - 40;
                g.drawImage(img, (w - 100) / 2, 15, 100, 100, this);
            }
        };
        panel.setPreferredSize(new Dimension(300, 200));

        JLabel nameLabel = new JLabel(itemName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton okButton = new JButton("Đóng");
        okButton.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(nameLabel, BorderLayout.CENTER);
        bottomPanel.add(okButton, BorderLayout.SOUTH);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < NUM_TILES; i++) {
            Point p = positions[i];

            AffineTransform old = g2.getTransform();
            g2.translate(p.x, p.y);
            g2.rotate(Math.toRadians(45)); // xoay ô vuông thành hình thoi

            Image img = tileImages[i];

            // Tô nền đậm nhẹ dưới mỗi ô để nổi bật với background
            g2.setColor(new Color(0, 0, 100, 100)); // màu xanh đậm trong suốt
            g2.fillRect(-TILE_SIZE / 2, -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);

            // Vẽ ảnh ô vuông lên trên
            AffineTransform at = new AffineTransform();
            at.translate(-TILE_SIZE / 2.0, -TILE_SIZE / 2.0); // tâm ảnh
            at.rotate(Math.toRadians(-45), TILE_SIZE / 2.0, TILE_SIZE / 2.0); // xoay 45°
            at.scale((double) TILE_SIZE / img.getWidth(null), (double) TILE_SIZE / img.getHeight(null));
            g2.drawImage(img, at, null);


            // Nếu ô này đang được chọn -> phủ lớp sáng vàng
            if (i == highlightIndex) {
                g2.setColor(new Color(255, 215, 0, 120));
                g2.fillRect(-TILE_SIZE / 2, -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
            }


            g2.setTransform(old);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Vòng Quay 12 Ô - Hình Thoi");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new DiamondSquareGacha());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void showInventory() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Túi đồ", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        if (inventory.isEmpty()) {
            // Khi túi đồ trống
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            JLabel emptyLabel = new JLabel("Túi đồ trống");
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 20));
            emptyLabel.setForeground(Color.GRAY);
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.add(emptyLabel);
            contentPanel.add(emptyPanel, BorderLayout.CENTER);
        } else {
            // Khi có item
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setBackground(Color.WHITE);

            for (String item : inventory) {
                int index = findItemIndexByName(item);
                Image img = tileImages[index];

                // Panel từng item
                JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                itemPanel.setBackground(Color.WHITE);

                // Ảnh item (resize về 40x40)
                Image scaled = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaled));

                // Tên item
                JLabel nameLabel = new JLabel(item);
                nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

                itemPanel.add(iconLabel);
                itemPanel.add(nameLabel);
                listPanel.add(itemPanel);
            }

            JScrollPane scrollPane = new JScrollPane(listPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            contentPanel.add(scrollPane, BorderLayout.CENTER);
        }

        JButton closeBtn = new JButton("Đóng");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        closeBtn.addActionListener(e -> dialog.dispose());
        contentPanel.add(closeBtn, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    // 🔍 Hàm phụ để tìm index của item trong mảng itemNames
    private int findItemIndexByName(String name) {
        for (int i = 0; i < itemNames.length; i++) {
            if (itemNames[i].equals(name)) return i;
        }
        return 0; // fallback nếu không tìm thấy
    }

}

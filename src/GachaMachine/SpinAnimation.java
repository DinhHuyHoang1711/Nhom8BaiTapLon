package GachaMachine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import arkanoid.OwnedManager;

/**
 * Quản lý hoạt ảnh vòng quay và kết quả
 */
public class SpinAnimation {
    private DiamondSquareGacha panel;
    private Timer timer;
    private int highlightIndex = -1;    // Vị trí hiện tại được tô sáng
    private boolean spinning = false;   // Trangj thái quay hay ko
    private int stopIndex;  // Vị trí dừng
    private Random random = new Random();
    private int numTiles;   // Số lượng vật phẩm
    private Item[] items;   // Danh sách vật phẩm
    private Inventory inventory;    // Túi lưu trữ vật phẩm đã quay được
    private OwnedManager ownedManager;  // Chứa danh sách các vật phẩm trong game

    /**
     * Khởi tạo SpinAnimation với các thành phần cần thiết.
     */
    public SpinAnimation(DiamondSquareGacha panel, int numTiles, Item[] items, Inventory inventory, OwnedManager ownedManager) {
        this.panel = panel;
        this.numTiles = numTiles;
        this.items = items;
        this.inventory = inventory;
        this.ownedManager = ownedManager;
    }

    // highlight khi quay
    public void highlightNext() {
        highlightIndex = (highlightIndex + 1) % numTiles;
        panel.repaint();
    }

    public boolean isStopPosition() {
        return highlightIndex == stopIndex;
    }

    public void stopSpin() {
        timer.stop();
        spinning = false;
        showResult(items[stopIndex]);
    }

    // Trả về số lượng ô hiện tại
    public int getNumTiles() {
        return numTiles;
    }

    // Bắt đầu quá trình quay
    public void startSpin() {
        if (spinning) return;

        spinning = true;
        stopIndex = random.nextInt(numTiles);   // chọn random 1 ô trúng

        timer = new Timer(40, new MyTimerListener(this));
        timer.start();
    }

    public int getHighlightIndex() {
        return highlightIndex;
    }

    // Show kết quả
    private void showResult(Item item) {
        inventory.addItem(item);
        switch(item.getName()) {
            case "Heart":
                ownedManager.addItem(Item.heart());
                break;
            case "Sword":
                ownedManager.addItem(Item.sword());
                break;
            case "Bow":
                ownedManager.addItem(Item.bow());
                break;
            case "Boom":
                ownedManager.addItem(Item.boom());
                break;
            case "Helmet":
                ownedManager.addItem(Item.helmet());
                break;
            case "Diamond":
                ownedManager.addItem(Item.diamond());
                break;
            case "Fire":
                ownedManager.addItem(Item.fire());
                break;
            case "Clock":
                ownedManager.addItem(Item.clock());
                break;
            case "Treasure Chest":
                ownedManager.addItem(Item.treasurechest());
                break;
            case "Lightning":
                ownedManager.addItem(Item.lightning());
                break;
            case "Brick":
                ownedManager.addItem(Item.brick());
                break;
            case "Meat":
                ownedManager.addItem(Item.meat());
                break;
            default:
                break;
        }

        // Cửa sổ ghi kết quả
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel)
                , "Kết quả", true);
        dialog.setSize(200, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(panel);

        // Tên Item
        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Hình ảnh Item
        JLabel icon = new JLabel(
                new ImageIcon(item.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH))
                , SwingConstants.CENTER);

        JButton ok = new JButton("Đóng");   // Đóng
        ok.addActionListener(e -> dialog.dispose());

        // Theme vào cửa sổ
        dialog.add(icon, BorderLayout.CENTER);
        dialog.add(nameLabel, BorderLayout.NORTH);
        dialog.add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
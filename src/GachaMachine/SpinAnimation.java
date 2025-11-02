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
    /**
     * Panel hiển thị vòng quay
     */
    private DiamondSquareGacha panel;
    /**
     * Bộ đếm thời gian tạo hiệu ứng quay
     */
    private Timer timer;
    /**
     * Chỉ số của ô hiện đang được highlight
     */
    private int highlightIndex = -1;
    /**
     * Cờ trạng thái đang quay hay không
     */
    private boolean spinning = false;
    /**
     * Chỉ số ô sẽ dừng lại cuối cùng
     */
    private int stopIndex;
    /**
     * Đối tượng random để tạo vị trí dừng ngẫu nhiên
     */
    private Random random = new Random();
    /**
     * Tổng số ô trong vòng quay
     */
    private int numTiles;
    /**
     * Danh sách tất cả vật phẩm trong vòng quay
     */
    private Item[] items;
    /**
     * Túi đồ để lưu vật phẩm sau khi quay trúng
     */
    private Inventory inventory;
    /**
     * Quản lý các vật phẩm sở hữu (chung toàn game)
     */
    private OwnedManager ownedManager;

    /**
     * Khởi tạo hiệu ứng quay.
     *
     * @param panel        giao diện chính của vòng quay
     * @param numTiles     số ô vật phẩm trong vòng quay
     * @param items        danh sách vật phẩm
     * @param inventory    túi đồ cá nhân của người chơi
     * @param ownedManager quản lý vật phẩm sở hữu chung
     */
    public SpinAnimation(DiamondSquareGacha panel, int numTiles, Item[] items, Inventory inventory, OwnedManager ownedManager) {
        this.panel = panel;
        this.numTiles = numTiles;
        this.items = items;
        this.inventory = inventory;
        this.ownedManager = ownedManager;
    }

    /**
     * Tăng highlightIndex để chuyển sang ô tiếp theo
     * rồi yêu cầu giao diện vẽ lại.
     */
    public void highlightNext() {
        highlightIndex = (highlightIndex + 1) % numTiles;
        panel.repaint();
    }

    /**
     * Kiểm tra xem vòng quay đã đến vị trí dừng chưa.
     *
     * @return true nếu đang ở đúng ô cần dừng
     */
    public boolean isStopPosition() {
        return highlightIndex == stopIndex;
    }

    /**
     * Dừng vòng quay, tắt timer và hiển thị kết quả.
     */
    public void stopSpin() {
        timer.stop();
        spinning = false;
        showResult(items[stopIndex]);
    }

    /**
     * @return số lượng ô hiện có trên vòng quay
     */
    public int getNumTiles() {
        return numTiles;
    }

    /**
     * Bắt đầu hiệu ứng quay. Nếu đang quay thì bỏ qua.
     */
    public void startSpin() {
        if (spinning) return;

        //  Đánh dấu trạng thái đang quay
        spinning = true;
        // Chọn vị trí dừng ngẫu nhiên
        stopIndex = random.nextInt(numTiles);
        // Tạo timer để cập nhật hiệu ứng mỗi 40ms
        timer = new Timer(40, new MyTimerListener(this));
        timer.start();
    }

    /**
     * @return vị trí hiện tại đang được highlight
     */
    public int getHighlightIndex() {
        return highlightIndex;
    }

    /**
     * Xử lý kết quả khi vòng quay dừng:
     * - Thêm item vào túi cá nhân và OwnedManager
     * - Hiển thị giao diện thông báo kết quả
     *
     * @param item vật phẩm trúng được
     */
    private void showResult(Item item) {
        // Lưu vào túi của người chơi
        inventory.addItem(item);

        // Lưu vào hệ thống sở hữu chung (OwnedManager)
        switch (item.getName()) {
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

        // Tạo hộp thoại thông báo kết quả
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel)
                , "Kết quả", true);
        dialog.setSize(200, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(panel);

        // Hiển thị tên vật phẩm
        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Hiển thị ảnh vật phẩm
        JLabel icon = new JLabel(
                new ImageIcon(item.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH))
                , SwingConstants.CENTER);

        // Nút đóng hộp thoại
        JButton ok = new JButton("Đóng");
        ok.addActionListener(e -> dialog.dispose());

        dialog.add(icon, BorderLayout.CENTER);
        dialog.add(nameLabel, BorderLayout.NORTH);
        dialog.add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
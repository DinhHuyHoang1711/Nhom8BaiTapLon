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
<<<<<<< HEAD
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
=======
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
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
     */
    public SpinAnimation(DiamondSquareGacha panel, int numTiles, Item[] items, Inventory inventory, OwnedManager ownedManager) {
        this.panel = panel;
        this.numTiles = numTiles;
        this.items = items;
        this.inventory = inventory;
        this.ownedManager = ownedManager;
    }

<<<<<<< HEAD
    // highlight khi quay
=======
    /**
     * Tăng highlightIndex để chuyển sang ô tiếp theo
     * rồi yêu cầu giao diện vẽ lại.
     */
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
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

<<<<<<< HEAD
    // Trả về số lượng ô hiện tại
=======
    /**
     * @return số lượng ô hiện có trên vòng quay
     */
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
    public int getNumTiles() {
        return numTiles;
    }

<<<<<<< HEAD
    // Bắt đầu quá trình quay
=======
    /**
     * Bắt đầu hiệu ứng quay. Nếu đang quay thì bỏ qua.
     */
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
    public void startSpin() {
        if (spinning) return;

        //  Đánh dấu trạng thái đang quay
        spinning = true;
<<<<<<< HEAD
        stopIndex = random.nextInt(numTiles);   // chọn random 1 ô trúng

=======
        // Chọn vị trí dừng ngẫu nhiên
        stopIndex = random.nextInt(numTiles);
        // Tạo timer để cập nhật hiệu ứng mỗi 40ms
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
        timer = new Timer(40, new MyTimerListener(this));
        timer.start();
    }

    /**
     * @return vị trí hiện tại đang được highlight
     */
    public int getHighlightIndex() {
        return highlightIndex;
    }

<<<<<<< HEAD
    // Show kết quả
=======
    /**
     * Xử lý kết quả khi vòng quay dừng:
     * - Thêm item vào túi cá nhân và OwnedManager
     * - Hiển thị giao diện thông báo kết quả
     *
     * @param item vật phẩm trúng được
     */
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
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

<<<<<<< HEAD
        // Cửa sổ ghi kết quả
=======
        // Tạo hộp thoại thông báo kết quả
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel)
                , "Kết quả", true);
        dialog.setSize(200, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(panel);

<<<<<<< HEAD
        // Tên Item
        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Hình ảnh Item
=======
        // Hiển thị tên vật phẩm
        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Hiển thị ảnh vật phẩm
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
        JLabel icon = new JLabel(
                new ImageIcon(item.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH))
                , SwingConstants.CENTER);

<<<<<<< HEAD
        JButton ok = new JButton("Đóng");   // Đóng
=======
        // Nút đóng hộp thoại
        JButton ok = new JButton("Đóng");
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
        ok.addActionListener(e -> dialog.dispose());

        // Theme vào cửa sổ
        dialog.add(icon, BorderLayout.CENTER);
        dialog.add(nameLabel, BorderLayout.NORTH);
        dialog.add(ok, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
package GachaMachine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Inventory (Túi đồ).
 */
class Inventory {
<<<<<<< HEAD
    // danh sách Item đang sở hữu
=======
    // Danh sách các vật phẩm mà người chơi đã trúng
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
    private List<Item> ownedItems = new ArrayList<>();

    // Danh sách toàn bộ vật phẩm có thể xuất hiẹn trong gacha
    private Item[] allItems;
    private static final int DISTANSE_ITEM = 95;

    /**
     * Khởi tạo túi đồ.
     *
     * @param allItems mảng tất cả vật phẩm có trong vòng quay gacha
     */
    public Inventory(Item[] allItems) {
        this.allItems = allItems;
    }

    /**
     * Thêm vật phẩm vừa trúng vào túi đồ.
     */
    public void addItem(Item item) {
        ownedItems.add(item);
    }

    /**
     * Hiển thị giao diện túi đồ.
     */
    public void showInventory(Component parent) {
        JFrame frame = new JFrame("Túi đồ");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(parent);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.WHITE);

        // Nếu túi trống
        if (ownedItems.isEmpty()) {
            JLabel label = new JLabel("Túi đồ trống", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 22));
            label.setForeground(Color.GRAY);
            label.setBounds(0, 200, 500, 50);
            frame.add(label);
        } else {
            // Tạo vùng hiển thị danh sách vật phẩm
            JPanel listPanel = new JPanel();
            listPanel.setLayout(null);
            listPanel.setBackground(Color.WHITE);

            int y = 10; // Vị trí Y của từng hàng item

            // Duyệt từng item trong túi
            for (Item item : ownedItems) {
                // mỗi vật phẩm là 1 hàng (ảnh + tên)
                JPanel row = new JPanel();
                row.setLayout(null);
                row.setBackground(Color.WHITE);
                row.setBounds(10, y, 460, 90);

                // Ảnh vật phẩm thu nhỏ
                Image img = item.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(img));
                imgLabel.setBounds(10, 5, 80, 80);
                row.add(imgLabel);

                // tên vật phẩm
                JLabel nameLabel = new JLabel(item.getName());
                nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                nameLabel.setBounds(110, 30, 300, 30);
                row.add(nameLabel);

                listPanel.add(row);
                listPanel.add(Box.createVerticalStrut(5));

<<<<<<< HEAD
                y += DISTANSE_ITEM; // khoảng cách giữa các item
=======
                y += 95; // Xuống dòng tiếp theo
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
            }

            // Điều chỉnh chiều cao panel theo số lượng item
            listPanel.setPreferredSize(new Dimension(450, ownedItems.size() * 95));

            // Nếu danh sách dài, cho vào ScrollPane
            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setBounds(10, 10, 470, 390);
            scroll.getViewport().setBackground(Color.WHITE);
            frame.add(scroll);
        }

        // Nút đóng cửa sổ túi đồ
        JButton closeBtn = new JButton("Đóng");
        closeBtn.setBounds(200, 420, 100, 35);
        closeBtn.addActionListener(e -> frame.dispose());
        frame.add(closeBtn);

        frame.setVisible(true);
    }
}

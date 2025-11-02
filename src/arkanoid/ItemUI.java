package arkanoid;

import javax.swing.*;
import java.awt.*;

import GachaMachine.Item;

import static arkanoid.GameObject.GAME_HEIGHT;
import static arkanoid.GameObject.GAME_WIDTH;

public class ItemUI extends JFrame {

    private final OwnedManager ownedManager;
    private Sound clickSound;
    private Image ItemListImg;
    private JLabel currentItemLabel;
    private BaloUI parentBalo;
    private Item[] itemList;   // danh sách Item

    private static final int ITEM_SIZE = 100;

    /**
     * constructor khởi tạo giao diện
     *
     * @param parentBalo   khi back quay lại Balo cũ
     * @param ownedManager chứa các Item đã sở hữu
     * @param itemList     Danh sách Item
     */
    public ItemUI(BaloUI parentBalo, OwnedManager ownedManager, Item[] itemList) {
        this.parentBalo = parentBalo;
        this.ownedManager = ownedManager;
        this.itemList = itemList;
        ItemInitUI();
    }

    // Khởi tạo toàn bộ giao diện
    public void ItemInitUI() {
        // Panel
        setTitle("Bộ sưu tập Item");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // âm thanh click và ảnh nền
        clickSound = new Sound("sound/click.wav");
        ItemListImg = new ImageIcon("img/list.jpg").getImage();

        // Panel chính
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ItemListImg, 0, 0, getWidth(), getHeight(), this); // Vẽ full kích thước panel
            }
        };
        panel.setLayout(null); // set layout tự do

        // Nút Back
        JButton back = ButtonManager.createImageButton(
                "img/back_button.png", "img/back_hover.png",
                10, 550, clickSound, e -> {
                    this.dispose();
                    parentBalo.setVisible(true);
                });
        panel.add(back);

        // Hiển thị các UI
        setContentPane(panel);
        setVisible(true);

        showCurrentItem(panel);
        loadItemsToPanel(panel);
    }

    // show Item hiện tại
    private void showCurrentItem(JPanel panel) {
        // mặc định lấy Item đầu tiên của danh sách
        Item currentItem = itemList[0];
        ImageIcon icon = new ImageIcon(currentItem.getImage());
        Image currentBallImg = icon.getImage().getScaledInstance(ITEM_SIZE * 2, ITEM_SIZE * 2, Image.SCALE_SMOOTH);

        currentItemLabel = new JLabel(new ImageIcon(currentBallImg)); // load
        currentItemLabel.setBounds(GAME_WIDTH / 2 - ITEM_SIZE, 120, ITEM_SIZE * 2, ITEM_SIZE * 2); // chỉnh tọa độ
        panel.add(currentItemLabel); // thêm vào panel
    }

    // Show danh sách các Item
    private void loadItemsToPanel(JPanel panel) {
        // tọa dộ bắt đầu danh sách
        int startX = 290;
        int startY = 320;
        int gap = 15; // khoảng cách
        int count = 0;

        // duyệt từng hình ảnh
        for (Item item : itemList) {
            ImageIcon icon = new ImageIcon(item.getImage());
            Image img = icon.getImage().getScaledInstance(ITEM_SIZE, ITEM_SIZE, Image.SCALE_SMOOTH);
            JButton ballButton = new JButton(new ImageIcon(img));

            /* vị trí danh sách Item
                gồm hai hàng
             */
            int x = startX + (ITEM_SIZE + gap) * (count % 6);  // mỗi hàng 6 item
            int y = startY + (count / 6) * (ITEM_SIZE + gap);

            ballButton.setBounds(x, y, ITEM_SIZE, ITEM_SIZE);
            ballButton.setBorderPainted(false); // tắt viền xung quanh button
            ballButton.setContentAreaFilled(false); // nền trong suôts
            ballButton.setFocusPainted(false); // focus
            ballButton.setOpaque(false); // vẽ đề toàn bộ nền

            // Sự kiện khi click
            ballButton.addActionListener(e -> {
                clickSound.play();
                int choice = JOptionPane.showConfirmDialog(this,
                        "Sử dụng trang bị này ?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    ownedManager.setCurrentItem(item);
                    JOptionPane.showMessageDialog(this, "Đã trang bị!");

                    //cap nhat hinh anh item moi
                    ImageIcon newIcon = new ImageIcon(item.getImage());
                    Image newImg = newIcon.getImage().getScaledInstance(ITEM_SIZE * 2, ITEM_SIZE * 2, Image.SCALE_SMOOTH);
                    currentItemLabel.setIcon(new ImageIcon(newImg));
                    panel.repaint();
                }
            });

            panel.add(ballButton);
            count++;
        }

        panel.repaint();
    }

    // chạy thử độc lập
    public static void main(String[] args) {
        OwnedManager ownedManager = new OwnedManager();
        Item[] itemList = Item.loadItems(12);
        new ItemUI(null, ownedManager, itemList);
    }
}

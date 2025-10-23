package GachaMachine;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Túi đồ lưu các vật phẩm đã trúng
 */
public class Inventory {
    private java.util.List<Item> ownedItems = new ArrayList<>();
    private Item[] allItems;

    public Inventory(Item[] allItems) {
        this.allItems = allItems;
    }

    public void addItem(Item item) {
        ownedItems.add(item);
    }

    public void showInventory(Component parent) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Túi đồ", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(parent);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);

        if (ownedItems.isEmpty()) {
            JLabel label = new JLabel("Túi đồ trống", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 20));
            label.setForeground(Color.GRAY);
            content.add(label, BorderLayout.CENTER);
        } else {
            JPanel list = new JPanel();
            list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
            list.setBackground(Color.WHITE);

            for (Item item : ownedItems) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                row.setBackground(Color.WHITE);
                Image scaled = item.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                row.add(new JLabel(new ImageIcon(scaled)));
                JLabel name = new JLabel(item.getName());
                name.setFont(new Font("Arial", Font.PLAIN, 16));
                row.add(name);
                list.add(row);
            }
            JScrollPane scroll = new JScrollPane(list);
            content.add(scroll, BorderLayout.CENTER);
        }

        JButton close = new JButton("Đóng");
        close.addActionListener(e -> dialog.dispose());
        content.add(close, BorderLayout.SOUTH);

        dialog.add(content);
        dialog.setVisible(true);
    }
}


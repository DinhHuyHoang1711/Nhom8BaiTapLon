package GachaMachine;

import javax.swing.*;
import java.awt.*;

/**
 * Đại diện cho một vật phẩm (item)
 */
public class Item {
    private String name;
    private Image image;

    public Item(String name, String imagePath) {
        this.name = name;
        this.image = new ImageIcon(imagePath).getImage();
    }

    public String getName() { return name; }
    public Image getImage() { return image; }

    // Tạo danh sách item mẫu
    public static Item[] loadItems(int count) {
        String[] names = {
                "Kiếm Rồng", "Khiên Gió", "Vòng Lửa", "Mũ Phù Thủy",
                "Áo Giáp Bạc", "Dây Chuyền Sấm Sét", "Nhẫn Băng",
                "Gậy Phép", "Giày Bay", "Cánh Thiên Thần",
                "Vương Miện", "Mắt Rồng"
        };
        Item[] arr = new Item[count];
        for (int i = 0; i < count; i++) {
            arr[i] = new Item(names[i], "images/test" + (i + 1) + "-removebg-preview.png");
        }
        return arr;
    }
}


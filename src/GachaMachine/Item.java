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

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    // Tạo danh sách item mẫu
    public static Item[] loadItems(int count) {
        String[] names = {
                "Heart", "Sword", "Bow", "Boom", "Helmet",
                "Diamond", "Fire", "Clock",
                "Treasure Chest", "Lightning", "Brick",
                "Meat"
        };
        Item[] arr = new Item[count];
        for (int i = 0; i < count; i++) {
            arr[i] = new Item(names[i], "images/test" + (i + 1) + "-removebg-preview.png");
        }
        return arr;
    }
}


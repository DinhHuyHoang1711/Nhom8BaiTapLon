package GachaMachine;

import javax.swing.*;
import java.awt.*;

/**
 * Đại diện cho một vật phẩm (item)
 */
public class Item {
    private String name;    // Tên Item
    private Image image;    // Hình ảnh
    private int cooldown;   // Thời gian hồi chiêu

    // Consstructor
    public Item(String name, String imagePath) {
        this.name = name;
        this.image = new ImageIcon(imagePath).getImage();
        this.cooldown = 0;
    }

    public Item(String name, String imagePath, int cooldown) {
        this.name = name;
        this.image = new ImageIcon(imagePath).getImage();
        this.cooldown = cooldown;
    }

    public Item(Item item) {
        this.name = item.getName();
        this.image = item.getImage();
        this.cooldown = item.getCooldown();
    }

    // GETTER
    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    // Tạo danh sách item mẫu
    public static Item[] loadItems(int count) {
        String[] names = {
                "Heart", "Sword", "Bow", "Boom", "Helmet",
                "Diamond", "Fire", "Clock",
                "Treasure Chest", "Lightning", "Brick",
                "Meat"
        };
        //cho nay co magic number
        Item[] arr = new Item[12];
        /*for (int i = 0; i < count; i++) {
            arr[i] = new Item(names[i], "images/test" + (i + 1) + "-removebg-preview.png");
        }

         */

        // Tất cả Item có trong game
        arr[0] = new Item(Item.heart());
        arr[1] = new Item(Item.sword());
        arr[2] = new Item(Item.bow());
        arr[3] = new Item(Item.boom());
        arr[4] = new Item(Item.helmet());
        arr[5] = new Item(Item.diamond());
        arr[6] = new Item(Item.fire());
        arr[7] = new Item(Item.clock());
        arr[8] = new Item(Item.treasurechest());
        arr[9] = new Item(Item.lightning());
        arr[10] = new Item(Item.brick());
        arr[11] = new Item(Item.meat());

        return arr;
    }

    // Khởi tạo từng Item
    public static Item heart() {
        return new Item("Heart", "images/test" + 1 + "-removebg-preview.png", 30000);
    }

    public static Item sword() {
        return new Item("Sword", "images/test" + 2 + "-removebg-preview.png", 20000);
    }

    public static Item bow() {
        return new Item("Bow", "images/test" + 3 + "-removebg-preview.png", 10000);
    }

    public static Item boom() {
        return new Item("Boom", "images/test" + 4 + "-removebg-preview.png", 60000);
    }

    public static Item helmet() {
        return new Item("Helmet", "images/test" + 5 + "-removebg-preview.png", 70000);
    }

    public static Item diamond() {
        return new Item("Diamond", "images/test" + 6 + "-removebg-preview.png", 60000);
    }

    public static Item fire() {
        return new Item("Fire", "images/test" + 7 + "-removebg-preview.png", 90000);
    }

    public static Item clock() {
        return new Item("Clock", "images/test" + 8 + "-removebg-preview.png", 30000);
    }

    public static Item treasurechest() {
        return new Item("Treasure Chest", "images/test" + 9 + "-removebg-preview.png", 200000);
    }

    public static Item lightning() {
        return new Item("Lightning", "images/test" + 10 + "-removebg-preview.png", 10000);
    }

    public static Item brick() {
        return new Item("Brick", "images/test" + 11 + "-removebg-preview.png", 40000);
    }

    public static Item meat() {
        return new Item("Meat", "images/test" +12 + "-removebg-preview.png", 60000);
    }

    // Đối chiếu
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item other = (Item) obj;
        return name != null && name.equals(other.name);
    }

    // Hash Code
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}


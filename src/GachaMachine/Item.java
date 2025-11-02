package GachaMachine;

import javax.swing.*;
import java.awt.*;

/**
 * Đại diện cho một vật phẩm (Item) trong hệ thống Gacha.
 * Mỗi vật phẩm có:
 * - Tên (name)
 * - Hình ảnh hiển thị (image)
 * - Thời gian hồi chiêu (cooldown) - nếu vật phẩm có kỹ năng sử dụng.
 */
public class Item {

    /**
     * Tên của vật phẩm
     */
    private String name;

    /**
     * Hình ảnh đại diện cho vật phẩm
     */
    private Image image;

    /**
     * Thời gian hồi chiêu
     */
    private int cooldown;

    /**
     * Constructor khởi tạo vật phẩm với tên và đường dẫn ảnh.
     *
     * @param name      tên vật phẩm
     * @param imagePath đường dẫn ảnh vật phẩm
     */
    public Item(String name, String imagePath) {
        this.name = name;
        this.image = new ImageIcon(imagePath).getImage();
        this.cooldown = 0;
    }

    /**
     * Constructor khởi tạo vật phẩm có kèm cooldown.
     *
     * @param name      tên vật phẩm
     * @param imagePath đường dẫn ảnh vật phẩm
     * @param cooldown  thời gian hồi chiêu của vật phẩm (ms)
     */
    public Item(String name, String imagePath, int cooldown) {
        this.name = name;
        this.image = new ImageIcon(imagePath).getImage();
        this.cooldown = cooldown;
    }

    /**
     * Constructor sao chép 1 vật phẩm (copy constructor).
     *
     * @param item vật phẩm cần sao chép
     */
    public Item(Item item) {
        this.name = item.getName();
        this.image = item.getImage();
        this.cooldown = item.getCooldown();
    }

    // getter.
    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    /**
     * Tải các vật phẩm có sẵn (12 vật phẩm mặc định trong vòng quay).
     *
     * @return mảng các vật phẩm
     */
    public static Item[] loadItems(int count) {
        String[] names = {
                "Heart", "Sword", "Bow", "Boom", "Helmet",
                "Diamond", "Fire", "Clock",
                "Treasure Chest", "Lightning", "Brick",
                "Meat"
        };
        // 12 vật phẩm cố định
        Item[] arr = new Item[12];

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

    /**
     * @return vật phẩm Heart kèm hình ảnh và thời gian hồi chiêu
     */
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
        return new Item("Meat", "images/test" + 12 + "-removebg-preview.png", 60000);
    }

    /**
     * Hai vật phẩm được coi là giống nhau nếu chúng có cùng tên.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item other = (Item) obj;
        return name != null && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}


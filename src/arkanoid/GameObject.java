package arkanoid;

import javax.swing.*;
import java.awt.*;

public class GameObject {

    /**
     * Chiều rộng tổng thể của cửa sổ game (pixel).
     */
    public static final int GAME_WIDTH = 1200;

    /**
     * Chiều cao tổng thể của cửa sổ game (pixel).
     */
    public static final int GAME_HEIGHT = 700;

    /**
     * Chiều rộng của khu vực chơi (không tính viền hoặc UI).
     */
    public static final int PLAYFRAME_WIDTH = 800;

    /**
     * Chiều cao của khu vực chơi (không tính viền hoặc UI).
     */
    public static final int PLAYFRAME_HEIGHT = 700;

    /**
     * Tọa độ X, Y của đối tượng trên màn hình.
     */
    protected int x, y;

    /**
     * Chiều rộng và chiều cao của đối tượng.
     */
    private int width, height;

    /**
     * Tốc độ di chuyển theo trục X và Y
     */
    private int dx, dy;

    /**
     * Đường dẫn đến hình ảnh (sprite) biểu diễn đối tượng.
     */
    private String imagePath;

    /**
     * Khởi tạo một {@code GameObject} mặc định.
     * <p>
     * Tất cả giá trị sẽ được gán là 0 hoặc {@code null}.
     */
    public GameObject() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.dx = 0;
        this.dy = 0;
        this.imagePath = null;
    }

    /**
     * Khởi tạo một {@code GameObject} với các thông số cụ thể.
     *
     * @param x       Tọa độ X ban đầu
     * @param y       Tọa độ Y ban đầu
     * @param width   Chiều rộng của đối tượng
     * @param height  Chiều cao của đối tượng
     * @param dx      Tốc độ di chuyển theo trục X
     * @param dy      Tốc độ di chuyển theo trục Y
     * @param imgPath Đường dẫn đến ảnh sprite của đối tượng
     */
    public GameObject(int x, int y, int width, int height, int dx, int dy,
                      String imgPath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = dx;
        this.dy = dy;
        this.imagePath = imgPath;
    }

    //setter va getter
    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDx() {
        return this.dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDy() {
        return this.dy;
    }

    public void setImagePath(String imgPath) {
        this.imagePath = imgPath;
    }

    public String getImagePath() {
        return this.imagePath;
    }

}

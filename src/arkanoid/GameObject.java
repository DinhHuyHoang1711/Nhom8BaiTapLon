package arkanoid;
import javax.swing.*;
import java.awt.*;

public class GameObject {
    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 700;
    public static final int PLAYFRAME_WIDTH = 800;
    public static final int PLAYFRAME_HEIGHT = 700;

    private int x, y;
    private int width, height;

    /* Dong nay co the xoa neu tao 2 lop extend GameObject
     la MoveableObject va ImmobileObject
     */
    private int dx, dy;
    private String imagePath;
    /*Constructor
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

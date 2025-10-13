package arkanoid;

public class GameObject {

    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 800;
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 20;
    public static final int BALL_SIZE = 10;
    public static final int BRICK_WIDTH = 50;
    public static final int BRICK_HEIGHT = 20;
    public static final int SPEED_BALL = 5;
    public static final int SPEED_PADDLE = 10;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void update() {

    }

    public void render() {

    }

    public int get_x() {
        return x;
    }

    public void set_x(int x) {
        this.x = x;
    }

    public int get_y() {
        return y;
    }

    public void set_y(int y) {
        this.y = y;
    }

    public int get_width() {
        return width;
    }

    public void set_width(int width) {
        this.width = width;
    }

    public int get_height() {
        return height;
    }

    public void set_height(int height) {
        this.height = height;
    }
}
package arkanoid;

import arkanoid.GameObject;

public class Brick extends GameObject {
    // Kích thước gạch
    private static final int BRICK_WIDTH = 90;
    private static final int BRICK_HEIGHT = 50;

    // Thuộc tính
    private int hitPoints;
    private String element;

    // ==== Constructor ==== //
    public Brick(int x, int y, int width, int height, int hp, String imgPath) {
        super(x, y, width, height, 0, 0, imgPath);
        this.hitPoints = Math.max(0, hp);
        this.element = "normal";
    }

    public Brick(int x, int y, int width, int height, int hp, String element, String imgPath) {
        super(x, y, width, height, 0, 0, imgPath);
        this.hitPoints = Math.max(0, hp);
        this.element = (element.isEmpty() ? "normal" : element);
    }

    public Brick(int x, int y, int width, int height, String imgPath) {
        this(x, y, width, height, 1, imgPath);
    }

    // ==== BASIC BRICK ==== //
    public static Brick lightOrangeBrick(int x, int y) { // 50
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 50,  "normal", "img/brick/BASIC/cam nhạt.png");
    }

    public static Brick orangeBrick(int x, int y) { // 75
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 75,  "normal", "img/brick/BASIC/cam.png");
    }

    public static Brick lightRedBrick(int x, int y) { // 100
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 100, "normal", "img/brick/BASIC/đỏ nhạt.png");
    }

    public static Brick redBrick(int x, int y) { // 125
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 125, "normal", "img/brick/BASIC/đỏ.png");
    }

    public static Brick brownBrick(int x, int y) { // 150
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 150, "normal", "img/brick/BASIC/nâu.png");
    }

    public static Brick purpleBrick(int x, int y) { // 175
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 175, "normal", "img/brick/BASIC/tím.png");
    }

    public static Brick blueBrick(int x, int y) { // 200
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 200, "normal", "img/brick/BASIC/xanh blue.png");
    }

    public static Brick limeBrick(int x, int y) { // 225
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 225, "normal", "img/brick/BASIC/xanh lá mạ.png");
    }

    public static Brick paleGreenBrick(int x, int y) { // 250
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 250, "normal", "img/brick/BASIC/xanh lợ lợ.png");
    }

    // ==== ELEMENT BRICK ==== //
    public static Brick fireBrick1(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 200,  "fire",  "img/brick/ELEMENTAL/lửa/lửa 1.png");
    }
    public static Brick fireBrick2(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 500,  "fire",  "img/brick/ELEMENTAL/lửa/lửa 2.png");
    }
    public static Brick fireBrick3(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 1000, "fire",  "img/brick/ELEMENTAL/lửa/lửa 3.png");
    }

    public static Brick waterBrick1(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 200,  "water", "img/brick/ELEMENTAL/nước/nước 1.png");
    }
    public static Brick waterBrick2(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 500,  "water", "img/brick/ELEMENTAL/nước/nước 2.png");
    }
    public static Brick waterBrick3(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 1000, "water", "img/brick/ELEMENTAL/nước/nước 3.png");
    }

    public static Brick windBrick1(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 200,  "wind",  "img/brick/ELEMENTAL/gió/gió 1.png");
    }
    public static Brick windBrick2(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 500,  "wind",  "img/brick/ELEMENTAL/gió/gió 2.png");
    }
    public static Brick windBrick3(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 1000, "wind",  "img/brick/ELEMENTAL/gió/gió 3.png");
    }

    public static Brick earthBrick1(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 200,  "earth", "img/brick/ELEMENTAL/đất/đất 1.png");
    }
    public static Brick earthBrick2(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 500,  "earth", "img/brick/ELEMENTAL/đất/đất 2.png");
    }
    public static Brick earthBrick3(int x, int y) {
        return new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 1000, "earth", "img/brick/ELEMENTAL/đất/đất 3.png");
    }

    // ==== GETTER/SETTER ==== //
    public void setHitPoints(int hp) {
        this.hitPoints = Math.max(0, hp);
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        if (element.isEmpty()) {
            this.element = "normal";
        } else {
            this.element = element;
        }
    }

    public void takeHit() {
        if (hitPoints > 0) hitPoints--;
    }

    public boolean isDestroyed() {
        return hitPoints == 0;
    }
}

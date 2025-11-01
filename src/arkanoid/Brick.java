package arkanoid;

import arkanoid.GameObject;

import java.util.ArrayList;

public class Brick extends GameObject {
    // Kích thước gạch
    protected static final int BRICK_WIDTH = 80;
    protected static final int BRICK_HEIGHT = 30;

    // Thuộc tính
    private int hitPoints;
    private String element;

    // === Earth-skill dynamic sprite ===
    private boolean summonedByBoss = false;
    private int summonWaveId = -1;



    // ==== Constructor ==== //
    public Brick() {
        super();
        this.hitPoints = 0;
        this.element = "";
    }

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

    public static ArrayList<Brick> buildLevel1Bricks() {
        ArrayList<Brick> list = new ArrayList<>();
        list.add(Brick.lightOrangeBrick(155, 40));
        list.add(Brick.lightOrangeBrick(237, 40));
        list.add(Brick.lightOrangeBrick(319, 40));
        list.add(Brick.lightOrangeBrick(401, 40));
        list.add(Brick.lightOrangeBrick(483, 40));
        list.add(Brick.lightOrangeBrick(565, 40));

        list.add(Brick.orangeBrick(155, 72));
        list.add(Brick.orangeBrick(237, 72));
        list.add(Brick.orangeBrick(319, 72));
        list.add(Brick.orangeBrick(401, 72));
        list.add(Brick.orangeBrick(483, 72));
        list.add(Brick.orangeBrick(565, 72));

        list.add(Brick.redBrick(155, 104));
        list.add(Brick.redBrick(237, 104));
        list.add(Brick.redBrick(319, 104));
        list.add(Brick.redBrick(401, 104));
        list.add(Brick.redBrick(483, 104));
        list.add(Brick.redBrick(565, 104));
        return list;
    }

    public static ArrayList<Brick> buildLevel2Bricks() {
        ArrayList<Brick> list = new ArrayList<>();
        list.add(Brick.fireBrick3(155, 40));
        list.add(Brick.fireBrick3(237, 40));
        list.add(Brick.fireBrick3(319, 40));
        list.add(Brick.fireBrick3(401, 40));
        list.add(Brick.fireBrick3(483, 40));
        list.add(Brick.fireBrick3(565, 40));

        list.add(Brick.fireBrick2(155, 72));
        list.add(Brick.fireBrick2(237, 72));
        list.add(Brick.fireBrick2(319, 72));
        list.add(Brick.fireBrick2(401, 72));
        list.add(Brick.fireBrick2(483, 72));
        list.add(Brick.fireBrick2(565, 72));

        list.add(Brick.fireBrick1(155, 104));
        list.add(Brick.fireBrick1(237, 104));
        list.add(Brick.fireBrick1(319, 104));
        list.add(Brick.fireBrick1(401, 104));
        list.add(Brick.fireBrick1(483, 104));
        list.add(Brick.fireBrick1(565, 104));
        return list;
    }

    public static ArrayList<Brick> buildLevel3Bricks() {
        ArrayList<Brick> list = new ArrayList<>();
        list.add(Brick.windBrick3(155, 40));
        list.add(Brick.windBrick3(237, 40));
        list.add(Brick.windBrick3(319, 40));
        list.add(Brick.windBrick3(401, 40));
        list.add(Brick.windBrick3(483, 40));
        list.add(Brick.windBrick3(565, 40));

        list.add(Brick.windBrick2(155, 72));
        list.add(Brick.windBrick2(237, 72));
        list.add(Brick.windBrick2(319, 72));
        list.add(Brick.windBrick2(401, 72));
        list.add(Brick.windBrick2(483, 72));
        list.add(Brick.windBrick2(565, 72));

        list.add(Brick.windBrick1(155, 104));
        list.add(Brick.windBrick1(237, 104));
        list.add(Brick.windBrick1(319, 104));
        list.add(Brick.windBrick1(401, 104));
        list.add(Brick.windBrick1(483, 104));
        list.add(Brick.windBrick1(565, 104));
        return list;
    }

    public static ArrayList<Brick> buildLevel4Bricks() {
        ArrayList<Brick> list = new ArrayList<>();
        list.add(Brick.waterBrick3(155, 40));
        list.add(Brick.waterBrick3(237, 40));
        list.add(Brick.waterBrick3(319, 40));
        list.add(Brick.waterBrick3(401, 40));
        list.add(Brick.waterBrick3(483, 40));
        list.add(Brick.waterBrick3(565, 40));

        list.add(Brick.waterBrick2(155, 72));
        list.add(Brick.waterBrick2(237, 72));
        list.add(Brick.waterBrick2(319, 72));
        list.add(Brick.waterBrick2(401, 72));
        list.add(Brick.waterBrick2(483, 72));
        list.add(Brick.waterBrick2(565, 72));

        list.add(Brick.waterBrick1(155, 104));
        list.add(Brick.waterBrick1(237, 104));
        list.add(Brick.waterBrick1(319, 104));
        list.add(Brick.waterBrick1(401, 104));
        list.add(Brick.waterBrick1(483, 104));
        list.add(Brick.waterBrick1(565, 104));
        return list;
    }

    public static ArrayList<Brick> buildLevel5Bricks() {
        ArrayList<Brick> list = new ArrayList<>();
        list.add(Brick.earthBrick3(155, 40));
        list.add(Brick.earthBrick3(237, 40));
        list.add(Brick.earthBrick3(319, 40));
        list.add(Brick.earthBrick3(401, 40));
        list.add(Brick.earthBrick3(483, 40));
        list.add(Brick.earthBrick3(565, 40));

        list.add(Brick.earthBrick2(155, 72));
        list.add(Brick.earthBrick2(237, 72));
        list.add(Brick.earthBrick2(319, 72));
        list.add(Brick.earthBrick2(401, 72));
        list.add(Brick.earthBrick2(483, 72));
        list.add(Brick.earthBrick2(565, 72));

        list.add(Brick.earthBrick1(155, 104));
        list.add(Brick.earthBrick1(237, 104));
        list.add(Brick.earthBrick1(319, 104));
        list.add(Brick.earthBrick1(401, 104));
        list.add(Brick.earthBrick1(483, 104));
        list.add(Brick.earthBrick1(565, 104));
        return list;
    }

    public static int[][] readGrid(String path) throws java.io.IOException {
        java.util.List<int[]> rows = new java.util.ArrayList<>();
        int cols = -1, ln = 0;

        try (java.io.BufferedReader br = java.nio.file.Files.newBufferedReader(
                java.nio.file.Paths.get(path), java.nio.charset.StandardCharsets.UTF_8)) {

            String line;
            while ((line = br.readLine()) != null) {
                ln++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] t = line.split("\\s+");
                if (cols < 0) cols = t.length;
                if (t.length != cols) throw new IllegalArgumentException("line " + ln + ": expected " + cols + " cols");

                int[] row = new int[cols];
                for (int i = 0; i < cols; i++) row[i] = Integer.parseInt(t[i]);
                rows.add(row);
            }
        }

        if (rows.isEmpty()) throw new IllegalStateException("empty map: " + path);
        return rows.toArray(new int[0][]);
    }

    public static Brick createBrickFromId(int id, int x, int y) {
        switch (id) {
            case 1: return Brick.lightOrangeBrick(x, y);
            case 2: return Brick.orangeBrick(x, y);
            case 3: return Brick.lightRedBrick(x, y);
            case 4: return Brick.redBrick(x, y);
            case 5: return Brick.brownBrick(x, y);
            case 6: return Brick.purpleBrick(x, y);
            case 7: return Brick.blueBrick(x, y);
            case 8: return Brick.limeBrick(x, y);
            case 9: return Brick.paleGreenBrick(x, y);

            case 10: return Brick.earthBrick3(x, y);
            case 11: return Brick.earthBrick2(x, y);
            case 12: return Brick.earthBrick1(x, y);

            case 13: return Brick.waterBrick3(x, y);
            case 14: return Brick.waterBrick2(x, y);
            case 15: return Brick.waterBrick1(x, y);

            case 16: return Brick.windBrick3(x, y);
            case 17: return Brick.windBrick2(x, y);
            case 18: return Brick.windBrick1(x, y);

            case 19: return Brick.fireBrick3(x, y);
            case 20: return Brick.fireBrick2(x, y);
            case 21: return Brick.fireBrick1(x, y);

            default: return null;
        }
    }

    // ==== GETTER/SETTER ==== //
    public void setHitPoints(int hp) {
        this.hitPoints = Math.max(0, hp);
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setSummonedByBoss(boolean v) {
        this.summonedByBoss = v;
    }

    public boolean isSummonedByBoss() {
        return this.summonedByBoss;
    }

    public void setSummonWaveId(int id) {
        this.summonWaveId = id;
    }

    public int getSummonWaveId() {
        return this.summonWaveId;
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

    public void takeHit(int damage) {
        //if (hitPoints > 0) hitPoints--;
        int dd = Math.max(0, damage);
        hitPoints = Math.max(0, hitPoints - dd);
    }

    public boolean isDestroyed() {
        return hitPoints == 0;
    }
}

import arkanoid.GameObject;

public class Brick extends GameObject {
    private int hitPoints;

    public Brick(int x, int y, int width, int height, int hp, String imgPath) {
        super(x, y, width, height, 0, 0, imgPath);
        this.hitPoints = Math.max(1, hp);
    }

    public Brick(int x, int y, int width, int height, String imgPath) {
        this(x, y, width, height, 1, imgPath);
    }

    public void setHitPoints(int hp) {
        this.hitPoints = Math.max(0, hp);
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void takeHit() {
        if (hitPoints > 0) hitPoints--;
    }

    public boolean isDestroyed() {
        return hitPoints == 0;
    }
}

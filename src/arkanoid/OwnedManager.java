package arkanoid;

import java.util.ArrayList;
import java.util.List;
import arkanoid.Ball;

import GachaMachine.Item;


public class OwnedManager {
    private List<Ball> balls = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    private Ball currentBall;
    private Item currentItem;

    public  OwnedManager() {
        this.currentBall = Ball.bongnguhanh();
        this.currentItem = Item.lightning();
        loadBalls();
    }

    public OwnedManager(Ball currentBall) {
        this.currentBall = currentBall;
        this.currentItem = null;
        loadBalls();
    }

    public OwnedManager(Ball currentBall, Item currentItem) {
        this.currentBall = currentBall;
        this.currentItem = currentItem;
    }

    public Ball getCurrentBall() {
        return currentBall;
    }

    public void setCurrentBall(Ball currentBall) {
        this.currentBall = currentBall;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void addBall(Ball ball) {
        if (!balls.contains(ball)) {
            balls.add(ball);
        }
    }

    public void addItem(Item item) {
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    public void removeBall(Ball ball) {
        balls.remove(ball);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Ball> getOwnedBalls() {
        return balls;
    }

    private void loadBalls() {
        balls.add(Ball.normalBall());
        balls.add(Ball.fireBall());
        balls.add(Ball.earthBall());
        balls.add(Ball.waterBall());
        balls.add(Ball.windBall());
    }
}

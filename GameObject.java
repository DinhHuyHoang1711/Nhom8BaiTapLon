package game_object;

abstract class GameObject {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    public GameObject(int x, int y, int width, int height);
    public void update();
    public void render();
    public void set_x(int x);
    public void set_y(int y);
    public void set_width(int width);
    public void set_height(int height);
}

class Brick extends GameObject {
    protected int hitPoints;
    
    public void set_hitPoints(int hitPoints);
    public void takeHit();
    public boolean isDestroyed();
}

class Ball extends GameObject {
    protected int dx, dy, speed;
    
    public void set_dx(int dx);
    public void set_dy(int dy);
    public void set_speed(int speed);
    public void bounceHorizontal();
    public void bounceVertical();
}

class Boss extends GameObject {
    protected int health;
    
    public void set_health(int health);
    public void takeDamage(int damage);
    public boolean isDefeated();
}

class PowerUp extends GameObject {
    protected String Type;
    protected int duration;
    
    public void set_Type(String Type);
    public void set_duration(int duration);
    public void applyEffect(Paddle paddle);
    public void removeEffect(Paddle paddle);
}

class Paddle extends GameObject {
    protected int speed;
    protected PowerUp currentPowerUp;
    
    public void set_speed(int speed);
    public void apply_PowerUp(currentPowerUp);
    public void moveRight();
    public void moveLeft();
}

class Artifact extends GameObject {
    protected String name;
    protected String description;
    protected int rarity;
    protected isActive;
    
    public void set_name(String name);
    public void set_description(String description);
    public void set_rarity(int rarity);
    public void activate();
    public void applyEffect();
}

class CurrentMoney extends GameObject {
    protected int balance;
    
    public void set_balance(int balance);
    public void CurrentMoney(int coin);
    public void gainMoney(int balance);
    public void loseMoney(int balance);
}
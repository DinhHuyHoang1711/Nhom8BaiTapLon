import game_object.*;
import level.*;

class GameManager {
    protected Paddle paddle;
    protected Ball ball;
    protected ArrayList <Brick> bricks;
    protected ArrayList <PowerUp> powerUps;
    protected Level currentLevel;
    protected ArrayList <Artifact> artifacts;
    protected int coin;
    protected int lives;
    
    public void set_paddle (Paddle paddle);
    public void set_ball (Ball ball);
    public void set_bricks (ArrayList <Brick> bricks);
    public void set_powerUps (ArrayList <PowerUp> powerUps);
    public void set_currentLevel (Level currentLevel);
    public void set_artifacts (ArrayList <Artifact> artifacts);
    public void set_coin (int coin);
    public void set_lives (int lives);
    
    public void startGame();
    public void updateGame();
    public void checkCollisions();
}
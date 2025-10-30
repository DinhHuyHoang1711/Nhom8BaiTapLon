package arkanoid;

import java.util.ArrayList;
import java.util.List;
import arkanoid.Ball;
import arkanoid.Artifact;

public class OwnedManager {
    private List<Ball> balls = new ArrayList<>();
    private List<Artifact> artifacts = new ArrayList<>();

    private Ball currentBall;
    private Artifact currentArtifact;

    public  OwnedManager() {
        this.currentBall = Ball.bongnguhanh();
        this.currentArtifact = null;
        loadBalls();
    }

    public OwnedManager(Ball currentBall, Artifact currentArtifact) {
        this.currentBall = currentBall;
        this.currentArtifact = currentArtifact;
    }

    public Ball getCurrentBall() {
        return currentBall;
    }

    public void setCurrentBall(Ball currentBall) {
        this.currentBall = currentBall;
    }

    public Artifact getCurrentArtifact() {
        return currentArtifact;
    }

    public void setCurrentArtifact(Artifact currentArtifact) {
        this.currentArtifact = currentArtifact;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void addBall(Ball ball) {
        if (!balls.contains(ball)) {
            balls.add(ball);
        }
    }

    public void addArtifact(Artifact artifact) {
        if (!artifacts.contains(artifact)) {
            artifacts.add(artifact);
        }
    }

    public void removeBall(Ball ball) {
        balls.remove(ball);
    }

    public void removeArtifact(Artifact artifact) {
        artifacts.remove(artifact);
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

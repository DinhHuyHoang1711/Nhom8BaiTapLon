package arkanoid;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private Clip clip;

    // khoi tao am thanh tu duong dan
    public Sound(String filePath) {
        try {
            File soundFile = new File(filePath);

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // phat 1 lan
    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // phat lap lai
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // dung
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    //giai phong
    public void close() {
        if (clip != null) {
            clip.close();
        }
    }
}

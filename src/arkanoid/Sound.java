package arkanoid;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Lớp Sound quản lý âm thanh
 */
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

    // phát 1 lần
    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // phát lặp lại
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // dừng phát
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // giải phóng
    public void close() {
        if (clip != null) {
            clip.close();
        }
    }
}

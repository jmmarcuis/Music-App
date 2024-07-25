package model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private Clip clip;
    private boolean isPlaying;
    private boolean isPaused;
    private FloatControl volumeControl;

    public void loadSong(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File audioFile = new File(filePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);

        clip = (Clip) AudioSystem.getLine(info);
        clip.open(audioStream);
        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    }

    public void play() {
        if (clip != null && !isPlaying) {
            clip.start();
            isPlaying = true;
            isPaused = false;
        }
    }

    public void pause() {
        if (clip != null && isPlaying) {
            clip.stop();
            isPlaying = false;
            isPaused = true;
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            isPlaying = false;
            isPaused = false;
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            volumeControl.setValue((max - min) * volume + min);
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void close() {
        if (clip != null) {
            clip.close();
        }
    }
}
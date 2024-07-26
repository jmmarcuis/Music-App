package model;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private Clip clip;
    private FloatControl volumeControl;
    private boolean isPlaying;
    private boolean isPaused;
    private String currentFilePath;
    private long pausedPosition;
    private AudioPlayerListener listener;

    public interface AudioPlayerListener {
        void playbackFinished();
    }

    public void loadSong(String filePath) throws IOException, UnsupportedAudioFileException, LineUnavailableException, JavaLayerException {
        this.currentFilePath = filePath;

        // Convert MP3 to WAV
        File wavFile = convertToWav(filePath);

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        pausedPosition = 0;

        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP && !isPaused) {
                if (listener != null) {
                    listener.playbackFinished();
                }
            }
        });
    }

    private File convertToWav(String mp3FilePath) throws JavaLayerException, IOException {
        File mp3File = new File(mp3FilePath);
        File wavFile = new File(mp3File.getParent(), mp3File.getName().replace(".mp3", ".wav"));

        if (!wavFile.exists()) {
            Converter converter = new Converter();
            converter.convert(mp3FilePath, wavFile.getAbsolutePath());
        }

        return wavFile;
    }

    public synchronized void play() {
        if (clip != null && !isPlaying) {
            clip.setMicrosecondPosition(pausedPosition);
            clip.start();
            isPlaying = true;
            isPaused = false;
        }
    }

    public synchronized void pause() {
        if (clip != null && isPlaying) {
            pausedPosition = clip.getMicrosecondPosition();
            clip.stop();
            isPlaying = false;
            isPaused = true;
        }
    }

    public synchronized void resume() {
        if (clip != null && isPaused) {
            clip.setMicrosecondPosition(pausedPosition);
            clip.start();
            isPlaying = true;
            isPaused = false;
        }
    }


    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.setMicrosecondPosition(0);
            isPlaying = false;
            isPaused = false;
            pausedPosition = 0;
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            // Convert volume to decibels
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float range = max - min;
            float gain = (range * volume) + min;
            volumeControl.setValue(gain);
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

    public long getCurrentPosition() {
        return clip != null ? clip.getMicrosecondPosition() : 0;
    }

    public long getTotalDuration() {
        return clip != null ? clip.getMicrosecondLength() : 0;
    }

    public void setAudioPlayerListener(AudioPlayerListener listener) {
        this.listener = listener;
    }

    public boolean hasSongLoaded() {
        return clip != null;
    }


}
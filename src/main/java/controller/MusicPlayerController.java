package controller;

import event.PlaybackControlListener;
import event.SongSelectionListener;
import event.VolumeChangeListener;
import javazoom.jl.decoder.JavaLayerException;
import model.AudioPlayer;
import model.LyricsManager;
import model.MusicLibrary;
import model.Song;
import view.MusicPlayerView;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MusicPlayerController {
    private final MusicLibrary library;
    private final AudioPlayer player;
    private final LyricsManager lyricsManager;
    private final MusicPlayerView view;
    private Song currentSong;
    private boolean wasPaused = false;

    public MusicPlayerController(MusicLibrary library, AudioPlayer player, LyricsManager lyricsManager, MusicPlayerView view) {
        this.library = library;
        this.player = player;
        this.lyricsManager = lyricsManager;
        this.view = view;

        initializeSongList();
        setupEventListeners();
        setupPlaybackListener();
    }

    private void initializeSongList() {
        List<Song> songs = library.getAllSongs();
        view.updateSongList(songs);
    }

    private void setupEventListeners() {
        view.setSongSelectionListener(new SongSelectionListener(this));
        view.setPlayButtonListener(new PlaybackControlListener(this, "play"));
        view.setPauseButtonListener(new PlaybackControlListener(this, "pause"));
        view.setStopButtonListener(new PlaybackControlListener(this, "stop"));
        view.setVolumeSliderListener(new VolumeChangeListener(this));
    }

    private void setupPlaybackListener() {
        player.setAudioPlayerListener(() -> SwingUtilities.invokeLater(() -> {
            view.updateProgressBar(0);
            view.updateNowPlaying("No song playing");
            currentSong = null;
            wasPaused = false;
        }));
    }

    public void handleSongSelection(String selectedSong) {
        currentSong = library.getSongByTitle(selectedSong);
        if (currentSong != null) {
            try {
                player.loadSong(currentSong.getFilePath());
                player.play();
                updateUI();
                String imageName = new File(currentSong.getImagePath()).getName();
                view.updateImage(imageName);
                wasPaused = false;
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | JavaLayerException ex) {
                view.showError("Error playing song: " + ex.getMessage());
            }
        }
    }

    public void handlePlayButton() {
        if (currentSong != null) {
            if (wasPaused) {
                player.resume();
                wasPaused = false;
            } else if (!player.isPlaying()) {
                player.play();
            }
            updateUI();
        }
    }

    public void handlePauseButton() {
        if (player.isPlaying()) {
            player.pause();
            wasPaused = true;
        }
    }

    public void handleStopButton() {
        player.stop();
        view.updateNowPlaying("No song playing");
        view.updateProgressBar(0);
        currentSong = null;
        wasPaused = false;
    }

    public void setVolume(int volume) {
        float normalizedVolume = (float) volume / 100;
        player.setVolume(normalizedVolume);
        // Update the slider's value in the view
        view.updateVolumeSlider(volume);
    }

    private void updateUI() {
        try {
            view.updateNowPlaying(currentSong.getTitle());
            view.updateLyrics(lyricsManager.getLyrics(currentSong));
            updateProgressBar();
        } catch (IOException ex) {
            view.showError("Error updating UI: " + ex.getMessage());
        }
    }

    private void updateProgressBar() {
        new Thread(() -> {
            while (player.isPlaying() || player.isPaused()) {
                long current = player.getCurrentPosition();
                long total = player.getTotalDuration();
                int progress = (int) ((float) current / total * 100);
                SwingUtilities.invokeLater(() -> view.updateProgressBar(progress));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void addSong(Song song) {
        library.addSong(song);
        initializeSongList();
    }

    public void removeSong(Song song) {
        library.removeSong(song);
        initializeSongList();
    }

    public void cleanup() {
        player.close();
        library.close();
    }
}

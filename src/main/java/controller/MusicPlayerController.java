package controller;

import event.PlaybackControlListener;
import event.SongSelectionListener;
import event.VolumeChangeListener;
import model.AudioPlayer;
import model.LyricsManager;
import model.MusicLibrary;
import model.Song;
import view.MusicPlayerView;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

public class MusicPlayerController {
    private MusicLibrary library;
    private AudioPlayer player;
    private LyricsManager lyricsManager;
    private MusicPlayerView view;
    private Song currentSong;

    public MusicPlayerController(MusicLibrary library, AudioPlayer player, LyricsManager lyricsManager, MusicPlayerView view) {
        this.library = library;
        this.player = player;
        this.lyricsManager = lyricsManager;
        this.view = view;

        initializeSongList();
        setupEventListeners();
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

    public void handleSongSelection(String selectedSong) {
        currentSong = library.getSongByTitle(selectedSong);
        if (currentSong != null) {
            try {
                // Load and play the audio
                player.loadSong(currentSong.getFilePath());
                player.play();

                // Update lyrics
                String lyrics = lyricsManager.getLyrics(currentSong);
                view.updateLyrics(lyrics);

                // Update image
                view.updateImage(currentSong.getImagePath());

                // Update now playing info
                view.updateNowPlaying(currentSong.getTitle());
            } catch (Exception ex) {
                view.showError("Error playing song: " + ex.getMessage());
            }
        }
    }
    public void handlePlayButton() {
        if (currentSong != null) {
            if (player.isPaused()) {
                player.play();
            } else if (!player.isPlaying()) {
                try {
                    player.loadSong(currentSong.getFilePath());
                    player.play();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    view.showError("Error playing song: " + ex.getMessage());
                }
            }
        }
    }

    public void handlePauseButton() {
        if (player.isPlaying()) {
            player.pause();
        }
    }

    public void handleStopButton() {
        player.stop();
        view.updateNowPlaying("No song playing");
    }

    public void setVolume(int volume) {
        float normalizedVolume = (float) volume / 100;
        player.setVolume(normalizedVolume);
    }

    public void addSong(Song song) {
        library.addSong(song);
        initializeSongList();  // Refresh the song list in the view
    }

    public void removeSong(Song song) {
        library.removeSong(song);
        initializeSongList();  // Refresh the song list in the view
    }

    public void cleanup() {
        player.close();
        library.close();
    }
}
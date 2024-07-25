import model.MusicLibrary;
import model.AudioPlayer;
import model.LyricsManager;

import controller.MusicPlayerController;

import persistence.SongRepository;
import view.MusicPlayerView;

public class MusicPlayerApp {
    public static void main(String[] args) {
        MusicLibrary library = new MusicLibrary();
        AudioPlayer player = new AudioPlayer();
        LyricsManager lyricsManager = new LyricsManager();
        MusicPlayerView view = new MusicPlayerView();
        MusicPlayerController controller = new MusicPlayerController(library, player, lyricsManager, view);
        view.setVisible(true);

        SongRepository repo = new SongRepository();
        repo.addSong("Let Down", "Radiohead", "music/Let Down.mp3");

    }
}
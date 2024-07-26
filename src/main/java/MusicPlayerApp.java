import controller.MusicPlayerController;
import model.AudioPlayer;
import model.LyricsManager;
import model.MusicLibrary;
import persistence.SongRepository;
import view.MusicPlayerView;

public class MusicPlayerApp {
    public static void main(String[] args) {
        SongRepository repository = new SongRepository();
        MusicLibrary library = new MusicLibrary(repository);


         AudioPlayer player = new AudioPlayer();
        LyricsManager lyricsManager = new LyricsManager();
        MusicPlayerView view = new MusicPlayerView();
        MusicPlayerController controller = new MusicPlayerController(library, player, lyricsManager, view);
        view.setVisible(true);
    }
}
import controller.MusicPlayerController;
import controller.SongCRUDController;
import model.AudioPlayer;
import model.LyricsManager;
import model.MusicLibrary;
import model.SongManager;
import persistence.SongRepository;
import view.MusicPlayerView;

public class MusicPlayerApp {
    public static void main(String[] args) {
        SongRepository repository = new SongRepository();
        MusicLibrary library = new MusicLibrary(repository);
        SongManager songManager = new SongManager( );

        AudioPlayer player = new AudioPlayer();
        LyricsManager lyricsManager = new LyricsManager();
        MusicPlayerView view = new MusicPlayerView();

        MusicPlayerController playerController = new MusicPlayerController(library, player, lyricsManager, view);
        SongCRUDController crudController = new SongCRUDController(view, songManager);

        // Initialize controllers
         crudController.initialize();

        view.setVisible(true);
    }
}
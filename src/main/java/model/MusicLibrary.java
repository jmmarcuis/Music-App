package model;

import persistence.SongRepository;
import java.util.ArrayList;
import java.util.List;

public class MusicLibrary {
    private List<Song> songs = new ArrayList<>();
    private SongRepository repository;

    public MusicLibrary(SongRepository repository) {
        this.repository = repository;
        loadSongs(); // Load songs from the repository
    }

    private void loadSongs() {
        songs = repository.getAllSongs();
    }

    public void addSong(Song song) {
        repository.addSong(song);
        songs.add(song);
    }

    public void removeSong(Song song) {
        repository.deleteSong(song.getId());
        songs.removeIf(s -> s.getId().equals(song.getId()));
    }

    public List<Song> getAllSongs() {
        return new ArrayList<>(songs);
    }

    public Song getSongByTitle(String title) {
        return songs.stream()
                .filter(song -> song.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    public void close() {
        repository.close();
    }
}

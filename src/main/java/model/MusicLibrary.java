package model;

import persistence.SongEntity;
import persistence.SongRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MusicLibrary {
    private List<Song> songs;
    private SongRepository repository;

    public MusicLibrary() {
        songs = new ArrayList<>();
        repository = new SongRepository(); // Initialize the repository

        // Add some sample songs
        songs.add(new Song(1L, "Let Down", "Radiohead", "music/Let Down.mp3"));
        songs.add(new Song(2L, "Another Song", "Another Artist", "music/Another Song.mp3"));
        // Add more songs as needed
    }

    public void addSong(Song song) {
        SongEntity entity = convertToEntity(song);
        repository.addSong(entity.getTitle(), entity.getArtist(), entity.getFilePath());
    }

    public void removeSong(Song song) {
        repository.deleteSong(song.getId());
    }

    public List<Song> getAllSongs() {
        List<SongEntity> entities = repository.getAllSongs();
        return entities.stream()
                .map(this::convertToSong)
                .collect(Collectors.toList());
    }

    public Song getSongByTitle(String title) {
        return getAllSongs().stream()
                .filter(song -> song.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    private SongEntity convertToEntity(Song song) {
        return new SongEntity(song.getTitle(), song.getArtist(), song.getFilePath(), song.getLyricsPath(), song.getImagePath());
    }

    private Song convertToSong(SongEntity entity) {
        return new Song(entity.getId(), entity.getTitle(), entity.getArtist(), entity.getFilePath());
    }


    public void close() {
        repository.close();
    }
}

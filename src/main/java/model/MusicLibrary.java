package model;

import persistence.SongEntity;
import persistence.SongRepository;

import java.util.ArrayList;
import java.util.List;

public class MusicLibrary {
    private List<Song> songs;
    private SongRepository repository;

    public MusicLibrary(SongRepository repository) {
        this.repository = repository;
        this.songs = new ArrayList<>();
        loadSongs();
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

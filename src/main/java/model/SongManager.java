package model;

import persistence.SongRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SongManager {
    private SongRepository songRepository;

    public SongManager() {
        this.songRepository = new SongRepository();
    }

    public void addSong(Song song, File audioFile, File lyricsFile, File imageFile) throws IOException {
        saveFiles(song, audioFile, lyricsFile, imageFile);
        songRepository.addSong(song);
    }

    public void editSong(Song song, File audioFile, File lyricsFile, File imageFile) throws IOException {
        saveFiles(song, audioFile, lyricsFile, imageFile);
        songRepository.updateSong(song);
    }

    public void deleteSong(Long songId) {
        Song song = songRepository.getSongById(songId);
        if (song != null) {
            deleteFiles(song);
            songRepository.deleteSong(songId);
        }
    }

    private void saveFiles(Song song, File audioFile, File lyricsFile, File imageFile) throws IOException {
        if (audioFile != null) {
            String audioFileName = saveFile(audioFile, "music", song.getTitle());
            song.setFilePath(audioFileName);
        }
        if (lyricsFile != null) {
            String lyricsFileName = saveFile(lyricsFile, "lyrics", song.getTitle());
            song.setLyricsPath(lyricsFileName);
        }
        if (imageFile != null) {
            String imageFileName = saveFile(imageFile, "image", song.getTitle());
            song.setImagePath(imageFileName);
        }
    }

    private String saveFile(File file, String folder, String baseName) throws IOException {
        String extension = getFileExtension(file.getName());
        String fileName = baseName.replaceAll("\\s+", "_") + extension;
        Path targetPath = new File("resources/" + folder, fileName).toPath();

        Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    private void deleteFiles(Song song) {
        deleteFile("music", song.getFilePath());
        deleteFile("lyrics", song.getLyricsPath());
        deleteFile("image", song.getImagePath());
    }

    private void deleteFile(String folder, String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            File file = new File("resources/" + folder, fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex) : "";
    }
}

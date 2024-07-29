package model;

import persistence.SongRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SongManager {
    private SongRepository songRepository;
    private static final Logger LOGGER = Logger.getLogger(SongManager.class.getName());

    private static final String MUSIC_DIRECTORY = "src/main/resources/music";
    private static final String IMAGE_DIRECTORY = "src/main/resources/image/";
    private static final String LYRICS_DIRECTORY = "src/main/resources/lyrics/";


    public SongManager() {
        this.songRepository = new SongRepository();
    }

    public void addSong(Song song, File audioFile, File lyricsFile, File imageFile) throws IOException {
        try {
            saveFiles(song, audioFile, lyricsFile, imageFile);
            songRepository.addSong(song);
            LOGGER.info("Song added successfully: " + song.getTitle());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error adding song: " + song.getTitle(), e);
            throw new IOException("Failed to add song: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error adding song: " + song.getTitle(), e);
            throw new RuntimeException("Unexpected error while adding song: " + e.getMessage(), e);
        }
    }

    public void editSong(Song song, File audioFile, File lyricsFile, File imageFile) throws IOException {
        try {
            saveFiles(song, audioFile, lyricsFile, imageFile);
            songRepository.updateSong(song);
            LOGGER.info("Song updated successfully: " + song.getTitle());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error updating song: " + song.getTitle(), e);
            throw new IOException("Failed to update song: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error updating song: " + song.getTitle(), e);
            throw new RuntimeException("Unexpected error while updating song: " + e.getMessage(), e);
        }
    }

    public void deleteSong(Long songId) {
        try {
            Song song = songRepository.getSongById(songId);
            if (song != null) {
                deleteFiles(song);  // Delete files associated with the song
                songRepository.deleteSong(songId);  // Delete song from the repository
                LOGGER.info("Song deleted successfully: " + song.getTitle());
            } else {
                LOGGER.warning("Attempted to delete non-existent song with ID: " + songId);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting song with ID: " + songId, e);
            throw new RuntimeException("Failed to delete song: " + e.getMessage(), e);
        }
    }


    public List<Song> getAllSongs() {
        try {
            List<Song> songs = songRepository.getAllSongs();
            LOGGER.info("Retrieved " + songs.size() + " songs");
            return songs;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all songs", e);
            throw new RuntimeException("Failed to retrieve songs: " + e.getMessage(), e);
        }
    }

    public Song getSongById(Long id) {
        try {
            Song song = songRepository.getSongById(id);
            if (song != null) {
                LOGGER.info("Retrieved song: " + song.getTitle());
            } else {
                LOGGER.warning("Song not found with ID: " + id);
            }
            return song;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving song with ID: " + id, e);
            throw new RuntimeException("Failed to retrieve song: " + e.getMessage(), e);
        }
    }

    public void saveFiles(Song song, File audioFile, File lyricsFile, File imageFile) throws IOException {
        if (audioFile != null) {
            File targetAudioFile = new File(MUSIC_DIRECTORY + audioFile.getName());
            Files.copy(audioFile.toPath(), targetAudioFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            song.setFilePath(targetAudioFile.getPath());
        }

        if (lyricsFile != null) {
            File targetLyricsFile = new File(LYRICS_DIRECTORY + lyricsFile.getName());
            Files.copy(lyricsFile.toPath(), targetLyricsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            song.setLyricsPath(targetLyricsFile.getPath());
        }

        if (imageFile != null) {
            File targetImageFile = new File(IMAGE_DIRECTORY + imageFile.getName());
            Files.copy(imageFile.toPath(), targetImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            song.setImagePath(targetImageFile.getPath());
        }
    }

    private String saveFile(File file, String folder, String baseName) throws IOException {
        String extension = getFileExtension(file.getName());
        String fileName = baseName.replaceAll("\\s+", "_") + extension;
        Path targetPath = new File("resources/" + folder, fileName).toPath();

        try {
            // Ensure directory exists
            Files.createDirectories(targetPath.getParent());

            // Copy file
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            LOGGER.info("File saved successfully: " + targetPath.toString());
            return fileName;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving file: " + file.getAbsolutePath() + " to " + targetPath.toString(), e);
            throw new IOException("Error saving file: " + file.getAbsolutePath() +
                    " to " + targetPath.toString() + ". " + e.getMessage(), e);
        }
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
                boolean deleted = file.delete();
                if (deleted) {
                    LOGGER.info("Deleted file: " + file.getAbsolutePath());
                } else {
                    LOGGER.warning("Failed to delete file: " + file.getAbsolutePath());
                }
            } else {
                LOGGER.warning("File not found for deletion: " + file.getAbsolutePath());
            }
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex) : "";
    }
}
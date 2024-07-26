package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LyricsManager {
    public String getLyrics(Song song) throws IOException {
        String lyricsPath = song.getLyricsPath();
        try (FileInputStream inputStream = new FileInputStream(lyricsPath)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("Lyrics file not found or cannot be read: " + lyricsPath, e);
        }
    }
}
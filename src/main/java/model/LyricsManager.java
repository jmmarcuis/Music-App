package model;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LyricsManager {
    public String getLyrics(Song song) throws IOException {
        String lyricsPath = song.getLyricsPath();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(lyricsPath)) {
            if (inputStream != null) {
                return new String(((InputStream) inputStream).readAllBytes(), StandardCharsets.UTF_8);
            } else {
                throw new IOException("Lyrics file not found: " + lyricsPath);
            }
        }
    }
}
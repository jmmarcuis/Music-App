package model;

public class Song {
    private Long id;
    private String title;
    private String artist;
    private String filePath;
    private String lyricsPath;
    private String imagePath;

    public Song(Long id, String title, String artist, String filePath, String lyricsPath, String imagePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.lyricsPath = lyricsPath;
        this.imagePath = imagePath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getLyricsPath() { return lyricsPath; }
    public void setLyricsPath(String lyricsPath) { this.lyricsPath = lyricsPath; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}

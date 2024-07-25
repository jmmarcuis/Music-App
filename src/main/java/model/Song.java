package model;

public class Song {
    private Long id;
    private String title;
    private String artist;
    private String filePath;

    public Song(Long id, String title, String artist, String filePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getLyricsPath() {
        return getResourcePath("lyrics", ".txt");
    }

    public String getImagePath() {
        return getResourcePath("images", ".jpg");
    }

    private String getResourcePath(String folder, String extension) {
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
        return folder + "/" + nameWithoutExtension + extension;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}
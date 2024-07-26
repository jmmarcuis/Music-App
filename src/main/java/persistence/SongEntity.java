package persistence;


import javax.persistence.*;

@Entity
@Table(name = "songs")
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "lyrics_path")
    private String lyricsPath;

    @Column(name = "image_path")
    private String imagePath;

    // Constructors, getters, and setters

    public SongEntity() {}


    public SongEntity(String title, String artist, String filePath, String lyricsPath, String imagePath) {
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.lyricsPath = lyricsPath;
        this.imagePath = imagePath;
    }

    // Getters and setters
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
}
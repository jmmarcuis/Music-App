package view;

import model.Song;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
public class MusicPlayerView extends JFrame {
    private JList<String> songList;
    private JTextArea lyricsArea;
    private JButton playButton, pauseButton, stopButton;
    private JSlider volumeSlider;
    private JLabel imageLabel;
    private JLabel nowPlayingLabel;

    public MusicPlayerView() {
        setTitle("Music Player");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Song list
        songList = new JList<>();
        JScrollPane songScrollPane = new JScrollPane(songList);
        songScrollPane.setPreferredSize(new Dimension(200, 0));
        add(songScrollPane, BorderLayout.WEST);

        // Lyrics area
        lyricsArea = new JTextArea();
        lyricsArea.setEditable(false);
        JScrollPane lyricsScrollPane = new JScrollPane(lyricsArea);
        add(lyricsScrollPane, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel();
        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");
        volumeSlider = new JSlider(0, 100, 50);
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(new JLabel("Volume:"));
        controlPanel.add(volumeSlider);
        add(controlPanel, BorderLayout.SOUTH);

        // Image panel
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 200));
        add(imageLabel, BorderLayout.EAST);

        // Now playing label
        nowPlayingLabel = new JLabel("No song playing");
        add(nowPlayingLabel, BorderLayout.NORTH);
    }

    public void updateSongList(List<Song> songs) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Song song : songs) {
            listModel.addElement(song.getTitle());
        }
        songList.setModel(listModel);
    }

    public void updateLyrics(String lyrics) {
        lyricsArea.setText(lyrics);
        lyricsArea.setCaretPosition(0);
    }

    public void updateImage(String imagePath) {
        try {
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            if (imageStream != null) {
                BufferedImage img = ImageIO.read(imageStream);
                if (img != null) {
                    Image scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImg));
                } else {
                    throw new IOException("Failed to read image");
                }
            } else {
                throw new IOException("Image file not found: " + imagePath);
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            imageLabel.setIcon(null); // Clear the image if there's an error
        }
    }
    public void updateNowPlaying(String songTitle) {
        nowPlayingLabel.setText("Now playing: " + songTitle);
    }

    public void setSongSelectionListener(javax.swing.event.ListSelectionListener listener) {
        songList.addListSelectionListener(listener);
    }

    public void setPlayButtonListener(ActionListener listener) {
        playButton.addActionListener(listener);
    }

    public void setPauseButtonListener(ActionListener listener) {
        pauseButton.addActionListener(listener);
    }

    public void setStopButtonListener(ActionListener listener) {
        stopButton.addActionListener(listener);
    }

    public void setVolumeSliderListener(javax.swing.event.ChangeListener listener) {
        volumeSlider.addChangeListener(listener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
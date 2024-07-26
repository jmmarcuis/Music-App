package view;

import model.Song;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private JButton addButton, editButton, deleteButton;
    private JSlider volumeSlider;
    private JLabel imageLabel;
    private JLabel nowPlayingLabel;
    private JProgressBar progressBar;

    public MusicPlayerView() {
        setTitle("Modern Music Player");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(18, 18, 18));

        // Left panel (Song list and CRUD buttons)
        JPanel leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.WEST);

        // Center panel (Lyrics and Image)
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel (Controls)
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Now playing label
        nowPlayingLabel = new JLabel("No song playing");
        nowPlayingLabel.setForeground(Color.WHITE);
        nowPlayingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nowPlayingLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(nowPlayingLabel, BorderLayout.NORTH);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setBackground(new Color(24, 24, 24));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        songList = new JList<>();
        songList.setBackground(new Color(30, 30, 30));
        songList.setForeground(Color.WHITE);
        songList.setSelectionBackground(new Color(55, 55, 55));
        JScrollPane songScrollPane = new JScrollPane(songList);
        songScrollPane.setPreferredSize(new Dimension(250, 500));

        JPanel crudButtonPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        crudButtonPanel.setOpaque(false);
        addButton = createStyledButton("Add");
        editButton = createStyledButton("Edit");
        deleteButton = createStyledButton("Delete");
        crudButtonPanel.add(addButton);
        crudButtonPanel.add(editButton);
        crudButtonPanel.add(deleteButton);

        leftPanel.add(songScrollPane, BorderLayout.CENTER);
        leftPanel.add(crudButtonPanel, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(24, 24, 24));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        lyricsArea = new JTextArea();
        lyricsArea.setEditable(false);
        lyricsArea.setBackground(new Color(30, 30, 30));
        lyricsArea.setForeground(Color.WHITE);
        lyricsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane lyricsScrollPane = new JScrollPane(lyricsArea);
        lyricsScrollPane.setPreferredSize(new Dimension(400, 400));

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(250, 250));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setText("No image");
        imageLabel.setForeground(Color.WHITE);

        centerPanel.add(lyricsScrollPane, BorderLayout.CENTER);
        centerPanel.add(imageLabel, BorderLayout.EAST);

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(24, 24, 24));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        controlPanel.setOpaque(false);
        playButton = createCircularButton("▶");
        pauseButton = createCircularButton("⏸");
        stopButton = createCircularButton("⏹");
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);

        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBackground(new Color(24, 24, 24));
        volumeSlider.setForeground(Color.WHITE);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setBackground(new Color(30, 30, 30));
        progressBar.setForeground(new Color(30, 215, 96));

        bottomPanel.add(controlPanel, BorderLayout.NORTH);
        bottomPanel.add(volumeSlider, BorderLayout.CENTER);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);

        return bottomPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(40, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    private JButton createCircularButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(50, 50));
        button.setBackground(new Color(30, 215, 96));
        button.setForeground(Color.BLACK);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        return button;
    }

    public void updateSongList(List<Song> songs) {
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Song song : songs) {
                listModel.addElement(song.getTitle());
            }
            songList.setModel(listModel);
        });
    }

    public void updateLyrics(String lyrics) {
        SwingUtilities.invokeLater(() -> {
            lyricsArea.setText(lyrics);
            lyricsArea.setCaretPosition(0);
        });
    }

    public void updateNowPlaying(String songTitle) {
        SwingUtilities.invokeLater(() -> nowPlayingLabel.setText("Now playing: " + songTitle));
    }

    public void updateProgressBar(int progress) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(progress);
            progressBar.setString(progress + "%");
        });
    }

    public void updateImage(String imagePath) {
        SwingUtilities.invokeLater(() -> {
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    // Use class loader to get the resource as a stream
                    InputStream imageStream = getClass().getResourceAsStream("/image/" + imagePath);
                    if (imageStream != null) {
                        BufferedImage img = ImageIO.read(imageStream);
                        Image scaledImg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImg));
                        imageLabel.setText("");
                    } else {
                        throw new IOException("Image not found: " + imagePath);
                    }
                } catch (IOException e) {
                    System.err.println("Error loading image: " + e.getMessage());
                    imageLabel.setIcon(null);
                    imageLabel.setText("Image not found");
                }
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("No image");
            }
        });
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

    public void updateVolumeSlider(int volume) {
        volumeSlider.setValue(volume);
    }


    public void showError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE));
    }




}
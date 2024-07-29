package controller;

import model.Song;
import model.SongManager;
import view.MusicPlayerView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SongCRUDController {
    private MusicPlayerView view;
    private SongManager songManager;

    public SongCRUDController(MusicPlayerView view, SongManager songManager) {
        this.view = view;
        this.songManager = songManager;
    }

    public void initialize() {
        view.setAddButtonListener(e -> showAddSongDialog());
        view.setEditButtonListener(e -> showEditSongDialog());
        view.setDeleteButtonListener(e -> deleteSelectedSong());
    }

    private void showAddSongDialog() {
        JDialog dialog = new JDialog(view, "Add New Song", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField titleField = new JTextField(20);
        JTextField artistField = new JTextField(20);
        JTextField audioFileField = new JTextField(20);
        JTextField lyricsFileField = new JTextField(20);
        JTextField imageFileField = new JTextField(20);

        JButton audioChooseButton = new JButton("Choose Audio File");
        JButton lyricsChooseButton = new JButton("Choose Lyrics File");
        JButton imageChooseButton = new JButton("Choose Image File");

        dialog.add(new JLabel("Title:"), gbc);
        dialog.add(titleField, gbc);
        dialog.add(new JLabel("Artist:"), gbc);
        dialog.add(artistField, gbc);
        dialog.add(new JLabel("Audio File:"), gbc);
        dialog.add(audioFileField, gbc);
        dialog.add(audioChooseButton, gbc);
        dialog.add(new JLabel("Lyrics File:"), gbc);
        dialog.add(lyricsFileField, gbc);
        dialog.add(lyricsChooseButton, gbc);
        dialog.add(new JLabel("Image File:"), gbc);
        dialog.add(imageFileField, gbc);
        dialog.add(imageChooseButton, gbc);

        JButton addButton = new JButton("Add Song");
        dialog.add(addButton, gbc);

        audioChooseButton.addActionListener(e -> chooseFile(audioFileField, "Audio Files", "mp3", "wav"));
        lyricsChooseButton.addActionListener(e -> chooseFile(lyricsFileField, "Text Files", "txt"));
        imageChooseButton.addActionListener(e -> chooseFile(imageFileField, "Image Files", "jpg", "png"));

        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String artist = artistField.getText();
            String audioPath = audioFileField.getText();
            String lyricsPath = lyricsFileField.getText();
            String imagePath = imageFileField.getText();

            if (!title.isEmpty() && !artist.isEmpty() && !audioPath.isEmpty()) {
                addSong(title, artist, new File(audioPath),
                        lyricsPath.isEmpty() ? null : new File(lyricsPath),
                        imagePath.isEmpty() ? null : new File(imagePath));
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Title, Artist, and Audio File are required.");
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(view);
        dialog.setVisible(true);
    }

    private void showEditSongDialog() {
        int selectedIndex = view.getSelectedSongIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(view, "Please select a song to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Song> songs = songManager.getAllSongs();
        Song selectedSong = songs.get(selectedIndex);

        JDialog dialog = new JDialog(view, "Edit Song", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField titleField = new JTextField(selectedSong.getTitle(), 20);
        JTextField artistField = new JTextField(selectedSong.getArtist(), 20);
        JTextField audioFileField = new JTextField(selectedSong.getFilePath() != null ? selectedSong.getFilePath() : "", 20);
        JTextField lyricsFileField = new JTextField(selectedSong.getLyricsPath() != null ? selectedSong.getLyricsPath() : "", 20);
        JTextField imageFileField = new JTextField(selectedSong.getImagePath() != null ? selectedSong.getImagePath() : "", 20);

        JButton audioChooseButton = new JButton("Choose Audio File");
        JButton lyricsChooseButton = new JButton("Choose Lyrics File");
        JButton imageChooseButton = new JButton("Choose Image File");

        dialog.add(new JLabel("Title:"), gbc);
        dialog.add(titleField, gbc);
        dialog.add(new JLabel("Artist:"), gbc);
        dialog.add(artistField, gbc);
        dialog.add(new JLabel("Audio File:"), gbc);
        dialog.add(audioFileField, gbc);
        dialog.add(audioChooseButton, gbc);
        dialog.add(new JLabel("Lyrics File:"), gbc);
        dialog.add(lyricsFileField, gbc);
        dialog.add(lyricsChooseButton, gbc);
        dialog.add(new JLabel("Image File:"), gbc);
        dialog.add(imageFileField, gbc);
        dialog.add(imageChooseButton, gbc);

        JButton saveButton = new JButton("Save Song");
        JButton cancelButton = new JButton("Cancel");
        dialog.add(saveButton, gbc);
        dialog.add(cancelButton, gbc);

        audioChooseButton.addActionListener(e -> chooseFile(audioFileField, "Audio Files", "mp3", "wav"));
        lyricsChooseButton.addActionListener(e -> chooseFile(lyricsFileField, "Text Files", "txt"));
        imageChooseButton.addActionListener(e -> chooseFile(imageFileField, "Image Files", "jpg", "png"));

        saveButton.addActionListener(e -> {
            String title = titleField.getText();
            String artist = artistField.getText();
            String audioPath = audioFileField.getText();
            String lyricsPath = lyricsFileField.getText();
            String imagePath = imageFileField.getText();

            if (!title.isEmpty() && !artist.isEmpty()) {
                Song updatedSong = new Song(selectedSong.getId(), title, artist, audioPath, lyricsPath, imagePath);
                editSong(updatedSong,
                        audioPath.isEmpty() ? null : new File(audioPath),
                        lyricsPath.isEmpty() ? null : new File(lyricsPath),
                        imagePath.isEmpty() ? null : new File(imagePath));
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Title and Artist are required.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(view);
        dialog.setVisible(true);
    }

    private void deleteSelectedSong() {
        int selectedIndex = view.getSelectedSongIndex();
        if (selectedIndex != -1) {
            String selectedSongTitle = view.getSelectedSongTitle();
            int confirm = JOptionPane.showConfirmDialog(view,
                    "Are you sure you want to delete the song: " + selectedSongTitle + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                List<Song> songs = songManager.getAllSongs();
                Song songToDelete = songs.get(selectedIndex);
                deleteSong(songToDelete.getId());
            }
        } else {
            JOptionPane.showMessageDialog(view, "No song selected to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chooseFile(JTextField field, String description, String... extensions) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(description, extensions));
        int result = fileChooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            field.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void addSong(String title, String artist, File audioFile, File lyricsFile, File imageFile) {
        try {
            Song newSong = new Song(null, title, artist, audioFile.getPath(),
                    lyricsFile != null ? lyricsFile.getPath() : null,
                    imageFile != null ? imageFile.getPath() : null);
            songManager.addSong(newSong, audioFile, lyricsFile, imageFile);
            view.updateSongList(songManager.getAllSongs());
        } catch (IOException e) {
            view.showError("Error adding song: " + e.getMessage());
        }
    }

    public void editSong(Song song, File audioFile, File lyricsFile, File imageFile) {
        try {
            songManager.editSong(song, audioFile, lyricsFile, imageFile);
            view.updateSongList(songManager.getAllSongs());
        } catch (IOException e) {
            view.showError("Error editing song: " + e.getMessage());
        }
    }

    public void deleteSong(Long songId) {
        songManager.deleteSong(songId);
        view.updateSongList(songManager.getAllSongs());
    }
}
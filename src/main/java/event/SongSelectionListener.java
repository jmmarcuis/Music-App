package event;

import controller.MusicPlayerController;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SongSelectionListener implements ListSelectionListener {
    private MusicPlayerController controller;

    public SongSelectionListener(MusicPlayerController controller) {
        this.controller = controller;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        // Ensure the event isn't fired twice
        if (!e.getValueIsAdjusting()) {
            JList<String> list = (JList<String>) e.getSource();
            String selectedSong = list.getSelectedValue();
            if (selectedSong != null) {
                // Stop any currently playing song
                controller.handleStopButton();
                // Then handle the new song selection
                controller.handleSongSelection(selectedSong);
            }
        }
    }

}
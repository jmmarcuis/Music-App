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
        // Ensure the event isn't fired twice (once on press, once on release)
        if (!e.getValueIsAdjusting()) {
            JList<String> list = (JList<String>) e.getSource();
            String selectedSong = list.getSelectedValue();
            if (selectedSong != null) {
                // The controller will handle the actual song loading and playing
                controller.handleSongSelection(selectedSong);
            }
        }
    }
}
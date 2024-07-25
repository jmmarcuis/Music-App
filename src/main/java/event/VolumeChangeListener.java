package event;


import controller.MusicPlayerController;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VolumeChangeListener implements ChangeListener {
    private MusicPlayerController controller;

    public VolumeChangeListener(MusicPlayerController controller) {
        this.controller = controller;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            int volume = source.getValue();
            controller.setVolume(volume);
        }
    }
}
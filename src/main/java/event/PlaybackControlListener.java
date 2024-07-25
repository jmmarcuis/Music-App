package event;


import controller.MusicPlayerController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlaybackControlListener implements ActionListener {
    private MusicPlayerController controller;
    private String action;

    public PlaybackControlListener(MusicPlayerController controller, String action) {
        this.controller = controller;
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (action) {
            case "play":
                controller.handlePlayButton();
                break;
            case "pause":
                controller.handlePauseButton();
                break;
            case "stop":
                controller.handleStopButton();
                break;
            default:
                System.out.println("Unknown action: " + action);
        }
    }
}
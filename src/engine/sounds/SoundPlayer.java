package engine.sounds;

import engine.utils.Constants;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;

public class SoundPlayer {

    private static JFXPanel fxPanel = new JFXPanel();
    private static HashMap<String, MediaPlayer> sounds = new HashMap<>();

    /**
     *
     * @param name Sound file name
     * @param volume Percentage volume : between 0 and 1
     * @param loops Amount of loops : -1 for infinite
     */
    public static void playSound(String name, double volume, int loops) {
        if(sounds.containsKey(name)) {
            MediaPlayer mediaPlayer = sounds.get(name);
            mediaPlayer.setVolume(volume);
            mediaPlayer.setCycleCount(loops);
            mediaPlayer.play();
        }
        else {
            Media hit = new Media(new File(Constants.SOUNDS_PATH + name).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setVolume(volume);
            mediaPlayer.setCycleCount(loops);
            mediaPlayer.play();
            sounds.put(name, mediaPlayer);
        }
    }

}

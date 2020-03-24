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

    public static void init() {
        File soundFolder = new File(Constants.SOUNDS_PATH);

        for (String soundName : soundFolder.list()) {
            Media hit = new Media(new File(Constants.SOUNDS_PATH + soundName).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            sounds.put(soundName, mediaPlayer);
        }
    }

    /**
     *
     * @param name Sound file name
     * @param volume Percentage volume : between 0 and 1
     * @param loops Amount of loops : -1 for infinite
     */
    public static void playSound(String name, double volume, int loops) {
        new Thread(() -> {
            if(sounds.containsKey(name)) {
                MediaPlayer mediaPlayer = sounds.get(name);
                mediaPlayer.setVolume(volume);
                mediaPlayer.setCycleCount(loops);
                mediaPlayer.stop();
                mediaPlayer.play();
            }
        }).start();
    }

}

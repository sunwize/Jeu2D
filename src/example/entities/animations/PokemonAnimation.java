package example.entities.animations;

import engine.utils.Utils;
import example.entities.Pokemon;
import org.json.JSONArray;

import java.awt.image.BufferedImage;

public class PokemonAnimation {

    private Pokemon pokemon;
    private BufferedImage[][] frames;
    private JSONArray durations;
    private boolean active;
    private long lastTime, time;
    private int frame, loops, spriteSize;

    public PokemonAnimation(Pokemon pokemon, BufferedImage spriteSheet, int spriteSize, JSONArray durations, int loops) {
        this.spriteSize = spriteSize;
        this.loops = loops;
        active = true;
        frame = 0;
        this.pokemon = pokemon;
        lastTime = System.currentTimeMillis();

        frames = new BufferedImage[(spriteSheet.getHeight() / spriteSize) + 2][spriteSheet.getWidth() / spriteSize];
        this.durations = durations;

        for(int i = 0; i < frames.length-2; i++) {
            for(int j = 0; j < frames[0].length; j++) {
                frames[i][j] = spriteSheet.getSubimage(j * spriteSize, i * spriteSize, spriteSize, spriteSize);
            }
        }

        for(int i = 0; i < frames[0].length; i++) {
            frames[2][i] = Utils.reverseImageX(frames[0][i]);
            frames[3][i] = Utils.reverseImageX(frames[1][i]);
        }
    }

    public void update() {
        if(active) {
            time = System.currentTimeMillis();
            if(loops != 0 && time - lastTime >= durations.getInt(frame)) { // Next frame
                frame++;
                if(frame == durations.length()) {
                    if(loops == -1)
                        frame = 0;
                    else if(loops > 0) {
                        frame = 0;
                        loops--;

                        if(loops == 0) {
                            frame = 0;
                            active = false;
                        }
                    }
                }
                lastTime = time;
            }
            else if(loops == 0)
                active = false;
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames[pokemon.getDirection()][frame];
    }

}

package engine.animations;

import engine.utils.Clock;

import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {

    private List<Frame> frames;
    private Clock clock;
    private int frame;
    private int loops, savedLoops; // Amount of loops : -1 is infinite
    private boolean active; // If should be removed from Animation Manager

    public Animation(List<Frame> frames, int loops) {
        this.frames = frames;
        this.loops = loops;
        savedLoops = loops;
        frame = 0;
        active = true;
        clock = new Clock();
    }

    public void reset() {
        loops = savedLoops;
        frame = 0;
        active = true;
        clock.reset();
    }

    public void update() {
        if(active) {
            if(loops != 0 && clock.elapsedTime() >= frames.get(frame).duration) { // Next frame
                frame++;
                clock.reset();
                if(frame == frames.size()) {
                    if(loops == -1)
                        frame = 0;
                    else if(loops > 0) {
                        frame = 0;
                        loops--;

                        if(loops == 0) {
                            frame = frames.size()-1; // Finish on last frame
                            active = false;
                        }
                    }
                }
            }
            else if(loops == 0)
                active = false;
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames.get(frame).image;
    }

    public boolean isActive() {
        return active;
    }

    public boolean looping() {
        return loops == -1;
    }

}

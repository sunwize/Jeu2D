package engine.animations;

import java.awt.image.BufferedImage;

public class Frame {

    public BufferedImage image;
    public int duration; // ms

    public Frame(BufferedImage image, int duration) {
        this.image = image;
        this.duration = duration;
    }

}

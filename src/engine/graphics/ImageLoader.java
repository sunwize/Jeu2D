package engine.graphics;

import engine.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ImageLoader {

    private static HashMap<String, BufferedImage> images = new HashMap<>();

    public static BufferedImage loadImage(String name) {
        if(images.containsKey(name))
            return images.get(name);
        else {
            try {
                BufferedImage image = ImageIO.read(new File(Constants.IMAGES_PATH + name));
                images.put(name, image);
                return image;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static BufferedImage loadImageFromUrl(String path) {
        if(images.containsKey(path))
            return images.get(path);
        else {
            try {
                BufferedImage image = ImageIO.read(new URL(path));
                images.put(path, image);
                return image;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}

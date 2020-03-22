package engine.utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static BufferedImage reverseImageX(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    public static String loadJSON(String path) {
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            String content = lines.collect(Collectors.joining(System.lineSeparator()));
            return content.trim();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

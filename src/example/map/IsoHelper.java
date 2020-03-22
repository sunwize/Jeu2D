package example.map;

import java.awt.*;

public class IsoHelper {

    public static Point.Double cartToIso(double cx, double cy) {
        double x = cx - cy;
        double y = (cx + cy) / 2.0;
        return new Point.Double(x, y);
    }

    public static Point.Double isoToCart(double cx, double cy) {
        double x = (2.0*cy + cx) / 2.0;
        double y = (2.0*cy - cx) / 2.0;

        return new Point.Double(x, y);
    }

    public static Point.Double tileCoordinates(double cx, double cy, int tileHeight) {
        double x = Math.floor(cx / tileHeight);
        double y = Math.floor(cy / tileHeight);

        return new Point.Double(x, y);
    }

}

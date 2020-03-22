package example.utils;

import engine.graphics.ImageLoader;

import java.awt.*;

public class Cursors {

    public static Cursor standard = Toolkit.getDefaultToolkit().createCustomCursor(
            ImageLoader.loadImage("cursor.png"),
            new Point(16,3),"standard cursor");

    public static Cursor select = Toolkit.getDefaultToolkit().createCustomCursor(
            ImageLoader.loadImage("select.png"),
            new Point(16,3),"select cursor");

    public static Cursor delete = Toolkit.getDefaultToolkit().createCustomCursor(
            ImageLoader.loadImage("delete.png"),
            new Point(16,3),"delete cursor");

}

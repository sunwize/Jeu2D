package jeu2d.maps;

import com.sun.javafx.geom.Vec2d;
import engine.display.Renderer;
import engine.graphics.ImageLoader;
import engine.utils.Constants;
import jeu2d.attacks.AreaManager;
import jeu2d.utils.Config;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.ListIterator;

public class ArenaMap {

    private LinkedList<Rectangle2D.Double> walls;
    private BufferedImage background;
    private AreaManager areaManager;

    public ArenaMap(String backgroundPath, AreaManager areaManager) {
        background = ImageLoader.loadImage(backgroundPath);
        this.areaManager = areaManager;
        walls = new LinkedList<>();
    }

    public void render(Renderer renderer) {
        renderer.drawImage(background, Constants.MAP_GRID_COLUMNS, 0, 0);

        if (Config.DEBUG) {
            for (Rectangle2D.Double wall : walls)
                renderer.drawRect(wall.x, wall.y, wall.width, wall.height, Color.BLUE, false);
        }
    }

    // Return true when there is NO collision
    public boolean checkWallsCollision(Rectangle2D.Double bounds) { // (cx, cy) is the tested destination
        ListIterator<Rectangle2D.Double> it = walls.listIterator();

        while (it.hasNext()) {
            Rectangle2D.Double wall = it.next();
            if (wall.intersects(bounds)) {
                return false;
            }
        }

        return true;
    }

    public void addWall(Rectangle2D.Double wall) {
        walls.add(wall);
    }

    public Vec2d friction() {
        return new Vec2d(0.75, 0.1);
    }

    public AreaManager getAreaManager() {
        return areaManager;
    }

}

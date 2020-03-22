package example.map;

import engine.display.Renderer;
import example.entities.Pokemon;

import java.awt.*;
import java.awt.geom.Point2D;

public class IsoTile {

    public IsoMap map;
    public Rectangle cartBounds;
    public Polygon isoBounds;
    public boolean hover;
    public Pokemon taken;

    public IsoTile(IsoMap map, int cx, int cy, int width, int height) {
        this.map = map;
        hover = false;
        cartBounds = new Rectangle(cx, cy, width, height);

        Point.Double p1 = IsoHelper.cartToIso(cartBounds.x, cartBounds.y);
        Point.Double p2 = IsoHelper.cartToIso(cartBounds.x + cartBounds.width, cartBounds.y);
        Point.Double p3 = IsoHelper.cartToIso(cartBounds.x + cartBounds.width, cartBounds.y + cartBounds.height);
        Point.Double p4 = IsoHelper.cartToIso(cartBounds.x, cartBounds.y + cartBounds.height);

        isoBounds = new Polygon(new int[]{(int) p1.x, (int) p2.x, (int) p3.x, (int) p4.x}, new int[]{(int) p1.y, (int) p2.y, (int) p3.y, (int) p4.y}, 4);

        for(int i = 0; i < isoBounds.npoints; i++) {
            isoBounds.xpoints[i] += map.getxOffset();
            isoBounds.ypoints[i] += map.getyOffset();
        }
        cartBounds.x += map.getxOffset();
        cartBounds.y += map.getyOffset();
    }

    public void render(Renderer renderer) {
        Color color = Color.BLACK;
        if(hover)
            color = Color.YELLOW;
        else if(taken != null)
            color = Color.RED;
        //renderer.drawRect(cartBounds.x, cartBounds.y, cartBounds.width, cartBounds.height, Color.BLACK, false);
        renderer.drawIsoTile(isoBounds, color, false);
        //Point.Double p = isoCoordinates();
        //renderer.drawOval(p.x - 0.5, p.y - 0.5, 1, 1, Color.CYAN);
    }

    public Point.Double isoCoordinates() {
        return new Point.Double(isoBounds.getBounds().getCenterX(), isoBounds.getBounds().getCenterY());
    }

    public Point.Double cartCoordinates() {
        return new Point2D.Double(cartBounds.getCenterX(), cartBounds.getCenterY());
    }

}

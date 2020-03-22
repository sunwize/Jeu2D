package engine.entities;

import engine.display.Renderer;

import java.awt.geom.Rectangle2D;

public interface IEntity {

    /**
     * @return if should be removed from entity manager
     */
    public boolean isActive();

    public void update();

    public void render(Renderer renderer);

    public default void dispose() {

    }

    public double getCX();

    public double getCY();

    public Rectangle2D.Double getBounds();

}

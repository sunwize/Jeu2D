package jeu2d.attacks;

import engine.display.Renderer;
import engine.entities.IEntity;
import jeu2d.entities.Character;

import java.awt.geom.Rectangle2D;

public interface Area {

    public Rectangle2D.Double getBounds();

    public Character getSource();

    public void onContact(IEntity target);

    public boolean isActive();

    public void update();

    public void render(Renderer renderer);

    public void dispose();

}

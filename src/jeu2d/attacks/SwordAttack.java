package jeu2d.attacks;

import engine.display.Renderer;
import engine.entities.IEntity;
import engine.utils.Clock;
import jeu2d.entities.Character;
import jeu2d.utils.Config;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.ListIterator;

public class SwordAttack implements Area {

    private Rectangle2D.Double bounds;
    private Character source;
    private int duration, damage;
    private Clock clock;
    private boolean active;
    private LinkedList<IEntity> touched;

    public SwordAttack(Rectangle2D.Double bounds, Character source, int damage, int duration) {
        this.bounds = bounds;
        this.source = source;
        this.damage = damage;
        this.duration = duration;
        active = true;
        touched = new LinkedList<>();
        clock = new Clock();
    }

    @Override
    public Rectangle2D.Double getBounds() {
        Rectangle2D.Double sourceBounds = source.getBounds();
        return new Rectangle2D.Double(sourceBounds.x + bounds.x, sourceBounds.y + bounds.y, bounds.width, bounds.height);
    }

    @Override
    public Character getSource() {
        return source;
    }

    @Override
    public void onContact(IEntity target) {
        if (target.equals(source) || touched.contains(target))
            return;
        touched.add(target);

        if (target instanceof Character) {
            Character c = (Character) target;
            c.hurt(0);
            double force = 5;
            if (source.getDirection() == Character.RIGHT)
                c.move(force, 0);
            else if (source.getDirection() == Character.LEFT)
                c.move(-force, 0);
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void update() {
        if (active) {
            if (clock.elapsedTime() > duration) {
                clock.reset();
                active = false;
            }
        }
    }

    @Override
    public void render(Renderer renderer) {
        if (Config.DEBUG) {
            Rectangle2D.Double bounds = getBounds();
            renderer.drawRect(bounds.x, bounds.y, bounds.width, bounds.height, Color.RED, false);
        }
    }

    @Override
    public void dispose() {
        ListIterator<IEntity> it = touched.listIterator();
        while (it.hasNext()) {
            IEntity e = it.next();
            it.remove();
            e.dispose();
        }
    }

}

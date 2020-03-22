package jeu2d.attacks;

import engine.display.Renderer;
import engine.entities.IEntity;
import engine.managers.EntityManager;
import engine.managers.Manager;
import jeu2d.entities.Character;

import java.util.ListIterator;

public class AreaManager extends Manager<Area> {

    private EntityManager entityManager;

    public AreaManager(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    @Override
    public void update() {
        ListIterator<Area> it = super.list.listIterator();
        while (it.hasNext()) {
            Area a = it.next();
            if (!a.isActive())
                it.remove();
            else {
                a.update();

                for (IEntity e : entityManager.elements()) {
                    if (e instanceof Character && e.getBounds().intersects(a.getBounds())) {
                        a.onContact(e);
                    }
                }
            }
        }
    }

    @Override
    public void render(Renderer renderer) {
        ListIterator<Area> it = super.list.listIterator();
        while (it.hasNext()) {
            it.next().render(renderer);
        }
    }

    @Override
    public void dispose() {
        ListIterator<Area> it = super.list.listIterator();
        while(it.hasNext()) {
            Area a = it.next();
            it.remove();
            a.dispose();
        }
    }

}

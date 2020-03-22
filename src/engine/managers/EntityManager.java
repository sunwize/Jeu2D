package engine.managers;

import engine.display.Renderer;
import engine.entities.IEntity;

import java.util.ListIterator;

public class EntityManager extends Manager<IEntity> {

    public EntityManager() {
        super();
    }

    @Override
    public void update() {
        ListIterator<IEntity> it = super.list.listIterator();
        while(it.hasNext()) {
            IEntity entity = it.next();
            entity.update();
            if(!entity.isActive()) {
                it.remove();
            }
        }
    }

    @Override
    public void render(Renderer renderer) {
        ListIterator<IEntity> it = super.list.listIterator();
        while(it.hasNext()) {
            it.next().render(renderer);
        }
    }

    @Override
    public void dispose() {
        ListIterator<IEntity> it = super.list.listIterator();
        while(it.hasNext()) {
            IEntity entity = it.next();
            it.remove();
            entity.dispose();
        }
    }

}

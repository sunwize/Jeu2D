package engine.managers;

import engine.display.Renderer;
import engine.ui.UIComponent;

import java.util.ListIterator;

public class UIManager extends Manager<UIComponent> {

    public UIManager() {
        super();
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Renderer renderer) {
        ListIterator<UIComponent> it = super.list.listIterator();
        while(it.hasNext()) {
            it.next().render(renderer);
        }
    }

    @Override
    public void dispose() {
        ListIterator<UIComponent> it = super.list.listIterator();
        while(it.hasNext()) {
            it.next().dispose();
        }
    }

}

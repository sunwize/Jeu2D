package engine.managers;

import engine.display.Renderer;

import java.util.LinkedList;

public abstract class Manager<T> {

    protected LinkedList<T> list;

    public Manager() {
        list = new LinkedList<>();
    }

    public void add(T element) {
        list.add(element);
    }

    public void remove(T element) {
        list.remove(element);
    }

    public LinkedList<T> elements() {
        return list;
    }

    public abstract void update();

    public abstract void render(Renderer renderer);

    public abstract void dispose();

}

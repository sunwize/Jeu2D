package engine.states;

import engine.display.Renderer;

public interface IState {

    public void update();

    public void render(Renderer renderer);

    public void dispose();

}

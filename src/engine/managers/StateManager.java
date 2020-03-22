package engine.managers;

import engine.display.Renderer;
import engine.states.IState;

import java.util.ListIterator;

public class StateManager extends Manager<IState> {

    public StateManager() {
        super();
    }

    public void exitCurrentState() {
        IState state = super.list.pop();
        state.dispose();
    }

    public void switchState(IState state) {
        super.list.push(state);
    }

    @Override
    public void update() {
        if(list.size() > 0)
            super.list.peek().update();
    }

    @Override
    public void render(Renderer renderer) {
        if(list.size() > 0)
            super.list.peek().render(renderer);
    }

    @Override
    public void dispose() {
        ListIterator<IState> it = super.list.listIterator();
        while(it.hasNext()) {
            IState state = it.next();
            it.remove();
            state.dispose();
        }
    }

}

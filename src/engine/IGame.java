package engine;

import engine.managers.StateManager;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

public interface IGame {

    public StateManager getStateManager();

    /**
     * @return The component used for input listeners
     * note: Must be focusable
     */
    public Component inputs();

    public default void clearInputs() {
        for(MouseListener ml : inputs().getMouseListeners())
            inputs().removeMouseListener(ml);
        for(MouseMotionListener mml : inputs().getMouseMotionListeners())
            inputs().removeMouseMotionListener(mml);
        for(MouseWheelListener mwl : inputs().getMouseWheelListeners())
            inputs().removeMouseWheelListener(mwl);
        for(KeyListener kl : inputs().getKeyListeners())
            inputs().removeKeyListener(kl);
    }

    public void update();

    public void render();

    public void dispose();

}

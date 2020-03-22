package example;

import engine.GameEngine;
import engine.IGame;
import engine.display.Display;
import engine.graphics.ImageLoader;
import engine.managers.StateManager;
import example.states.Menu;

import java.awt.*;

public class Game implements IGame {

    private StateManager stateManager;
    private Display display;

    public Game() {
        display = new Display("Game");
        stateManager = new StateManager();
        stateManager.switchState(new Menu(this));
        display.getFrame().setIconImage(ImageLoader.loadImage("icon.png"));
    }

    @Override
    public StateManager getStateManager() {
        return stateManager;
    }

    @Override
    public Component inputs() {
        return display.getCanvas();
    }

    @Override
    public void update() {
        stateManager.update();
    }

    @Override
    public void render() {
        display.preRender();

        // Rendering
        stateManager.render(display.getRenderer());
        // End rendering

        display.postRender();
    }

    @Override
    public void dispose() {
        display.dispose();
        stateManager.dispose();
    }

    public Display getDisplay() {
        return display;
    }

    public static void main(String[] args) {
        IGame game = new Game();
        new GameEngine(game);
    }

}

package jeu2d;

import engine.GameEngine;
import engine.IGame;
import engine.display.Display;
import engine.managers.StateManager;
import jeu2d.states.ArenaState;

import java.awt.*;

public class Game implements IGame {

    private StateManager stateManager;
    private Display display;

    public Game() {
        display = new Display("Jeu 2D");
        stateManager = new StateManager();
        stateManager.switchState(new ArenaState(this));
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
        stateManager.render(display.getRenderer());
        display.postRender();
    }

    @Override
    public void dispose() {
        display.dispose();
        stateManager.dispose();
    }

    public static void main(String[] args) {
        IGame game = new Game();
        new GameEngine(game);
    }
}

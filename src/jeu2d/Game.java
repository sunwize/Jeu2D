package jeu2d;

import engine.GameEngine;
import engine.IGame;
import engine.display.Display;
import engine.managers.StateManager;
import engine.sounds.SoundPlayer;
import jeu2d.states.ArenaState;

import java.awt.*;

public class Game implements IGame {

    private StateManager stateManager;
    private Display display;

    public Game() {
        SoundPlayer.init();
        display = new Display("Abort The Mission");
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

package jeu2d.states;

import engine.IGame;
import engine.display.Renderer;
import engine.managers.EntityManager;
import engine.states.IState;
import jeu2d.attacks.AreaManager;
import jeu2d.entities.Character;
import jeu2d.entities.controllers.Controller;
import jeu2d.maps.ArenaMap;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ArenaState implements IState {

    private IGame game;
    private ArenaMap map;
    private EntityManager entityManager;
    private AreaManager areaManager;
    private Controller controller;

    public ArenaState(IGame game) {
        this.game = game;
        entityManager = new EntityManager();
        areaManager = new AreaManager(entityManager);

        map = new ArenaMap("background.png", areaManager);
        map.addWall(new Rectangle2D.Double(32, 60, 96, 10));
        map.addWall(new Rectangle2D.Double(45, 46, 21, 2));
        map.addWall(new Rectangle2D.Double(94, 46, 21, 2));
        map.addWall(new Rectangle2D.Double(70, 33, 20, 1));

        Character character = new Character("knight", map, 50, 20);
        controller = new Controller(character);
        this.game.inputs().addKeyListener(controller);
        entityManager.add(character);

        Character test = new Character("knight", map, 70, 20);
        entityManager.add(test);
    }

    @Override
    public void update() {
        controller.update();
        areaManager.update();
        entityManager.update();
    }

    @Override
    public void render(Renderer renderer) {
        renderer.background(Color.BLACK);
        map.render(renderer);
        areaManager.render(renderer);
        entityManager.render(renderer);
    }

    @Override
    public void dispose() {
        entityManager.dispose();
        game.clearInputs();
        areaManager.dispose();
    }

}

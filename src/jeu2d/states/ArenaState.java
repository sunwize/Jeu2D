package jeu2d.states;

import engine.IGame;
import engine.display.Renderer;
import engine.managers.EntityManager;
import engine.states.IState;
import jeu2d.attacks.AreaManager;
import jeu2d.entities.Character;
import jeu2d.entities.HealthBar;
import jeu2d.entities.controllers.Controller;
import jeu2d.maps.ArenaMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class ArenaState implements IState {

    private IGame game;
    private ArenaMap map;
    private EntityManager entityManager;
    private AreaManager areaManager;
    private Controller player1, player2;
    private HealthBar healthBarLeft, healthBarRight;

    public ArenaState(IGame game) {
        this.game = game;
        entityManager = new EntityManager();
        areaManager = new AreaManager(entityManager);

        map = new ArenaMap("background.png", "ambient.mp3", areaManager);
        map.addWall(new Rectangle2D.Double(32, 60, 96, 10));
        map.addWall(new Rectangle2D.Double(45, 46, 21, 2));
        map.addWall(new Rectangle2D.Double(94, 46, 21, 2));
        map.addWall(new Rectangle2D.Double(70, 33, 20, 1));

        Character character = new Character("knight", map, 50, 20);
        healthBarLeft = new HealthBar(character, HealthBar.LEFT);
        player1 = new Controller(character);
        this.game.inputs().addKeyListener(player1);
        entityManager.add(character);

        Character test = new Character("knight", map, 70, 20);
        healthBarRight = new HealthBar(test, HealthBar.RIGHT);
        player2 = new Controller(test);
        this.game.inputs().addKeyListener(player2);
        entityManager.add(test);

        player2.MOVE_LEFT = KeyEvent.VK_NUMPAD4;
        player2.MOVE_RIGHT = KeyEvent.VK_NUMPAD6;
        player2.ATTACK = KeyEvent.VK_NUMPAD0;
        player2.JUMP = KeyEvent.VK_NUMPAD8;
        player2.DEBUG = KeyEvent.VK_LESS;
    }

    @Override
    public void update() {
        player1.update();
        player2.update();
        areaManager.update();
        entityManager.update();
    }

    @Override
    public void render(Renderer renderer) {
        renderer.background(Color.BLACK);
        map.render(renderer);
        areaManager.render(renderer);
        entityManager.render(renderer);
        healthBarLeft.render(renderer);
        healthBarRight.render(renderer);
    }

    @Override
    public void dispose() {
        entityManager.dispose();
        game.clearInputs();
        areaManager.dispose();
    }

}

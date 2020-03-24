package jeu2d.entities.controllers;

import engine.inputs.KeyController;
import jeu2d.entities.Character;
import jeu2d.utils.Config;

import java.awt.event.KeyEvent;

public class Controller extends KeyController {

    public int MOVE_LEFT = KeyEvent.VK_LEFT;
    public int MOVE_RIGHT = KeyEvent.VK_RIGHT;
    public int ATTACK = KeyEvent.VK_X;
    public int JUMP = KeyEvent.VK_SPACE;
    public int DEBUG = KeyEvent.VK_CONTROL;

    private Character character;

    public Controller(Character character) {
        this.character = character;
    }

    public void update() {
        if (character == null)
            return;
        if (keyPressed(MOVE_RIGHT))
            character.move(0.35, 0);
        if (keyPressed(MOVE_LEFT))
            character.move(-0.35, 0);
        if (keyPressedOnce(ATTACK) && character.attack());

        if (keyPressed(JUMP))
            character.jump();

        if (keyPressedOnce(DEBUG))
            Config.DEBUG = !Config.DEBUG;
    }

}

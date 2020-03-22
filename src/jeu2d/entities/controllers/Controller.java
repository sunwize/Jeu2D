package jeu2d.entities.controllers;

import engine.inputs.KeyController;
import jeu2d.entities.Character;
import jeu2d.utils.Config;

import java.awt.event.KeyEvent;

public class Controller extends KeyController {

    private Character character;

    public Controller(Character character) {
        this.character = character;
    }

    public void update() {
        if (keyPressed(KeyEvent.VK_RIGHT))
            character.move(0.2, 0);
        if (keyPressed(KeyEvent.VK_LEFT))
            character.move(-0.2, 0);
        if (keyPressedOnce(KeyEvent.VK_X) && character.attack());

        if (keyPressed(KeyEvent.VK_SPACE) && character.canJump())
            character.jump();

        if (keyPressedOnce(KeyEvent.VK_CONTROL))
            Config.DEBUG = !Config.DEBUG;
    }

}

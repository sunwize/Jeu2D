package jeu2d.entities;

import engine.display.Renderer;
import engine.utils.Constants;

import java.awt.*;

public class HealthBar {

    public static int LEFT = 0, RIGHT = 1;

    private Character character;
    private int position;

    public HealthBar(Character character, int position) {
        this.character = character;
        this.position = position;
    }

    public void render(Renderer renderer) {
        if (position == LEFT) {
            renderer.drawRect(3, 3, 50, 2.5, Color.RED, true);
            renderer.drawRect(3, 3, 50 * healthRatio(), 2.5, Color.GREEN, true);
        } else if (position == RIGHT) {
            renderer.drawRect(Constants.MAP_GRID_COLUMNS - 54, 3, 50, 2.5, Color.GREEN, true);
            renderer.drawRect(Constants.MAP_GRID_COLUMNS - 54, 3, 50 * (1.0 - healthRatio()), 2.5, Color.RED, true);
        }
    }

    private double healthRatio() {
        if (character != null && character.getHealth() > 0)
            return character.getHealth() * 1.0 / character.getMaxHealth();
        else
            return 0;
    }

}

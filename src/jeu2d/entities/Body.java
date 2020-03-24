package jeu2d.entities;

import com.sun.javafx.geom.Vec2d;
import engine.display.Renderer;
import jeu2d.maps.ArenaMap;
import jeu2d.utils.Config;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Body {

    public Rectangle2D.Double bounds;
    private ArenaMap map;
    public Vec2d velocity, acceleration, maxSpeed;
    private boolean grounded = false;

    public Body(Rectangle2D.Double bounds, ArenaMap map) {
        this.bounds = bounds;
        this.map = map;
        velocity = new Vec2d(0, 0);
        acceleration = new Vec2d(0, 0);
        maxSpeed = new Vec2d(0.6, 0.9);
    }

    public void move(double cx, double cy) {
        acceleration.x += cx;
        acceleration.y += cy;
    }

    public void jump() {
        if (!grounded)
            return;
        acceleration.y = -1.2;
        velocity.y = 0;
    }

    public void update() {
        System.out.println(velocity.x);
        // X acceleration
        double acc = 0;
        if (acceleration.x > 0) {
            acc = Math.min(acceleration.x / 3.0, maxSpeed.x);
        } else if (acceleration.x < 0) {
            acc = Math.max(acceleration.x / 3.0, -maxSpeed.x);
        }

        velocity.x += acc;
        acceleration.x *= 0.3;
        acceleration.x = Math.abs(acceleration.x) < 0.01 ? 0 : acceleration.x;

        // Y acceleration
        acc = 0;
        acceleration.y += map.gravity();
        if (velocity.y < 0 && acceleration.y > 0) {
            acc = 0.01;
        } else {
            acc = acceleration.y / 3.0;
        }
        velocity.y += acc;
        acceleration.y *= 0.4;

        if (grounded)
            velocity.x *= map.friction().x; // X ground friction
        else
            velocity.x *= map.friction().x * 0.85; // X air friction

        // Security reset
        if (Math.abs(velocity.x) < 0.01)
            velocity.x = 0;
        else {
            // Check max speed
            velocity.x = Math.min(velocity.x, maxSpeed.x);
            velocity.x = Math.max(velocity.x, -maxSpeed.x);
        }
        if (Math.abs(velocity.y) < 0.01)
            velocity.y = 0;
        else {
            // Check max speed ONLY when falling
            if (velocity.y > 0)
                velocity.y = Math.min(velocity.y, maxSpeed.y);
        }

        // Check collisions

        // X collision
        if (map.checkWallsCollision(new Rectangle2D.Double(bounds.x + velocity.x, bounds.y, bounds.width, bounds.height))) {
            // No collision
            bounds.x += velocity.x;
        }
        else if (!map.checkWallsCollision(bounds)) { // Stuck in a wall when falling
            bounds.x += velocity.x;
        }
        else {
            // Collision
        }

        // Y collision
        // Ignore collision when jumping
        if (velocity.y < 0 || map.checkWallsCollision(new Rectangle2D.Double(bounds.x, bounds.y + velocity.y, bounds.width, bounds.height))) {
            // No collision
            bounds.y += velocity.y;
            grounded = false;
        }
        else if (!map.checkWallsCollision(bounds)) { // Stuck in a wall when falling
            bounds.y += velocity.y;
        }
        else {
            // Collision
            if (velocity.y > 0) { // Going down
                // Touch ground
                grounded = true;

                // Lower velocity to fit the ground
                velocity.y *= 0.1;
            }

            velocity.y *= map.friction().y; // Y friction
        }

        if (velocity.y > 0 && velocity.y < 0.01)
            velocity.y = 0;
    }

    public void render(Renderer renderer) {
        if (Config.DEBUG)
            renderer.drawRect(bounds.x, bounds.y, bounds.width, bounds.height, Color.GREEN, false);
    }

    public boolean grounded() {
        return grounded;
    }

}

package jeu2d.entities;

import com.sun.javafx.geom.Vec2d;
import engine.animations.Animation;
import engine.animations.Frame;
import engine.display.Renderer;
import engine.entities.IEntity;
import engine.graphics.ImageLoader;
import engine.utils.Constants;
import engine.utils.Utils;
import jeu2d.attacks.SwordAttack;
import jeu2d.maps.ArenaMap;
import jeu2d.utils.Config;
import org.json.JSONObject;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class Character implements IEntity {

    public static int LEFT = 0, RIGHT = 1;

    private ArenaMap map;
    private HashMap<String, Animation> animations;
    private String selectedAnimation = "idle";
    private int direction = RIGHT;
    private Vec2d position, velocity, boundsOffset, maxSpeed;
    private Rectangle2D.Double bounds;
    private boolean jumping = false, falling = true;
    private double jumpForce;

    public Character(String path, ArenaMap map, double cx, double cy) {
        this.map = map;
        position = new Vec2d(cx, cy);
        velocity = new Vec2d(0, 0);
        maxSpeed = new Vec2d(0.5, 0.6);
        boundsOffset = new Vec2d(5.5, 2);
        bounds = new Rectangle2D.Double(position.x + boundsOffset.x, position.y + boundsOffset.y, 4, 9);
        loadAnimations(path);
    }

    private void loadAnimations(String path) {
        animations = new HashMap<>();
        File dir = new File(Constants.IMAGES_PATH + "/" + path);

        for (String filename : dir.list()) { // Load character folder
            File dir2 = new File(Constants.IMAGES_PATH + "/" + path + "/" + filename);
            LinkedList<Frame> frames = new LinkedList<>();
            int loops = 0;
            int animationSpeed = 0;

            for (String animFileName : dir2.list()) {
                if (animFileName.contains(".json")) {
                    String configFile = Constants.IMAGES_PATH + "/" + path + "/" + filename + "/" + animFileName;
                    JSONObject config = new JSONObject(Utils.loadJSON(configFile));
                    loops = config.getInt("loops");
                    animationSpeed = config.getInt("animation_speed_ms");
                }
            }

            for (String animFileName : dir2.list()) { // Load animations folders
                if (animFileName.contains(".png")) { // It's a frame
                    String name = path + "/" + filename + "/" + animFileName;
                    frames.add(new Frame(ImageLoader.loadImage(name), animationSpeed));
                }
            }

            animations.put(dir2.getName(), new Animation(frames, loops));
        }
    }

    public void move(double cx, double cy) {
        if (selectedAnimation != "attack" && selectedAnimation != "air_attack") { // Can't turn when attacking
            if (cx > 0)
                direction = RIGHT;
            else if (cx < 0)
                direction = LEFT;
        }

        this.velocity.x += cx;
        this.velocity.y += cy;
    }

    protected void move() {
        falling = true;
        if (jumping) {
            if (jumpForce <= 0) { // Top of the jump
                jumping = false;
                falling = true;
            }
            else {
                falling = false;
                velocity.y -= jumpForce;
                jumpForce -= 0.01;
            }
        }

        // Moving is harder in the air
        if (falling)
            velocity.x *= 0.85;

        if (velocity.x == 0 && velocity.y == 0)
            return;

        // Max speed
        velocity.x = Math.min(velocity.x, maxSpeed.x);
        velocity.x = Math.max(velocity.x, -maxSpeed.x);
        velocity.y = Math.min(velocity.y, maxSpeed.y);
        velocity.y = Math.max(velocity.y, -maxSpeed.y);

        Rectangle2D.Double bounds = getBounds();

        // X collision
        if (map.checkWallsCollision(new Rectangle2D.Double(bounds.x + velocity.x, bounds.y, bounds.width, bounds.height)))
            this.position.x += this.velocity.x;
        else
            velocity.x = 0;

        // Y collision
        if (velocity.y <= 0 || map.checkWallsCollision(new Rectangle2D.Double(bounds.x, bounds.y + velocity.y, bounds.width, bounds.height)))
            this.position.y += this.velocity.y;
        else if (!map.checkWallsCollision(bounds)) {
            this.position.y += this.velocity.y;
            jumping = true;
        }
        else {
            falling = false;
            velocity.y = 0;
            jumpForce = 0;
        }

        // Friction
        if (Math.abs(velocity.x) > 0.001)
            velocity.x *= map.friction().x;
        else
            velocity.x = 0;

        if (Math.abs(velocity.y) > 0.001)
            velocity.y *= map.friction().y;
        else
            velocity.y = 0;
    }

    public void animate() {
        if (velocity.x == 0 && velocity.y == 0)
            selectAnimation("idle");
        else if (Math.abs(velocity.x) > 0.1)
            selectAnimation("run");
        if (velocity.y > 0)
            selectAnimation("fall");
        else if (velocity.y < 0)
            selectAnimation("jump");

        animations.get(selectedAnimation).update();
    }

    public void selectAnimation(String animationName) {
        if (selectedAnimation == animationName)
            return;
        if (selectedAnimation == "attack" && animations.get(selectedAnimation).isActive())
            return;
        if (selectedAnimation == "air_attack" && animations.get(selectedAnimation).isActive())
            return;

        selectedAnimation = animationName;
    }

    public void jump() {
        falling = false;
        jumping = true;
        jumpForce = 0.55;
        animations.get("jump").reset();
    }

    public boolean attack() {
        if (attacking())
            return false;
        if (grounded()) {
            animations.get("attack").reset();
            selectAnimation("attack");
        } else {
            animations.get("air_attack").reset();
            selectAnimation("air_attack");
        }

        Rectangle2D.Double bounds = getBounds();
        Rectangle2D.Double attackBounds;
        if (direction == RIGHT) {
            attackBounds = new Rectangle2D.Double(bounds.width, 0, 5, bounds.height);
        } else {
            attackBounds = new Rectangle2D.Double(-5, 0, 5, bounds.height);
        }
        map.getAreaManager().add(new SwordAttack(attackBounds, this, 200, 400));

        return true;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void update() {
        animate();

        if (falling)
            move(0, 0.7); // Gravity
        move();
    }

    @Override
    public void render(Renderer renderer) {
        BufferedImage frame = animations.get(selectedAnimation).getCurrentFrame();
        if (direction == 1)
            renderer.drawImage(frame, 15, position.x, position.y, false, false);
        else if (direction == 0)
            renderer.drawImage(frame, 15, position.x, position.y, true);

        if (Config.DEBUG) {
            Rectangle2D bounds = getBounds();
            renderer.drawRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), Color.GREEN, false);
        }
    }

    @Override
    public double getCX() {
        return 0;
    }

    @Override
    public double getCY() {
        return 0;
    }

    @Override
    public Rectangle2D.Double getBounds() {
        bounds.x = position.x + boundsOffset.x;
        bounds.y = position.y + boundsOffset.y;
        return bounds;
    }

    public boolean canJump() {
        return !falling && !jumping;
    }

    public boolean grounded() {
        return !map.checkWallsCollision(new Rectangle2D.Double(bounds.x, bounds.y + 1, bounds.width, bounds.height));
    }

    public boolean attacking() {
        return selectedAnimation == "attack" || selectedAnimation == "air_attack";
    }

}

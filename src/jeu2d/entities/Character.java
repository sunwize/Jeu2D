package jeu2d.entities;

import com.sun.javafx.geom.Vec2d;
import engine.animations.Animation;
import engine.animations.Frame;
import engine.display.Renderer;
import engine.entities.IEntity;
import engine.graphics.ImageLoader;
import engine.sounds.SoundPlayer;
import engine.utils.Constants;
import engine.utils.Utils;
import jeu2d.attacks.SwordAttack;
import jeu2d.maps.ArenaMap;
import org.json.JSONObject;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Character implements IEntity {

    public static int LEFT = 0, RIGHT = 1;

    private ArenaMap map;
    private Body body;
    private HashMap<String, Animation> animations;
    private String selectedAnimation = "idle";
    private int direction = RIGHT, health, maxHealth = 100;
    private Vec2d boundsOffset;

    // Animations purposes
    private LinkedList<String> unCancelable;
    private LinkedList<String> prioritized;

    public Character(String path, ArenaMap map, double cx, double cy) {
        this.map = map;
        boundsOffset = new Vec2d(5.5, 2);
        body = new Body(new Rectangle2D.Double(cx, cy, 4, 9), map);
        loadAnimations(path);
        health = maxHealth;
    }

    private void loadAnimations(String path) {
        animations = new HashMap<>();
        unCancelable = new LinkedList<>();
        prioritized = new LinkedList<>();
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

                    if (!config.getBoolean("cancelable"))
                        unCancelable.add(dir2.getName());
                    if (config.getBoolean("prioritized"))
                        prioritized.add(dir2.getName());
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
        if (selectedAnimation != "attack" && selectedAnimation != "air_attack" && selectedAnimation != "hurt") { // Can't turn when attacking or hurt
            if (cx > 0)
                direction = RIGHT;
            else if (cx < 0)
                direction = LEFT;
        }

        this.body.move(cx, cy);
    }

    public void animate() {
        if (Math.abs(body.velocity.x) < 0.1 && Math.abs(body.velocity.y) < 0.1)
            selectAnimation("idle");
        else if (Math.abs(body.velocity.x) > 0.1)
            selectAnimation("run");
        if (body.velocity.y > 0)
            selectAnimation("fall");
        else if (body.velocity.y < 0)
            selectAnimation("jump");

        animations.get(selectedAnimation).update();
    }

    public void selectAnimation(String animationName) {
        if (selectedAnimation == animationName)
            return;
        if (prioritized.contains(animationName)) {
            selectedAnimation = animationName;
            return;
        }
        if (unCancelable.contains(selectedAnimation) && animations.get(selectedAnimation).isActive())
            return;

        selectedAnimation = animationName;
    }

    public void jump() {
        body.jump();
        if (grounded()) {
            animations.get("jump").reset();
            SoundPlayer.playSound("jump.mp3", 0.5, 1);
        }
    }

    public boolean attack() {
        if (attacking() || hurt())
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
        int num = new Random().nextInt(3) + 1;
        SoundPlayer.playSound("sword_attack" + num + ".mp3", 0.8, 1);
        map.getAreaManager().add(new SwordAttack(attackBounds, this, 200, 400));

        return true;
    }

    public void hurt(int damage) {
        if (hurt() || dying())
            return;
        health = Math.max(0, health - damage);

        if (health > 0) {
            animations.get("hurt").reset();
            selectAnimation("hurt");
        } else {
            animations.get("die").reset();
            selectAnimation("die");
        }
    }

    @Override
    public boolean isActive() {
        return dying() || health > 0;
    }

    @Override
    public void update() {
        body.update();

        if (attacking() && grounded())
            body.velocity.x *= 0.5;

        animate();

        if (body.bounds.y > 100)
            health = 0;

        if (dying())
            System.out.println(selectedAnimation);
    }

    @Override
    public void render(Renderer renderer) {
        BufferedImage frame = animations.get(selectedAnimation).getCurrentFrame();
        if (direction == 1)
            renderer.drawImage(frame, 15, body.bounds.x - boundsOffset.x, body.bounds.y - boundsOffset.y, false, false);
        else if (direction == 0)
            renderer.drawImage(frame, 15, body.bounds.x - boundsOffset.x, body.bounds.y - boundsOffset.y, true);

        body.render(renderer);
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
        return body.bounds;
    }

    public int getDirection() {
        return direction;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public boolean grounded() {
        return body.grounded();
    }

    public boolean attacking() {
        return selectedAnimation == "attack" || selectedAnimation == "air_attack";
    }

    public boolean hurt() {
        return selectedAnimation == "hurt";
    }

    public boolean dying() {
        return selectedAnimation == "die";
    }

}

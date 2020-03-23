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
    private Body body;
    private HashMap<String, Animation> animations;
    private String selectedAnimation = "idle";
    private int direction = RIGHT;
    private Vec2d boundsOffset;

    public Character(String path, ArenaMap map, double cx, double cy) {
        this.map = map;
        boundsOffset = new Vec2d(5.5, 2);
        body = new Body(new Rectangle2D.Double(cx, cy, 4, 9), map);
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
        if (selectedAnimation != "attack" && selectedAnimation != "air_attack" && selectedAnimation != "hurt") { // Can't turn when attacking or hurt
            if (cx > 0)
                direction = RIGHT;
            else if (cx < 0)
                direction = LEFT;
        }

        this.body.move(cx, cy);
    }

    public void animate() {
        if (body.velocity.x == 0 && body.velocity.y == 0)
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
        if (animationName == "hurt") {
            selectedAnimation = animationName;
            return;
        }
        if (selectedAnimation == "attack" && animations.get(selectedAnimation).isActive())
            return;
        if (selectedAnimation == "air_attack" && animations.get(selectedAnimation).isActive())
            return;
        if (selectedAnimation == "hurt" && animations.get(selectedAnimation).isActive())
            return;

        selectedAnimation = animationName;
    }

    public void jump() {
        body.jump();
        if (grounded())
            animations.get("jump").reset();
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
        map.getAreaManager().add(new SwordAttack(attackBounds, this, 200, 400));

        return true;
    }

    public void hurt(int damage) {
        if (hurt())
            return;
        animations.get("hurt").reset();
        selectAnimation("hurt");
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void update() {
        body.update();
        animate();
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

    public boolean grounded() {
        return body.grounded();
    }

    public boolean attacking() {
        return selectedAnimation == "attack" || selectedAnimation == "air_attack";
    }

    public boolean hurt() {
        return selectedAnimation == "hurt";
    }

}

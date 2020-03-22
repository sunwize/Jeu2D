package example.entities;

import engine.display.Renderer;
import engine.entities.IEntity;
import engine.pathfinding.Mover;
import engine.utils.Constants;
import example.entities.animations.AnimationManager;
import example.entities.pathfinding.Direction;
import example.entities.pathfinding.IsoPathfinder;
import example.map.IsoHelper;
import example.map.IsoMap;
import example.map.IsoTile;

import java.awt.*;
import java.awt.geom.Point2D;

public class Pokemon implements IEntity, Mover {

    private AnimationManager animationManager;
    private IsoMap map;
    private IsoPathfinder pathfinder;
    private double cx, cy, speed;
    private String name;
    private int direction;

    public Pokemon(String name, IsoMap map, double cx, double cy) {
        speed = 0.3;
        this.map = map;
        this.cx = cx;
        this.cy = cy;
        this.name = name;
        animationManager = new AnimationManager(this);
        this.direction = Direction.DOWN_LEFT;
        pathfinder = new IsoPathfinder(map, this);
    }

    public void move(double cx, double cy) {
        animationManager.select("move");
        this.cx += cx;
        this.cy += cy;

        if(cx > 0)
            direction = Direction.DOWN_RIGHT;
        else if(cx < 0)
            direction = Direction.UP_LEFT;
        else if(cy > 0)
            direction = Direction.DOWN_LEFT;
        else if(cy < 0)
            direction = Direction.UP_RIGHT;
    }

    public IsoPathfinder getPathfinder() {
        return pathfinder;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void update() {
        animationManager.update();
        pathfinder.update();
    }

    @Override
    public void render(Renderer renderer) {
        pathfinder.render(renderer);
        animationManager.render(renderer);
        //Rectangle.Double bounds = getBounds();
        //renderer.drawRect(bounds.x, bounds.y, bounds.width, bounds.height, Color.RED, true);
    }

    @Override
    public void dispose() {

    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public int getSize() {
        return 10;
    }

    @Override
    public double getCX() {
        return cx;
    }

    @Override
    public double getCY() {
        return cy;
    }

    public Point.Double isoCoordinates() {
        double x = cx - map.getxOffset();
        double y = cy - map.getyOffset();
        Point.Double p = IsoHelper.cartToIso(x, y);
        p.x += map.getxOffset();
        p.y += map.getyOffset();
        return p;
    }

    public Point.Double cartCoordinates() {
        return new Point2D.Double(cx, cy);
    }

    public double getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public Rectangle.Double getBounds() {
        Point.Double p = isoCoordinates();
        return new Rectangle.Double(p.x - getSize()/4, p.y - getSize()/2, getSize()/2, getSize()/2);
    }

    public void setPosition(double cx, double cy) {
        this.cx = cx;
        this.cy = cy;
    }

    public boolean placeOnTile(int x, int y) {
        IsoTile tile = map.getTiles()[y][x];

        if(tile.taken != null)
            return false;

        Point.Double p = tile.cartCoordinates();
        setPosition(p.x, p.y);
        map.getTiles()[y][x].taken = this;
        return true;
    }

    public Point getTileCoordinates() {
        double offset = Constants.TILE_SIZE + map.getTileOffset();
        double x = cx - map.getxOffset();
        double y = cy - map.getyOffset();
        x /= offset;
        y /= offset;
        return new Point((int) x, (int) y);
    }

    public IsoTile getTile() {
        Point p = getTileCoordinates();
        return map.getTiles()[p.y][p.x];
    }

    public IsoMap getMap() {
        return map;
    }
}

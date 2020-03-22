package example.entities.pathfinding;

import engine.display.Renderer;
import engine.pathfinding.AStarPathFinder;
import engine.pathfinding.Path;
import example.entities.Pokemon;
import example.map.IsoMap;
import example.map.IsoTile;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.Random;

public class IsoPathfinder {

    private AStarPathFinder pathFinder;
    private Pokemon pokemon;
    private IsoMap map;
    private LinkedList<Path.Step> path;
    private IsoTile takenTile;

    public IsoPathfinder(IsoMap map, Pokemon pokemon) {
        this.map = map;
        this.pokemon = pokemon;
        path = new LinkedList<>();
        pathFinder = new AStarPathFinder(map, 200, false);
    }

    public void findPath(int x, int y, Runnable onSuccess, Runnable onFailure) {
        new Thread(() -> {
            Point p = pokemon.getTileCoordinates();
            Path steps = pathFinder.findPath(pokemon, p.x, p.y, x, y);
            if(steps != null) { // Success
                if(takenTile != null)
                    takenTile.taken = null;
                path = new LinkedList<>(steps.getSteps());

                if(onSuccess != null)
                    onSuccess.run();
            }
            else { // Failure
                pokemon.getAnimationManager().select("idle");
                path.clear();
                Point tc = pokemon.getTileCoordinates();
                pokemon.placeOnTile(tc.x, tc.y);

                if(onFailure != null)
                    onFailure.run();
            }
        }).start();
    }

    public void findPath(int x, int y) {
        findPath(x, y, null, null);
    }

    public void render(Renderer renderer) {
        if(path != null) {
            for(int i = 0; i < path.size() - 1; i++) {
                Path.Step step1 = path.get(i);
                Path.Step step2 = path.get(i+1);
                Point.Double p1 = map.getTiles()[step1.getY()][step1.getX()].isoCoordinates();
                Point.Double p2 = map.getTiles()[step2.getY()][step2.getX()].isoCoordinates();
                renderer.drawLine(new Line2D.Double(p1.x, p1.y, p2.x, p2.y), Color.BLUE);
            }
        }
    }

    public void update() {
        if(path.size() > 0) {
            Point.Double pos = pokemon.cartCoordinates();
            Path.Step step = path.peek();

            Point pokeTile = pokemon.getTileCoordinates();
            takenTile = pokemon.getMap().getTiles()[pokeTile.y][pokeTile.x];

            IsoTile tileToTake = map.getTiles()[step.getY()][step.getX()];
            Point.Double togo = tileToTake.cartCoordinates();

            if(tileToTake.taken == null || tileToTake.taken == pokemon) {
                takenTile.taken = null;
                takenTile = tileToTake;
                takenTile.taken = pokemon;
            }
            else if(tileToTake.taken != pokemon) {
                Path.Step last = path.getLast();
                findPath(last.getX(), last.getY(), () -> path.pop(), null);
                return;
            }

            double speed = pokemon.getSpeed();
            double radius = speed;

            // pos.x > togo.x - radius && pos.x < togo.x + radius && pos.y > togo.y - radius && pos.y < togo.y + radius
            if(pos.x > togo.x - radius && pos.x < togo.x + radius && pos.y > togo.y - radius && pos.y < togo.y + radius) {
                path.pop();
                pokemon.setPosition(togo.x, togo.y);
                //pokemon.placeOnTile(step.getX(), step.getY());

                if(path.size() == 0)
                    pokemon.getAnimationManager().select("idle");

                return;
            }

            double xMove = 0, yMove = 0;

            if(pos.x < togo.x)
                xMove = speed;
            else if(pos.x > togo.x)
                xMove = -speed;
            else if(pos.y < togo.y)
                yMove = speed;
            else if(pos.y > togo.y)
                yMove = -speed;

            pokemon.move(xMove, yMove);
        }
    }

}

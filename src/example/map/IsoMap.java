package example.map;

import engine.display.Renderer;
import engine.pathfinding.Mover;
import engine.pathfinding.TileBasedMap;
import engine.utils.Constants;
import example.Game;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class IsoMap implements TileBasedMap {

    private IsoTile[][] tiles;
    private int xOffset, yOffset;
    private int tileOffset; // Distance between 2 tiles

    public IsoMap(Game game, int width, int height, int xOffset, int yOffset, int tileOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.tileOffset = tileOffset;
        tiles = new IsoTile[height][width];
        int offset = tileOffset + Constants.TILE_SIZE;

        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[0].length; j++) {
                tiles[i][j] = new IsoTile(this, j*offset,i*offset, Constants.TILE_SIZE, Constants.TILE_SIZE);
            }
        }
    }

    public void render(Renderer renderer) {
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[0].length; j++) {
                tiles[i][j].render(renderer);
            }
        }
    }

    public IsoTile[][] getTiles() {
        return tiles;
    }

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public int getTileOffset() {
        return tileOffset;
    }

    @Override
    public int getWidthInTiles() {
        return tiles[0].length;
    }

    @Override
    public int getHeightInTiles() {
        return tiles.length;
    }

    @Override
    public void pathFinderVisited(int x, int y) {

    }

    @Override
    public boolean blocked(Mover mover, int x, int y) {
        return tiles[y][x].taken != null && tiles[y][x].taken != mover;
    }

    @Override
    public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
        return 1;
    }

}

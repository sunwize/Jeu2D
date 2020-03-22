package engine.pathfinding;

public class ManhattanHeuristic implements AStarHeuristic
{

    @Override
    public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty)
    {
        float dx = Math.abs(tx - x);
        float dy = Math.abs(ty - y);
        return dx+dy;
    }
}

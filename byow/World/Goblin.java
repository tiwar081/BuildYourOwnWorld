package byow.World;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.RoomVectorsStuff.*;

import java.util.ArrayList;
import java.util.Random;

import static byow.RoomVectorsStuff.usefulFuncs.isValidPos;

public class Goblin {
    Vector currPos;
    Random rand;
    GameWorld gWorld;
    Graph graph;

    ArrayList<Integer> pathToPlayer;
    public Goblin(Random r, GameWorld g) {
        rand = r;
        gWorld = g;
        graph = g.getFloorGraphAStar();
        //TODO: set currPos to start
    }

    public Vector makeNextMove() {
        //Goblin should make a random movement every second move when player isn't in line of sight. Else, along path.
        if (playerInSight()) {
            if (rand.nextBoolean()) {
                //TODO: Make a random move

            }
        } else {
            currPos = gWorld.intToVector(ShortestPath.getNextMove(pathToPlayer));
        }
        return currPos;
    }

    public boolean isValidMove(Vector pos) {
        return isValidPos(pos, gWorld.getFullWorld()) && !gWorld.getTile(pos).isBlocksPlayer();
    }

    private boolean playerInSight() {
        return gWorld.monsterLineOfSight(currPos);
    }

    private Vector playerPos() {
        return gWorld.getPlayerPosition();
    }

    private void updatePath() {
        pathToPlayer = (new ShortestPath(gWorld.getVertex(currPos), gWorld.getVertex(playerPos()), graph)).getPath();
    }
}

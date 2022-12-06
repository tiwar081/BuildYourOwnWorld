package byow.World;

import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.RoomVectorsStuff.*;

import java.util.ArrayList;
import java.util.Random;

import static byow.RoomVectorsStuff.usefulFuncs.isValidPos;

public class Goblin {
    private double confusionProbability = 1;
    private static Vector[] moves = {new Vector(0, 1), new Vector(-1, 0), new Vector(1, 0), new Vector(0, -1)};
    private Vector currPos;
    private Random rand;
    private GameWorld gWorld;
    private Graph graph;

    ArrayList<Integer> pathToPlayer;
    public Goblin(Random r, GameWorld g) {
        rand = r;
        gWorld = g;
        graph = g.getFloorGraphAStar();

        Room goblinRoom = gWorld.getRoomIterator().next();
        currPos = goblinRoom.genHallwayTarget(rand);

        updatePath();
    }

    public Vector makeNextMove() {
        //Goblin should make a random movement every second move when player isn't in line of sight. Else, along path.
        updatePath();
        if (!playerInSight()) {
            double probability = RandomUtils.uniform(rand, 0, 1);
            if (probability > (1 - confusionProbability)) {
                Vector temp = currPos.add(moves[rand.nextInt(4)]);
                if (isValidMove(temp)) {
                    currPos = temp;
                }
                return currPos;
            }
        }
        int newPos = ShortestPath.getNextMove(pathToPlayer);
        if (newPos == -1) {
            return currPos; //gWorld.getPlayerPosition().subtract(currPos).normalize().add(currPos);
        }
        currPos = gWorld.intToVector(newPos);
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

    public Vector getCurrPos() {
        return currPos;
    }

    public ArrayList<Integer> getPathToPlayer() {
        return pathToPlayer;
    }
}

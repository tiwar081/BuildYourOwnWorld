package byow.RoomVectorsStuff;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Graph;

import java.util.Collection;
import static byow.RoomVectorsStuff.usefulFuncs.*;

public class GameWorld {
    private Vector playerPosition;
    private Collection<Room> allRooms;
    private TETile[][] world;
    private TETile[][] interactingWorld;
    private Graph floorGraph;

    public GameWorld(TETile[][] world, Collection<Room> allRooms) {
        this.allRooms = allRooms;
        this.world = world;
        interactingWorld = world.clone();
    }

    public void movePlayerIn(char inputDir) {
        Vector direction = convertToVector(inputDir);
        Vector newPlayerPosition = playerPosition.add(direction);
        if (isValidMove(newPlayerPosition)) {
            setTile(playerPosition, getTile(playerPosition), interactingWorld);
            setTile(newPlayerPosition, Tileset.AVATAR, interactingWorld);
            playerPosition = newPlayerPosition;
        }
    }
    private boolean isValidMove(Vector pos) {
        return !playerPosition.equals(pos) && isValidPos(pos, world) && getTile(pos)!=Tileset.WALL;
    }
    public static void setTile(Vector pos, TETile tile, TETile[][] world) {
        world[(int) pos.getX()][(int) pos.getY()] = tile;
    }
    public TETile getTile(Vector pos) {
        return world[(int) pos.getX()][(int) pos.getY()];
    }
    private Vector convertToVector(char inputDir) {
        switch (inputDir) {
            case 'W':
            case 'w':
                return new Vector(0,1);
            case 'a':
            case 'A':
                return new Vector(1, 0);
            case 's':
            case 'S':
                return new Vector(0, -1);
            case 'd':
            case 'D':
                return new Vector(-1, 0);
        }
        return new Vector(0, 0);
    }
    public void generateGraph() {
        //TODO: Generate a graph using the world instance variable
        // idea just make each floor a vertex and connect it if touching
    }
}

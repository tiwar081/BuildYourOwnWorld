package byow.RoomVectorsStuff;

import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Graph;

import java.util.Collection;
import java.util.Random;

import static byow.RoomVectorsStuff.usefulFuncs.*;

public class GameWorld {
    private Vector playerPosition;
    private Collection<Room> allRooms;
    private TETile[][] world;
    private TETile[][] interactingWorld;
    private int[][] graphMap;
    private Graph floorGraph;

    public GameWorld(Random rand, TETile[][] world, Collection<Room> allRooms) {
        this.allRooms = allRooms;
        this.world = world;
        interactingWorld = TETile.copyOf(world);
        graphMap = new int[world.length][world[0].length];
        generatePlayerStartPosition(rand);
    }
    public TETile[][] getWorld() {
        return interactingWorld;
    }
    public void generatePlayerStartPosition(Random rand) {
        Object[] roomList = allRooms.toArray();
        RandomUtils.shuffle(rand, roomList);
        Room playerRoom = (Room) roomList[0];
        playerPosition = playerRoom.genHallwayTarget(rand);
        setTile(playerPosition, Tileset.AVATAR, interactingWorld);
    }
    public void movePlayerIn(char inputDir) {
        if (Engine.verbose) {
            System.out.println("DEBUG: Trying to move to " + inputDir);
        }
        System.out.println("");
        Vector direction = convertToVector(inputDir);
        Vector newPlayerPosition = playerPosition.add(direction);
        if (isValidMove(newPlayerPosition)) {
            setInteractingWorldTile(playerPosition, getTile(playerPosition));
            setInteractingWorldTile(newPlayerPosition, Tileset.AVATAR);
            playerPosition = newPlayerPosition;
            if (Engine.verbose) {
                System.out.println("DEBUG: Successfully moved " + inputDir);
            }
        }

    }
    private boolean isValidMove(Vector pos) {
        return !playerPosition.equals(pos) && isValidPos(pos, world) && getTile(pos)!=Tileset.WALL;
    }
    public void setInteractingWorldTile(Vector pos, TETile tile) {
        interactingWorld[(int) pos.getX()][(int) pos.getY()] = tile;
    }
    public static void setTile(Vector pos, TETile tile, TETile[][] world) {
        world[(int) pos.getX()][(int) pos.getY()] = tile;
    }
    public TETile getTile(Vector pos) {
        return world[(int) pos.getX()][(int) pos.getY()];
    }
    public TETile getTile(int x, int y) {
        return world[x][y];
    }
    private Vector convertToVector(char inputDir) {
        switch (inputDir) {
            case 'W':
            case 'w':
                return new Vector(0,1);
            case 'd':
            case 'D':
                return new Vector(1, 0);
            case 's':
            case 'S':
                return new Vector(0, -1);
            case 'a':
            case 'A':
                return new Vector(-1, 0);
        }
        return new Vector(0, 0);
    }
    public void generateGraph() {
        /** Generate a graph using the world instance variable
         *  make each floor a vertex and connect it if touching
         */
        floorGraph = new Graph(countFlooring());
        for (int i = 0; i < world.length - 1; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if(getTile(i, j) == Tileset.FLOOR && getTile(i + 1, j) == Tileset.FLOOR) {
                    floorGraph.addEdge(graphMap[i][j], graphMap[i + 1][j]);
                }
            }
        }
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length - 1; j++) {
                if(getTile(i, j) == Tileset.FLOOR && getTile(i, j + 1) == Tileset.FLOOR) {
                    floorGraph.addEdge(graphMap[i][j], graphMap[i][j + 1]);
                }
            }
        }
    }
    private int countFlooring() {
        int count = 0;
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if(getTile(i, j) == Tileset.FLOOR) {
                    graphMap[i][j] = count;
                    count++;
                }
            }
        }
        return count;
    }
}

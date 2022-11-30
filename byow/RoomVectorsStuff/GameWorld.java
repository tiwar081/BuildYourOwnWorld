package byow.RoomVectorsStuff;

import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Graph;

import java.util.*;

import static byow.RoomVectorsStuff.usefulFuncs.*;

public class GameWorld {
    private Vector playerPosition;
    private Collection<Room> allRooms;
    private TETile[][] blankWorld;
    private TETile[][] world;
    private TETile[][] interactingWorld;
    private int[][] graphMap;
    private Graph floorGraphAStar;
    private boolean viewEntireWorld = false;
    private int visionDepth = 8;
    private HashSet<Vector> vectorsToAllTiles = new HashSet<>();

    public GameWorld(Random rand, TETile[][] world, Collection<Room> allRooms) {
        this.allRooms = allRooms;
        blankWorld = emptyWorld();
        this.world = world;
        interactingWorld = TETile.copyOf(world);
        graphMap = new int[world.length][world[0].length];
        generatePlayerStartPosition(rand);
        generateGraph();
        setVectorsToAllTiles();
    }
    public TETile[][] getWorld() {

        if (viewEntireWorld) {
            return interactingWorld;
        }
        TETile[][] smallerWorld = TETile.copyOf(blankWorld);

        HashMap<Vector, Double> visibleTiles = findVisibleTiles();
        Vector pos;
        for (Vector relPos:visibleTiles.keySet()) {
            pos = relativeToAbsoluteVector(relPos);
            TETile dimmedTile = TETile.intensityVariant(getInteractiveTile(pos), 1-visibleTiles.get(relPos)/visionDepth);
            setTile(pos, dimmedTile, smallerWorld);
        }
        return smallerWorld;
    }
    private void toggleWorldView() {
        viewEntireWorld = !viewEntireWorld;
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
    public TETile getInteractiveTile(Vector pos) {
        return interactingWorld[(int) pos.getX()][(int) pos.getY()];
    }
    public TETile getTile(int x, int y) {
        return world[x][y];
    }
    public TETile getInteractiveTile(int x, int y) {
        return interactingWorld[x][y];
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
            case 'T':
                toggleWorldView();
        }
        return new Vector(0, 0);
    }
    public void generateGraph() {
        /** Generate a graph using the world instance variable
         *  make each floor a vertex and connect it if touching
         *  NOT COMPLETE - MIGHT HAVE SOME BUG
         */
        //TODO: Fix potential bugs??
        floorGraphAStar = new Graph(countTiles());
        for (int i = 0; i < world.length - 1; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if(shouldConnectTiles(i, j, i + 1, j, false)) {
                    floorGraphAStar.addEdge(graphMap[i][j], graphMap[i + 1][j]);
                }
            }
        }
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length - 1; j++) {
                if(shouldConnectTiles(i, j, i, j + 1, false)) {
                    floorGraphAStar.addEdge(graphMap[i][j], graphMap[i][j + 1]);
                }
            }
        }
    }
    private int countTiles() {
        int graphCount = 0;
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                graphMap[i][j] = graphCount;
                graphCount++;
            }
        }
        return graphCount;
    }
    private boolean shouldConnectTiles(int x1, int y1, int x2, int y2, boolean includeWalls) {
        if (includeWalls) {
            return getTile(x1, y1) == Tileset.FLOOR || getTile(x2, y2) == Tileset.FLOOR;
        }
        return getTile(x1, y1) == Tileset.FLOOR && getTile(x2, y2) == Tileset.FLOOR;
    }
    private HashMap<Vector, Double> findVisibleTiles() {
        HashMap<Vector, Double> output = new HashMap<>();
        PriorityQueue<Vector> pq = getRelativeVectorsToAllTiles();
        Vector currVector = pq.remove();
        while (currVector.getMagnitude() <= visionDepth) {
            output.put(currVector, currVector.getMagnitude());
            currVector = pq.remove();
        }
        return output;
    }
    private void setVectorsToAllTiles() {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                vectorsToAllTiles.add(new Vector(x, y));
            }
        }
    }
    private PriorityQueue<Vector> getRelativeVectorsToAllTiles() {
        PriorityQueue<Vector> relVectors = new PriorityQueue<>(new VectorMagComparator());
        for (Vector v:vectorsToAllTiles) {
            relVectors.add(v.subtract(playerPosition));
        }
        return relVectors;
    }
    private Vector relativeToAbsoluteVector(Vector relVector) {
        return relVector.add(playerPosition);
    }
    public String getTileName(double[] pos) {
        return getInteractiveTile((int) pos[0], (int) pos[1]).description();
    }
}

package byow.World;

import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.RoomVectorsStuff.Room;
import byow.RoomVectorsStuff.ShortestPath;
import byow.RoomVectorsStuff.Vector;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Graph;

import java.util.*;

import static byow.RoomVectorsStuff.usefulFuncs.*;

public class GameWorld {
    private Vector playerPosition;
    private Vector keyPosition;
    private String playerInput;
    private Collection<Room> allRooms;
    private Iterator<Room> roomIterator;
    private Room finalRoom;
    private TETile[][] blankWorld;
    private TETile[][] world;
    private TETile[][] interactingWorld;
    private int[][] graphMap;
    private Graph floorGraphAStar;
    private HashMap<Integer, Vector> vertexToPos = new HashMap<>();
    private boolean viewEntireWorld = false;
    private int visionDepth = 5;
    private HashSet<Vector> playerVisibleVectors = new HashSet<>();
    private HashSet<Vector> vectorsToAllTiles = new HashSet<>();


    public GameWorld(Random rand, TETile[][] world, Collection<Room> allRooms, String input) {
        this.allRooms = allRooms;
        this.roomIterator = (Iterator<Room>) allRooms.iterator();
        this.finalRoom = roomIterator.next();
        this.keyPosition = roomIterator.next().genHallwayTarget(rand);

        blankWorld = emptyWorld();
        this.world = world;
        addFinalRoom();
        setTile(keyPosition, Tileset.KEY_FLOOR, world);
        interactingWorld = TETile.copyOf(world);
        graphMap = new int[world.length][world[0].length];

        playerInput = input;
        generatePlayerStartPosition(rand);
        generateGraph();
        generateVisibleVectors();
    }
    public String getPlayerInput() {
        return playerInput;
    }
    private void addFinalRoom() {
        addRoomDoors(finalRoom, world);
    }
    private void unlockFinalRoom() {
        addRoomUnlockedDoors(finalRoom, world);
    }
    public TETile[][] getWorld() {

        if (viewEntireWorld) {
            TETile[][] astarworld = TETile.copyOf(interactingWorld);
            //TODO: MODIFY astarworld
            ShortestPath sh = new ShortestPath(getVertex(keyPosition), getVertex(playerPosition), floorGraphAStar);
            for (Vector pos:sh.getPath()) {
                setTile(pos, Tileset.FLOWER, astarworld);
            }
            return astarworld;
        }
        TETile[][] smallerWorld = TETile.copyOf(blankWorld);

        HashMap<Vector, Double> visibleTiles = findVisibleTiles();
        for (Vector pos:visibleTiles.keySet()) {
            TETile dimmedTile = TETile.intensityVariant(getInteractiveTile(pos), 1-visibleTiles.get(pos)/visionDepth);
            setTile(pos, dimmedTile, smallerWorld);
        }
        return smallerWorld;
    }
    private void toggleWorldView() {
        viewEntireWorld = !viewEntireWorld;
    }
    public void generatePlayerStartPosition(Random rand) {
        Room playerRoom = roomIterator.next();
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
            playerInput += inputDir;
            setInteractingWorldTile(playerPosition, getTile(playerPosition));
            setInteractingWorldTile(newPlayerPosition, Tileset.AVATAR);
            playerPosition = newPlayerPosition;
            if (Engine.verbose) {
                System.out.println("DEBUG: Successfully moved " + inputDir);
            }
        }

    }
    private boolean isValidMove(Vector pos) {
        return !playerPosition.equals(pos) && isValidPos(pos, world) && !getTile(pos).isBlocksPlayer();
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
    public static TETile getTile(Vector pos, TETile[][] world) {
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
    private int getVertex(Vector pos) {
        return graphMap[(int) pos.getX()][(int) pos.getY()];
    }
    public void generateGraph() {
        /** Generate a graph using the world instance variable
         *  make each floor a vertex and connect it if touching
         *  NOT COMPLETE - MIGHT HAVE SOME BUG
         */
        //TODO: Fix potential bugs??
        floorGraphAStar = new Graph(countTiles());
        //Vertical Connections
        for (int i = 0; i < world.length - 1; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if(shouldConnectTiles(i, j, i + 1, j, false)) {
                    floorGraphAStar.addEdge(graphMap[i][j], graphMap[i + 1][j]);
                }
            }
        }
        //Horizontal Connections
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length - 1; j++) {
                if(shouldConnectTiles(i, j, i, j + 1, false)) {
                    floorGraphAStar.addEdge(graphMap[i][j], graphMap[i][j + 1]);
                }
            }
        }
    }
    public int countTiles() {
        int graphCount = 0;
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                graphMap[i][j] = graphCount;
                vertexToPos.put(graphCount, new Vector(i, j));
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
        Vector currentVector;
        for (Vector v:playerVisibleVectors) {
            currentVector = v.add(playerPosition);
            if (isValidPos(currentVector, world) && monsterLineOfSight(currentVector)) {
                output.put(currentVector.getCopy(), v.getMagnitude());
            }
        }
        return output;
    }
    public String getTileName(double[] pos) {
        /**
         * Used for finding the name of a tile
         */
        return getInteractiveTile((int) pos[0], (int) pos[1]).description();
    }
    private void generateVisibleVectors() {
        playerVisibleVectors = new HashSet<>();
        Vector tileVector;
        for (int i = -visionDepth; i < visionDepth; i++) {
            for (int j = -visionDepth; j < visionDepth; j++) {
                tileVector = new Vector(i, j);
                if (tileVector.getMagnitude() < visionDepth) {
                    playerVisibleVectors.add(tileVector.getCopy());
                }
            }
        }
    }
    private boolean monsterLineOfSight(Vector monsterPos) {
        Vector distance = monsterPos.subtract(playerPosition);
        double moveTowards = Math.abs(maxMag(distance.getX(), distance.getY()));
        Vector unitXYVector = distance.scale(1 / moveTowards);
        Vector tile, upperTile, lowerTile;
        for (double scalar = 1; scalar < moveTowards; scalar++) {
            tile = playerPosition.add(unitXYVector.scale(scalar));
            upperTile = new Vector(Math.ceil(tile.getX()), Math.ceil(tile.getY()));
            lowerTile = new Vector(Math.floor(tile.getX()), Math.floor(tile.getY()));
            if ((isValidPos(upperTile, world) && getTile(upperTile).isBlocksPlayer()) &&
                    (isValidPos(lowerTile, world) && getTile(lowerTile).isBlocksPlayer())) {
                return false;
            }
        }
        return true;
    }
    private double maxMag(double a, double b) {
        if (Math.abs(a) > Math.abs(b)) {
            return a;
        }
        return b;
    }
}

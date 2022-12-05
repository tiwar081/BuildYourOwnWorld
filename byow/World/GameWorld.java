package byow.World;

import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.RoomVectorsStuff.Room;
import byow.RoomVectorsStuff.Vector;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Graph;

import java.util.*;

import static byow.RoomVectorsStuff.usefulFuncs.*;

public class GameWorld {
    private byow.RoomVectorsStuff.Vector playerPosition;
    private String playerInput;
    private Collection<Room> allRooms;
    private TETile[][] blankWorld;
    private TETile[][] world;
    private TETile[][] interactingWorld;
    private int[][] graphMap;
    private Graph floorGraphAStar;
    private HashMap<Integer, byow.RoomVectorsStuff.Vector> vertexToPos = new HashMap<>();
    private boolean viewEntireWorld = false;
    private int visionDepth = 8;
    private HashSet<byow.RoomVectorsStuff.Vector> playerVisibleVectors = new HashSet<>();
    private HashSet<byow.RoomVectorsStuff.Vector> vectorsToAllTiles = new HashSet<>();


    public GameWorld(Random rand, TETile[][] world, Collection<Room> allRooms, String input) {
        this.allRooms = allRooms;
        blankWorld = emptyWorld();
        this.world = world;
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
    public TETile[][] getWorld() {

        if (viewEntireWorld) {
            TETile[][] astarworld = TETile.copyOf(interactingWorld);
            //TODO: MODIFY astarworld
            return astarworld;
        }
        TETile[][] smallerWorld = TETile.copyOf(blankWorld);

        HashMap<byow.RoomVectorsStuff.Vector, Double> visibleTiles = findVisibleTiles();
        for (byow.RoomVectorsStuff.Vector pos:visibleTiles.keySet()) {
            TETile dimmedTile = TETile.intensityVariant(getInteractiveTile(pos), 1-visibleTiles.get(pos)/visionDepth);
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
        byow.RoomVectorsStuff.Vector direction = convertToVector(inputDir);
        byow.RoomVectorsStuff.Vector newPlayerPosition = playerPosition.add(direction);
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
    private boolean isValidMove(byow.RoomVectorsStuff.Vector pos) {
        return !playerPosition.equals(pos) && isValidPos(pos, world) && getTile(pos)!=Tileset.WALL;
    }
    public void setInteractingWorldTile(byow.RoomVectorsStuff.Vector pos, TETile tile) {
        interactingWorld[(int) pos.getX()][(int) pos.getY()] = tile;
    }
    public static void setTile(byow.RoomVectorsStuff.Vector pos, TETile tile, TETile[][] world) {
        world[(int) pos.getX()][(int) pos.getY()] = tile;
    }
    public TETile getTile(byow.RoomVectorsStuff.Vector pos) {
        return world[(int) pos.getX()][(int) pos.getY()];
    }
    public TETile getInteractiveTile(byow.RoomVectorsStuff.Vector pos) {
        return interactingWorld[(int) pos.getX()][(int) pos.getY()];
    }
    public TETile getTile(int x, int y) {
        return world[x][y];
    }
    public TETile getInteractiveTile(int x, int y) {
        return interactingWorld[x][y];
    }
    private byow.RoomVectorsStuff.Vector convertToVector(char inputDir) {
        switch (inputDir) {
            case 'W':
            case 'w':
                return new byow.RoomVectorsStuff.Vector(0,1);
            case 'd':
            case 'D':
                return new byow.RoomVectorsStuff.Vector(1, 0);
            case 's':
            case 'S':
                return new byow.RoomVectorsStuff.Vector(0, -1);
            case 'a':
            case 'A':
                return new byow.RoomVectorsStuff.Vector(-1, 0);
            case 'T':
                toggleWorldView();
        }
        return new byow.RoomVectorsStuff.Vector(0, 0);
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
                vertexToPos.put(graphCount, new byow.RoomVectorsStuff.Vector(i, j));
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
    private HashMap<byow.RoomVectorsStuff.Vector, Double> findVisibleTiles() {
        HashMap<byow.RoomVectorsStuff.Vector, Double> output = new HashMap<>();
        byow.RoomVectorsStuff.Vector currentVector;
        for (byow.RoomVectorsStuff.Vector v:playerVisibleVectors) {
            currentVector = v.add(playerPosition);
            if (isValidPos(currentVector, world)) {
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
        byow.RoomVectorsStuff.Vector tileVector;
        for (int i = -visionDepth; i < visionDepth; i++) {
            for (int j = -visionDepth; j < visionDepth; j++) {
                tileVector = new byow.RoomVectorsStuff.Vector(i, j);
                if (tileVector.getMagnitude() < visionDepth) {
                    playerVisibleVectors.add(tileVector.getCopy());
                }
            }
        }
    }
    private boolean monsterLineOfSight(byow.RoomVectorsStuff.Vector monsterPos) {
        byow.RoomVectorsStuff.Vector distance = monsterPos.subtract(playerPosition);
        double moveTowards = maxMag(distance.getX(), distance.getY());
        byow.RoomVectorsStuff.Vector unitXYVector = distance.scale(1 / moveTowards);
        Vector tile;
        for (int scalar = 1; scalar < moveTowards; scalar++) {
            tile = playerPosition.add(unitXYVector.scale(scalar));
            tile.setYDirection(Math.round(tile.getY()));
            tile.setXDirection(Math.round(tile.getX()));
            if (isValidPos(tile, world) && getTile(tile).isBlocksPlayer()) {
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

//        double xStart = monsterPos.getX();
//        double yStart = monsterPos.getY();
//        double xDiff = playerPosition.getX() - xStart;
//        double yDiff = playerPosition.getY() - yStart;
//        double slope = yDiff/xDiff;
//        double xDiffSign = xDiff/Math.abs(xDiff);
//        Vector tilePos;
//        if (yDiff < xDiff) {
//            for (int x_pos = 1; Math.abs(x_pos) < Math.abs(xDiff); x_pos += xDiffSign) {
//                double y_pos = slope * x_pos + yStart;
//                int lowerY = (int) Math.floor(y_pos);
//                int upperY = (int) Math.ceil(y_pos);
//                if (upperY - y_pos < lowerY - y_pos) {
//                    tilePos = new Vector(x_pos + (int) xStart, upperY);
//                    if (isValidPos(tilePos, world) && getTile(tilePos) == Tileset.WALL) {
//                        return false;
//                    }
//                } else {
//                    tilePos = new Vector(x_pos + (int) xStart, lowerY);
//                    if (isValidPos(tilePos, world) && getTile(tilePos) == Tileset.WALL) {
//                        return false;
//                    }
//                }
//            }
//        } else {
//            for (int y_pos = 1; Math.abs(y_pos) < Math.abs(xDiff); y_pos += xDiffSign) {
//                double x_pos = (1 / slope) * (y_pos - yStart);
//                int lowerX = (int) Math.floor(x_pos);
//                int upperX = (int) Math.ceil(x_pos);
//                if (lowerX - x_pos < upperX - x_pos) {
//                    tilePos = new Vector(lowerX, y_pos + yStart);
//                    if (isValidPos(tilePos, world) && getTile(tilePos) == Tileset.WALL) {
//                        return false;
//                    }
//                } else {
//                    tilePos = new Vector(upperX, y_pos + yStart);
//                    if (isValidPos(tilePos, world) && getTile(tilePos) == Tileset.WALL) {
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }
}

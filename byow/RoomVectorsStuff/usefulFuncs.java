package byow.RoomVectorsStuff;
import java.awt.*;
import java.util.Random;
import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import static byow.World.GameWorld.getTile;
import static byow.World.GameWorld.setTile;

public class usefulFuncs {
    public static TETile[][] emptyWorld() {
        TETile[][] emptyWorld = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int i = 0; i < Engine.WIDTH; i++) {
            for (int j = 0; j < Engine.HEIGHT; j++) {
                emptyWorld[i][j] = Tileset.NOTHING;
            }
        }

        return emptyWorld;
    }

    public static int[] genRoomDim(Random r) {
        int width = RandomUtils.uniform(r, Engine.MIN_ROOM_SIZE, Engine.MAX_ROOM_SIZE + 1);
        int length = RandomUtils.uniform(r, Engine.MIN_ROOM_SIZE, Engine.MAX_ROOM_SIZE + 1);
        if (width == 1) {
            width = RandomUtils.uniform(r, Engine.MIN_ROOM_SIZE, Engine.MAX_ROOM_SIZE + 1);
        }
        if (length == 1) {
            length = RandomUtils.uniform(r, Engine.MIN_ROOM_SIZE, Engine.MAX_ROOM_SIZE + 1);
        }
        if (width == 1) {
            length = 1;
        }
        if (length == 1) {
            width = 1;
        }

        return new int[] {width, length};
    }

    public static double[] genRoomPos(Random r, int length, int width) {
        /**
         * Generate a random valid room position
         * Set proper bounds in random generation such that
         * the room must be contained inside the world
         * Might need Engine.WIDTH and Engine.HEIGHT
         * Returns [xpos, ypos]
         */
        double bottomRightx = RandomUtils.uniform(r, 1.5, Engine.WIDTH - 1 - width);
        double bottomRighty = RandomUtils.uniform(r, 1.5, Engine.HEIGHT - 1 - length);

        return new double[] {bottomRightx + (double) width/2, bottomRighty + (double) length/2};
    }

    //adds room to world
    public static void addRoom(Room room, TETile[][] world) {
        for (int i = room.xLeft(); i <= room.xRight(); i++) {
            for (int j = room.yBottom(); j <= room.yTop(); j++) {
                world[i][j] = Tileset.ROOM_FLOOR;
            }
        }
    }

    public static void addRoomWalls(Room room, TETile[][] world) {
        //TODO: Make this code more clean
        // Don't need to draw out every single one of these tiles
        for (Vector pos:room.wallPositions()) {
            setTile(pos, Tileset.WALL, world);
        }
    }
    public static void addRoomDoors(Room room, TETile[][] world) {
        //TODO: Make this code more clean
        // Don't need to draw out every single one of these tiles
        for (int i = room.xLeft(); i <= room.xRight(); i++) {
            for (int j = room.yBottom(); j <= room.yTop(); j++) {
                world[i][j] = Tileset.GOLDEN_FLOOR;
            }
        }

        for (Vector pos:room.wallPositions()) {
            if (getTile(pos, world) == Tileset.FLOOR) {
                setTile(pos, Tileset.LOCKED_DOOR, world);
            }
        }
    }
    public static void addRoomUnlockedDoors(Room room, TETile[][] world) {
        for (Vector pos:room.wallPositions()) {
            if (getTile(pos, world) == Tileset.LOCKED_DOOR) {
                setTile(pos, Tileset.UNLOCKED_DOOR, world);
            }
        }
    }
    public static boolean isValidPos(Vector pos, TETile[][] world) {
        return (pos.getX() >= 0 && pos.getY() >= 0) && (pos.getX() < world.length && pos.getY() < world[0].length);
    }
}

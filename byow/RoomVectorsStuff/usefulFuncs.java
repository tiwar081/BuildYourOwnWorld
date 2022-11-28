package byow.RoomVectorsStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

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
        double bottomRightx = (double) RandomUtils.uniform(r, 1, Engine.WIDTH - 1 - width);
        double bottomRighty = (double) RandomUtils.uniform(r, 1, Engine.HEIGHT - 1 - length);

        return new double[] {bottomRightx + (double) width/2, bottomRighty + (double) length/2};
    }

    //adds room to world
    public static void addRoom(Room room, TETile[][] world) {
        for (int i = room.xLeft(); i <= room.xRight(); i++) {
            for (int j = room.yBottom(); j <= room.yTop(); j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    public static Vector chooseNextHallwayTile(Random rand, Vector netDirection) {
        // Find vertical and horizontal distances that need to be traveled
        int vertDistance = (int) netDirection.getY();
        int horzDistance = (int) netDirection.getX();

        // Choose a random weighted direction to travel, negative is vertical, and positive is horizontal
        int randomDirection = RandomUtils.uniform(rand, -Math.abs(vertDistance), Math.abs(horzDistance)-1);

        if (randomDirection >= 0) {
            // Change direction in the sign of horzDistance needed to be traveled
            return new Vector(horzDistance, 0).normalize();
        } else {
            // Change direction in the sign of horzDistance needed to be traveled
            return new Vector(0, vertDistance).normalize();
        }
    }
    public static void drawHallway(Random rand, Room startRoom, Room endRoom, TETile[][] world) {
        Vector startPos = startRoom.genHallwayTarget(rand);
        Vector endPos = endRoom.genHallwayTarget(rand);
        ArrayList<Vector> laidDownFloor = new ArrayList<>();

        while (!startPos.equals(endPos)) {
            startPos = startPos.add(chooseNextHallwayTile(rand, endPos.subtract(startPos)));
            setTile(startPos, Tileset.FLOOR, world);
            laidDownFloor.add(startPos.getCopy());
        }
        addWalls(laidDownFloor, world);
    }
    public static void addWalls(ArrayList<Vector> flooring, TETile[][] world) {
        for (Vector floorPos:flooring) {
            for (Vector pos: floorPos.surroundingVectors()) {
                if (isTileEmpty(floorPos, world)) {
                    setTile(floorPos, Tileset.WALL, world);
                }
            }
        }
    }
    public static boolean isTileEmpty(Vector pos, TETile[][] world) {
        return world[(int) pos.getX()][(int) pos.getY()] == null;
    }
    public static void setTile(Vector pos, TETile tile, TETile[][] world) {
        world[(int) pos.getX()][(int) pos.getY()] = tile;
    }
    public static Room getRandomRoom(Random rand, List<Room> roomList) {
        int randomIndex = RandomUtils.uniform(rand, roomList.size());
        return roomList.get(randomIndex);
    }
}

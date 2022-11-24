package byow.RoomVectorsStuff;
import java.util.Random;

import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;

public class usefulFuncs {
    public static int genRoomSize(Random r) {
        int smallest = Engine.MIN_ROOM_SIZE;
        int numSizes = Engine.MAX_ROOM_SIZE - Engine.MIN_ROOM_SIZE;
        return RandomUtils.uniform(r, numSizes) + smallest;
    }

    public static int[] genRoomPos(Random r, int length, int width) {
        /**
         * Generate a random valid room position
         * Set proper bounds in random generation such that
         * the room must be contained inside the world
         * Might need Engine.WIDTH and Engine.HEIGHT
         * Returns [xpos, ypos]
         */

    }
    public static Room generateRoom(int x, int y, int length, int width) {

    }

    public static void drawRoom(Room room, TETile[][] world) {

    }
}

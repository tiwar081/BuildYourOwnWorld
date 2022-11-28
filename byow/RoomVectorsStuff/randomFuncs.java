package byow.RoomVectorsStuff;
import java.awt.*;
import java.util.Random;

import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

public class randomFuncs {
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
        return new int[1];
    }
    public static Room generateRoom(int x, int y, int length, int width) {
        return new Room(0,0,0,0);
    }

    public static void drawRoom(Room room, TETile[][] world) {

    }

    public static void drawStartMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 4 / 5, "61B: THE GAME");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 3 / 5, "New Game (N)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 2.5 / 5, "Load Game (L)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 2 / 5, "Quit (Q)");

        StdDraw.show();
    }
}

package byow.RoomVectorsStuff;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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


        //        //TODO: If the game is not over, display encouragement, and let the user know if they
        //        // should be typing their answer or watching for the next round.
        //        Font fontSmall = new Font("Monaco", Font.PLAIN, 20);
        //        StdDraw.setFont(fontSmall);
        //        int height2 = this.height-1;
        //        StdDraw.text(3, height2, "Round: ");
        //        StdDraw.text(6, height2, ""+this.round);
        //
        //        if (!playerTurn) {
        //            StdDraw.text(Engine.WIDTH / 2, height2, "Watch!");
        //        } else {
        //            StdDraw.text(Engine.WIDTH / 2, height2, "Type!");
        //        }
        //        StdDraw.text(Engine.WIDTH-6, height2, encorage);
        //
        //        StdDraw.text(0, height2-0.5, "_".repeat(this. * 4));
        StdDraw.show();
    }

    public static long randomSeed() {
        return (long) RandomUtils.uniform(new Random(), 0, 1000000);
    }
}

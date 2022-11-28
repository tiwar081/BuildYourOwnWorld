package byow.RoomVectorsStuff;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

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
}

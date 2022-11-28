package byow.RoomVectorsStuff;

import byow.Core.Engine;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class DisplayFuncs {
    public static void drawStartMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 4 / 5, "61B: THE GAME");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 3 / 5, "New Game (N)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 2.5 / 5, "Load Game (L)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 2 / 5, "Random Game (R)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 1.5 / 5, "Quit (Q)");

        StdDraw.show();
    }
    public static void drawSeed(String seed) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 4 / 5, "Enter Seed Number");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 3.5 / 5, "followed by S");

        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 2.5 / 5, seed);

        StdDraw.show();
    }
    public static void drawRandomSeed(String seed) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 4 / 5, "Randomly Generated Seed Number");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 3.5 / 5, "Press any key to confirm");

        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 2.5 / 5, seed);

        StdDraw.show();
    }
}

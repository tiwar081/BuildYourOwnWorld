package byow.RoomVectorsStuff;

import byow.Core.Engine;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class DisplayFuncs {
    public static void drawStartMenu(TERenderer rend) {
        StdDraw.clear(Color.BLACK);
        rend.renderGradient();
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 4 / 5, "THE GAME");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 3 / 5, "New Game (N)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 2.5 / 5, "Load Game (L)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 2 / 5, "Random Game (R)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 1.5 / 5, "Quit (Q)");

        StdDraw.show();
    }
    public static void drawSeed(TERenderer rend, String seed) {
        StdDraw.clear(Color.BLACK);
        rend.renderGradient();
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 4 / 5, "Enter Seed Number");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 3.5 / 5, "followed by S");

        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 2.5 / 5, seed);

        StdDraw.show();
    }
    public static void drawRandomSeed(TERenderer rend, String seed) {
        StdDraw.clear(Color.BLACK);
        rend.renderGradient();
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 4 / 5, "Randomly Generated Seed Number");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 3.5 / 5, "Press any key to confirm");

        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 2.5 / 5, seed);

        StdDraw.show();
    }
    public static void drawLoseScreen(TERenderer rend) {
        StdDraw.clear(Color.BLACK);
        rend.renderGradient();
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 4 / 5, "You Lost");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 3 / 5, "Better Luck Next Time");

        StdDraw.show();
    }
    public static void drawEndScreen(TERenderer rend) {
        StdDraw.clear(Color.BLACK);
        rend.renderGradient();
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 4 / 5, "This session has ended");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 3 / 5, "See you soon!");

        StdDraw.show();
    }
    public static Color gradient(Color a, Color b, double percentRatio) {
        if (percentRatio > 1) {
            throw new IllegalArgumentException();
        }
        double aPercent = percentRatio;
        double bPercent = 1 - percentRatio;
        return new Color(
                (int) ((double) a.getRed() * aPercent + (double) b.getRed() * bPercent),
                (int) ((double) a.getGreen() * aPercent + (double) b.getGreen() * bPercent),
                (int) ((double) a.getBlue() * aPercent + (double) b.getBlue() * bPercent));
    }
}

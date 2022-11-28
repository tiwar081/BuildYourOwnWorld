package byow.Core;
import byow.Core.Engine;
import byow.RoomVectorsStuff.*;
import byow.TileEngine.*;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
//        if (args.length > 2) {
//            System.out.println("Can only have two arguments - the flag and input string");
//            System.exit(0);
//        } else if (args.length == 2 && args[0].equals("-s")) {
//            Engine engine = new Engine();
//            engine.interactWithInputString(args[1]);
//            System.out.println(engine.toString());
//        } else {
//            Engine engine = new Engine();
//            engine.interactWithKeyboard();
//        }

        String input = "N123S";
        TETile[][] world = new Engine().interactWithInputString(input);

        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        ter.renderFrame(world);
    }
}

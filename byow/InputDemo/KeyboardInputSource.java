package byow.InputDemo;

/**
 * Created by hug.
 */
import edu.princeton.cs.algs4.StdDraw;

import static byow.RoomVectorsStuff.DisplayFuncs.drawStartMenu;

public class KeyboardInputSource implements InputSource {
    private static final boolean PRINT_TYPED_KEYS = false;
    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (PRINT_TYPED_KEYS) {
                    System.out.print(c);
                }
                return c;
            }
        }
    }

    public boolean possibleNextInput() {
        return true;
    }
}

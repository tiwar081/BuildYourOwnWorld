package byow.RoomVectorsStuff;
import java.util.HashSet;
import java.util.Random;
import byow.Core.RandomUtils;

public class randomFuncs {
    public static int genRoomSize(Random r) {
        int smallest = 1;
        int numSizes = 5;
        return RandomUtils.uniform(r, numSizes) + smallest;
    }

    public static Room generateRoom(int space_dim_x, int space_dim_y) {
        
    }

    public static void drawRoom(Room room) {

    }
}

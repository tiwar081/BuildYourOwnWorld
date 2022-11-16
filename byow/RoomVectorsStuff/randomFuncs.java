package byow.RoomVectorsStuff;
import java.util.HashSet;
import java.util.Random;
import byow.Core.RandomUtils;

public class randomFuncs {
    public static int genRoomSize() {
        Random x = new Random();
        int smallest = 2;
        int numSizes = 4;
        return RandomUtils.uniform(x, numSizes) + smallest;
    }

    public static Room generateRoom(int space_dim_x, int space_dim_y) {

    }

    public static void drawRoom(Room room) {
        
    }
}

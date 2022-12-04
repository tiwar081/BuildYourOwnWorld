package byow.RoomVectorsStuff;
import java.util.Comparator;

public class VectorMagComparator implements Comparator<Vector>{
    public int compare(Vector a, Vector b) {
        if (a.getMagnitude() - b.getMagnitude() > 0) {
            return 1;
        } else if (a.getMagnitude() - b.getMagnitude() < 0) {
            return -1;
        }
        return 0;
    }
}

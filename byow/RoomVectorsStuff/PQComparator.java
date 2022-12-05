package byow.RoomVectorsStuff;
import byow.Core.Engine;

import java.util.HashMap;
import java.util.Comparator;

public class PQComparator implements Comparator<Integer> {
    public HashMap<Integer, Double> distTo;
    public Integer end;

    public PQComparator(HashMap<Integer, Double> d, int e) {
        distTo = d;
        end = e;
    }

    public Double distance(int v, int w) {
        return usefulFuncs.intToVector(v).subtract(usefulFuncs.intToVector(w)).getMagnitude();
    }

    public int compare (Integer v, Integer w) {
        Double vComp = distTo.get(v) + distance(v, end);
        Double wComp = distTo.get(w) + distance(w, end);
        if (vComp > wComp) {
            return 1;
        } else if (wComp > vComp) {
            return -1;
        }
        return 0;
    }
}

package byow.RoomVectorsStuff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import byow.Core.Engine;
import java.util.Comparator;
import edu.princeton.cs.algs4.Graph;

public class ShortestPath {
    private HashMap<Integer, Integer> predecessorEdge;
    private HashMap<Integer, Double> distTo;
    private HashMap<Integer, Boolean> Marked;
    private PriorityQueue<Integer> PQ;
    private int start;
    private int end;
    private Graph World;
    private final int NUMTILES = Engine.WIDTH * Engine.HEIGHT;

    public ShortestPath(int s, int e, Graph G) {
        start = s;
        end = e;
        World = G;

        predecessorEdge = new HashMap<Integer, Integer>();

        Marked = new HashMap<>();
        for (int i = 0; i < NUMTILES; i++) {
            Marked.put(i, false);
        }
        Marked.put(start, true);

        distTo = new HashMap<Integer, Double>();
        distTo.put(start, 0.0);

        PQComparator C =  new PQComparator(distTo, end);
        PQ = new PriorityQueue<Integer>(100, C);

        int currNode = start;
        PQ.add(currNode);
        while (currNode != end) {
            PQ.poll();
            relaxOutgoingEdges(currNode);
            currNode = PQ.peek();
        }
    }

    public Integer getNextMove() {
        return getPath().get(1);
    }

    public ArrayList<Integer> getPath() {
        int currnode = end;
        ArrayList<Integer> path = new ArrayList<>();

        while(currnode != start) {
            path.add(0, currnode);
            currnode = predecessorEdge.get(currnode);
        }

        return path;
    }

    private static void reinsertPQElem(PriorityQueue<Integer> PQ, Integer v) {
        PQ.remove(v);
        PQ.add(v);
    }

    //Switches/adds distTo[w] and edgeTo[w]
    private void relaxOutgoingEdges(int v) {
        for (int w : World.adj(v)) {
            if (Marked.get(w)) {
                if (distTo.get(w) > distTo.get(v) + 1.0) {
                    predecessorEdge.put(w, v);
                    distTo.put(w, distTo.get(v) + 1.0);
                    reinsertPQElem(PQ, w);
                }
            } else {
                Marked.put(w, true);
                predecessorEdge.put(w, v);
                distTo.put(w, distTo.get(v) + 1.0);
                PQ.add(w);
            }
        }
    }
}

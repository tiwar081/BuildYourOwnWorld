package byow.RoomVectorsStuff;

import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HallwaysFuncs {
    public static Vector chooseNextHallwayTile(Random rand, Vector netDirection) {
        // Find vertical and horizontal distances that need to be traveled
        int vertDistance = (int) netDirection.getY();
        int horzDistance = (int) netDirection.getX();

        // Choose a random weighted direction to travel, negative is vertical, and positive is horizontal
        int randomDirection = RandomUtils.uniform(rand, -Math.abs(vertDistance), Math.abs(horzDistance)-1);

        if (randomDirection >= 0) {
            // Change direction in the sign of horzDistance needed to be traveled
            return new Vector(horzDistance, 0).normalize();
        } else {
            // Change direction in the sign of horzDistance needed to be traveled
            return new Vector(0, vertDistance).normalize();
        }
    }
    public static void drawHallway(Random rand, Room startRoom, Room endRoom, TETile[][] world) {
        Vector startPos = startRoom.genHallwayTarget(rand);
        Vector endPos   = endRoom.genHallwayTarget(rand);
        ArrayList<Vector> laidDownFloor = new ArrayList<>();

        while (!startPos.equals(endPos)) {
            startPos = startPos.add(chooseNextHallwayTile(rand, endPos.subtract(startPos)));
            setTile(startPos, Tileset.FLOOR, world);
            laidDownFloor.add(startPos.getCopy());
        }
        addWalls(laidDownFloor, world);
    }
    public static void addWalls(ArrayList<Vector> flooring, TETile[][] world) {
        for (Vector floorPos:flooring) {
            for (Vector pos: floorPos.surroundingVectors()) {
                if (isTileEmpty(floorPos, world)) {
                    setTile(floorPos, Tileset.WALL, world);
                }
            }
        }
    }
    public static boolean isTileEmpty(Vector pos, TETile[][] world) {
        return world[(int) pos.getX()][(int) pos.getY()] == null;
    }
    public static void setTile(Vector pos, TETile tile, TETile[][] world) {
        world[(int) pos.getX()][(int) pos.getY()] = tile;
    }
    public static Room getRandomRoom(Random rand, List<Room> roomList) {
        int randomIndex = RandomUtils.uniform(rand, roomList.size());
        return roomList.get(randomIndex);
    }

}

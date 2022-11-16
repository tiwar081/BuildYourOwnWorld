package byow.RoomVectorsStuff;

import byow.Core.RandomUtils;

import java.util.HashSet;
import java.util.Random;

public class Room {
    // x and y position of center
    private double x_pos;
    private double y_pos;
    // total length and width of room
    private double length;
    private double width;
    // left and right vector for overlap comparisons between other rooms
    private Vector leftVector;
    private Vector rightVector;
    private HashSet<Room> connectedRooms;

    public Room(double x, double y, int l, int w) {
        x_pos = x;
        y_pos = y;
        length = l;
        width = w;
        // Need vectors to top left and top right for overlap calculation
        leftVector = new Vector(-length/2, width/2);
        rightVector = new Vector(length/2, width/2);
    }
    public int xLeft() {
        return (int) (x_pos - (length - 1)/2);
    }
    public int yBottom() {
        return (int) (y_pos - (width - 1)/2);
    }
    public int xRight() {
        return (int) (x_pos + (length - 1)/2);
    }
    public int yTop() {
        return (int) (y_pos + (width - 1)/2);
    }

    public void connect(Room b) {
        /**
         * Connect 2 rooms with an edge in a connected graph by using a connect method.
         * Allows for latter checking if rooms are adjecent
         */
        connectedRooms.add(b);
    }
    public boolean isConnectedTo(Room b) {
        /**
         * Check if 2 rooms are already adjecent to each other with a direct hallway between the 2 rooms.
         */
        return connectedRooms.contains(b);
    }
    public int[] genHallwayTarget(Random rand) {
        /**
         * Generate a random position inside the room from which to start the hallway
         * Returns an integer array with [xpos, ypos]
         */
        int cardinalDirection = RandomUtils.uniform(rand, 0, 4);
        int randomXDirection = RandomUtils.uniform(rand, xLeft(), xRight() + 1);
        int randomYDirection = RandomUtils.uniform(rand, yBottom(), yTop() + 1);
        int[] hallwayTarget = {randomXDirection, randomYDirection};

        switch (cardinalDirection) {
            case 0:
                //North/Top Hallway
                hallwayTarget[1] = yTop();
                break;
            case 1:
                // East/Right Hallway
                hallwayTarget[0] = xRight();
                break;
            case 2:
                // South/Bottom Hallway
                hallwayTarget[1] = yBottom();
                break;
            case 3:
                // West/Left Hallway
                hallwayTarget[0] = xLeft();
                break;
        }
        return hallwayTarget;
    }
    private Vector vectorBetween(Room b) {
        /**
         * Calculate the vector between the centers of the current room and room b
         */
        return new Vector(b.x_pos - x_pos, b.y_pos - y_pos);
    }

    public boolean overlapsWith(Room b) {
        /**
         * Check if a room overlaps with another room using vector calculations
         */
        Vector vectorBetweenRooms = vectorBetween(b);
        Vector comparisonVector;
        if (vectorBetweenRooms.pointsRight()) {
            comparisonVector = rightVector.add(b.rightVector);
        } else {
            comparisonVector = leftVector.add(b.leftVector);
        }
        double projection = comparisonVector.projectTo(vectorBetweenRooms);

        return (projection >= vectorBetweenRooms.getMagnitude());
    }
    public boolean overlapsWith(Room[] b) {
        for (Room otherRoom : b) {
            if (overlapsWith(otherRoom)) {
                return true;
            }
        }
        return false;
    }

}

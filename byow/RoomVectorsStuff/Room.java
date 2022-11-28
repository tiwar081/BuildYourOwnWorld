package byow.RoomVectorsStuff;

import byow.Core.RandomUtils;

import java.util.*;

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
        leftVector = new Vector(-length/2 - 1, width/2 + 1);
        rightVector = new Vector(length/2 + 1, width/2 + 1);
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
    public Vector genHallwayTarget(Random rand) {
        /**
         * Generate a random position inside the room from which to start the hallway
         * Returns an integer array with [xpos, ypos]
         */
        int cardinalDirection = RandomUtils.uniform(rand, 0, 4);
        int randomXDirection = RandomUtils.uniform(rand, xLeft(), xRight() + 1);
        int randomYDirection = RandomUtils.uniform(rand, yBottom(), yTop() + 1);
        Vector hallwayTarget = new Vector(randomXDirection, randomYDirection);

        switch (cardinalDirection) {
            case 0:
                //North/Top Hallway
                hallwayTarget.setYDirection(yTop());
                break;
            case 1:
                // East/Right Hallway
                hallwayTarget.setXDirection(xRight());
                break;
            case 2:
                // South/Bottom Hallway
                hallwayTarget.setYDirection(yBottom());
                break;
            case 3:
                // West/Left Hallway
                hallwayTarget.setXDirection(xLeft());
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
    public ArrayList<Vector> closestRooms(Collection<Room> b) {
        // TODO: implement a closestRooms function to make hallway
        // connection easier
        return new ArrayList<>();
    }

    public boolean overlapsWith(Room b) {
        /**
         * Check if a room overlaps with another room using vector calculations
         */
        Vector vectorBetweenRooms = vectorBetween(b);
        Vector comparisonVectorRight = rightVector.add(b.rightVector);
        Vector comparisonVectorLeft = leftVector.add(b.leftVector);

        double projection = Math.max(Math.abs(comparisonVectorRight.projectTo(vectorBetweenRooms)), Math.abs(comparisonVectorLeft.projectTo(vectorBetweenRooms)));

        return (projection >= vectorBetweenRooms.getMagnitude());
    }
    public boolean overlapsWith(Collection<Room> b) {
        for (Room otherRoom : b) {
            if (overlapsWith(otherRoom)) {
                return true;
            }
        }
        return false;
    }

}

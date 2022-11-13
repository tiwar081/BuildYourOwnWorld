package byow.RoomVectorsStuff;

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

    public Room(double x, double y, int l, int w) {
        x_pos = x;
        y_pos = y;
        length = l;
        width = w;
        // Need vectors to top left and top right for overlap calculation
        leftVector = new Vector(-length/2, width/2);
        rightVector = new Vector(length/2, width/2);
    }

    public int floorTileXStart() {
        return (int) (x_pos - (length - 1)/2);
    }
    public int floorTileYStart() {
        return (int) (y_pos - (width - 1)/2);
    }
    public int floorTileXEnd() {
        return (int) (x_pos + (length - 1)/2);
    }
    public int floorTileYEnd() {
        return (int) (y_pos + (width - 1)/2);
    }

    public Vector vectorBetween(Room b) {
        /**
         * Calculate the vector between the centers of the current and room b
         */
        return new Vector(b.x_pos - x_pos, b.y_pos - y_pos);
    }

    public boolean overlapsWith(Room b) {
        /**
         * Check if a room overlaps with another room using
         * vector calculations
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


}

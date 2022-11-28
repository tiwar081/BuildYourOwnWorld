package byow.RoomVectorsStuff;

import java.util.ArrayList;

public class Vector {
    // X and Y direction for vector, and store magnitude of vectors
    private double x_dir;
    private double y_dir;

    public Vector(double x, double y) {
        x_dir = x;
        y_dir = y;
    }
    public boolean pointsRight() {
        /**
         * Check if points towards the right direction, otherwise return false
         */
        return (x_dir > 0);
    }
    public ArrayList<Vector> surroundingVectors() {
        ArrayList<Vector> output = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!(x == 0 && y == 0)) {
                    output.add(add(new Vector(x, y)));
                }
            }
        }
        return output;
    }
    public void setXDirection(double x) {
        x_dir = x;
    }
    public void setYDirection(double y) {
        y_dir = y;
    }
    public double getX() {
        return x_dir;
    }
    public double getY() {
        return y_dir;
    }
    public Vector getCopy() {
        return new Vector(x_dir, y_dir);
    }
    public Vector add(Vector b) {
        /**
         * Perform vector addition by combining the x coordinates and the y coordinates
         */
        return new Vector(x_dir + b.x_dir, y_dir + b.y_dir);
    }

    public Vector subtract(Vector b) {
        /**
         * Perform vector addition by combining the x coordinates and the y coordinates
         */
        return new Vector(x_dir - b.x_dir, y_dir - b.y_dir);
    }
    public Vector normalize() {
        /**
         * Normalize the vectors by dividing by the magnitude
         */
        return new Vector(x_dir/getMagnitude(), y_dir/getMagnitude());
    }

    public double getMagnitude() {
        /**
         * Get magnitude of a vector
         */
         return Math.sqrt(Math.pow(x_dir, 2) + Math.pow(y_dir, 2));
    }

    public double dotProduct(Vector b) {
        /**
         * Calculate the vector dot product between vectors
         */
        return x_dir * b.x_dir + y_dir * b.y_dir;
    }

    public double projectTo(Vector b) {
        /**
         * Calculate the orthogonal projection onto another vector
         */
        return this.dotProduct(b)/b.getMagnitude();
    }
    public boolean equals(Vector b) {
        return (getX() == b.getX() && getY() == b.getY());
    }
}

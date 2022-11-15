package byow.RoomVectorsStuff;

public class Vector {
    // X and Y direction for vector, and store magnitude of vectors
    private double x_dir;
    private double y_dir;
    private double mag;

    public Vector(double x, double y) {
        x_dir = x;
        y_dir = y;
        mag = Math.pow(Math.pow(x_dir, 2) + Math.pow(x_dir, 2), 0.5);
    }
    public boolean pointsRight() {
        /**
         * Check if points towards the right direction, otherwise return false
         */
        return (x_dir > 0);
    }
    public Vector add(Vector b) {
        /**
         * Perform vector addition by combining the x coordinates and the y coordinates
         */
        return new Vector(x_dir + b.x_dir, y_dir + b.y_dir);
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
        return mag;
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
}

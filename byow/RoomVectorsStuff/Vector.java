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

    public Vector add(Vector b) {
        return new Vector(x_dir + b.x_dir, y_dir + b.y_dir);
    }

    public Vector normalize() {
        return new Vector(x_dir/mag, y_dir/mag);
    }

    public double dotProduct(Vector b) {
        return x_dir * b.x_dir + y_dir * b.y_dir;
    }

    public double projectTo(Vector b) {
        return this.dotProduct(b)/b.mag;
    }
}

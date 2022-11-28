package byow.RoomVectorsStuff;

import byow.TileEngine.TETile;

import java.util.Collection;

public class GameWorld {
    private Vector playerPosition;
    private Collection<Room> allRooms;
    private TETile[][] world;
    private TETile[][] interactingWorld;

    public GameWorld(TETile[][] world, Collection<Room> allRooms) {
        this.allRooms = allRooms;
        this.world = world;
        interactingWorld = world.clone();
    }

    public void movePlayerIn(char inputDir) {
        Vector direction = convertToVector(inputDir);
        Vector newPlayerPosition = playerPosition.add(direction);
        if (direction.getMagnitude() > 1 && checkValidPosition(newPlayerPosition)) {

        }
    }
    private Vector convertToVector(char inputDir) {
        switch (inputDir) {
            case 'W':
            case 'w':
                return new Vector(0,1);
            case 'a':
            case 'A':
                return new Vector(1, 0);
            case 's':
            case 'S':
                return new Vector(0, -1);
            case 'd':
            case 'D':
                return new Vector(-1, 0);
        }
        return new Vector(0, 0);
    }
    private boolean checkValidPosition(Vector newPlayerPosition) {
        //TODO: Check if the player will move into wall
        // or other invalid position
        return false;
    }

}

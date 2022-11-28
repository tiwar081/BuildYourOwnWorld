package byow.Core;

import byow.RoomVectorsStuff.ParseString;
import byow.RoomVectorsStuff.Room;
import byow.RoomVectorsStuff.usefulFuncs;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static byow.RoomVectorsStuff.usefulFuncs.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int MAX_ROOMS = 25;
    public static final int ROOM_ATTEMPTS = 5;
    public static final int MIN_ROOM_SIZE = 1;
    public static final int MAX_ROOM_SIZE = 5;
    public static final int EXTRA_HALLWAYS = 10;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {

    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        TETile[][] finalWorldFrame = emptyWorld();
        Random rand = new Random(ParseString.getSeed(input));
        int[] room_dim;
        double[] pos = new double[] {0.0, 0.0};
        Room curr_room;
        ArrayList<Room> disconnectedRooms = new ArrayList<>();
        ArrayList<Room> connectedRooms = new ArrayList<>();
        for (int i = 0; i < MAX_ROOMS; i++) {
            room_dim = genRoomDim(rand);
            for (int j = 0; j < ROOM_ATTEMPTS; j++) {
                pos = genRoomPos(rand, room_dim[0], room_dim[1]);
                curr_room = new Room(pos[0], pos[1], room_dim[0], room_dim[1]);
                if (!curr_room.overlapsWith(disconnectedRooms)) {
                    disconnectedRooms.add(curr_room);
                    break;
                }
            }
        }

        for (Room r : disconnectedRooms) {
            addRoom(r, finalWorldFrame);
        }

        // Move the first room over to the connected rooms
        Room selectedRoom = getRandomRoom(rand, disconnectedRooms);
        disconnectedRooms.remove(selectedRoom);
        connectedRooms.add(selectedRoom);

        while (disconnectedRooms.size() > 0) {
            // While there are still disconnected rooms, select a random room from
            // the connected rooms and the disconnected rooms and connect them
            Room startRoom = getRandomRoom(rand, connectedRooms);
            Room endRoom = getRandomRoom(rand, disconnectedRooms);

            // Connect the rooms
            drawHallway(rand, startRoom, endRoom, finalWorldFrame);
            disconnectedRooms.remove(endRoom);
            connectedRooms.add(endRoom);
        }

        for (int i = 0; i < EXTRA_HALLWAYS; i++) {
            Room startRoom = getRandomRoom(rand, connectedRooms);
            connectedRooms.remove(startRoom);

            Room endRoom = getRandomRoom(rand, connectedRooms);
            connectedRooms.add(startRoom);

            // Connect the rooms
            drawHallway(rand, startRoom, endRoom, finalWorldFrame);
        }

        return finalWorldFrame;
    }
}

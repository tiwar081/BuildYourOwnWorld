package byow.Core;

import byow.RoomVectorsStuff.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.World.GameWorld;
import byow.World.SaveWorld;

import java.util.*;

import static byow.RoomVectorsStuff.DisplayFuncs.*;
import static byow.RoomVectorsStuff.usefulFuncs.*;
import static byow.RoomVectorsStuff.HallwaysFuncs.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 75;
    public static final int yOffSet = 2;
    public static final int MAX_ROOMS = 50;
    public static final int ROOM_ATTEMPTS = 5;
    public static final int MIN_ROOM_SIZE = 1;
    public static final int MAX_ROOM_SIZE = 5;
    public static final int EXTRA_HALLWAYS = 0;
    public static final boolean verbose = false;
    public static final char[] validSeedLetters = "sSnNlLRrqQ1234567890".toCharArray();
    public static final char[] validLetters = "aAwWsSdDtT:rR".toCharArray();
    public static final char[] terminalSeedLetters = "sSlLqQrR".toCharArray();
    public static final char terminalLetter = 'Q';
    public HashSet<Character> validSeedLettersSet = new HashSet<>();
    public HashSet<Character> validLettersSet = new HashSet<>();
    public HashSet<Character> terminalSeedLettersSet = new HashSet<>();
    public Engine() {
        for (char letter:validSeedLetters) {
            validSeedLettersSet.add(letter);
        }
        for (char letter:validLetters) {
            validLettersSet.add(letter);
        }
        for (char letter:terminalSeedLetters) {
            terminalSeedLettersSet.add(letter);
        }
    }
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputHandling ih = new InputHandling();
        // Start Menu
        TERenderer rend = new TERenderer();
        rend.initialize(WIDTH, HEIGHT + yOffSet);
        drawStartMenu(rend);

        // Get user input
        InputHandling inputH = new InputHandling();
        String input = inputH.inputSeed(true, validSeedLettersSet, terminalSeedLettersSet, rend);


        // Generate World from String Seed
        TETile[][] world;
        GameWorld gworld;
        Random rand;
        switch (input.charAt(input.length() - 1)) {
            case 's':
            case 'S':
                break;
            case 'r':
            case 'R':
                input = inputH.inputSeed(false, validSeedLettersSet, terminalSeedLettersSet, rend);
                drawRandomSeed(rend, input);
                inputH.anyInput();
                break;
            case 'l':
            case 'L':
                //TODO: Generate world from saved world
                input = SaveWorld.readWorld();
                break;
            case 'q':
            case 'Q':
                if (verbose) {
                    System.out.println("DEBUG: Ending Game");
                }
                rend.initialize(WIDTH, HEIGHT + yOffSet);
                drawEndScreen(rend);
                return;
        }
        gworld = interactWithInputStringGworld(input);
        rend.initialize(WIDTH, HEIGHT + yOffSet, 0, -yOffSet);
        rend.renderFrame(gworld.getWorld());

        // Move around in World
        char playerInput = ih.inputPlayer(validLettersSet);
        while (playerInput != terminalLetter) {
            if (playerInput != 'R') {
                gworld.movePlayerIn(playerInput);
                rend.renderFrame(gworld.getWorld());
            } else {
                rend.renderFrame(gworld.getWorld(), gworld.getTileName(ih.getKeyboardPos()));
            }
            playerInput = ih.inputPlayer(validLettersSet);
        }
        //TODO: Implement Save World Feature
        rend.initialize(WIDTH, HEIGHT + yOffSet);
        drawEndScreen(rend);
        SaveWorld.saveWorld(gworld.getPlayerInput());
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
        GameWorld gworld = interactWithInputStringGworld(input);
        return gworld.getWorld();
    }
    public GameWorld interactWithInputStringGworld(String input) {
        TETile[][] finalWorldFrame = emptyWorld();
        Random rand = new Random(ParseString.getSeed(input));
        char[] everyPlayerInput = ParseString.getPlayerInput(input);
        int[] room_dim;
        double[] pos;
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
                    System.out.println(curr_room.xLeft() + " " + curr_room.yBottom());
                    break;
                }
            }
        }

        for (Room r : disconnectedRooms) {
            addRoom(r, finalWorldFrame);
            addRoomWalls(r, finalWorldFrame);
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
        for (Room r : connectedRooms) {
            addRoom(r, finalWorldFrame);
        }

        GameWorld gworld = new GameWorld(rand, finalWorldFrame, connectedRooms, input);
        for (char playerInput:everyPlayerInput) {
            gworld.movePlayerIn(playerInput);
        }
        return gworld;
    }
}

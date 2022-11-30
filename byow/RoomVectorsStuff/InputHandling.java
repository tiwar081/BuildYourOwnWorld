package byow.RoomVectorsStuff;

import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TERenderer;

import java.util.Collection;
import java.util.Random;

import static byow.RoomVectorsStuff.DisplayFuncs.drawSeed;

public class InputHandling {
    private static final int KEYBOARD = 0;
    private static final int RANDOM = 1;
    private static final int STRING = 10;

    public String inputSeed(boolean useKeyboard, Collection<Character> validLetters, Collection<Character> terminalLetters, TERenderer rend) {
        String seed = "";

        if (useKeyboard) {
            InputSource inputSource = new KeyboardInputSource();
            if (Engine.verbose) {
                System.out.println("DEBUG: Waiting for Input");
            }
            char lastInput = 'N';
            while (!terminalLetters.contains(lastInput)) {
                lastInput = inputSource.getNextKey();
                if (validLetters.contains(lastInput)) {
                    seed += lastInput;
                    if (Engine.verbose) {
                        System.out.println(seed);
                    }
                    drawSeed(rend, seed.substring(1));
                }
            }
        } else {
            seed += "N" + RandomUtils.uniform(new Random(), 50L) + "S";
        }
        if (Engine.verbose) {
            System.out.println("DEBUG: Returning Seed");
        }
        return seed;
    }
    public void anyInput() {
        InputSource inputSource = new KeyboardInputSource();
        inputSource.getNextKey();
    }
    public char inputPlayer(Collection<Character> validLetters) {
        InputSource inputSource = new KeyboardInputSource();
        char input = inputSource.getNextKey();
        while (!validLetters.contains(input)) {
            input = inputSource.getNextKey();
        }
        if (input == ':') {
            input = inputSource.getNextKey();
            if (input != 'Q') {
                return inputPlayer(validLetters);
            }
        }
        return input;
    }
}


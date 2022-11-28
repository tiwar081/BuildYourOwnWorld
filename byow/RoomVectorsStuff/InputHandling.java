package byow.RoomVectorsStuff;

import byow.Core.RandomUtils;
import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.RandomInputSource;
import byow.InputDemo.StringInputDevice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class InputHandling {
    private static final int KEYBOARD = 0;
    private static final int RANDOM = 1;
    private static final int STRING = 10;

    public String inputSeed(boolean useKeyboard, Collection<Character> validLetters, Collection<Character> terminalLetters) {
        String seed = "";

        if (useKeyboard) {
            InputSource inputSource = new KeyboardInputSource();
            char lastInput = 'N';
            while (!terminalLetters.contains(lastInput)) {
                lastInput = inputSource.getNextKey();
                if (validLetters.contains(lastInput)) {
                    seed += lastInput;
                }
            }
        } else {
            seed += "N" + RandomUtils.uniform(new Random(), 50L) + "S";
        }
        return seed;
    }
}


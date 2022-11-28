package byow.RoomVectorsStuff;

public class ParseString {
    public static Long getSeed(String input) {
        /**
         * Get the seed by getting the start and end string
         */
        int posStart = input.indexOf("N") + 1;
        int posEnd = input.indexOf("S");
        String seedString = input.substring(posStart, posEnd);
        return Long.valueOf(seedString);
    }
}

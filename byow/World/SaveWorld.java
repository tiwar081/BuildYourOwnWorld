package byow.World;

import byow.Core.Engine;

import java.io.*;
import java.util.Scanner;

public class SaveWorld {
    public static String readWorld() {
        String filename = "saveWorld1";
        return readFile(filename);
    }
    public static void saveWorld(String input) {
        String filename = "saveWorld1";
        createFile(filename);
        saveFile(input, filename);
    }
    private static String readFile(String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            String data = "";
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }
            myReader.close();
            if (Engine.verbose) {
                System.out.println("Reading Input: " + data);
            }
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("Error: couldn't read file");
            e.printStackTrace();
        }
        return "";
    }
    public static void createFile(String filename) {
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile() && Engine.verbose) {
                System.out.println("Created world save file");
            }
        } catch (IOException e) {
            System.out.println("Error: couldn't create file");
            e.printStackTrace();
        }
    }
    public static void saveFile(String input, String filename) {
        try {
            File myObj = new File(filename);
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(input);
            myWriter.close();
            if (Engine.verbose) {
                System.out.println("Successfully Saved File!");
            }
        } catch (IOException e) {
            System.out.println("Error: couldn't save file");
            e.printStackTrace();
        }
    }
}

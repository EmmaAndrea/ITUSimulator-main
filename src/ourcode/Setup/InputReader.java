package ourcode.Setup;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * The InputReader class provides the necessary assets to read the input file.
 * The InputReader abstracts file handling, providing methods for accessing input content.
 */
public class InputReader {
    FileReader fileReader;
    BufferedReader bufferedReader;
    Map<String, Integer> map_of_spawns;
    private final List<String> lines;

    /**
     * Constructs an input reader, taking a file path as a parameter.
     * Instantiates an array list intended to hold all lines in input file.
     * Then iterates through ever line in file and adds to array list of lines.
     */
    public InputReader(String filePath) throws IOException {
        fileReader = new FileReader(filePath);
        bufferedReader = new BufferedReader(fileReader);
        map_of_spawns = new HashMap<>();
        lines = new ArrayList<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
            //System.out.println(line);
        }
    }

    /**
     * Returns the first number in the input file; the world size.
     */
    public int readWorldSize() {
        String line = lines.get(0).trim();
        return Integer.parseInt(line);
    }

    /**
     * Reads the input file and updates map_of_spawns of the types and amounts.
     * Type is a String that is a key in the HashMap.
     * Amount is an int that is a value in the HashMap.
     * In case that 'amount' in file is an interval, a random number herein is chosen.
     */
    public void readSpawns() {
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(" ");
            String type = parts[0];
            int amount;

            if (parts[1].contains("-")) {
                String[] min_max = parts[1].split("-");
                int min = Integer.parseInt(min_max[0]);
                int max = Integer.parseInt(min_max[1]);
                Random random = new Random();
                amount = random.nextInt(max - min + 1) + min; // random from 0 to max-min+1, then + min
            } if ()

            else {
                amount = Integer.parseInt(parts[1]);
            }

            map_of_spawns.put(type, amount);
        }
    }

    /**
     * Returns the value of a given key in map_of_spawns.
     * Takes a string as a parameter, which is supposed to be the key.
     */
    public int getAmount(String key) {
        return map_of_spawns.getOrDefault(key, 0);
    }
}


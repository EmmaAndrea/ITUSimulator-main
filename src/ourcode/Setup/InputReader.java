package ourcode.Setup;

import itumulator.world.Location;

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
    private final Map<String, Location> map_of_bear_territories;

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
        map_of_bear_territories = new HashMap<>();

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
     * Reads spawn data from a list of lines, parsing each line to determine the type of spawn,
     * the amount, and potentially the territory coordinates. It updates two maps: one for the total
     * amount of each type of spawn and another for bear territories if specified.
     */
    public void readSpawns() {
        int bearCount = 0;
        int totalBearAmount = 0;
        Random random = new Random();

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(" ");
            String type = parts[0];
            int amount;

            // Determines the amount, handling both single values and ranges
            if (parts[1].contains("-")) {
                String[] range = parts[1].split("-");
                int min = Integer.parseInt(range[0]);
                int max = Integer.parseInt(range[1]);
                amount = random.nextInt(max - min + 1) + min; // Random amount within the specified range
            } else {
                amount = Integer.parseInt(parts[1]); // Fixed amount
            }

            // Processing for bear spawns
            if ("bear".equals(type)) {
                bearCount++;
                String bearType = "bear" + bearCount;
                totalBearAmount += amount;

                // Parses and stores territory information if available
                if (parts.length > 2) {
                    String[] territoryParts = parts[2].split(",");
                    int x = Integer.parseInt(territoryParts[0].replaceAll("\\D", ""));
                    int y = Integer.parseInt(territoryParts[1].replaceAll("\\D", ""));
                    Location territory = new Location(x, y);
                    map_of_bear_territories.put(bearType, territory);
                }

                map_of_spawns.put("bear", totalBearAmount);
            } else {
                map_of_spawns.put(type, amount);
            }
        }
    }

    /**
     * Returns the value of a given key in map_of_spawns.
     * Takes a string as a parameter, which is supposed to be the key.
     */
    public int getAmount(String key) {
        return map_of_spawns.getOrDefault(key, 0);
    }

    /**
     * Gets the map of bera territories.
     * @return a map of string to location of bear territories.
     */
    public Map<String, Location> getMap_of_bear_territories() {
        return map_of_bear_territories;
    }

    /**
     * Gets the territory of a given bear.
     * @param bear the bear you wish to retrieve the territory from.
     * @return the location of the bear's territory.
     */
    public Location getTerritory(String bear){
        return map_of_bear_territories.get(bear);
    }
}


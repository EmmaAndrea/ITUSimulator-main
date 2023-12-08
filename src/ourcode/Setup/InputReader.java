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
    private Map<String, Location> map_of_bear_territories;
    private Map<Integer, Integer> map_of_wolf_packs;

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
        map_of_wolf_packs = new HashMap<>();

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
        int packcount = 0;
        int wolfcount = 0;

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(" ");
            boolean isCordyceps = parts[0].equalsIgnoreCase("cordyceps");
            String type = isCordyceps ? parts[1] : parts[0];
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

            // Handle special "cordyceps" case
            if (isCordyceps) {
                String cordycepsKey = "cordyceps " + type;
                map_of_spawns.put(cordycepsKey, map_of_spawns.getOrDefault(cordycepsKey, 0) + amount);
                continue;
            }

            // Handle wolf packs
            if (type.equals("wolf")) {
                map_of_wolf_packs.put(packcount++, amount);
                if (map_of_spawns.containsKey("wolf")){
                    wolfcount = wolfcount + map_of_spawns.get("wolf");
                    wolfcount = wolfcount + amount;
                    map_of_spawns.put("wolf", wolfcount);
                    continue;
                }
            }

            // Processing for bear spawns
            if (type.equals("bear")) {
                bearCount++;
                String bearType = "bear" + bearCount;
                totalBearAmount += amount;

                // Parses and stores territory information if available
                if (parts.length > 2) {
                    int x = Integer.parseInt(String.valueOf(parts[2].charAt(1)));
                    int y = Integer.parseInt(String.valueOf(parts[2].charAt(3)));
                    Location territory = new Location(x, y);
                    map_of_bear_territories.put(bearType, territory);
                }

                map_of_spawns.put("bear", totalBearAmount);
            } else {
                map_of_spawns.put(type, map_of_spawns.getOrDefault(type, 0) + amount);
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

    public Map<Integer, Integer> getMap_of_wolf_packs(){
        return map_of_wolf_packs;
    }
}


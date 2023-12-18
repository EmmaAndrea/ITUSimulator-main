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
    private Map<Integer, Integer> map_of_social_predator_packs;

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
        map_of_social_predator_packs = new HashMap<>();

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
        int bear_count = 0;
        int total_bear_amount = 0;
        Random random = new Random();
        int pack_count = 0;
        int wolf_count = 0;

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(" ");
            boolean isCordyceps = parts[0].equalsIgnoreCase("cordyceps");
            boolean isFungi = parts.length > 1 && parts[1].equalsIgnoreCase("fungi");
            String type = parts[0];

            // Check for dinosaur or dinosaur egg
            if (type.equals("dinosaur")) {
                if (parts.length > 1 && parts[1].equals("egg")) {
                    type = "dinosaur egg";
                }
            }

            int amountIndex = type.equals("dinosaur egg") ? 2 : 1;

            if (isCordyceps) {
                type = "cordyceps " + parts[1];
                amountIndex = 2;
            } else if (isFungi) {
                type = "carcass fungi";
                amountIndex = 2;
            }

            int amount;

            // Handle range or single value for amount
            if (parts[amountIndex].contains("-")) {
                String[] range = parts[amountIndex].split("-");
                int min = Integer.parseInt(range[0]);
                int max = Integer.parseInt(range[1]);
                amount = random.nextInt(max - min + 1) + min; // Random amount within the specified range
            } else {
                amount = Integer.parseInt(parts[amountIndex]); // Fixed amount
            }

            // Additional processing for specific types
            if (type.equals("wolf")) {
                map_of_social_predator_packs.put(pack_count++, amount);
                wolf_count += amount;
                map_of_spawns.put("wolf", wolf_count);
            } else if (type.startsWith("bear")) {
                bear_count++;
                String bear_type = "bear" + bear_count;
                total_bear_amount += amount;

                // Parses and stores territory information if available
                if (parts.length > 2 + amountIndex) {
                    int x = Integer.parseInt(String.valueOf(parts[2 + amountIndex].charAt(1)));
                    int y = Integer.parseInt(String.valueOf(parts[2 + amountIndex].charAt(3)));
                    Location territory = new Location(x, y);
                    map_of_bear_territories.put(bear_type, territory);
                }

                map_of_spawns.put("bear", total_bear_amount);
            } else {
                // Updating the map for each type
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

    public Map<Integer, Integer> getMap_of_social_predator_packs(){
        return map_of_social_predator_packs;
    }
}


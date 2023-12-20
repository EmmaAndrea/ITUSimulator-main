package ourcode.Setup;

import itumulator.world.Location;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * The InputReader class is responsible for reading and processing input data from a file.
 * It handles reading the world size, spawn data, and territories for entities in the simulation.
 * The class uses FileReader and BufferedReader to read the input file and stores the data in various maps.
 */
public class InputReader {
    // FileReader to read from the input file.
    FileReader fileReader;

    // BufferedReader to buffer the file input.
    BufferedReader bufferedReader;

    // Map storing the count of each type of spawn read from the input file.
    Map<String, Integer> map_of_spawns;

    // List of lines read from the input file.
    private final List<String> lines;

    // Map storing the locations of bear territories.
    private Map<String, Location> map_of_bear_territories;

    // Map storing the pack information for social predators like wolves.
    private Map<Integer, Integer> map_of_social_predator_packs;

    /**
     * Constructor for InputReader. Initializes the FileReader and BufferedReader
     * to read from the specified file path. It also initializes maps and lists used
     * to store the read data.
     *
     * @param filePath Path to the input file.
     * @throws IOException If an I/O error occurs.
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
        }
    }

    /**
     * Reads the first line of the input file to determine the size of the simulation world.
     *
     * @return The size of the simulation world as an integer.
     */
    public int readWorldSize() {
        String line = lines.get(0).trim();
        return Integer.parseInt(line);
    }

    /**
     * Parses the spawn data from the input file. This method identifies the type
     * and amount of each spawn and updates maps accordingly, including bear territories
     * and social predator packs.
     */
    public void readSpawns() {
        int bear_count = 0; // Counter for the number of bear spawns.
        int total_bear_amount = 0; // Total count of bears to be spawned.
        Random random = new Random(); // Random object for generating numbers.
        int pack_count = 0; // Counter for the number of wolf packs.
        int wolf_count = 0; // Total count of wolves to be spawned.

        // Loop through each line in the input file after the first line (world size).
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(" "); // Split each line into parts.
            boolean isCordyceps = parts[0].equalsIgnoreCase("cordyceps"); // Check if spawn is cordyceps-infected.
            boolean isFungi = parts.length > 1 && parts[1].equalsIgnoreCase("fungi"); // Check if spawn is a carcass with fungi.
            String type = parts[0].toLowerCase(); // The type of spawn (e.g., bear, wolf).

            // Special handling for dinosaur eggs.
            if (type.equals("dinosaur")) {
                if (parts.length > 1 && parts[1].equals("egg")) {
                    type = "dinosaur egg";
                }
            }

            int amountIndex = type.equals("dinosaur egg") ? 2 : 1; // Determine the index of the amount in the line.

            // Adjust type for cordyceps-infected or fungi entities.
            if (isCordyceps) {
                type = "cordyceps " + parts[1];
                amountIndex = 2;
            } else if (isFungi) {
                type = "carcass fungi";
                amountIndex = 2;
            }

            int amount; // The amount of the spawn type.

            // Handle range (e.g., "3-5") or single value for the amount.
            if (parts[amountIndex].contains("-")) {
                String[] range = parts[amountIndex].split("-");
                int min = Integer.parseInt(range[0]);
                int max = Integer.parseInt(range[1]);
                amount = random.nextInt(max - min + 1) + min; // Random amount within the specified range.
            } else {
                amount = Integer.parseInt(parts[amountIndex]); // Fixed amount as specified.
            }

            // Processing for wolves: maintaining pack count and total wolf count.
            if (type.equals("wolf")) {
                map_of_social_predator_packs.put(pack_count, amount);
                pack_count++;
                wolf_count += amount;
                map_of_spawns.put("wolf", wolf_count); // Update wolf count in the spawn map.
            }
            // Processing for bears: keeping track of each bear spawn and their territories.
            else if (type.startsWith("bear")) {
                bear_count++;
                String bear_type = "bear" + bear_count; // Unique identifier for each bear spawn.
                total_bear_amount += amount; // Update total bear count.

                // Parse and store territory information if provided.
                if (parts.length > 2) {
                    String[] numbers = parts[2].split(",");
                    String xx = numbers[0].substring(1);
                    String yy = numbers[1].substring(0, numbers[1].length()-1);

                    int x = Integer.parseInt(xx);
                    int y = Integer.parseInt(yy);
                    Location territory = new Location(x, y);
                    map_of_bear_territories.put(bear_type, territory); // Map bear type to its territory location.
                }

                map_of_spawns.put("bear", total_bear_amount); // Update bear count in the spawn map.
            }
            // General processing for all other types.
            else {
                map_of_spawns.put(type, map_of_spawns.getOrDefault(type, 0) + amount); // Update count for this type in the spawn map.
            }
        }
    }

    /**
     * Retrieves the amount of a specified type of entity to be spawned.
     *
     * @param key The key representing the type of entity.
     * @return The amount of the specified entity type to be spawned.
     */
    public int getAmount(String key) {
        return map_of_spawns.getOrDefault(key, 0);
    }

    /**
     * Retrieves the map containing bear territory information.
     *
     * @return A map associating bear identifiers with their territory locations.
     */
    public Map<String, Location> getMapOfBearTerritories() {
        return map_of_bear_territories;
    }

    /**
     * Retrieves the territory location for a specified bear.
     *
     * @param bear The identifier of the bear whose territory is to be retrieved.
     * @return The Location object representing the bear's territory.
     */
    public Location getTerritory(String bear){
        return map_of_bear_territories.get(bear);
    }

    /**
     * Retrieves the map containing information about social predator packs.
     *
     * @return A map associating pack identifiers with their member count.
     */
    public Map<Integer, Integer> getMap_of_social_predator_packs(){
        return map_of_social_predator_packs;
    }
}


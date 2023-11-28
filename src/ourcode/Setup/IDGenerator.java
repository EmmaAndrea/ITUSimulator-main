package ourcode.Setup;

import itumulator.world.Location;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.Organism;

import java.util.*;

/**
 * The IDGenerator class generates a unique ID and adds it to the list of IDs.
 * The hashmaps allow us to identify organisms and burrows based on loaction or id.
 * Useful for distinguishing organsims in the World.
 */
public class IDGenerator {
    private HashSet<Integer> IDs;

    private int ID;
    protected HashMap<Location, Integer> map_location_to_id;
    protected HashMap<Integer, Organism> map_id_to_organism;
    protected HashMap<Integer, Burrow> map_id_to_burrow;

    /**
     * Instantiates the list of IDs, and hashmaps to obtain id and corresponding organism from location
     */
    public IDGenerator() {
        IDs = new HashSet<>();
        map_location_to_id = new HashMap<>();
        map_id_to_organism = new HashMap<>();
        map_id_to_burrow = new HashMap<>();
    }

    /**
     * Generates the ID.
     * Adds it to the list of IDs.
     * Returns a string of the generated ID.
     * Throws exception if capacity of IDs is reached (>9999).
     */
    public int getID() {
        Random random = new Random();
        ID = random.nextInt(9999);
        while(IDs.contains(ID)){
            ID = random.nextInt(9999);
        }
        return ID;
    }

    /**
     * Checks if the given ID is already in the list of IDs.
     */
    public boolean isIDTaken(String ID) {
        return IDs.contains(ID);
    }

    /**
     * The following methods add an organism or burrow to the corresponding maps
     * They also provide get methods for various scenarios
     * @param location
     * @param id
     * //@param organism
     * //@param burrow
     */
    public void addLocationToIdMap(Location location, int id) {
        map_location_to_id.put(location, id);
    }

    public Map<Location, Integer> getMapLocationToId() {
        return map_location_to_id;
    }

    public int getId(Location location) {
        return map_location_to_id.get(location);
    }

    public void addAnimalToIdMap(int id, Organism organism) {
        map_id_to_organism.put(id, organism);
    }

    public Organism getOrganism(int id) {
        return map_id_to_organism.get(id);
    }

    public Organism getOrganism(Location location) {
        return map_id_to_organism.get(map_location_to_id.get(location));
    }

    public void addBurrowToIdMap(int id, Burrow burrow){
        map_id_to_burrow.put(id, burrow);
    }
}


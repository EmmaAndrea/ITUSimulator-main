package ourcode.Setup;

import itumulator.world.Location;
import ourcode.Obstacles.Burrow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * The IDGenerator class generates a unique ID and adds it to the list of IDs.
 * The hashmaps allow us to identify organisms and burrows based on location or id.
 * Useful for distinguishing organisms in the World.
 */
public class IDGenerator {
    private final HashSet<Integer> IDs;

    private int ID;
    protected HashMap<Location, Integer> map_location_to_id;
    protected HashMap<Integer, Entity> map_id_to_entity;
    protected HashMap<Integer, Burrow> map_id_to_burrow;

    protected HashMap<Location, Burrow> map_location_to_burrow;

    protected ArrayList<Location> locations_of_burrows;


    /**
     * Instantiates the list of IDs, and hashmaps to obtain id and corresponding organism from location
     */
    public IDGenerator() {
        IDs = new HashSet<>();
        map_location_to_id = new HashMap<>();
        map_id_to_entity = new HashMap<>();
        map_location_to_burrow = new HashMap<>();
        map_id_to_burrow = new HashMap<>();
        locations_of_burrows = new ArrayList<>();
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
     * The following methods add an organism or burrow to the corresponding maps
     * They also provide get methods for various scenarios
     */
    public void addLocationToIdMap(Location location, int id) {
        map_location_to_id.put(location, id);
    }

    public void addEntityToIdMap(int id, Entity entity) {
        map_id_to_entity.put(id, entity);
    }

    public Entity getEntity(Location location) {
        return map_id_to_entity.get(map_location_to_id.get(location));
    }

    public void addBurrowToLocationMap(Location location, Burrow burrow){
        map_location_to_burrow.put(location, burrow);
        locations_of_burrows.add(location);

    }

    public Burrow getBurrow(Location location){
        return map_location_to_burrow.get(location);
    }

    public ArrayList<Location> getLocationOfBurrows(){
        return locations_of_burrows;
    }

}

// burrow to id, burrow to loaction, location to burrow, id to burrow
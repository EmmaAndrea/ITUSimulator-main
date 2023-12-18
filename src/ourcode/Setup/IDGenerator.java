package ourcode.Setup;

import itumulator.world.Location;
import ourcode.Obstacles.Burrow;
import ourcode.Obstacles.Habitat;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * The IDGenerator class generates unique IDs for entities in the simulation.
 * It maintains hashmaps to associate entities and habitats with their locations and IDs,
 * enabling efficient tracking and retrieval of these objects within the simulation world.
 */
public class IDGenerator {
    // HashSet storing all generated IDs to ensure uniqueness.
    private final HashSet<Integer> IDs;

    // Variable for storing the latest generated ID.
    private int ID;

    // HashMap linking locations in the simulation world to entity IDs.
    protected HashMap<Location, Integer> map_location_to_id;

    // HashMap linking entity IDs to their respective Entity objects.
    protected HashMap<Integer, Entity> map_id_to_entity;

    // HashMap linking entity IDs to their respective Burrow objects.
    protected HashMap<Integer, Burrow> map_id_to_burrow;

    // HashMap linking locations to Habitat objects.
    protected HashMap<Location, Habitat> map_location_to_habitat;

    // ArrayList storing locations of all habitats in the simulation.
    protected ArrayList<Location> locations_of_habitats;

    // HashMap linking locations to Grass objects.
    protected HashMap<Location, Grass> map_location_to_grass;

    // HashMap linking entity IDs to their respective Grass objects.
    protected HashMap<Integer, Grass> map_id_to_grass;

    /**
     * Constructor for IDGenerator. Initializes internal data structures used for
     * managing IDs and their associations with entities and locations.
     */
    public IDGenerator() {
        IDs = new HashSet<>();
        map_location_to_id = new HashMap<>();
        map_id_to_entity = new HashMap<>();
        map_location_to_habitat = new HashMap<>();
        map_id_to_burrow = new HashMap<>();
        locations_of_habitats = new ArrayList<>();
        map_id_to_grass = new HashMap<>();
        map_location_to_grass = new HashMap<>();
    }

    /**
     * Generates a unique ID for entities in the simulation.
     * Ensures the uniqueness by checking against existing IDs.
     *
     * @return The generated unique ID.
     * @throws RuntimeException if the ID capacity is exceeded.
     */
    public int generateID() {
        Random random = new Random();
        ID = random.nextInt(9999);
        while(IDs.contains(ID)){
            ID = random.nextInt(9999);
        }
        return ID;
    }

    /**
     * Adds a mapping between a location and an entity's ID.
     *
     * @param location The location of the entity.
     * @param id The unique ID of the entity.
     */
    public void addLocationToIdMap(Location location, int id) {
        map_location_to_id.put(location, id);

    }

    /**
     * Adds a mapping between an entity's ID and the entity itself.
     *
     * @param id The unique ID of the entity.
     * @param entity The entity associated with the ID.
     */
    public void addEntityToIdMap(int id, Entity entity) {
        map_id_to_entity.put(id, entity);
    }

    /**
     * Retrieves an entity based on its location.
     *
     * @param location The location of the entity.
     * @return The entity located at the specified location.
     */
    public Entity getEntity(Location location) {
        return map_id_to_entity.get(map_location_to_id.get(location));
    }

    /**
     * Adds a mapping between a location and a habitat.
     *
     * @param location The location of the habitat.
     * @param habitat The habitat at the specified location.
     */
    public void addBurrowToLocationMap(Location location, Habitat habitat) {
        map_location_to_habitat.put(location, habitat);
        locations_of_habitats.add(location);

    }

    /**
     * Adds a mapping between a location and a grass object.
     *
     * @param location The location of the grass.
     * @param grass The grass object at the specified location.
     */
    public void addGrassToLocationMap(Location location, Grass grass) {
        map_location_to_grass.put(location, grass);
    }
}
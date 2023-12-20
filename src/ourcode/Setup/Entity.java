package ourcode.Setup;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.Random;

/**
 * Represents an abstract entity in a simulated world. An entity can be uniquely identified by its type and ID.
 * This class serves as a base for all objects in the world, providing common properties and behaviors
 * that are shared across different types of entities.
 */
public abstract class Entity implements Actor {
    protected String type;  // The type (subclass) of the entity.
    protected int age;  // The life counter for the entity, used for aging and lifecycle management.
    protected int max_age;  // The maximum age the entity can reach before dying.
    protected IDGenerator id_generator;  // ID generator for creating a unique ID for the entity.
    protected int id;  // The unique identifier of the entity.
    protected boolean hasBeenKilled;  // Flag to check if the entity has been killed.

    /**
     * Constructor for an Entity. Initializes the entity with an ID, sets its age to 1,
     * and marks it as not killed by default.
     *
     * @param original_id_generator The IDGenerator instance used for assigning a unique ID.
     */
    public Entity(IDGenerator original_id_generator) {
        id_generator = original_id_generator;
        id = id_generator.generateID();
        age = 1;
        hasBeenKilled = false;
        type = "";
    }

    /**
     * Defines the basic action of an entity in each simulation step, primarily aging.
     *
     * @param world The simulation world in which the entity exists.
     */
    public void act(World world) {
        age++;
    }

    /**
     * Spawns the entity in a random unoccupied location within the simulation world.
     *
     * @param world The simulation world where the entity is to be spawned.
     */
    public void spawn(World world) {
        Location location = getRandomLocation(world); // Finds a random location.

        // Handles whether a blocking or non-blocking entity is to be spawned into an occupied location.
        while ((this instanceof NonBlocking && world.containsNonBlocking(location)) ||
                (!(this instanceof NonBlocking) && !world.isTileEmpty(location))) {
            location = getRandomLocation(world);
        }

        world.setTile(location, this); // Spawn entity into this empty location.
        id_generator.addLocationToIdMap(location, id);
        id_generator.addEntityToIdMap(id, this);
    }

    /**
     * Generates and returns a random location within the simulation world.
     *
     * @param world The simulation world to generate a location in.
     * @return A random location within the world.
     */
    protected Location getRandomLocation(World world) {
        Random random = new Random();

        int randomX = random.nextInt(world.getSize());
        int randomY = random.nextInt(world.getSize());

        return new Location(randomX, randomY);
    }

    /**
     * Retrieves the type of the entity.
     *
     * @return A string representing the type of the entity.
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieves the unique ID of the entity.
     *
     * @return The unique identification number of the entity.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the age of the entity.
     * This method is used to manually adjust the entity's age.
     *
     * @param age The new age to be set for the entity.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Retrieves the current age of the entity.
     * This method is useful for determining the lifecycle stage of the entity.
     *
     * @return The current age of the entity.
     */
    public int getAge() {
        return age;
    }

    /**
     * Deletes everything on a given tile in the simulation world.
     * This method is used to clear a tile of all entities, both blocking and non-blocking.
     *
     * @param world The simulation world in which the tile is located.
     * @param location The location of the tile to be cleared.
     */
    public void deleteEverythingOnTile(World world, Location location) {
        if (world.containsNonBlocking(location)) {
            world.delete(world.getNonBlocking(location));
        }
        if (!world.isTileEmpty(location)) {
            world.delete(world.getTile(location));
        }
    }
}
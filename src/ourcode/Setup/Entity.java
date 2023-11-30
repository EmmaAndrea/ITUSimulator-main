package ourcode.Setup;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Setup.IDGenerator;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Organism class gives the abstraction of the living. For a world to include many different lifeforms,
 * there has to be a generalization for living creatures.
 * The organism class will help provide the utilities necessary to visualize a certain creature.
 */
public abstract class Entity {
    // Type (subclass) of entity.
    protected String type;

    // Life counter for the entity. Used for when to die or bree etc.
    protected int age;

    // Max age for the entity. Used for when to die.
    protected int max_age;

    // ID generator useful for distinction between entities.
    protected IDGenerator id_generator;

    // Represents the generated ID.
    protected int id;

    // Boolean to check if an entity has been killed
    protected boolean hasBeenKilled;

    /**
     * Constructor for an Organism, the parent class for all life on the world.
     * Needs an IDGenerator and a type of organism.
     */
    public Entity(IDGenerator original_id_generator) {
        id_generator = original_id_generator;
        id = id_generator.getID();
        age = 1;
        hasBeenKilled = false;
    }

    /**
     * Spawns an organism in a random unoccupied location.
     */
    public void spawn(World world) {
        Location location = getRandomLocation(world); // Finds a random location.

        while (!world.isTileEmpty(location)) { // If it's not empty, find a new random location.
            location = getRandomLocation(world);
        }

        world.setTile(location, this); // If it's empty, spawn organism into this location.

        id_generator.addLocationToIdMap(location, id);
        id_generator.addAnimalToIdMap(id, this);
    }

    /**
     * Returns a random location.
     */
    protected Location getRandomLocation(World world) {
        Random random = new Random();

        // generates random x and y coordinates
        int randomX = random.nextInt(world.getSize());
        int randomY = random.nextInt(world.getSize());

        return new Location(randomX, randomY);
    }

    /**
     * Returns a String describing the type of entity.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the id number
     */
    public int getId() {
        return id;
    }

    /**
     * Returns 0 if it is currently night.
     * Else, returns how many steps until it is night.
     */
    public int timeToNight(World world){
        if (world.getCurrentTime() % 20 >= 10){
            return 0;
        } else {
            return 10 - world.getCurrentTime() % 20;
        }
    }

}

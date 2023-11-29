package ourcode.Organism;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Setup.IDGenerator;

import java.util.Random;

/**
 * The Organism class gives the abstraction of the living. For a world to include many different lifeforms,
 * there has to be a generalization for living creatures.
 * The organism class will help provide the utilities necessary to visualize a certain creature.
 */
public abstract class Organism implements Actor {
    // Type (subclass) of organism
    protected String type;

    // Life counter for the organism. Used for when to die or breed.
    protected int age;

    // Max age for the organism. Used for when to die.
    protected int max_age;

    // ID generator useful for distinction between organism.
    protected IDGenerator id_generator;

    // Represents the generated ID.
    protected int id;

    // Represents how much hunger to deduct when particular organism is eaten.
    protected int nutritional_value;


    /**
     * Constructor for an Organism, the parent class for all life on the world.
     * Needs an IDGenerator and a type of organism.
     */
    public Organism(IDGenerator original_id_generator) {
        id_generator = original_id_generator;
        id = id_generator.getID();
        age = 1;
        nutritional_value = 2;
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
     * The general act method, calling both animalAct() and plantAct()
     */
    public void act(World world) {
        age++;
        // An organism can die of old age.
        if (age > max_age) {
            world.delete(this);
        } else {
            // Checks if the organism still should be alive after running act method.
            // before: world.getEntities().containsKey(this)
            if (animalAct(world)) {
                // then run the plant methods
                plantAct(world);
            } else {
                //delete the animal if animalAct returns false
                world.delete(this);
            }
        }
    }

    /**
     * An act method for animals. The animals increases their age by 1 for each act by calling the 'ageIncrease()' method
     */
    public void animalAct(World world) {
    }

    /**
     * An act method for plants. Plants will have their age increased by 1 calling the 'ageIncrease()' method
     */
    public void plantAct(World world) {

    }

    /**
     * Returns a String describing the type of organism.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns an Integer describing how long the organism has been on the world.
     */
    public int getAge() {
        return age;
    }

    /**
     *Returns the nutritional value of the organism, ergo how much hunger it satisfies when eaten.
     */
    public int getNutritionalValue() {
        return nutritional_value;
    }


    /**
     * Returns the age threshold.
     */
    public int getMaxAge() {
        return max_age;
    }

    /**
     * Returns the id number
     */
    public int getId() {
        return id;
    }

}

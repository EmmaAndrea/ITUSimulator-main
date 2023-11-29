package ourcode.Organism;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
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

    // Boolean to check if an organism has been killed
    protected boolean hasBeenKilled;


    /**
     * Constructor for an Organism, the parent class for all life on the world.
     * Needs an IDGenerator and a type of organism.
     */
    public Organism(IDGenerator original_id_generator) {
        id_generator = original_id_generator;
        id = id_generator.getID();
        age = 1;
        nutritional_value = 2;
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
     * An act method for animals. The animals increase their age by 1 for each act by calling the 'ageIncrease()' method
     */


    public boolean animalAct(World world) {
        return true;
    }


    /**
     * An act method for plants. Plants will have their age increased by 1 calling the 'proceedAge()' method
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
     *Returns the nutritional value of the organism, ergo how much hunger it satisfies when eaten.
     */
    public int getNutritionalValue() {
        return nutritional_value;
    }

    /**
     * Returns the id number
     */
    public int getId() {
        return id;
    }

    /**
     * Checks surrounding locations within a radius of one.
     * Returns a 'random' location
     */
    public Location getRandomSurroundingFreeLocation(World world) {
        // Retrieve current location
        Location current_location = world.getLocation(this);

        // Makes list of possible spawn locations (locations with no blocking elements).
        ArrayList<Location> possible_spawn_locations = new ArrayList<>();
        for (Location surroundingTile : world.getSurroundingTiles(current_location, 1)) {
            if (world.isTileEmpty(surroundingTile)) {
                possible_spawn_locations.add(surroundingTile);
            }
        }

        // Removes itself from possible locations to spawn.
        possible_spawn_locations.remove(world.getLocation(this));

        // Return null value if there is not empty location (to be used in if statement in larger method to check)
        if (possible_spawn_locations.isEmpty()) {
            return null;
        }

        // Finds a random index in this list of locations.
        Random random = new Random();
        int random_index = random.nextInt(0, possible_spawn_locations.size());

        return possible_spawn_locations.get(random_index);
    }

}

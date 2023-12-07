package ourcode.Organism;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Wolf;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a generic organism in a simulation environment.
 * This abstract class serves as a base for all life forms in the simulated world,
 * providing common properties and behaviors that are shared across different types of organisms.
 */
public abstract class Organism extends Entity implements Actor {
    // Represents how much hunger to deduct when particular organism is eaten.
    protected int nutritional_value;

    protected int trophic_level;

    private final ReentrantLock lock;

    /**
     * Constructor for Organism, initializing common attributes for all organisms in the world.
     * @param original_id_generator The IDGenerator used for assigning unique IDs to the organism.
     */
    public Organism(IDGenerator original_id_generator) {
        super(original_id_generator);
        nutritional_value = 2;
        lock = new ReentrantLock();
        trophic_level = 0;
    }

    /**
     * General act method, invoking both animalAct() and plantAct().
     * Handles the aging process and checks for the organism's survival post-action.
     * @param world The simulation world where the organism exists.
     */
    public void act(World world) {
        age++;

        // An organism can die of old age.
        if (age > max_age) {
            world.delete(this);
        } else {
            lock.lock();
                // Checks if the organism still should be alive after running act method.
                // before: world.getEntities().containsKey(this)
                if (animalAct(world)) {
                    // then run the plant methods
                    plantAct(world);
                } else {
                    //delete the animal if animalAct returns false
                    System.out.println(type + " died");
                    if (this instanceof Wolf wolf) wolf.deleteMe(world);
                    else world.delete(this);
                }
            lock.unlock();
        }
    }

    /**
     * Represents the action an animal takes during a simulation step.
     * This method should be overridden by subclasses to define specific animal behaviors.
     * @param world The simulation world where the animal exists.
     * @return true if the animal successfully completes its action, false otherwise.
     */
    public boolean animalAct(World world) {
        return true;
    }


    /**
     * An act method for plants, potentially involving growth or other plant-specific behaviors.
     * @param world The simulation world where the plant exists.
     */
    public void plantAct(World world) {
    }

    /**
     * Returns the nutritional value of the organism, indicating how much hunger it satisfies when eaten.
     * @return The nutritional value of the organism.
     */
    public int getNutritionalValue() {
        return nutritional_value;
    }

    /**
     * Checks surrounding locations within a radius of one and returns a list of free locations.
     * @param world The simulation world to check surrounding locations in.
     * @return A list of free locations around the organism, or null if none are available.
     */
    public ArrayList<Location> getSurroundingFreeLocation(World world) {
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

        // Return null value if there is no empty location (to be used in if statement in larger method to check).
        if (possible_spawn_locations.isEmpty()) {
            return null;
        }

        return possible_spawn_locations;
    }

    /**
     * Retrieves a random location from the list of surrounding free locations in the given world.
     * This method first checks if there are any free locations around in the world. If there are,
     * it selects one at random and returns it. If there are no free locations available,
     * it returns null.
     *
     * @param world the World object representing the environment where the free locations are to be found.
     * @return a randomly selected Location from the list of free locations if available, otherwise null.
     */
    public Location getRandomMoveLocation(World world){
        // Finds a random index in this list of locations.
        Random random = new Random();
        //
        if (getSurroundingFreeLocation(world) != null) {
            //
            int random_index = random.nextInt(0, getSurroundingFreeLocation(world).size());
            //
            return getSurroundingFreeLocation(world).get(random_index);
        } else return null;
    }

    /**
     * Searches for grass in the surrounding tiles of an organism's current location.
     * This method checks each surrounding tile to see if it contains grass. If grass
     * is found on any of these tiles, the location of the grass is returned.
     *
     * @param world The simulation world in which the organism and grass exist.
     * @return The Location of the grass if found in the surrounding tiles, otherwise null.
     */
    public Location getGrassLocation(World world) {
        // Check if there are any surrounding free locations in the world.
        if (getSurroundingFreeLocation(world) != null) {
            // Iterate through each surrounding location.
            for (Location location : getSurroundingFreeLocation(world)) {
                // Check if the location contains non-blocking entities.
                if (world.containsNonBlocking(location)) {
                    // Return the location if it contains Grass.
                    if (world.getNonBlocking(location) instanceof Grass) {
                        return location;
                    }
                }
            }
            // Return null if no grass is found.
            return null;
        }
        // Return null if there are no surrounding free locations.
        return null;
    }

    /**
     * Returns the trophic level of the entity.
     * This integer describes the entity's place in the food chain.
     * @return trophic level.
     */
    public int getTrophicLevel() {
        return trophic_level;
    }
}

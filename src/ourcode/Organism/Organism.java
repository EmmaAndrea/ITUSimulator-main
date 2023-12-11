package ourcode.Organism;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a generic organism in a simulation environment.
 * This abstract class serves as a base for all life forms in the simulated world,
 * providing common properties and behaviors that are shared across different types of organisms.
 */
public abstract class Organism extends Entity implements Actor {
    // Represents how much hunger to deduct when particular organism is eaten.
    protected int nutritional_value;

    protected int trophic_level = 0;

    /**
     * Constructor for Organism, initializing common attributes for all organisms in the world.
     * @param original_id_generator The IDGenerator used for assigning unique IDs to the organism.
     */
    public Organism(IDGenerator original_id_generator) {
        super(original_id_generator);
        nutritional_value = 2;
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
    }

    /**
     * Checks surrounding locations within a radius of one and returns a list of free locations.
     * @param world The simulation world to check surrounding locations in.
     * @return A list of free locations around the organism, or null if none are available.
     */
    public ArrayList<Location> getSurroundingFreeLocations(World world) {
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
        if (getSurroundingFreeLocations(world) != null) {
            //
            int random_index = random.nextInt(0, getSurroundingFreeLocations(world).size());
            //
            return getSurroundingFreeLocations(world).get(random_index);
        } else return null;
    }

    /**
     * Returns the trophic level of the entity.
     * This integer describes the entity's place in the food chain.
     * @return trophic level.
     */
    public int getTrophicLevel() {
        return trophic_level;
    }

    /**
     * Returns the nutritional value of the organism, indicating how much hunger it satisfies when eaten.
     * @return The nutritional value of the organism.
     */
    public int getNutritionalValue() {
        return nutritional_value;
    }
}

package ourcode.Organism;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Organism class gives the abstraction of the living. For a world to include many different lifeforms,
 * there has to be a generalization for living creatures.
 * The organism class will help provide the utilities necessary to visualize a certain creature.
 */
public abstract class Organism extends Entity implements Actor {
    // Represents how much hunger to deduct when particular organism is eaten.
    protected int nutritional_value;

    /**
     * Constructor for an Organism, the parent class for all life on the world.
     * Needs an IDGenerator and a type of organism.
     */
    public Organism(IDGenerator original_id_generator) {
        super(original_id_generator);
        nutritional_value = 2;
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
     *Returns the nutritional value of the organism, ergo how much hunger it satisfies when eaten.
     */
    public int getNutritionalValue() {
        return nutritional_value;
    }

    /**
     * Checks surrounding locations within a radius of one.
     * Returns a list of free locations around the organism
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
     * uses list of free locations to find a random location to go to
     */
    public Location getRandomMoveLocation(World world){
        // Finds a random index in this list of locations.
        Random random = new Random();
        if (getSurroundingFreeLocation(world) != null) {
            int random_index = random.nextInt(0, getSurroundingFreeLocation(world).size());

            return getSurroundingFreeLocation(world).get(random_index);

        } else return null;
    }

    /**
     * Finds out if there is grass on an organisms surrounding tile
     * Returns the location of the grass if there is one
     * Otherwise null
     */
    public Location getGrassLocation(World world) {
        if (getSurroundingFreeLocation(world) != null) {
            for (Location location : getSurroundingFreeLocation(world)) {
                if (world.containsNonBlocking(location)) {
                    if ((world.getNonBlocking(location) instanceof Grass)) {
                        return location;
                    }
                }
            } return null;
        } return null;
    }
}

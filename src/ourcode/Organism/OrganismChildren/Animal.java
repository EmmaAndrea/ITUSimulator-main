package ourcode.Organism.OrganismChildren;

import itumulator.world.World;
import itumulator.world.Location;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

/**
 * The Animal class gives the abstraction of an Animal. An animal inherits from the Organism
 *  class and has the field hunger.
 */
public abstract class Animal extends Organism {
    public int hunger;
    public int max_hunger;

    /**
     * The constructor of an Animal.
     */
    public Animal(IDGenerator original_id_generator, String type) {
        super(original_id_generator, type); // life_counter = 1;
        hunger = 1;
        max_hunger = 1;
    }

    /**
     * Spawns an Animal at a random location.
     */
    public void eat(Organism organism) {
        // Eats another organism and deducts hunger by the nutritional value of the eaten organism.
        hunger -= organism.getNutritionalValue();

        // delete eaten organism from world
        // our code here
    }

    /**
     * Calls the act method from Actor and overrides the method. Increases hunger by 1.
     */
    public void act(World world) {
        super.act(world);

        // Animal gains hunger by 1.
        hunger++;

        // An animal can die of hunger if it is hungrier than its max hunger.
        if (hunger > max_hunger) {
            world.delete(this);
        }

        // Sets the new location of the animal to be the same, if there is nowhere to go
        Location new_location = world.getCurrentLocation();

        // Finds a random free tile.
        for (Location location : world.getEmptySurroundingTiles(world.getCurrentLocation())) {
            new_location = location;
        }
        // Moves animal to this new tile
        world.move(this, new_location);
    }

        /**
         * Gives the ability to breed with a partner, if there exists another one of its kind.
         */
    public void breed(World world) {
        // only if there exists 2 or more rabbits - check entities list?
        // only if the rabbits are 20 steps old (modulo 20, only every 20 steps)
        // only if there is space for one more rabbit
        // check these factors in the act method

        // calls spawn() method from Organism class
        super.spawn(world); // should make new rabbit with new id, but doesn't

    }

    /**
     * A method for creating a new burrow from the Burrow class.
     */
    public void makeBurrow(){

    }

    public int getHunger() {
        return hunger;
    }

    public int getMaxHunger() {
        return max_hunger;
    }

    public void deductHunger(int nutritional_value) {
        hunger -= nutritional_value;
    }
}

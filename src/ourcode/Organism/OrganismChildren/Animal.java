package ourcode.Organism.OrganismChildren;

import itumulator.world.World;
import itumulator.world.Location;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

/**
 * The Animal class gives the abstraction of an Animal. An animal inherits from the Organism
 *  class and has the fields hunger and max hunger.
 *  The animal class controls the movement of an animal
 *  the eat method is here, which gets the correct nutritional value of the organism being eaten
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
     * Finds nutritional value of the object we are standing on
     */
    public int getStandingOnNutritionalValue(World world) {
        // Deducts hunger by the nutritional value of the eaten organism.
        Organism organism = id_generator.getOrganism(world.getLocation(world.getNonBlocking(world.getCurrentLocation())));
        return organism.getNutritionalValue();
    }

    /**
     * A method for giving animals the ability to eat. They decrease their 'hunger' by a certain amount of 'nutritional value'
     *  and the object the animal is standing on will be 'eaten' by calling the 'delete()' method from the World class
     */
    public void eat(World world) {
        hunger -= getStandingOnNutritionalValue(world);
        world.delete(world.getNonBlocking(world.getCurrentLocation()));
    }

    /**
     * Calls the act method from Actor and overrides the method. Increases hunger by 1.
     */
    public void animalAct(World world) {
        super.animalAct(world);

        // Animal gains hunger by 1.
        hunger++;

        // An animal can die of hunger if it is hungrier than its max hunger.
        if (hunger > max_hunger) {
            world.delete(this);
        } else {
            // Sets the new location of the animal to be the same, if there is nowhere to go
            Location new_location = world.getCurrentLocation();

            // Finds a random free tile.
            for (Location location : world.getEmptySurroundingTiles(world.getCurrentLocation())) {
                new_location = location;
            }
            // Moves animal to this new tile
            world.move(this, new_location);

            herbivoreAct(world);
        }
    }

    public void herbivoreAct(World world) {}

    //when food is removed from world, take away hunger from animal

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
    public void makeBurrow() {

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

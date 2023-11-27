package ourcode.Organism.OrganismChildren.AnimalChildren;

import itumulator.world.Location;
import itumulator.world.World;

import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.IDGenerator;

/**
 * The Herbivore class gives the abstraction of the types of animals who are non-meat eaters.
 */

public abstract class Herbivore extends Animal {

    public Herbivore(IDGenerator original_id_generator, String type) {
        super(original_id_generator, type);
    }

    public void spawn(World world) {
        super.spawn(world);
    }


    /**
     * Needs to be adapted so any herbivore can eat any plant!!!
     */
    @Override
    public void act(World world) {
        super.act(world);
        // checks if the hunger of a given Herbivore has reached the limit of hunger, then 'kills' it.
        // the 'else if' checks if a given Herbivore is 'hungry' and will use the 'eat()' method.
        if (getHunger() > getMaxHunger()) {
            world.delete(this);
        } else if (getHunger() >= 2 && world.containsNonBlocking(world.getCurrentLocation())) { // problem here, what if rabbits eats turtle satisfying 3 hunger, the hunger = -1
            Location current_location = world.getCurrentLocation();

            world.delete(world.getNonBlocking(current_location));

            // eat();
        }
    }

    /**
     * Subtracts 2 from hunger.
     * Unfinished method!!! Quick fix.
     */
    /*
    public void eat(Organism organism) {
        deductHunger(organism.getNutritionalValue()); // random int 2 instead of nutritional value
    }
     */
}

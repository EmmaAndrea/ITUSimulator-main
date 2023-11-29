package ourcode.Organism.OrganismChildren.AnimalChildren;

import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.IDGenerator;

/**
 * The Herbivore class gives the abstraction of the types of animals who are non-meat eaters.
 */

public abstract class Herbivore extends Animal {

    public Herbivore(IDGenerator original_id_generator) {
        super(original_id_generator);
    }

    public void spawn(World world) {
        super.spawn(world);
    }


    /**
     * checks if a given Herbivore is hungry enough and will use the 'eat()' method.
     */
    @Override
    public void herbivoreAct(World world) {

        if (world.containsNonBlocking(world.getCurrentLocation()) && hunger >= getStandingOnNutritionalValue(world)) {
            eat(world);
        }
    }
}

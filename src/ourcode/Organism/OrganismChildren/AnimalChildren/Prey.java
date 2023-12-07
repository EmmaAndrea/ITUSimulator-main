package ourcode.Organism.OrganismChildren.AnimalChildren;

import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.IDGenerator;

/**
 * The Prey class provides an abstraction for animals characterized as prey in the ecosystem.
 * These animals primarily have a lower trophic level and exhibit specific behaviors
 * associated with herbivores, such as feeding on grass.
 */
public abstract class Prey extends Animal {

    /**
     * Constructs a Prey instance with a specified ID generator.
     * Initializes the trophic level specific to prey animals.
     *
     * @param original_id_generator The ID generator for the prey.
     */
    public Prey(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator, has_cordyceps);
        trophic_level = 2;
    }

    @Override
    public void act(World world) {
        super.act(world);
    }

    /**
     * Spawns this prey into the world. This method leverages the spawn behavior
     * defined in the Animal superclass, applying any additional rules or logic
     * specific to prey.
     *
     * @param world The world in which the prey is spawned.
     */
    public void spawn(World world) {
        super.spawn(world);
    }


    /**
     * Defines the behavior of a herbivore in each simulation step. Checks if the
     * herbivore is hungry and eats grass if available on its current location.
     *
     * @param world The simulation world in which the herbivore exists.
     */
    /*
    @Override
    public void herbivoreAct(World world) throws Exception {
        if (world.getEntities().containsKey(this) && !being_eaten) {
            // If the herbivore is not currently in a burrow.
            if (!in_hiding) {

                // If the herbivore is standing on a nonblocking tile
                if (world.containsNonBlocking(world.getLocation(this))) {

                    // If the non-blocking at this location is grass.
                    if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {

                        // If the herbivore is hungrier than how full the grass will make it.
                        if (hunger >= getStandingOnNutritionalValue(world)) {
                            eat(world, world.getNonBlocking(world.getLocation(this)));
                        }
                    }
                }

            }
        }
    }
    */
}

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
    /**
     * Defines the standard behavior of a prey during each simulation step.
     *
     * @param world The simulation world in which the prey exists.
     */
    @Override
    public void act(World world) {
        super.act(world);
    }
}

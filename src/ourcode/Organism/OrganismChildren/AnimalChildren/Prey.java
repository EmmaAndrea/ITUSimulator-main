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
}

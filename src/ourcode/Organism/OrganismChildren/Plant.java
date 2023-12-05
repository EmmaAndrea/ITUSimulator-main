package ourcode.Organism.OrganismChildren;

import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

/**
 * Represents an abstract plant entity in a simulated world.
 * This class extends Organism, serving as a foundational class for all types of plant-like entities in the simulation.
 * It defines basic plant behaviors and properties, such as growth, lifecycle stages, and interactions with the environment.
 */
public abstract class Plant extends Organism {

    /**
     * Constructs a new Plant with a unique identifier.
     * Initializes the plant and sets up any necessary properties or states specific to plants.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the plant.
     */
    public Plant(IDGenerator original_id_generator) {
        super(original_id_generator);
        this.trophic_level = 1;
    }

    /**
     * Executes plant-specific actions during a simulation step.
     * This method can be overridden by subclasses to implement behaviors like growth, reproduction, or response to environmental changes.
     *
     * @param world The simulation world in which the plant exists, providing context for its actions.
     */
    @Override
    public void plantAct(World world) {
        super.plantAct(world);
    }
}

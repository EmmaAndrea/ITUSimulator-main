package ourcode.Organism.OrganismChildren;

import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

/**
 * Represents an abstract plant entity in a simulated world.
 * This class extends the 'Organism' class, serving as a foundational class for all types of plant-like entities in the simulation.
 * It provides basic plant behaviors and properties, such as growth, lifecycle stages, and interactions with the environment.
 * Plants occupy the lowest trophic level in the simulation, representing primary producers.
 */
public abstract class Plant extends Organism {

    /**
     * Constructs a new Plant with a unique identifier.
     * Initializes the plant with a specific trophic level indicative of primary producers in ecological systems.
     * Sets up any necessary properties or states specific to plants.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the plant.
     */
    public Plant(IDGenerator original_id_generator) {
        super(original_id_generator);
        this.trophic_level = 1;
    }

    /**
     * Defines the general behavior of a plant in each simulation step.
     * This method can be overridden by subclasses to implement specific plant behaviors.
     *
     * @param world The simulation world where the plant exists.
     */
    @Override
    public void act(World world) {
        super.act(world);
    }
}

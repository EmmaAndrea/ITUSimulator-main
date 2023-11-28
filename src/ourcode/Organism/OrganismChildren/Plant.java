package ourcode.Organism.OrganismChildren;

import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

/**
 * Represents a Plant entity in a simulated world.
 * This abstract class extends Organism, providing a base for all plant-like entities.
 * Has a life counter that tracks the plant's age or stage in its lifecycle.
 */
public abstract class Plant extends Organism {

    public Plant(IDGenerator original_id_generator) {
        super(original_id_generator);
    }

    /**
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void plantAct(World world) {
        super.plantAct(world);
    }
}

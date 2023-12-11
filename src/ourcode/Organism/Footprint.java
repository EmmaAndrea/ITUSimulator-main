package ourcode.Organism;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a footprint in the simulation environment. This class extends 'Entity'
 * and implements 'DynamicDisplayInformationProvider' for graphical representation.
 * Footprints are created as a trace of an animal's movement and disappear after a certain age.
 */
public class Footprint extends Entity implements DynamicDisplayInformationProvider, NonBlocking {

    /**
     * Constructs a Footprint with a unique identifier. Initializes the maximum age
     * after which the footprint will disappear.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the footprint.
     */
    public Footprint(IDGenerator original_id_generator) {
        super(original_id_generator);
        max_age = 2;
    }

    /**
     * Defines the behavior of the footprint in each simulation step. The footprint is removed
     * from the world after reaching its maximum age.
     *
     * @param world The simulation world in which the footprint exists.
     */
    public void act(World world) {
        age++;

        if (age >= max_age) {
            world.delete(this);
        }
    }

    /**
     * Provides the display information for the footprint. This method defines how the footprint
     * is visually represented in the simulation.
     *
     * @return DisplayInformation object containing color and image data for the footprint's representation.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.darkGray, "footprint");
    }
}
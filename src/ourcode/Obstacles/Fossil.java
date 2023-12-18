package ourcode.Obstacles;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a fossil in the simulation environment. Extends the 'Entity' class and
 * implements 'DynamicDisplayInformationProvider' for graphical representation.
 * Fossils are remnants of past organisms that can be discovered in the simulation world.
 */
public class Fossil extends Entity implements DynamicDisplayInformationProvider {

    /**
     * Constructs a Fossil with a unique identifier.
     *
     * @param original_id_generator The IDGenerator instance for generating the unique identifier.
     */
    public Fossil(IDGenerator original_id_generator) {
        super(original_id_generator);
    }

    /**
     * Defines the behavior of the fossil in each simulation step, particularly its longevity in the world.
     *
     * @param world The simulation world in which the fossil exists.
     */
    public void act(World world) {
        super.act(world);

        if (age > 50) {
            world.delete(this);
        }
    }

    /**
     * Provides the display information for the fossil, defining its visual representation in the simulation.
     *
     * @return DisplayInformation object containing color and image data for the fossil's representation.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.red, "fossil");
    }
}

package ourcode.Obstacles;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a cave in the simulation environment. This class extends 'Habitat' and
 * implements 'DynamicDisplayInformationProvider' for graphical representation.
 * Caves serve as a habitat or shelter for various animals within the simulation.
 */
public class Cave extends Habitat implements DynamicDisplayInformationProvider {

    /**
     * Constructs a Cave with a unique identifier. Initializes the type of the habitat as 'cave'.
     *
     * @param id_generator The IDGenerator instance that provides the unique identifier for the cave.
     */
    public Cave(IDGenerator id_generator) {
        super(id_generator);
        type = "cave";
    }

    /**
     * Provides the display information for the cave. This method defines how the cave
     * is visually represented in the simulation.
     *
     * @return DisplayInformation object containing color and image data for the cave's representation.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.cyan, "cave");
    }
}
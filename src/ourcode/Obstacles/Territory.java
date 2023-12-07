package ourcode.Obstacles;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a territory in the simulation environment.
 * This class extends 'Habitat' and is part of the obstacles package, providing a specific type of habitat
 * that can be used to define territories for various entities in the simulation.
 * It also implements 'DynamicDisplayInformationProvider' for graphical representation.
 */
public class Territory extends Habitat implements DynamicDisplayInformationProvider {

    /**
     * Constructs a Territory object with a specific ID generator.
     * Initializes the territory with the given ID generator.
     *
     * @param id_generator The IDGenerator used for generating unique IDs for the territory.
     */
    public Territory (IDGenerator id_generator) {
        super(id_generator);
        type = "territory";
    }

    /**
     * Provides the display information for this territory.
     * This method defines how the territory is visually represented in the simulation.
     *
     * @return DisplayInformation object containing color and image data for the territory's representation.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.red, "bear-territory");
    }
}
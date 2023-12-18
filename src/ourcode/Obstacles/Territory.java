package ourcode.Obstacles;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a territory in the simulation environment. Extends the 'Habitat' class and is part of the obstacles package,
 * providing a specific type of habitat that is used to define territories for various entities within the simulation.
 * Territories can be assigned to different animal species, delineating their living or hunting grounds.
 * This class also implements 'DynamicDisplayInformationProvider' for graphical representation, aiding in the
 * visualization of territories within the simulation.
 */
public class Territory extends Habitat implements DynamicDisplayInformationProvider {

    /**
     * Constructs a Territory object with a specific ID generator.
     * Initializes the territory with the given ID generator, setting up its unique identity within the simulation.
     *
     * @param id_generator The IDGenerator used for generating unique IDs for the territory.
     */
    public Territory (IDGenerator id_generator) {
        super(id_generator);
        type = "territory";
    }

    /**
     * Provides the display information for this territory. This method defines the visual characteristics of the territory
     * in the simulation, which can be used to distinguish it from other environmental elements.
     *
     * @return DisplayInformation object containing color and image data for the territory's representation.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.red, "bear-territory");
    }
}
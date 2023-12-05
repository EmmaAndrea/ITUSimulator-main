package ourcode.Obstacles;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a burrow in the simulation, primarily used as a home for rabbits.
 * This class provides functionalities to manage the rabbits residing in the burrow,
 * and to display the burrow within the simulation's world.
 */
public class Burrow extends Habitat implements DynamicDisplayInformationProvider {
    /**
     * Constructs a new Burrow with a unique identifier.
     * Initializes the list of residents and sets the burrow's type in the simulation world.
     *
     * @param id The unique identifier for the burrow, typically provided by an IDGenerator.
     */
    public Burrow(IDGenerator id) {
        super(id);
        type = "burrow";
    }

    /**
     * Provides display information for the burrow, including its color and representation in the simulation.
     *
     * @return DisplayInformation object containing visual details of the burrow.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.red, "hole");
    }
}

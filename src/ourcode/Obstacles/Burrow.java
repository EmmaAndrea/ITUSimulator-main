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
public class Burrow extends Obstacle implements DynamicDisplayInformationProvider {
    /** List of rabbits which are currently residing in the burrow. */
    List<Rabbit> residents;

    /**
     * Constructs a new Burrow with a unique identifier.
     * Initializes the list of residents and sets the burrow's type in the simulation world.
     *
     * @param id The unique identifier for the burrow, typically provided by an IDGenerator.
     */
    public Burrow(IDGenerator id) {
        super(id);
        type = "burrow";
        residents = new ArrayList<>();
    }

    /**
     * Spawns the burrow in the given world.
     * This method should be called to initialize and place the burrow in the simulation.
     *
     * @param world The world in which the burrow will be placed.
     */
    public void spawn(World world) {
        super.spawn(world);
    }

    /**
     * Adds a rabbit to the list of residents in the burrow.
     *
     * @param rabbit The rabbit to be added as a resident of the burrow.
     */
    public void addResident(Rabbit rabbit){
        residents.add(rabbit);
    }

    /**
     * Retrieves the list of rabbits currently residing in the burrow.
     *
     * @return A list of rabbits residing in the burrow.
     */
    public List<Rabbit> getResidents() {
        return residents;
    }

    /**
     * Provides display information for the burrow, including its color and representation in the simulation.
     *
     * @return DisplayInformation object containing visual details of the burrow.
     */
    @Override
    public DisplayInformation getInformation() {
        if (age >= 9) {
            return new DisplayInformation(Color.red, "hole");
        } else {
            return new DisplayInformation(Color.red, "hole-small");
        }
    }
}

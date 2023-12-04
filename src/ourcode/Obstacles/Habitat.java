package ourcode.Obstacles;

import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class Habitat extends Obstacle {
    /** List of rabbits which are currently residing in the burrow. */
    List<Animal> residents;

    /**
     * Constructs a new habitat with a unique identifier.
     * Initializes the list of residents and sets the burrow's type in the simulation world.
     *
     * @param id The unique identifier for the habitat, typically provided by an IDGenerator.
     */
    public Habitat(IDGenerator id) {
        super(id);
        residents = new ArrayList<>();
    }

    /**
     * Spawns the burrow in the given world.
     * This method should be called to initialize and place the burrow in the simulation.
     *
     * @param world The world in which the habitat will be placed.
     */
    public void spawn(World world) {
        super.spawn(world);
    }

    /**
     * Adds a rabbit to the list of residents in the burrow.
     *
     * @param animal The rabbit to be added as a resident of the burrow.
     */
    public void addResident(Animal animal){
        residents.add(animal);
    }

    /**
     * Retrieves the list of rabbits currently residing in the burrow.
     *
     * @return A list of rabbits residing in the burrow.
     */
    public List<Animal> getResidents() {
        return residents;
    }

}

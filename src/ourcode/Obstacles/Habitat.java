package ourcode.Obstacles;

import itumulator.world.NonBlocking;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract habitat in the simulation environment. Extends the 'Obstacle' class.
 * Habitats provide shelter and living space for various animal entities within the simulation.
 */
public abstract class Habitat extends Entity implements NonBlocking {
    List<Animal> residents; // List of animals which are currently residing in the habitat.

    /**
     * Constructs a new habitat with a unique identifier.
     * Initializes the list of residents and sets the habitat's type in the simulation world.
     *
     * @param id_generator The unique identifier for the habitat, typically provided by an IDGenerator.
     */
    public Habitat(IDGenerator id_generator) {
        super(id_generator);
        residents = new ArrayList<>();
    }

    /**
     * Spawns the habitat in the given world.
     * This method should be called to initialize and place the habitat in the simulation.
     *
     * @param world The world in which the habitat will be placed.
     */
    public void spawn(World world) {
        super.spawn(world);
    }

    /**
     * Adds an animal to the list of residents in the habitat.
     *
     * @param animal The animal to be added as a resident of the habitat.
     */
    public void addResident(Animal animal) {
        residents.add(animal);
    }

    /**
     * Removes an animal from the list of residents in the habitat.
     *
     * @param animal The animal to be removed from the habitat's residency.
     */
    public void removeResident(Animal animal) {
        residents.remove(animal);
    }

    /**
     * Retrieves the list of animals currently residing in the habitat.
     *
     * @return A list of animals residing in the habitat.
     */
    public List<Animal> getResidents() {
        return residents;
    }

}

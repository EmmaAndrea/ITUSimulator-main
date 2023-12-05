package ourcode.Obstacles;

import itumulator.world.NonBlocking;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

/**
 * Represents an abstract obstacle within the simulation environment.
 * This class serves as a base for various types of obstacles that can be encountered in the simulated world.
 * It inherits from the 'NonBlocking' interface from the 'ITUmulator' library, indicating that these obstacles
 * do not block the movement of entities in the simulation.
 */
public abstract class Obstacle extends Entity implements NonBlocking {
    /**
     * Constructs a new Obstacle with a unique identifier provided by an IDGenerator.
     *
     * @param originID The IDGenerator instance that provides the unique identifier for the obstacle.
     */
    public Obstacle(IDGenerator originID) {
        super(originID);
    }
}
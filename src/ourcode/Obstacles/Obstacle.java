package ourcode.Obstacles;

import itumulator.world.NonBlocking;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

/**
 * The Obstacle class provides the abstraction of obstacles in the world. Obstacles inherits from the 'NonBlocking' class
 * from the 'ITUmulator' library.
 */
public abstract class Obstacle extends Entity implements NonBlocking {
    public Obstacle(IDGenerator originID) {
        super(originID);
        id_generator = originID;
    }
}

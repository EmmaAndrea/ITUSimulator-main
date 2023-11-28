package ourcode.Organism.OrganismChildren.PlantChildren;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import ourcode.Organism.OrganismChildren.Plant;
import ourcode.Setup.IDGenerator;

/**
 * Represents a non-blocking plant in a simulated world.
 * This abstract class extends Plant and implements NonBlocking.
 * NonBlockingPlant provides the functionality to spawn in random locations
 * in the world that do not contain other non-blocking entities.
 */
public abstract class NonBlockingPlant extends Plant implements NonBlocking {
    public NonBlockingPlant(IDGenerator original_id_generator, String type) {
        super(original_id_generator, type);
    }

    /**
     * Spawns a non-blocking plant in a random location that does not already contain a non-blocking.
     */
    @Override
    public void spawn(World world) {
        Location location = getRandomLocation(world);

        // If the tile contains a nonblocking, then try again.
        while (world.containsNonBlocking(location)) {
            location = getRandomLocation(world);
        }
        world.setTile(location, this);
    }

    /**
     * Calls super.act.
     */
    @Override
    public void act(World world) {
        super.act(world);
    }
}
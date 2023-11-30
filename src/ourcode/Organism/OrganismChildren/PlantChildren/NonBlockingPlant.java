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
    public NonBlockingPlant(IDGenerator original_id_generator) {
        super(original_id_generator);
    }

    /**
     * Spawns a non-blocking plant in a random location that does not already contain a non-blocking.
     * Adds the nonblocking plant to the id_generator hashmaps.
     */
    @Override
    public void spawn(World world) {
        Location location = getRandomLocation(world);

        // If the tile contains a nonblocking, then try again.
        while (world.containsNonBlocking(location)) {
            location = getRandomLocation(world);
        }
        world.setTile(location, this);

        id_generator.addEntityToIdMap(id, this);
        id_generator.addLocationToIdMap(world.getLocation(this), id);
        // add plant to id list with entities location.
    }

    /**
     * Calls super.act.
     */
    @Override
    public void plantAct(World world) {
        super.plantAct(world);
    }
}
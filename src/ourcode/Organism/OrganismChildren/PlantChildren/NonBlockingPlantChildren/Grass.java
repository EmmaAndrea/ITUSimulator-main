package ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren;

import itumulator.world.World;
import itumulator.world.Location;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlant;
import ourcode.Setup.IDGenerator;

/**
 * Represents a Grass entity in a simulated world.
 * Grass is a type of NonBlockingPlant that has the ability to spread under certain conditions.
 * When the act() is invoked, grass checks its life counter and decides whether to spread or not.
 * Spreading involves creating new Grass objects in adjacent available tiles.
 */
public class Grass extends NonBlockingPlant {

    /**
     * The constructor of a new grass.
     */
    public Grass(IDGenerator original_id_generator) {
        super(original_id_generator, "grass");
        max_age = 200;
    }

    /**
     * Controls what grass does when act() is called from the world.
     * Grass spreads after a given time (lifeCounter).
     */


    @Override
    public void act(World world) {
        super.act(world);

        // After a given amount of steps, the grass will spread
        if (age % 5 == 0) {
            spread(world);
        }
    }

    /**
     * Spawns new grass where there is an available tile.
     */
    public void spread(World world) {
        // Retrieve current location once
        Location currentLocation = world.getLocation(this);

        // Find a suitable location to spread grass
        Location spreadLocation = null;
        for (Location surroundingTile : world.getSurroundingTiles(currentLocation, 1)) {
            if (!world.containsNonBlocking(surroundingTile)) {
                spreadLocation = surroundingTile;
                break;
            }
        }

        // If a suitable location is found, spread the grass
        if (spreadLocation != null) {
            Grass spreadedgrass = new Grass(id_generator); // right now, spread grass has same id as original grass
            world.setTile(spreadLocation, spreadedgrass);
        }
    }
}

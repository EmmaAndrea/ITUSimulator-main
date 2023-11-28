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
        super(original_id_generator);
        type = "grass";
        max_age = 30;
        nutritional_value = 4;
    }

    /**
     * Controls what grass does when act() is called from the world.
     * Grass spreads every fifth step.
     */
    @Override
    public void plantAct(World world) {
        super.plantAct(world);

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
        Grass spreaded_grass = null;
        if (spreadLocation != null) {
            spreaded_grass = new Grass(id_generator);
            world.setTile(spreadLocation, spreaded_grass);
            id_generator.addAnimalToIdMap(spreaded_grass.getId(), spreaded_grass);
            id_generator.addLocationToIdMap(spreadLocation, spreaded_grass.getId());
        }

    }
}

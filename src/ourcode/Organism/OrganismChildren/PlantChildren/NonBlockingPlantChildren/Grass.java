package ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import itumulator.world.Location;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlant;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a Grass entity in a simulated world.
 * Grass is a type of NonBlockingPlant that can spread to adjacent tiles under certain conditions,
 * contributing to the dynamic environment of the simulation.
 */
public class Grass extends NonBlockingPlant implements DynamicDisplayInformationProvider {

    /**
     * Constructs a new Grass entity with a unique identifier and initializes its properties.
     * Sets the type as 'grass', defines its maximum age and nutritional value.
     *
     * @param original_id_generator The IDGenerator instance providing the unique identifier for the grass.
     */
    public Grass(IDGenerator original_id_generator) {
        super(original_id_generator);
        type = "grass";
        max_age = 30;
        nutritional_value = 4;
    }

    /**
     * Defines the behavior of grass in each simulation step. This includes aging and potentially spreading
     * to adjacent tiles every predefined number of steps.
     *
     * @param world The simulation world in which the grass exists.
     */
    @Override
    public void act(World world) {
        super.act(world);

        if (age >= max_age) {
            world.delete(this);
            return;
        }

        // After a given amount of steps, the grass will spread
        if (age % 8 == 0) {
            spread(world);
        }
    }

    /**
     * Spreads the grass to an adjacent available tile. This method is called as part of the plant's lifecycle,
     * allowing the grass to expand its presence in the simulation world.
     *
     * @param world The simulation world where the grass spreading occurs.
     */
    public void spread(World world) {
        // Retrieve current location once.
        Location current_location = world.getLocation(this);

        // Find a suitable location to spread grass.
        Location spreadLocation = null;
        for (Location surroundingTile : world.getSurroundingTiles(current_location, 1)) {
            if (!world.containsNonBlocking(surroundingTile)) {
                spreadLocation = surroundingTile;
                break;
            }
        }

        // If a suitable location is found, spread the grass.
        if (spreadLocation != null) {
            Grass spreaded_grass = new Grass(id_generator);
            world.setTile(spreadLocation, spreaded_grass);
            id_generator.addEntityToIdMap(spreaded_grass.getId(), spreaded_grass);
            id_generator.addLocationToIdMap(world.getLocation(spreaded_grass), spreaded_grass.getId());
            id_generator.addGrassToLocationMap(spreadLocation, spreaded_grass);
        }
    }

    /**
     * Provides the visual representation of the grass in the simulation. The appearance changes based on the grass's age,
     * differentiating between young and mature grass.
     *
     * @return DisplayInformation containing the color and icon representation of the grass.
     */
    @Override
    public DisplayInformation getInformation() {
        if (age >= 8) {
            return new DisplayInformation(Color.green, "grass");
        } else {
            return new DisplayInformation(Color.green, "grass-young");
        }
    }

    /**
     * Handles the spawning of the grass entity in the simulation world. Adds the grass to various maps
     * for tracking purposes using the ID generator.
     *
     * @param world The simulation world where the grass is spawned.
     */
    @Override
    public void spawn(World world) {
        super.spawn(world);
        id_generator.addGrassToLocationMap(world.getLocation(this), this);
    }
}
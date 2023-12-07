package ourcode.Organism.OrganismChildren.PlantChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Plant;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * The Bush class represents a bush plant in the ecosystem, characterized by growing berries.
 * It extends the Plant class and implements display functionality for visualization in the simulation.
 */
public class Bush extends Plant implements DynamicDisplayInformationProvider {
    protected int berries_amount;

    /**
     * Constructs a Bush instance with a specified ID generator.
     * Initializes the bush's properties such as its type, berry count and nutritional value.
     *
     * @param idGenerator The IDGenerator instance for generating the unique ID of the bush.
     */
    public Bush(IDGenerator idGenerator) {
        super(idGenerator);
        type = "berry";
        berries_amount = 0;
        nutritional_value = 3;
        max_age = 100000;
    }

    /**
     * Defines the behavior of a bush in each simulation step. This includes invoking
     * the superclass (plant) act method and growing berries.
     *
     * @param world The simulation world in which the bush exists.
     */
    @Override
    public void act(World world) {
        super.act(world);
        grow();
    }

    /**
     * Increases the number of berries on the bush.
     */
    public void grow() {
        berries_amount += 3;
    }

    /**
     * Retrieves the current number of berries on the bush.
     *
     * @return The number of berries.
     */
    public int getBerriesAmount() {
        return berries_amount;
    }

    /**
     * Consumes a portion of the berries on the bush, reducing their count.
     */
    public void eatBerries() {
        berries_amount -= 3;
    }

    /**
     * Determines the graphical representation of the bush based on its berry count.
     *
     * @return DisplayInformation containing the color and image key for rendering the bush.
     */
    @Override
    public DisplayInformation getInformation() {
        if (berries_amount <= 0) {
            return new DisplayInformation(Color.cyan, "bush");
        } else {
            return new DisplayInformation(Color.cyan, "bush-berries");
        }
    }
}

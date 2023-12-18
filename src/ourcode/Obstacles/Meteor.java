package ourcode.Obstacles;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a meteor in the simulation environment. This class extends 'Entity' and
 * implements 'DynamicDisplayInformationProvider' for graphical representation.
 * Meteors are used in the event of dinosaur extinction.
 * Meteors are transient objects that transform into fossils.
 */
public class Meteor extends Entity implements DynamicDisplayInformationProvider {

    /**
     * Constructs a Meteor with a unique identifier. Initializes its type, age, and maximum age.
     *
     * @param original_id_generator The IDGenerator instance for generating the unique identifier.
     */
    public Meteor(IDGenerator original_id_generator) {
        super(original_id_generator);
        type = "meteor";
        max_age = 5;
    }

    /**
     * Defines the behavior of the meteor in each simulation step. The meteor transforms into a fossil
     * upon reaching its maximum age.
     *
     * @param world The simulation world in which the meteor exists.
     */
    public void act(World world) {
        super.act(world);

        if (age >= max_age) {
            becomeFossil(world);
        }
    }

    /**
     * Transforms the meteor into a fossil at its current location in the simulation world.
     *
     * @param world The simulation world where the transformation occurs.
     */
    public void becomeFossil(World world) {
        Fossil fossil = new Fossil(id_generator);
        Location current_location = world.getLocation(this);
        deleteEverythingOnTile(world, current_location);
        world.setTile(current_location, fossil);
    }

    /**
     * Provides the display information for the meteor, defining its visual representation in the simulation.
     * The appearance changes based on the meteor's age.
     *
     * @return DisplayInformation object containing color and image data for the meteor's representation.
     */
    @Override
    public DisplayInformation getInformation() {
        if (age <= 1) {
            return new DisplayInformation(Color.red, "meteor-1");
        } else if (age == 2) {
            return new DisplayInformation(Color.red, "meteor-2");
        } else if (age == 3) {
            return new DisplayInformation(Color.red, "meteor-3");
        } else {
            return new DisplayInformation(Color.red, "meteor-4");
        }
    }
}

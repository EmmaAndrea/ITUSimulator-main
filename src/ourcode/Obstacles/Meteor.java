package ourcode.Obstacles;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class Meteor extends Entity implements DynamicDisplayInformationProvider {

    /**
     * Constructor for a meteor, gives them an ID, sets 'age' to 1 and 'max_age' to 5.
     *
     * @param original_id_generator The IDGenerator instance from program-runner.
     */
    public Meteor(IDGenerator original_id_generator) {
        super(original_id_generator);
        type = "meteor";
        max_age = 5;
    }

    public void act(World world) {
        super.act(world);

        if (age >= max_age) {
            becomeFossil(world);
        }
    }

    public void becomeFossil(World world) {
        Fossil fossil = new Fossil(id_generator);
        Location current_location = world.getLocation(this);
        deleteEverythingOnTile(world, current_location);
        world.setTile(current_location, fossil);
    }

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

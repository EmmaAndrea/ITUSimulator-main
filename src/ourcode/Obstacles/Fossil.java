package ourcode.Obstacles;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class Fossil extends Entity implements DynamicDisplayInformationProvider {

    /**
     * Constructor for a fossil.
     *
     * @param original_id_generator The IDGenerator instance from program-runner.
     */
    public Fossil(IDGenerator original_id_generator) {
        super(original_id_generator);
    }

    public void act(World world) {
        super.act(world);

        if (age > 50) {
            world.delete(this);
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.red, "fossil");
    }
}

package ourcode.Organism;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class Footprint extends Entity implements DynamicDisplayInformationProvider {

    /**
     * Constructor for a footprint, gives them an identity, sets 'age' to 1.
     *
     * @param original_id_generator The IDGenerator from the program-runner.
     */
    public Footprint(IDGenerator original_id_generator) {
        super(original_id_generator);
        max_age = 30;
    }

    public void act(World world) {
        if (age >= max_age) {
            world.remove(this);
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.darkGray, "footprint");
    }
}

package ourcode.Obstacles;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class Cave extends Habitat implements DynamicDisplayInformationProvider {
    public Cave(IDGenerator id_generator) {
        super(id_generator);
        type = "cave";
    }

    /**
     * The graphical settings for the cave.
     * @return Returns the display information of the cave.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.cyan, "cave");
    }
}
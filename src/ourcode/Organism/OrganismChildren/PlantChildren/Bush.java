package ourcode.Organism.OrganismChildren.PlantChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import ourcode.Organism.OrganismChildren.Plant;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class Bush extends Plant implements DynamicDisplayInformationProvider {
    protected int berries;
    public Bush(IDGenerator idGenerator) {
        super(idGenerator);
        type = "berry";
        berries = 0;
        nutritional_value = 3;
        max_age = 100000;
    }

    public void grow() {
        berries =+ 3;
    }

    public int getBerries() {
        return berries;
    }

    public void eatBerries() {
        berries =-3;
    }

    @Override
    public DisplayInformation getInformation() {
        if (berries == 0) {
            return new DisplayInformation(Color.cyan, "bush");
        } else {
            return new DisplayInformation(Color.cyan, "bush-berries");
        }
    }
}

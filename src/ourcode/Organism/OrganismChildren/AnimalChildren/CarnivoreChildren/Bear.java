package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import ourcode.Organism.OrganismChildren.AnimalChildren.Carnivore;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class Bear extends Carnivore implements DynamicDisplayInformationProvider {

    protected Location territory;
    public Bear(IDGenerator idGenerator) {
        super(idGenerator);
        trophic_level = 4;
        territory = null;

    }

    @Override
    public DisplayInformation getInformation() {
        if (age >= 11) {
            return new DisplayInformation(Color.cyan, "bear-large");
        } else {
            return new DisplayInformation(Color.cyan, "bear-small");
        }
    }


}

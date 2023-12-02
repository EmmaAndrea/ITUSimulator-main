package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import ourcode.Organism.OrganismChildren.AnimalChildren.Carnivore;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bear extends Carnivore implements DynamicDisplayInformationProvider {

    protected Location territory;

    public Bear(IDGenerator idGenerator) {
        super(idGenerator);
        trophic_level = 4;
        territory = null;
        type = "bear";
        max_age = 190;
        max_hunger = 30;

        consumable_foods = new ArrayList<>(List.of("grass", "wolf", "bear"));
    }

    /**
     * Graphics for old, young and wounded bear.
     * @return the display information for the bear in its current state.
     */
    @Override
    public DisplayInformation getInformation() {
        if (age >= 11) {
            if (wounded) {
                return new DisplayInformation(Color.cyan, "bear-large-wounded");
            } else {
                return new DisplayInformation(Color.cyan, "bear-large");
            }
        } else {
            if (wounded) {
                return new DisplayInformation(Color.cyan, "bear-small-wounded");
            } else {
                return new DisplayInformation(Color.cyan, "bear-small");
            }
        }
    }

    public void setTerritory(Location territory) {
        this.territory = territory;
    }
}

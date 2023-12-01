package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Carnivore;
import ourcode.Setup.IDGenerator;

public class Bear extends Carnivore {

    protected Location territory;
    public Bear(IDGenerator idGenerator) {
        super(idGenerator);
        trophic_level = 4;
        territory = null;

    }

}

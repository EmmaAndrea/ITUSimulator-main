package ourcode.Organism;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Dinosaur;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class DinosaurEgg extends Animal implements DynamicDisplayInformationProvider {

    public DinosaurEgg(IDGenerator id_generator, boolean has_cordyceps) {
        super(id_generator, has_cordyceps);
        max_age = 28;
    }

    public void act(World world) {
        super.act(world);

        if (age >= max_age) {
            becomeDinosaur(world);
        }
    }

    public void becomeDinosaur(World world) {
        Location current_location = world.getLocation(this);
        world.delete(this);
        Dinosaur baby = new Dinosaur(id_generator, has_cordyceps);
        world.setTile(current_location, baby);
    }

    public DisplayInformation getInformation() {
        if (age < 10) {
            return new DisplayInformation(Color.ORANGE, "egg");
        } else if (age < 20) {
            return new DisplayInformation(Color.ORANGE, "egg-slightly-hatched");
        } else {
            return new DisplayInformation(Color.ORANGE, "egg-hatched");
        }
    }
}
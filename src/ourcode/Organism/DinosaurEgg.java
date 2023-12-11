package ourcode.Organism;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Dinosaur;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a dinosaur egg in the simulation environment. This class extends 'Animal' and
 * implements 'DynamicDisplayInformationProvider' for graphical representation. The egg hatches
 * into a dinosaur after reaching a certain age.
 */
public class DinosaurEgg extends Animal implements DynamicDisplayInformationProvider {

    /**
     * Constructs a DinosaurEgg with a unique identifier and cordyceps status.
     * Sets the maximum age after which the egg will hatch into a dinosaur.
     *
     * @param id_generator The IDGenerator instance that provides the unique identifier for the egg.
     * @param has_cordyceps Boolean indicating if the egg is infected with cordyceps.
     */
    public DinosaurEgg(IDGenerator id_generator, boolean has_cordyceps) {
        super(id_generator, has_cordyceps);
        max_age = 28;
    }

    /**
     * Defines the behavior of the dinosaur egg in each simulation step. The egg hatches
     * into a dinosaur upon reaching its maximum age.
     *
     * @param world The simulation world in which the egg exists.
     */
    public void act(World world) {
        super.act(world);

        if (age >= max_age) {
            becomeDinosaur(world);
        }
    }

    /**
     * Transforms the dinosaur egg into a baby dinosaur. The egg is removed from the world,
     * and a new dinosaur is created at the same location.
     *
     * @param world The simulation world where the transformation occurs.
     */
    public void becomeDinosaur(World world) {
        Location current_location = world.getLocation(this);
        world.delete(this);
        Dinosaur baby = new Dinosaur(id_generator, has_cordyceps);
        world.setTile(current_location, baby);
    }

    /**
     * Provides the display information for the dinosaur egg. The appearance changes based on the egg's age,
     * representing different stages of hatching.
     *
     * @return DisplayInformation object containing color and image data for the egg's representation.
     */
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
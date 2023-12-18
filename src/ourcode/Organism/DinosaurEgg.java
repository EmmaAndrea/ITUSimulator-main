package ourcode.Organism;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Dinosaur;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.TyrannosaurusRex;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a dinosaur egg in the simulation environment. This class extends 'Animal' and
 * implements 'DynamicDisplayInformationProvider' for graphical representation. The egg hatches
 * into a dinosaur after reaching a certain age.
 */
public class DinosaurEgg extends Animal implements DynamicDisplayInformationProvider {

    Dinosaur mother; // Reference to the dinosaur that laid the egg.

    /**
     * Constructs a DinosaurEgg with a unique identifier and cordyceps status.
     * Initializes the egg's properties, including the maximum age for hatching.
     *
     * @param id_generator The IDGenerator instance providing the unique identifier for the egg.
     * @param has_cordyceps Boolean indicating if the egg is infected with cordyceps.
     */
    public DinosaurEgg(IDGenerator id_generator, boolean has_cordyceps) {
        super(id_generator, has_cordyceps);
        max_age = 28;
    }

    /**
     * Defines the behavior of the dinosaur egg in each simulation step, particularly focusing on the hatching process.
     * The egg hatches into a dinosaur upon reaching its maximum age.
     *
     * @param world The simulation world where the egg exists.
     */
    @Override
    public void act(World world) {
        age++;

        if (age >= max_age) {
            becomeDinosaur(world);
        }
    }

    /**
     * Sets the reference to the mother dinosaur of this egg. This association is used for lineage tracking
     * and potential inherited behaviors or attributes in the simulation.
     *
     * @param mother The dinosaur that laid this egg, considered as the mother.
     */
    public void setMother(Dinosaur mother){
        this.mother = mother;
    }

    /**
     * Transforms the egg into a baby dinosaur at its current location, simulating the hatching process.
     * The egg is replaced by a new dinosaur entity in the simulation world.
     *
     * @param world The simulation world where the hatching occurs.
     */
    public void becomeDinosaur(World world) {
        Location current_location = world.getLocation(this);
        world.delete(this);
        Dinosaur baby = null;
        if (type == "tyrannosaurus") {
            baby = new TyrannosaurusRex(id_generator, has_cordyceps);
        }
        if (mother != null) {
            baby.setMother(mother);
            mother.becomeMother(baby);
        }
        world.setTile(current_location, baby);
    }

    /**
     * Provides the display information for the dinosaur egg, depicting various stages of its development.
     * The appearance changes based on the egg's age, indicating different stages of hatching.
     *
     * @return DisplayInformation object containing color and image data for the egg's visual representation.
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
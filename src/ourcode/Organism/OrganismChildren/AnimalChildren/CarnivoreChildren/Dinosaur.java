package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.DinosaurEgg;
import ourcode.Organism.Footprint;
import ourcode.Organism.Gender;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Dinosaur extends Predator implements DynamicDisplayInformationProvider {
    protected Location previous_location;
    /**
     * Constructs a dinosaur instance. Calls the constructor of the superclass Animal
     * and initializes specific attributes for a predator.
     *
     * @param original_id_generator The IDGenerator instance for generating the unique ID of the predator.
     */
    public Dinosaur(IDGenerator original_id_generator, boolean has_cordyceps) {

        super(original_id_generator, has_cordyceps);
        trophic_level = 6;
        type = "dinosaur";
        max_age = 250;
        max_hunger = 50;
        power = 7;
        max_damage = 30;
        consumable_foods = new ArrayList<>(List.of("wolf", "bear", "rabbit"));
        bedtime = 12;
        wakeup = 18;
    }

    /**
     * Determines the graphic of the dinosaur based on its current condition and age.
     * @return Returns the graphics information for the wolf.
     */
    @Override
    public void act(World world) {
        setPrevious_location(world);
        super.act(world);

        if (checkEmptySpace(world, world.getLocation(this))) {
            world.setTile(previous_location, new Footprint(id_generator));
        }

        if (getGender() == Gender.Female) { // more parameters
           if (steps_since_last_birth > 50) {
               layEgg(world);
           }
        }
    }

    public void layEgg(World world) {
        DinosaurEgg dinosaurEgg = new DinosaurEgg(id_generator, has_cordyceps);
        world.setTile(previous_location, dinosaurEgg);

        steps_since_last_birth = 0;
    }

    public void setPrevious_location(World world) {
        previous_location = world.getLocation(this);
    }

    @Override
    public DisplayInformation getInformation() {
        if (age >= 20) {
            if (is_sleeping) {
                return new DisplayInformation(Color.black, "dinosaur-adult-sleeping");
            } else if (damage_taken > 0) {
                return new DisplayInformation(Color.black, "dinosaur-adult-wounded");
            } else {
                return new DisplayInformation(Color.black, "dinosaur-adult");
            }
        } else {
            if (is_sleeping) {
                return new DisplayInformation(Color.black, "dinosaur-baby-sleeping");
            } else if (damage_taken > 0) {
                return new DisplayInformation(Color.black, "dinosaur-baby-wounded");
            } else {
                return new DisplayInformation(Color.black, "dinosaur-baby");
            }
        }
    }
}
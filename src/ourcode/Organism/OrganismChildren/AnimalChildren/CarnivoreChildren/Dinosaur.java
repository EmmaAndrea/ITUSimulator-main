package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.DinosaurEgg;
import ourcode.Organism.Footprint;
import ourcode.Organism.Gender;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Setup.IDGenerator;

import java.awt.*;

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
        trophic_level = 3;
        type = "dinosaur";
        max_age = 250;
        max_hunger = 50;
        power = 4;
        max_damage = 30;
        consumable_foods.add("dinosaur");
        bedtime = 12;
        wakeup = 18;
        nutritional_value = 20;
    }

    @Override
    public void act(World world) {
        setPrevious_location(world);
        Location current_location = world.getLocation(this);
        super.act(world);

        if (age >= 20) {
            trophic_level = 6;
            power = 7;
        }

        if (!is_hiding) {
            grace_period = 0;
        }

        // Stop at once if something happened that killed the dinosaur.
        if (!world.contains(this)) {
            return;
        }

        // Make footprint
        if (checkEmptySpace(world, current_location)) {
            if (!world.containsNonBlocking(current_location)) {
                world.setTile(previous_location, new Footprint(id_generator));
            }
        }

        if (hunger > 30) {
            hunt(world);
        }

        if (checkBreedStats(world)) {
           breed(world);
        }
    }

    /**
     * Lays egg.
     * @param world The simulation world in which breeding occurs.
     */

    @Override
    public boolean checkHasBreedMate(World world){
        Location my_location =null;
        try {
            my_location = world.getLocation(this);
        } catch (Exception e) {
            System.out.println("dinosaur died");
        }
        for (Location location: world.getSurroundingTiles(my_location, 2)) {
            if (!world.isTileEmpty(location)) {
                if (world.getTile(location) instanceof Animal animal && animal.getType().equals(type) && animal.getGender() == Gender.Male) {
                    return true;
                }
            }
        } return false;
    }

    @Override
    public void breed(World world) {
        System.out.println("made baby dino");
        DinosaurEgg dinosaurEgg = new DinosaurEgg(id_generator, has_cordyceps);
        if (world.isTileEmpty(previous_location)) {
            world.setTile(previous_location, dinosaurEgg);
            steps_since_last_birth = 0;

            // add taking care of egg?
        }
    }

    public void setPrevious_location(World world) {
        previous_location = world.getLocation(this);
    }

    /**
     * Determines the graphic of the dinosaur based on its current condition and age.
     * @return Returns the graphics information for the dinosaur.
     */
    @Override
    public DisplayInformation getInformation() {
        if (!has_cordyceps) {
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
        } else {
            if (age >= 20) {
                if (is_sleeping) {
                    return new DisplayInformation(Color.black, "dinosaur-adult-sleeping-cordyceps");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.black, "dinosaur-adult-wounded-cordyceps");
                } else {
                    return new DisplayInformation(Color.black, "dinosaur-adult-cordyceps");
                }
            } else {
                if (is_sleeping) {
                    return new DisplayInformation(Color.black, "dinosaur-baby-sleeping-cordyceps");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.black, "dinosaur-baby-wounded-cordyceps");
                } else {
                    return new DisplayInformation(Color.black, "dinosaur-baby-cordyceps");
                }
            }
        }
    }
}
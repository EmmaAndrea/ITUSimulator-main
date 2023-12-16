package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Meteor;
import ourcode.Organism.DinosaurEgg;
import ourcode.Organism.Footprint;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Dinosaur extends Predator implements DynamicDisplayInformationProvider {
    protected Location previous_location;

    private Dinosaur mother;
    /**
     * Constructs a dinosaur instance. Calls the constructor of the superclass Animal
     * and initializes specific attributes for a predator.
     *
     * @param original_id_generator The IDGenerator instance for generating the unique ID of the predator.
     */
    public Dinosaur(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator, has_cordyceps);
        trophic_level = 2;
        type = "dinosaur";
        max_age = 200;
        max_hunger = 80;
        power = 3;
        max_damage = 120;
        consumable_foods.add("dinosaur");
        bedtime = 12;
        wakeup = 18;
        nutritional_value = 20;
        friends = new ArrayList<>();
    }

    @Override
    public void act(World world) {
        if (!world.contains(this)) {
            return;
        }

        previous_location = world.getLocation(this);
        Location current_location = world.getLocation(this);
        super.act(world);

        if (!world.getEntities().containsKey(mother)) {
            mother = null;
        }

        if (age >= 14) {
            trophic_level = 6;
            power = 6;
        } else {
            if (mother != null) followMother(world);
            return;
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

        if (kaboomOdds()) {
            goExtinct(world);
        }

        else if (checkBreedStats(world)) {
           breed(world);
           return;
        }

        else if (hunger > 10) {
            if (!hunt(world)){
                nextMove(world);
            }
        }
        nextMove(world);
    }

    /**
     * Lays egg.
     * @param world The simulation world in which breeding occurs.
     */
    @Override
    public boolean checkHasBreedMate(World world) {
        Location my_location;
        try {
            my_location = world.getLocation(this);
        } catch (Exception e) {
            System.out.println("dinosaur died");
            return false;
        }
        for (Location location : world.getSurroundingTiles(my_location, 4)) {
            if (!world.isTileEmpty(location)) {
                if (world.getTile(location) instanceof Dinosaur dinosaur && dinosaur.getGender() != gender) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void breed(World world) {
        System.out.println(type + id + " made baby dino");
        DinosaurEgg dinosaurEgg = new DinosaurEgg(id_generator, has_cordyceps);
        dinosaurEgg.setMother(this);
        if (world.isTileEmpty(previous_location)) {
            world.setTile(previous_location, dinosaurEgg);
            steps_since_last_birth = 0;
        }
    }

    public void becomeMother(Dinosaur baby) {
        friends.add(baby);
    }

    public void setMother(Dinosaur mother) {
        this.mother = mother;
        friends.add(mother);
    }

    public void followMother(World world){
        if (distanceTo(world, world.getLocation(mother)) > 1) {
            for (int i = 0 ; i < distanceTo(world, world.getLocation(mother)) ; i++){
                moveCloser(world, world.getLocation(mother));
            }
        }
    }

    public void goExtinct(World world) {
        System.out.println("went extinct");
        Meteor meteor = new Meteor(id_generator);
        Location current_location = world.getLocation(this);
        deleteEverythingOnTile(world, current_location);
        world.setTile(current_location, meteor);
    }

    public boolean kaboomOdds() {
        return new Random().nextInt(150) < 1;
    }

    /**
     * Determines the graphic of the dinosaur based on its current condition and age.
     * @return Returns the graphics information for the dinosaur.
     */
    @Override
    public DisplayInformation getInformation() {
        if (!has_cordyceps) {
            if (age >= 14) {
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
            if (age >= 14) {
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
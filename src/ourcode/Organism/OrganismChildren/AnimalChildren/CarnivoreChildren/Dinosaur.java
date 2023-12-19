package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Footprint;
import ourcode.Obstacles.Meteor;
import ourcode.Organism.DinosaurEgg;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a generic Dinosaur entity in a simulated world.
 * This abstract class extends the Predator class and provides specific behaviors
 * and attributes related to dinosaurs, such as a high trophic level and the ability to lay eggs.
 */
public abstract class Dinosaur extends Predator implements DynamicDisplayInformationProvider {
    protected Location previous_location;
    private Dinosaur mother;

    /**
     * Constructs a Dinosaur with specific characteristics, initializing attributes
     * such as trophic level, hunger limits, and attack power.
     *
     * @param original_id_generator The IDGenerator instance for generating the unique ID of the Dinosaur.
     * @param has_cordyceps Indicates whether the Dinosaur is infected with cordyceps at birth.
     */
    public Dinosaur(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator, has_cordyceps);
        trophic_level = 2;
        max_hunger = 80;
        power = 3;
        max_damage = 120;
        consumable_foods.add("dinosaur");
        nutritional_value = 20;
        friends = new ArrayList<>();
    }

    /**
     * Defines the behavior of the Dinosaur in each simulation step.
     * This includes following its mother if young, hunting, breeding, and potentially going extinct.
     *
     * @param world The simulation world in which the Dinosaur exists.
     */
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

        if (checkBreedStats(world)) {
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
     * Checks if the Dinosaur meets the conditions for breeding and if a mate is in proximity.
     *
     * @param world The simulation world where breeding is checked.
     * @return true if breeding conditions are met, false otherwise.
     */
    @Override
    public boolean checkHasBreedMate(World world) {
        Location my_location;
        try {
            my_location = world.getLocation(this);
        } catch (Exception e) {
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

    /**
     * Initiates the breeding process for the Dinosaur, resulting in laying a Dinosaur Egg.
     *
     * @param world The simulation world in which breeding occurs.
     */
    @Override
    public void breed(World world) {
        DinosaurEgg dinosaurEgg = new DinosaurEgg(id_generator, has_cordyceps);
        dinosaurEgg.setMother(this);
            for (Location location: world.getSurroundingTiles(world.getLocation(this), 1)) {
                if (world.isTileEmpty(location)) {
                    world.setTile(location, dinosaurEgg);
                    break;
                }
            }
            steps_since_last_birth = 0;
    }

    /**
     * Sets this Dinosaur as the mother of a baby Dinosaur, adding the baby to its list of friends.
     * This method represents the bond between the mother and its offspring.
     *
     * @param baby The baby Dinosaur that this Dinosaur becomes the mother of.
     */
    public void becomeMother(Dinosaur baby) {
        friends.add(baby);
    }

    /**
     * Assigns a Dinosaur as the mother of this Dinosaur and adds the mother to its list of friends.
     * This method establishes a parental link between this Dinosaur and its mother.
     *
     * @param mother The Dinosaur to be set as the mother of this Dinosaur.
     */
    public void setMother(Dinosaur mother) {
        this.mother = mother;
        friends.add(mother);
    }

    /**
     * Causes the young Dinosaur to follow its mother, ensuring it stays close for protection and guidance.
     * This method is important for the survival of young Dinosaurs in the simulation.
     *
     * @param world The simulation world where the mother and baby Dinosaurs are located.
     */
    public void followMother(World world){
        if (distanceTo(world, world.getLocation(mother)) > 1) {
            for (int i = 0 ; i < distanceTo(world, world.getLocation(mother)) ; i++){
                moveCloser(world, world.getLocation(mother));
            }
        }
    }

    /**
     * Triggers the extinction event for the Dinosaur, replacing it with a meteor in the simulation.
     * This method represents a dramatic event leading to the Dinosaur's sudden disappearance.
     *
     * @param world The simulation world where the extinction event occurs.
     */
    public void goExtinct(World world) {
        Meteor meteor = new Meteor(id_generator);
        Location current_location = world.getLocation(this);
        deleteEverythingOnTile(world, current_location);
        world.setTile(current_location, meteor);
    }

    /**
     * Determines the odds of a catastrophic event leading to the Dinosaur's extinction.
     * This method adds an element of unpredictability to the Dinosaur's lifecycle.
     *
     * @return true if the random odds trigger an extinction event, false otherwise.
     */
    public boolean kaboomOdds() {
        return new Random().nextInt(150) < 1;
    }
}
package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Obstacles.Habitat;
import ourcode.Organism.Gender;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Prey;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Rabbit entity in the simulated world.
 * Rabbits are a type of Herbivore, characterized by their interactions with burrows.
 * They have unique behaviors like seeking burrows for shelter and foraging for food.
 */
public class Rabbit extends Prey implements DynamicDisplayInformationProvider {

    ArrayList <Habitat> my_burrows;
    Rabbit mate;
    boolean has_burrow; // Indicates whether the rabbit has a burrow.

    /**
     * Constructs a Rabbit with a unique identifier, initializes its basic characteristics and
     * sets up its relationship with burrows.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the rabbit.
     * @param has_cordyceps If the animal is being born in an infected state (with cordyceps).
     */
    public Rabbit(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator, has_cordyceps);
        type = "rabbit";
        max_hunger = 24;
        nutritional_value = 4;
        max_age = 100;
        trophic_level = 2;
        power = 1;
        has_burrow = false;
        max_damage = 18;
        consumable_foods = new ArrayList<>(List.of("grass")); // Can only eat grass.
        this.has_cordyceps = has_cordyceps;
        bedtime = 9;
        wakeup = 19;
        mate = null;
    }

    @Override
    public void act(World world) {
        super.act(world);

        if (is_hiding) {
            return;
        }

        if (isBedtime(world)) {
            return;
        }

        boolean isCloseToBurrow = false;
        // if it is not in its burrow
        if (habitat != null) {
            if (distanceTo(world, world.getLocation(habitat)) < 3) {
                isCloseToBurrow = true;
            }
            if (world.getCurrentTime() < 10 && world.getCurrentTime() > 5 && !isCloseToBurrow) {
                moveCloser(world, world.getLocation(habitat));
                return;
            } else nextMove(world);
        }
        nextMove(world);
    }

    /**
     * Creates a new burrow at the specified location and updates the animal's state accordingly.
     * This method also updates the ID generator maps with the new burrow's location and ID.
     * The animal is then prompted to make its next move in the world.
     *
     * @param world The simulation world where the new burrow is created.
     */
    @Override
    public void makeHabitat(World world) {
        Location location = world.getLocation(this);

        if(world.containsNonBlocking(location)){
            if (world.getNonBlocking(location) instanceof Burrow burrow){
                habitat = burrow;
            }
        } else if (age < 5) return;

        if (checkEmptySpace(world, world.getLocation(this))) {
            habitat = new Burrow(id_generator);
            world.setTile(location, habitat);
            id_generator.addBurrowToLocationMap(world.getLocation(this), habitat);
            id_generator.addLocationToIdMap(world.getLocation(this), habitat.getId());
        } else return;

        my_burrows = new ArrayList<>();
        my_burrows.add(habitat);
        has_burrow = true;
    }

    @Override
    public boolean checkHasBreedMate(World world){
        if (is_hiding) {
            for (Object o : world.getEntities().keySet()) {
                if (o instanceof Animal animal && animal.getType().equals(type) && animal.getGender() == Gender.Male) {
                    return true;
                }
            }
        } return false;
    }

    /**
     * Provides the visual representation of the rabbit in the simulation. Changes appearance based on the rabbit's age.
     *
     * @return DisplayInformation containing the color and icon representation of the rabbit.
     */
    @Override
    public DisplayInformation getInformation() {
        if (!has_cordyceps) {
            if (age >= 20) {
                return new DisplayInformation(Color.black, "rabbit-large");
            } else {
                return new DisplayInformation(Color.black, "rabbit-small");
            }
        } else {
            if (age >= 20) {
                return new DisplayInformation(Color.black, "rabbit-large-cordyceps");
            } else {
                return new DisplayInformation(Color.black, "rabbit-small-cordyceps");
            }
        }
    }
}
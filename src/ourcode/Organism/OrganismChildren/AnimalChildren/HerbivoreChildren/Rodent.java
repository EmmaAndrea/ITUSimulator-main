package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Obstacles.Habitat;
import ourcode.Organism.Gender;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Prey;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract Rodent entity in the simulated world.
 * This class extends 'Prey', focusing on behaviors and characteristics specific to rodents,
 * such as their interaction with burrows and foraging activities.
 */
public abstract class Rodent extends Prey {

    ArrayList<Habitat> my_burrows; // List of burrows associated with the rodent.
    Rodent mate;                   // Potential mate for breeding purposes.
    boolean has_burrow;            // Indicates whether the rodent has a burrow.

    /**
     * Constructs a Rabbit with a unique identifier, initializes its basic characteristics and
     * sets up its relationship with burrows.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the rabbit.
     * @param has_cordyceps If the animal is being born in an infected state (with cordyceps).
     */
    public Rodent(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator, has_cordyceps);
        max_hunger = 24;                             // Maximum hunger level before the rodent starves.
        nutritional_value = 4;                       // Nutritional value when consumed by predators.
        trophic_level = 2;                           // Position in the food chain.
        power = 1;                                   // Combat power level in interactions.
        has_burrow = false;                          // Initially, the rodent does not have a burrow.
        max_damage = 12;                             // Maximum damage the rodent can sustain.
        consumable_foods = new ArrayList<>(List.of("grass")); // Rodents can only consume grass.
        this.has_cordyceps = has_cordyceps;          // Infection status of the rodent.
        mate = null;                                 // Initially, the rodent does not have a mate.
    }

    /**
     * Defines the actions performed by the rodent in each simulation step.
     * This includes checking for bedtime, seeking burrows, and making next moves.
     *
     * @param world The simulation world where the rodent acts.
     */
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
        if (habitat != null && world.getEntities().containsKey(this)) {
            try {
                world.getLocation(habitat);
            } catch (Exception e) {
                System.out.println("habitat missing");
                habitat = null;
                return;
            }
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
                return;
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

    /**
     * Checks if there is a potential mate for breeding available in the vicinity of the rodent.
     * This method is particularly used to determine if breeding conditions are met while the rodent is hiding.
     *
     * @param world The simulation world where the rodent and other entities exist.
     * @return True if a potential mate is found, false otherwise.
     */
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
}
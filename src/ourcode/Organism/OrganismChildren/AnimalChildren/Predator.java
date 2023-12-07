package ourcode.Organism.OrganismChildren.AnimalChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.IDGenerator;

import java.util.Set;

/**
 * The Predator class provides an abstraction for all carnivorous animals.
 * These animals exclusively consume meat and typically hunt their prey
 * to obtain sustenance.
 */
public abstract class Predator extends Animal {
    protected boolean is_sleeping;

    /**
     * Constructs a Predator instance. Calls the constructor of the superclass Animal
     * and initializes specific attributes for a predator.
     *
     * @param original_id_generator The IDGenerator instance for generating the unique ID of the predator.
     */
    public Predator(IDGenerator original_id_generator) {
        super(original_id_generator);
        is_sleeping = false;
    }

    /**
     * Spawns this predator in the world. This method leverages the spawn behavior
     * defined in the Animal superclass, applying any additional rules or logic
     * specific to predators.
     *
     * @param world The world in which the predator is spawned.
     */
    public void spawn(World world) {
        super.spawn(world);
    }

    /**
     * Defines the behavior of a carnivore in each simulation step. This method
     * should be implemented to specify the actions of the carnivore in the world.
     *
     * @param world The simulation world in which the carnivore exists.
     */
    @Override
    public void carnivoreAct(World world) {
    }

    /**
     * Attacks a specified animal in the world. If the target animal is defeated,
     * the predator eats it if hungry enough.
     *
     * @param world  The simulation world where the attack occurs.
     * @param animal The animal being attacked.
     */
    @Override
    public void attack(World world, Animal animal) {
        animal.damage(power);
        if (!animal.checkDamage()) {
            if (hunger >= animal.getNutritionalValue()) eat(world, animal);
        }
    }

    /**
     * Searches for and hunts down potential prey within a specified range in the world.
     * The predator moves closer to the prey and attacks if it is a consumable animal.
     *
     * @param world The simulation world where hunting takes place.
     */
    public void hunt(World world) {
        Set<Location> surrounding_tiles = world.getSurroundingTiles(world.getLocation(this), 5);

        // First, check for blocking organisms.
        // Though, if there is an animal of higher trophic level, move away from this animal.
        for (Location location : surrounding_tiles) {

            // If the tile at given location isn't empty.
            if (!world.isTileEmpty(location)) {

                // Declares a new object at given location.
                Object object = world.getTile(location);

                // Casts object to Organism class and checks if the object is an Organism.
                if (object instanceof Animal animal) {
                    if (consumable_foods.contains(animal.getType())) {
                        for (int i = 2; i <= distanceTo(world, location); i++) {
                            moveCloser(world, location);
                        }
                    }
                }
            }
        }
    }
}
package ourcode.Organism.OrganismChildren.AnimalChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The Predator class provides an abstraction for all carnivorous animals.
 * These animals exclusively consume meat and typically hunt their prey
 * to obtain sustenance.
 */
public abstract class Predator extends Animal {
    protected boolean is_sleeping;

    protected Location carcass_location;
    /**
     * Constructs a Predator instance. Calls the constructor of the superclass Animal
     * and initializes specific attributes for a predator.
     *
     * @param original_id_generator The IDGenerator instance for generating the unique ID of the predator.
     */
    public Predator(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator, has_cordyceps);
        consumable_foods = new ArrayList<>(List.of("rabbit", "bear", "wolf", "carcass"));
    }

    /**
     * Defines the behavior of a carnivore in each simulation step. This method
     * should be implemented to specify the actions of the carnivore in the world.
     *
     * @param world The simulation world in which the carnivore exists.
     */

    public void act(World world) {
        super.act(world);
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
        System.out.println(type + " hit " + animal.getType() + " for " + power + " damage");
        if (animal.isDead()) {
            animal.setHasBeenKilled();
        } animal.setGracePeriod(0);
    }

    @Override
    public boolean sameTypeInteraction(World world, Animal animal){
        // make sure attacker isn't being killed while attacking
        grace_period = 1;
        if (isCloseToDeath()) moveAway(world, world.getLocation(animal));
        else attack(world, animal);
        grace_period = 0;
        return true;
    }

    /**
     * Searches for and hunts down potential prey within a specified range in the world.
     * The predator moves closer to the prey and attacks if it is a consumable animal.
     *
     * @param world The simulation world where hunting takes place.
     */
    public boolean hunt(World world) {
        if (!world.contains(this) || hasBeenKilled || is_hiding) return true;

        Set<Location> surrounding_tiles = world.getSurroundingTiles(world.getLocation(this), 7);

        // First, check for blocking organisms.
        // Though, if there is an animal of higher trophic level, move away from this animal.
        for (Location location : surrounding_tiles) {

            // If the tile at given location isn't empty.
            if (!world.isTileEmpty(location)) {

                // Declares a new object at given location.
                Object object = world.getTile(location);

                // Casts object to Organism class and checks if the object is an Organism.
                if (object instanceof Animal animal) {
                    synchronized (animal) {
                        if (animal.getGracePeriod() == 0) {
                            if (!friends.contains(animal)) {
                                if (animal.getTrophicLevel() <= trophic_level) {
                                    if (consumable_foods.contains(animal.getType())) {
                                        for (int i = 1; i <= distanceTo(world, location); i++) {
                                            System.out.println(type + id + " moves closer to " + animal.getType() + animal.getId() + " in hunt mode");
                                            moveCloser(world, location);
                                        }
                                        System.out.println(type + id + " attacks " + animal.getType() + animal.getId() + " in hunt mode");
                                        attack(world, animal);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
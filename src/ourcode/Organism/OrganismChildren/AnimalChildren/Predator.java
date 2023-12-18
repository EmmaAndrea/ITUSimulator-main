package ourcode.Organism.OrganismChildren.AnimalChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents an abstract predator in a simulated world.
 * Predators are carnivorous animals that hunt other organisms for food.
 * This class extends the Animal class, incorporating specific behaviors
 * and characteristics inherent to predatory animals.
 */
public abstract class Predator extends Animal {
    protected boolean is_sleeping;  // Indicates whether the predator is currently sleeping.
    protected Location carcass_location;  // Location of the last kill, if any.

    /**
     * Constructs a Predator instance. Initializes specific attributes for a predator
     * such as consumable food types and other properties.
     *
     * @param original_id_generator The IDGenerator instance for generating the unique ID of the predator.
     * @param has_cordyceps Indicates whether the predator is born with cordyceps infection.
     */
    public Predator(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator, has_cordyceps);
        consumable_foods = new ArrayList<>(List.of("rabbit", "bear", "wolf", "carcass"));
    }

    /**
     * Defines the standard behavior of a predator during each simulation step.
     * This behavior includes hunting, moving, and other interactions within the world.
     *
     * @param world The simulation world in which the predator exists.
     */
    public void act(World world) {
        super.act(world);
    }

    /**
     * Attacks a specified animal within the world. If the target animal is defeated,
     * it becomes potential food for the predator.
     *
     * @param world  The simulation world where the attack occurs.
     * @param animal The target animal of the attack.
     */
    @Override
    public void attack(World world, Animal animal) {
        animal.damage(power);
        System.out.println(type + " hit " + animal.getType() + " for " + power + " damage");
        if (animal.isDead()) {
            animal.setHasBeenKilled();
        } animal.setGracePeriod(0);
    }

    /**
     * Determines the interaction of the predator with another animal of the same type.
     * This might include territorial disputes or other forms of social interaction.
     *
     * @param world  The world where the interaction occurs.
     * @param animal The animal with which the predator interacts.
     * @return true if an interaction occurs, false otherwise.
     */
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
     * Hunts for prey within a specified range. The predator moves closer to and
     * attacks any consumable animal it encounters.
     *
     * @param world The simulation world where hunting occurs.
     * @return true if a hunting action is performed, false otherwise.
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
        return false;
    }
}
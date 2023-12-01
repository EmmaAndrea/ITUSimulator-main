package ourcode.Organism.OrganismChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Gender;
import ourcode.Organism.Organism;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.util.Random;

/**
 * The Animal class gives the abstraction of an Animal. An animal inherits from the Organism
 *  class and has the fields hunger and max hunger.
 *  The animal class controls the movement of an animal
 *  the eat method is here, which gets the correct nutritional value of the organism being eaten
 */
public abstract class Animal extends Organism {
    public int hunger;
    public int max_hunger;
    public int steps_since_last_birth;
    protected boolean in_hiding;
    Gender gender;

    /**
     * The constructor of an Animal.
     */
    public Animal(IDGenerator original_id_generator) {
        super(original_id_generator); // life_counter = 1;
        hunger = 1;
        max_hunger = 1; // this value is random and will get initialized to another value in the children classes.
        steps_since_last_birth = 0;
        in_hiding = false;
        Gender gender = new Random().nextBoolean() ? Gender.Male : Gender.Female; // Randomly male or female.
    }

    /**
     * Returns the nutritional value of the organism of the current location.
     */
    public int getStandingOnNutritionalValue(World world) {
        Entity entity = id_generator.getEntity(world.getLocation(world.getNonBlocking(world.getLocation(this))));

        if (entity instanceof Organism organism) {
            return organism.getNutritionalValue();
        }

        // Handle the case where the entity is not an Organism (e.g., return a default value or throw an exception)
        return 0; // Or handle this scenario appropriately
    }

    /**
     * A method for giving animals the ability to eat. They decrease their 'hunger' by a certain amount of 'nutritional value'
     *  and the object the animal is standing on will be 'eaten' by calling the 'delete()' method from the World class
     */
    public void eat(World world) {
        // Deducts the animal's hunger with the nutritional value of the eaten organism.
        hunger -= getStandingOnNutritionalValue(world);

        // Deletes the eaten organism from the world.
        world.delete(world.getNonBlocking(world.getLocation(this)));
    }

    /**
     * Calls the act method from Organism.
     * Increases hunger by 1.
     * Dies, if max hunger is reached.
     * Moves, if possible.
     * Breeds, if circumstances are met.
     * Calls herbivoreAct().
     */

    @Override
    public boolean animalAct(World world) {

        // Adds hunger if it is not sleeping/hiding.
        if (!in_hiding) {
            hunger++;
        }

        // Adds one to the counter of how many days since it gave birth to an offspring.
        steps_since_last_birth++;

        // Checks if it dies of hunger; if not, move, breed if possible, and go to next step in act process: herbivoreAct.
        if (checkHunger()) {
            if (world.getEntities().get(this) != null) {
                nextMove(world);
                checkBreed(world);
            }
            herbivoreAct(world);
            //predatorAct coming soon
        } else {
            return false;
        }
        return true;
    }

    /**
     * explain here why we chose to have an herbivoreAct method
     */
    public void herbivoreAct(World world) {

    }

    /**
     * Makes a baby of the animal in the parameter.
     * Creates a new instance of that type of animal.
     * Spawns at a random possible location.
     */
    public void breed(World world, Animal animal) {
        // Retrieve current location
        Location random_location = getRandomMoveLocation(world);

        // Checks if there is a possible spawn location.
        if (random_location != null) {

            // Finds which type of animal to make baby of.
            Animal offspring = null;
            String animal_type = animal.getType();
            switch (animal_type) {
                case "rabbit":
                    offspring = new Rabbit(id_generator);
                    break;
                case "wolf":
                    //baby = new Wolf(id_generator);
                    break;
                default:
                    //baby = new Bear(id_generator);
            }

            // Spawns baby and adds to location and id maps.
            world.setTile(random_location, offspring);
            assert offspring != null; // We promise that a baby exists.
            id_generator.addEntityToIdMap(offspring.getId(), offspring);
            id_generator.addLocationToIdMap(random_location, offspring.getId());
        }
    }

    /**
     * Checks if the circumstances for breeding are met:
     * If there is a nearby same animal type;
     * If animal is older than 20;
     * If there is space for one more animal.
     * ERROR: SOMETIMES BREEDS ITSELF
     */
    public void checkBreed(World world) {
        // eventually check if the mating animal is of opposite gender
        // eventually check if mating animal also fulfills criteria.

        // If there is another one of its type in the surrounding tiles.
        for (Location location : world.getSurroundingTiles(world.getLocation(this), 1)) {
            if (location != world.getLocation(this)) { // this part is redundant as get surrounding doesn't include center

                if (id_generator.getEntity(location) != null && id_generator.getEntity(location).getType().equals(type)) {

                    // If the animal is in breeding age (older than 10% and younger than 90% of its age).
                    if (age >= max_age * 0.1) {
                        if (age <= max_age * 0.9) {

                            // If there is space for one more animal.
                            if (!world.getEmptySurroundingTiles().isEmpty()) {

                                // If they haven't bred in 10 steps.
                                if (steps_since_last_birth >= 5) {
                                    breed(world, this);
                                    steps_since_last_birth = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Animal dies of hunger if it is hungrier than its max hunger.
     * If the animal dies from hunger, method returns false.
     */
    public boolean checkHunger() {
        return hunger <= max_hunger;
    }

    /**
     * Moves to new tile, if there is a free surrounding tile.
     */
    public void nextMove(World world) {

        // Moves to new location, if the surrounding tiles aren't all full
        if (getRandomMoveLocation(world) != null) {
            world.move(this, getRandomMoveLocation(world));
        }
    }

    /**
     * If animal needs to go to specific location
     * Checks if the x coordinate is different, and then moves closer
     * If not, it checks if the y coordinate is different, and then moves closer
     */
    public void moveCloser(World world, Location target_location) {
        Location current_location = world.getLocation(this);

        // Instantiate variables for finding out which direction to move.
        int dx = 0;
        int dy = 0;

        // Check what direction we need to move.
        if (target_location.getX() > current_location.getX()) dx = 1;
        if (target_location.getX() < current_location.getX()) dx = -1;
        if (target_location.getY() > current_location.getY()) dy = 1;
        if (target_location.getY() < current_location.getY()) dy = -1;

        // Move in the X direction first, if needed.
        if (dx != 0) {
            Location new_location = new Location(current_location.getX() + dx, current_location.getY());
            if (world.isTileEmpty(new_location)) {
                world.move(this, new_location);
                return;
            }
        }

        // If moving in the X direction is not needed, move in the Y direction.
        if (dy != 0) {
            Location new_location = new Location(current_location.getX(), current_location.getY() + dy);
            if (world.isTileEmpty(new_location)) {
                world.move(this, new_location);
            }
        }
    }

    public void moveAway() {

    }

    /**
     * Returns the sum of steps needed to align x + y coordinates with current world location to a desired location.
     * Useful for finding out how far away something is.
     * E.g. if it's closer to being night than how long it will take for a rabbit to go to burrow/
     */
    public int distanceTo(World world, Location location) {
        Location currentLocation = world.getLocation(this);

        // Calculate the absolute difference in x and y coordinates
        int step_x = Math.abs(location.getX() - currentLocation.getX());
        int step_y = Math.abs(location.getY() - currentLocation.getY());

        // Return the sum of x and y steps
        return step_x + step_y;
    }
}
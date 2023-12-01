package ourcode.Organism.OrganismChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.Gender;
import ourcode.Organism.Organism;
import ourcode.Organism.OrganismChildren.AnimalChildren.Herbivore;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.Random;

import static ourcode.Organism.Gender.Female;
import static ourcode.Organism.Gender.Male;

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

    int grass_eaten;

    /**
     * The constructor of an Animal.
     */
    public Animal(IDGenerator original_id_generator) {
        super(original_id_generator); // life_counter = 1;
        hunger = 1;
        max_hunger = 1; // this value is random and will get initialized to another value in the children classes.
        steps_since_last_birth = 0;
        in_hiding = false;
        gender = new Random().nextBoolean() ? Male : Female; // Randomly male or female.
        grass_eaten = 0;
    }

    /**
     * Returns the nutritional value of the organism of the current location.
     */
    public int getStandingOnNutritionalValue(World world) {
        Entity entity = id_generator.getGrass(world.getLocation(this));

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

        grass_eaten++;
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
            if (this instanceof Herbivore) {
                herbivoreAct(world);

                if (world.getEntities().get(this) != null) {
                    breed(world);
                }
            } else {
                // predatorAct coming soon
                if (!in_hiding) {
                    //checkBreed(world);
                    nextMove(world);
                }
            }
        } else return false;

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
    public void breed(World world) {
        if (checkBreed(world)) {
            spawnEntity(world, type);
        }
    }

    /**
     *
     */
    public void spawnEntity(World world, String entityType) {
        switch (entityType) {
            case "rabbit":
                Rabbit rabbit = new Rabbit(id_generator);
                rabbit.spawn(world);
                break;
            case "grass":
                Grass grass = new Grass(id_generator);
                grass.spawn(world);
                break;
            case "burrow":
                Burrow burrow = new Burrow(id_generator);
                burrow.spawn(world);
                break;
            default:
                System.out.println("Unknown entity type: " + entityType);
        }
    }

    /**
     * Checks if the circumstances for breeding are met: if it's:
     * female;
     * in breeding age;
     * nearby a potential mate;
     * the opposite gender of the potential mate;
     * the same type as the potential mate;
     * available to breed, considering the surrounding space.
     */

    public boolean checkBreed(World world) {
        // Only females can give birth.
        if (gender == Female) {

            // If animal is in breeding age.
            if (age >= max_age * 0.15 && age <= max_age * 0.85) {

                // If it's not too much time since they last gave birthed.
                if (steps_since_last_birth >= 12) {

                    // Get nearby locations to check for surrounding potential mates.
                    ArrayList<Location> surrounding_locations = new ArrayList<>();
                    for (Location location : world.getSurroundingTiles(world.getLocation(this), 1)){

                        // If the location in question is not empty.
                        if (!world.isTileEmpty(location)) {
                            surrounding_locations.add(location);
                        }

                        // Get the animal that is on the tiles.
                        for (Object object : world.getEntities().keySet()) {
                            if (surrounding_locations.contains(world.getEntities().get(object))) {

                                // Casts object to Animal with variable name 'animal'.
                                if (object instanceof Animal animal) {

                                    // Check if that animal has the same type as this.
                                    if (animal.getType().equals(type)) {

                                        // If they have opposite genders.
                                        if (animal.getGender() == Male) {

                                            // If there is room to breed.
                                            if (world.getEmptySurroundingTiles(world.getLocation(this)) != null) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // If any of these are false, return false.
        return false;
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

    /**
     * Returns the gender of an animal.
     */
    public Gender getGender() {
        return gender;
    }
}
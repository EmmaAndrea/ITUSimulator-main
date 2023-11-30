package ourcode.Organism.OrganismChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Setup.IDGenerator;

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

    /**
     * The constructor of an Animal.
     */
    public Animal(IDGenerator original_id_generator) {
        super(original_id_generator); // life_counter = 1;
        hunger = 1;
        max_hunger = 1; // this value is random and will get initialized to another value in the children classes.
        steps_since_last_birth = 0;
        in_hiding = false;
    }

    /**
     * Returns the nutritional value of the organism of the current location.
     */
    public int getStandingOnNutritionalValue(World world) {
        Organism organism = id_generator.getOrganism(world.getLocation(world.getNonBlocking(world.getLocation(this))));
        return organism.getNutritionalValue();
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
            if (!in_hiding) {
                nextMove(world);
                checkBreed(world);
                herbivoreAct(world);
            }
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
        Location random_location = getRandomSurroundingFreeLocation(world);

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
            id_generator.addAnimalToIdMap(offspring.getId(), offspring);
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

                if (id_generator.getOrganism(location) != null && id_generator.getOrganism(location).getType().equals(type)) {

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
        // Gets random location.
        Location random_location = getRandomSurroundingFreeLocation(world);

        // Moves to new location, if location isn't null.
        if (random_location != null) {
            world.move(this, getRandomSurroundingFreeLocation(world));
        }
    }

    /**
     * EMMA FOR THE LOVE OF GOD HUSK AT FREAKING JAVADOC!!!!!!!!!!!!!!!!!!!
     */
    public void moveCloser(World world, Location location) {
        if (location.getX() != world.getLocation(this).getX()) {
            if (location.getX() > world.getLocation(this).getX()) {
                Location new_location = new Location(world.getLocation(this).getX() + 1, world.getLocation(this).getY());
                world.move(this, new_location);
            } else if (location.getX() < world.getLocation(this).getX()) {
                Location new_location = new Location(world.getLocation(this).getX() - 1, world.getLocation(this).getY());
                world.move(this, new_location);
            }
        } else if (location.getY() != world.getLocation(this).getY()){
            if (location.getY() > world.getLocation(this).getY()){
                Location new_location = new Location(world.getLocation(this).getX(), world.getLocation(this).getY() + 1);
                world.move(this, new_location);
            } else if (location.getY() < world.getLocation(this).getY()){
                Location new_location = new Location(world.getLocation(this).getX(), world.getLocation(this).getY() - 1);
                world.move(this, new_location);
            }
        }
    }

    public void moveFurther() {

    }

    /**
     * Returns the sum of steps needed to align x + y coordinates with current world location to a desired location.
     * Useful for finding out how far away something is.
     * E.g. if it's closer to being night than how long it will take for a rabbit to go to burrow/
     */
    public int distanceTo(World world, Location location) {
        int step_x = 0;
        int step_y = 0;

        // Gets distance to align x coordinates.
        if (location.getX() != world.getLocation(this).getX()) {
            if (location.getX() > world.getLocation(this).getX()) {
                step_x = location.getX() - world.getLocation(this).getX();
            } else if (location.getX() < world.getLocation(this).getX()) {
                step_x = location.getX() + world.getLocation(this).getX();
            }
        }

        // Gets distance to align y coordinates.
        if (location.getY() != world.getLocation(this).getY()) {
            if (location.getY() > world.getLocation(this).getY()) {
                step_y = location.getY() - world.getLocation(this).getY();
            } else if (location.getY() < world.getLocation(this).getY()) {
                step_y = location.getY() + world.getLocation(this).getY();
            }
        }

        // Returns the sum of x and y steps.
        return step_x + step_y;
    }
}

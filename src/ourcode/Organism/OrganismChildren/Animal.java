package ourcode.Organism.OrganismChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.Gender;
import ourcode.Organism.Organism;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import static ourcode.Organism.Gender.Female;
import static ourcode.Organism.Gender.Male;

/**
 * Represents an abstract animal in a simulation environment. This class extends the Organism class,
 * adding specific attributes and behaviors pertinent to animals, such as hunger management, movement,
 * and breeding mechanisms. It serves as a base for different animal types within the simulation.
 */
public abstract class Animal extends Organism {
    protected int hunger; // Current hunger level of the animal.
    protected int max_hunger; // Maximum hunger level before the animal dies.
    protected int steps_since_last_birth; // Steps since the animal last gave birth.
    protected boolean in_hiding; // Indicates whether the animal is in hiding.
    Gender gender; // Gender of the animal.
    protected int grass_eaten; // Tracks the amount of grass the animal has eaten.
    protected ArrayList<String> consumable_foods; // List of which classes the animal can eat.
    protected boolean being_hunted;
    protected boolean wounded;

    /**
     * Constructs a new Animal with a unique identifier.
     * Initializes hunger levels, breeding steps, hiding state, and randomly assigns gender.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the animal.
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
     * Calculates and returns the nutritional value of the organism the animal is currently standing on.
     *
     * @param world The world in which the animal exists.
     * @return The nutritional value of the organism at the current location.
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
     * Enables the animal to eat, reducing its hunger based on the nutritional value of the organism consumed.
     * The consumed organism is removed from the world.
     *
     * @param world The world in which the animal and its food source exist.
     */
    public void eat(World world) {
        // Deducts the animal's hunger with the nutritional value of the eaten organism.
        hunger -= getStandingOnNutritionalValue(world);

        // Deletes the eaten organism from the world.
        world.delete(world.getNonBlocking(world.getLocation(this)));

        grass_eaten++;
    }

    /**
     * Performs the action sequence for an animal during a simulation step. This includes increasing hunger,
     * checking for survival, moving, breeding, and performing species-specific actions.
     *
     * @param world The simulation world in which the animal exists.
     * @return true if the animal survives this step, false otherwise.
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
     * Defines specific actions for herbivores in the simulation. This method should be overridden
     * by herbivore subclasses to implement specific behaviors like grazing or avoiding predators.
     *
     * @param world The simulation world in which the herbivore exists.
     */
    public void herbivoreAct(World world) {
    }

    /**
     * Initiates the breeding process if certain conditions are met, such as the presence of a mate and suitable breeding conditions.
     * Creates and spawns a new entity of the same type as this animal.
     *
     * @param world The simulation world in which breeding occurs.
     */
    public void breed(World world) {
        if (checkBreed(world)) {
            spawnEntity(world, type);
        }
    }

    /**
     * Spawns a new entity of the specified type in the simulation world.
     * Handles creation and initialization of different entity types like Rabbit, Grass, or Burrow.
     *
     * @param world The simulation world where the new entity will be spawned.
     * @param entityType The type of entity to spawn.
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
     * Checks whether the conditions for breeding are met, such as gender compatibility, age, and proximity to a potential mate.
     *
     * @param world The simulation world where breeding might occur.
     * @return true if the animal can breed, false otherwise.
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
     * Determines whether the animal survives based on its current hunger level.
     *
     * @return true if the animal's hunger is below the maximum threshold, false if it dies of hunger.
     */
    public boolean checkHunger() {
        return hunger <= max_hunger;
    }

    /**
     * Determines and executes the animal's next move in the simulation world, considering available free tiles.
     *
     * @param world The simulation world where the animal moves.
     */
    public void nextMove(World world) {

        // Moves to new location, if the surrounding tiles aren't all full
        if (getRandomMoveLocation(world) != null) {
            world.move(this, getRandomMoveLocation(world));
        }
    }

    /**
     * Moves the animal closer to a target location, considering available paths and obstacles.
     *
     * @param world The simulation world where the movement occurs.
     * @param target_location The target location towards which the animal moves.
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
     * Calculates the distance from the animal's current location to a specified location in the world.
     * This method is useful for navigation and decision-making processes, like finding food or avoiding predators.
     *
     * @param world The simulation world where the distance is calculated.
     * @param location The location to which the distance is calculated.
     * @return The number of simulation steps required to reach the specified location.
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
     * Retrieves the gender of the animal, which is used in various behavioral and breeding logic.
     *
     * @return The gender of the animal, either Male or Female.
     */
    public Gender getGender() {
        return gender;
    }
}
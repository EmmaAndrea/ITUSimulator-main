package ourcode.Organism.OrganismChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.Gender;
import ourcode.Organism.Organism;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Bear;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Wolf;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.PlantChildren.Bush;
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
    protected Gender gender; // Gender of the animal.
    protected ArrayList<String> consumable_foods; // List of which classes the animal can eat.
    protected boolean being_hunted;
    protected int damage_taken;
    protected int power; // used to decide how much damage a bear deals.
    protected int max_damage;
    protected boolean being_eaten;

    protected int grace_period; //

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
        being_hunted = false;
        damage_taken = 0;
        grace_period = 0;
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
            if (checkDamage() && world.getEntities().containsKey(this)) {
                herbivoreAct(world);
                omnivoreAct(world);
                carnivoreAct(world);
                return true;
            } else return false;
        } else return false;
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
     * Defines specific actions for carnivore in the simulation. This method should be overridden
     * by carnivore subclasses to implement specific behaviors like hunting or pack movements.
     *
     * @param world The simulation world in which the carnivore exists.
     */
    public void carnivoreAct(World world) {

    }

    /**
     * Defines specific actions for herbivores in the simulation. This method should be overridden
     * by herbivore subclasses to implement specific behaviors like hunting or eating berries.
     *
     * @param world The simulation world in which the omnivore exists.
     */
    public void omnivoreAct(World world) {
    }

    /**
     * Enables the animal to eat, reducing its hunger based on the nutritional value of the organism consumed.
     * The consumed organism is removed from the world.
     *
     * @param world The world in which the animal and its food source exist.
     */
    public void eat(World world, Object object) {
        // Deducts the animal's hunger with the nutritional value of the eaten organism.
        if (object instanceof Grass grass) {
            hunger -= 2;
            // Deletes the eaten organism from the world.
            world.delete(world.getNonBlocking(world.getLocation(this)));
            // check
            System.out.println(this.getType() + " " + this.getId() + " ate " + grass.getId());
        } else if (object instanceof Animal animal) {
            synchronized (animal) {
                if (animal.getGracePeriod() == 0) {
                    // takes off hunger by eating
                    hunger -= animal.getNutritionalValue();
                    // gives back damage by eating
                    if (damage_taken >= animal.getNutritionalValue()) damage_taken -= animal.getNutritionalValue();
                    // check
                    System.out.println(this.getType() + this.getId() + " ate " + animal.getType() + animal.getId());
                    // makes sure wolf is deleted properly
                    if (animal instanceof Wolf wolf) {
                        if (this instanceof Wolf thiswolf) {
                            if (wolf.isAlpha()) thiswolf.overtakePack(wolf);
                        }
                        wolf.deleteMe(world);
                    } else world.delete(animal);
                }
            }
        }
    }
    /**
     * Initiates the breeding process if certain conditions are met, such as the presence of a mate and suitable breeding conditions.
     * Creates and spawns a new entity of the same type as this animal.
     *
     * @param world The simulation world in which breeding occurs.
     */
    public void breed(World world) {
        if (this.getType().equals("bear") && world.getCurrentTime() > 19){
            System.out.println("bear");
        }
        if (checkBreed(world)) {
            spawnEntity(world, type);
        }
    }

    /**
     * Spawns a new entity of the specified type in the simulation world.
     * Handles creation and initialization of different entity types like Rabbit, Grass, or Burrow.
     *
     * @param world      The simulation world where the new entity will be spawned.
     * @param entityType The type of entity to spawn.
     */
    public void spawnEntity(World world, String entityType) {
        switch (entityType) {
            case "bear":
                Bear bear = new Bear(id_generator);
                bear.spawn(world);
            case "wolf":
                Wolf wolf = new Wolf(id_generator);
                wolf.spawn(world);
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
                    for (Location location : world.getSurroundingTiles(world.getLocation(this), 1)) {

                        // If the location in question is not empty.
                        if (!world.isTileEmpty(location)) {
                            surrounding_locations.add(location);
                        }

                        // Get the animal that is on the tiles.
                        for (Location checklocation : surrounding_locations) {
                            Object object = world.getTile(checklocation);

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
        // check for food nearby
        if (world.getEntities().containsKey(this)) {
            if (!findFoodOrSafety(world)) {
                // If no food or danger is found, move randomly.
                if (getRandomMoveLocation(world) != null) {
                    world.move(this, getRandomMoveLocation(world));
                }
            }
        }
    }

    /**
     * Moves the animal closer to a target location, considering available paths and obstacles.
     *
     * @param world           The simulation world where the movement occurs.
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


    public void moveAway(World world, Location danger_location) {
        Location current_location = world.getLocation(this);

        // Instantiate variables for finding out which direction to move.
        int dx = 0;
        int dy = 0;

        // Check what direction we need to move.
        if (danger_location.getX() > current_location.getX()) dx = -1;
        if (danger_location.getX() < current_location.getX()) dx = 1;
        if (danger_location.getY() > current_location.getY()) dy = -1;
        if (danger_location.getY() < current_location.getY()) dy = 1;

        // Move in the X direction first, if needed.
        if (dx != 0) {
            Location new_location = new Location(current_location.getX() + dx, current_location.getY());
            int new_coordinate = current_location.getX() + dx;
            if(world.getSize() > new_coordinate && new_coordinate >= 0){
                if (world.isTileEmpty(new_location)) {
                    world.move(this, new_location);
                    return;
                }
            }
        }

        // If moving in the X direction is not needed, move in the Y direction.
        if (dy != 0) {
            Location new_location = new Location(current_location.getX(), current_location.getY() + dy);
            int new_coordinate = current_location.getY() + dy;
            if(world.getSize() > new_coordinate && new_coordinate >= 0) {
                if (world.isTileEmpty(new_location)) {
                    world.move(this, new_location);
                }
            }
        }
    }

    /**
     * Calculates the distance from the animal's current location to a specified location in the world.
     * This method is useful for navigation and decision-making processes, like finding food or avoiding predators.
     *
     * @param world    The simulation world where the distance is calculated.
     * @param location The location to which the distance is calculated.
     * @return The number of simulation steps required to reach the specified location.
     */
    public int distanceTo(World world, Location location) {
        if (location != null) {
            Location currentLocation = world.getLocation(this);

            // Calculate the absolute difference in x and y coordinates
            int step_x = Math.abs(location.getX() - currentLocation.getX());
            int step_y = Math.abs(location.getY() - currentLocation.getY());

            // Return the sum of x and y steps
            return step_x + step_y;
        } return 0;
    }

    /**
     * Attempts to find food or safety for the animal in the simulation world.
     * The method first checks for blocking organisms in the surrounding tiles. If it encounters an organism
     * with a higher trophic level (indicating a predator), it moves away from that organism. If it finds
     * an organism that is a viable food source (as listed in consumable_foods), it moves closer to that organism.
     * If no food or threats are found, it checks for non-blocking organisms like grass and moves closer if
     * food is found. Otherwise, the animal moves to a random location.
     *
     * @param world The simulation world in which the animal is trying to find food or safety.
     * @return true if the animal moves towards food or away from a predator, false if it moves to a random location.
     */
    public boolean findFoodOrSafety(World world) {
        // Get surrounding tiles to iterate through them.
        Set<Location> surrounding_tiles = world.getSurroundingTiles(world.getLocation(this), 1);

        // First, check for blocking organisms.
        // Though, if there is an animal of higher trophic level, move away from this animal.
        for (Location location : surrounding_tiles) {

            // If the tile at given location isn't empty.
            if (!world.isTileEmpty(location)) {

                // Declares a new object at given location.
                Object object = world.getTile(location);

                // Casts object to Organism class and checks if the object is an Organism.
                if (object instanceof Animal animal) {
                    if(animal instanceof Wolf wolf){
                        if (wolf.getPack() != null && wolf.getPack().contains(this)){
                            System.out.println("found pack");
                            break;
                        } else if (animal.getTrophicLevel() > trophic_level) {
                            moveAway(world, location);
                            being_hunted = true;
                            return true;
                        } else if (this instanceof Predator predator) {
                            synchronized (predator) {
                                if (animal.getTrophicLevel() < trophic_level && consumable_foods.contains(animal.getType())) {
                                    if (hunger > 4) predator.attack(world, animal);
                                    return true;
                                }
                            }
                        }
                    } else if (animal.getTrophicLevel() > trophic_level) {
                        moveAway(world, location);
                        being_hunted = true;
                        return true;
                        // If the organism has a higher trophic level than itself.
                    } else if (this instanceof Predator predator) {
                        synchronized (predator) {
                            if (animal.getTrophicLevel() < trophic_level && consumable_foods.contains(animal.getType())) {
                                if (hunger > 4) predator.attack(world, animal);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        // Next, check for non-blocking organisms like grass.
        for (Location location : surrounding_tiles) {
            if (world.containsNonBlocking(location)) {
                Object object = world.getNonBlocking(location);
                if (object instanceof Organism organism) {
                    if (consumable_foods.contains(organism.getType())) {
                        moveCloser(world, location);
                        if (world.containsNonBlocking(world.getLocation(this))) {
                            if (world.getNonBlocking(world.getLocation(this)) instanceof Grass grass) {
                                if (hunger >= 2) {
                                    if (world.getEntities().containsKey(this)) eat(world, grass);
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else if (!world.isTileEmpty(location)){
                // Declares a new object at given location.
                Object object = world.getTile(location);

                // Casts object to Organism class and checks if the object is an Organism.
                if (object instanceof Bush bush) {
                    if (consumable_foods.contains("bush")) {
                        if (hunger > 4) bush.eatBerries();
                        System.out.println(type + " ate berries");
                        return false;
                    }
                }
            }
        }

        // If no food or danger is found, return false.
        return false;
    }


    /**
     * Retrieves the gender of the animal, which is used in various behavioral and breeding logic.
     *
     * @return The gender of the animal, either Male or Female.
     */
    public Gender getGender() {
        return gender;
    }


    public void attack(World world, Animal animal){

    }
    public void damage(int power){
        damage_taken += power;
    }

    public boolean checkDamage(){
        return damage_taken <= max_damage;
    }

    public void setBeingEaten(Boolean b){
        being_eaten = b;
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

    public int getGracePeriod(){
        return grace_period;
    }

    /**
     * Retrieves the days since the last time this animal made a baby.
     * @return steps_since_last_day
     */
    public int getStepsSinceLastBirth() {
        return steps_since_last_birth;
    }

    /**
     * Changes the gender to male or female, depending on the paramter..
     * This method is created for test purposes!
     * @param gender String of what gender to transition to.
     */
    public void setGender(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            this.gender = Gender.Male;
        } else {
            this.gender = Gender.Female;
        }
    }

    public int getHunger() {
        return hunger;
    }
}


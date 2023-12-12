package ourcode.Organism.OrganismChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Habitat;
import ourcode.Organism.Gender;
import ourcode.Organism.Organism;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Wolf;
import ourcode.Organism.OrganismChildren.PlantChildren.Bush;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.Entity;
import ourcode.Setup.IDGenerator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import static ourcode.Organism.Gender.Female;
import static ourcode.Organism.Gender.Male;

/**
 * Represents an abstract animal in a simulation environment. This class extends the Organism class,
 * adding specific attributes and behaviors pertinent to animals, such as hunger management, movement,
 * and breeding mechanisms. It serves as a base for different animal types within the simulation.
 */
public abstract class Animal extends Organism {
    protected int hunger; // Current hunger level of the animal.
    protected int max_hunger; // Maximum hunger level an animal can sustain before dying.
    protected int steps_since_last_birth; // Number of simulation steps since the animal last gave birth.
    protected boolean in_hiding; // Flag indicating if the animal is currently in hiding.
    protected Gender gender; // Gender of the animal, affecting its breeding behaviors.
    protected ArrayList<String> consumable_foods; // List of food types that the animal can consume.
    protected int damage_taken; // Amount of damage the animal has taken.
    protected int power; // Power level of the animal, used in combat scenarios.
    protected int max_damage; // Maximum amount of damage the animal can sustain before dying.
    protected boolean has_cordyceps; // Indicates whether the animal is infected with the cordyceps fungus.
    protected ArrayList<Animal> friends; // List of other animals considered as friends.
    protected Habitat habitat; // The habitat associated with the animal.
    protected boolean has_habitat; // Indicates whether the animal has an associated habitat.
    private final ReentrantLock lock; // A lock for managing concurrency in multithreaded environments.
    protected int bedtime; // The simulation step when the animal goes to sleep.
    protected int wakeup; // The simulation step when the animal wakes up.
    protected boolean pack_hunting; // Indicates if the animal is currently engaged in pack hunting.

    /**
     * Constructs a new Animal with a unique identifier.
     * Initializes hunger levels, breeding steps, hiding state, and randomly assigns gender.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the animal.
     * @param has_cordyceps Indicates whether the animal is infected with the cordyceps fungus.
     */
    public Animal(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator); // life_counter = 1;

        hunger = 1;
        max_hunger = 1; // this value is random and will get initialized to another value in the children classes.
        steps_since_last_birth = 0;
        in_hiding = false;
        gender = new Random().nextBoolean() ? Male : Female; // Randomly male or female.
        damage_taken = 0;
        grace_period = 0;
        this.has_cordyceps = has_cordyceps;
        friends = new ArrayList<>();
        habitat = null;
        has_habitat = false; // watch out for problem with this when spawning wolves in packs.
        lock = new ReentrantLock();
        bedtime = 0;
        wakeup = 0;
        pack_hunting = false;
    }

    /**
     * Performs the action sequence for an animal during a simulation step. This includes increasing hunger,
     * checking for survival, moving, breeding, and performing species-specific actions.
     *
     * @param world The simulation world in which the animal exists.
     */
    @Override
    public void act(World world) {
        super.act(world);

        if (world.getCurrentTime() == 14){
            System.out.println("hi");
        }

        steps_since_last_birth++;

        if (!in_hiding) {
            hunger++;
        }

        if (this.hasBeenKilled || age >= max_age || hunger >= max_hunger) {
            dieAndBecomeCarcass(world);
            return;
        }

        if (isBedtime(world) && !in_hiding && habitat != null){
            if (distanceTo(world, world.getLocation(habitat)) > 0){
                moveCloser(world, world.getLocation(habitat));
            }
            if (distanceTo(world, world.getLocation(habitat)) < 1) {
                if (grace_period == 0){
                    grace_period = 1;
                    return;
                } else enterHabitat(world);
            } else return;

        } else if (!isBedtime(world) && in_hiding){
            exitHabitat(world);
        }

        lock.lock();

        // If the animal is currently inside its habitat.
        if (in_hiding) {
            if (checkBreed(world)) {
                try {
                    breed(world);
                    return;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        // If the animal is outside its habitat.
        else {
            // Make habitat if it doesn't have one.
            if (habitat == null) {
                makeHabitat(world);
            } if (!in_hiding) {
                if (!findFoodOrSafety(world)) {
                    nextMove(world);
                }
            }
        }
        lock.unlock();
    }

    /**
     * Method for creating a habitat for the animal in the simulation world.
     * This method should be implemented specifically for each animal subclass.
     *
     * @param world The simulation world in which the habitat is created.
     */
    public void makeHabitat(World world) {
        // individual for each animal
    }

    /**
     * Method for reducing code duplication of this sequence
     * Used to allow animals to make habitats
     *
     * @param world The simulation world.
     * @param location The location to check for available space.
     * @return true if the space is empty and suitable for habitat creation.
     */
    public boolean checkEmptySpace(World world, Location location) {
        if (world.containsNonBlocking(location)) {
            if (world.getNonBlocking(location) instanceof Grass grass) {
                world.delete(grass);
            }
        }
        return !world.containsNonBlocking(location);
    }

    /**
     * Initiates the breeding process if certain conditions are met, such as the presence of a mate and suitable breeding conditions.
     * Creates and spawns a new entity of the same type as this animal.
     *
     * @param world The simulation world in which breeding occurs.
     */
    public void eat(World world, Organism organism) {
        // Deducts the animal's hunger with the nutritional value of the eaten organism.
        if (organism instanceof Grass grass) {
            hunger -= 2;
            // Deletes the eaten organism from the world.
            world.delete(world.getNonBlocking(world.getLocation(this)));
            // check
            System.out.println(this.getType() + " " + this.getId() + " ate " + "grass" + grass.getId());

        } else if (organism instanceof Animal animal) {
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
                        wolf.dieAndBecomeCarcass(world);
                    } else animal.dieAndBecomeCarcass(world);
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
    public void breed(World world) throws Exception {
        Class<? extends Animal> animalClass = this.getClass();

        // Assuming idGenerator is available in the scope and hasCordyceps is either a field or a parameter
        Constructor<? extends Animal> constructor = animalClass.getDeclaredConstructor(IDGenerator.class, boolean.class);
        Animal cub = constructor.newInstance(id_generator, has_cordyceps);

        world.add(cub);
        habitat.addResident(cub);
        cub.setGracePeriod(1);
        cub.setHabitat(habitat);
        cub.enterHabitat(world);
        System.out.println("made cub");

        steps_since_last_birth = 0;
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
                if (steps_since_last_birth >= 120) {

                    for (Animal animal : habitat.getResidents()) {
                        if (animal.getGender() == Male) {
                            return true;
                        }
                    }
                }
            }
        }
        // If any of these are false, return false.
        return false;
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
        int dx = Integer.compare(target_location.getX(), current_location.getX());
        int dy = Integer.compare(target_location.getY(), current_location.getY());
        move(world, dx, dy);
    }

    /**
     * Moves the animal away from a danger location, considering available paths and obstacles.
     *
     * @param world The simulation world where the movement occurs.
     * @param danger_location The location from which the animal moves away.
     */
    public void moveAway(World world, Location danger_location) {
        Location current_location = world.getLocation(this);
        int dx = -Integer.compare(danger_location.getX(), current_location.getX());
        int dy = -Integer.compare(danger_location.getY(), current_location.getY());
        move(world, dx, dy);
    }

    /**
     * Helper method to handle the common logic of moving the animal.
     *
     * @param world The simulation world where the movement occurs.
     * @param dx The movement step in the x-direction.
     * @param dy The movement step in the y-direction.
     */
    private void move(World world, int dx, int dy) {
        Location current_location = world.getLocation(this);

        // Attempt to move in the X direction
        if (dx != 0) {
            Location newXLocation = new Location(current_location.getX() + dx, current_location.getY());
            if (isValidMove(world, newXLocation)) {
                world.move(this, newXLocation);
                return;
            }
        }

        // Attempt to move in the Y direction if X movement was not possible
        if (dy != 0) {
            Location newYLocation = new Location(current_location.getX(), current_location.getY() + dy);
            if (isValidMove(world, newYLocation)) {
                world.move(this, newYLocation);
            }
        }
    }

    private boolean isValidMove(World world, Location newLocation) {
        int newX = newLocation.getX();
        int newY = newLocation.getY();
        return newX >= 0 && newX < world.getSize() && newY >= 0 && newY < world.getSize() && world.isTileEmpty(newLocation);
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
        if (location != null && !in_hiding && world.contains(this)) {
            Location currentLocation = world.getLocation(this);

            // Calculate the absolute difference in x and y coordinates
            int step_x = Math.abs(location.getX() - currentLocation.getX());
            int step_y = Math.abs(location.getY() - currentLocation.getY());

            // Return the sum of x and y steps
            return step_x + step_y;
        } return 0;
    }

    /**
     * Enters the habitat that the animal is standing on, if it is empty and is the animal's habitat.
     * This action hides the animal from the simulation world.
     *
     * @param world The simulation world where the habitat entrance occurs.
     */
    public void enterHabitat(World world) {

        // add animal to list of residents of habitat
        habitat.addResident(this);

        // remove animal from map
        world.remove(this);

        // goes into hiding
        in_hiding = true;

        // overridden by bear
    }


    /**
     * Exits the habitat in which the animal is currently hiding.
     * This action makes the animal visible and active again in the simulation world.
     *
     * @param world The simulation world where the habitat exit occurs.
     */
    public void exitHabitat(World world) {
        habitat.removeResident(this);
        world.setTile(getSpawnLocation(world), this);
        in_hiding = false;
        // overridden by bear
    }

    /**
     * Determines the spawn location for the animal when exiting its habitat.
     * This location is chosen from available spots surrounding the habitat.
     *
     * @param world The simulation world where the habitat is located.
     * @return The chosen location for the animal to spawn.
     */
    public Location getSpawnLocation(World world){
        Location habitat_location = world.getLocation(habitat);

        // Makes list of possible spawn locations (locations with no blocking elements).
        ArrayList<Location> possible_spawn_locations = new ArrayList<>();
        for (Location surroundingTile : world.getSurroundingTiles(habitat_location, 1)) {
            if (world.isTileEmpty(surroundingTile)) {
                possible_spawn_locations.add(surroundingTile);
            }
        }

        // Removes itself from possible locations to spawn.
        possible_spawn_locations.remove(habitat_location);

        // Return null value if there is no empty location (to be used in if statement in larger method to check).
        if (possible_spawn_locations.isEmpty()) {
            return null;
        }
        // Finds a random index in this list of locations.
        Random random = new Random();
        int random_index = random.nextInt(0, possible_spawn_locations.size());

        return possible_spawn_locations.get(random_index);
    }

    /**
     * Assigns a habitat to the animal.
     * This habitat is used for hiding and potential breeding activities.
     *
     * @param habitat The habitat to be assigned to the animal.
     */
    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
        has_habitat = true;
    }

    /**
     * Retrieves the gender of the animal.
     * This information is crucial for breeding and other gender-specific behaviors.
     *
     * @return The gender of the animal.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Placeholder method for animal attacks, to be implemented in subclasses.
     * Defines how an animal attacks another entity in the simulation world.
     *
     * @param world The simulation world where the attack occurs.
     * @param animal The target animal of the attack.
     */
    public void attack(World world, Animal animal){

    }

    /**
     * Inflicts damage on the animal.
     * This method increases the damage taken by the animal, affecting its health and survival.
     *
     * @param power The amount of damage to be inflicted on the animal.
     */
    public void damage(int power){
        damage_taken += power;
    }

    /**
     * Checks if the animal is dead due to sustained damage.
     * Returns true if the damage taken exceeds the maximum damage threshold.
     *
     * @return True if the animal is dead, false otherwise.
     */
    public boolean ifDeadReturnTrue(){
        return damage_taken >= max_damage;
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
     * Retrieves the grace period of the animal.
     * This period is used in specific scenarios to avoid simulation errors.
     *
     * @return The grace period of the animal.
     */
    public int getGracePeriod() {
        return grace_period;
    }

    /**
     * Manually sets the gender of the animal, typically used for testing purposes.
     * Changes the gender to either male or female based on the input parameter.
     *
     * @param gender The gender to be assigned to the animal.
     */
    public void setGender(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            this.gender = Gender.Male;
        } else {
            this.gender = Gender.Female;
        }
    }

    /**
     * Determines if it is currently the animal's bedtime based on the simulation world's time.
     * This method is used to control the animal's sleep cycle.
     *
     * @param world The simulation world where the time is checked.
     * @return True if it is bedtime for the animal, false otherwise.
     */
    public boolean isBedtime(World world){
        if (world.getCurrentTime() > bedtime) {
            return world.getCurrentTime() < wakeup;
        }
        return false;
    }

    /**
     * Adds another animal to the list of friends of this animal.
     * Friends are typically excluded from predatory behaviors.
     *
     * @param animal The animal which is to be added to the list of friends.
     */
    public void setFriends(Animal animal) {
        friends.add(animal);
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
        // Validate if the animal is on the map
        if (!world.contains(this) || in_hiding) {
            System.out.println("Warning: Animal not on the map");
            return false;
        }

        lock.lock();
        try {
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
                        System.out.println(type + " tries to eat " + animal.getType());
                        if (animal.getGrace_period() == 0) {
                            if (!friends.contains(animal)) {
                                if (animal.getTrophicLevel() > trophic_level) {
                                    moveAway(world, location);
                                    lock.unlock();
                                    return true;
                                    // If the organism has a higher trophic level than itself.
                                }
                                if (animal.getTrophicLevel() <= trophic_level && consumable_foods.contains(animal.getType())) {
                                    if (hunger >= animal.getNutritionalValue()) {
                                        eat(world, animal);
                                        lock.unlock();
                                        return true;
                                    }
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
                                    if (hunger >= grass.getNutritionalValue()) {
                                        eat(world, grass);
                                        lock.unlock();
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } else if (!world.isTileEmpty(location)) {
                    // Declares a new object at given location.
                    Object object = world.getTile(location);

                    // Casts object to Organism class and checks if the object is an Organism.
                    if (object instanceof Bush bush) {
                        if (consumable_foods.contains("bush")) {
                            if (hunger > 4) bush.eatBerries();
                            System.out.println(type + " ate berries");
                            lock.unlock();
                            return false;
                        }
                    }
                }
            }

            // If no food or danger is found, return false.
            lock.unlock();
            return false;
        } catch (IllegalArgumentException iae) {
            lock.unlock();
            return false;
        }
    }

    /**
     * Retrieves the amount of damage this wolf has taken.
     *
     * @return The damage taken.
     */
    public int getDamageTaken() {
        return damage_taken;
    }

    /**
     * Transforms the organism into a carcass upon its death. The organism is replaced by a carcass
     * at its current location in the simulation world. The new carcass retains the nutritional value,
     * type, and cordyceps status of the original organism.
     *
     * @param world The simulation world where the transformation occurs.
     */
    public void dieAndBecomeCarcass(World world) {
        if (world.contains(this) && !this.in_hiding) {
            grace_period = 2;
            System.out.println("i, " + type + id + ", became a carcass");
            Location current_location = world.getLocation(this);
            Carcass carcass = new Carcass(id_generator, nutritional_value, type, has_cordyceps);
            carcass.setGracePeriod(1);
            world.delete(this);
            world.setTile(current_location, carcass);
        }
    }
}


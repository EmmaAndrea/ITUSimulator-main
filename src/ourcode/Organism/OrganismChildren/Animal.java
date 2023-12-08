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
    protected boolean has_cordyceps;
    protected int grace_period; // holy period after coming out of burrow such that canvas errors are evaded.
    protected ArrayList<Animal> friends;
    protected Habitat habitat;
    protected boolean has_habitat;
    private final ReentrantLock lock;
    protected int bedtime;
    protected int wakeup;

    /**
     * Constructs a new Animal with a unique identifier.
     * Initializes hunger levels, breeding steps, hiding state, and randomly assigns gender.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the animal.
     */
    public Animal(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator); // life_counter = 1;
        hunger = 1;
        max_hunger = 1; // this value is random and will get initialized to another value in the children classes.
        steps_since_last_birth = 0;
        in_hiding = false;
        gender = new Random().nextBoolean() ? Male : Female; // Randomly male or female.
        being_hunted = false;
        damage_taken = 0;
        grace_period = 0;
        this.has_cordyceps = has_cordyceps;
        friends = new ArrayList<>();
        habitat = null;
        has_habitat = false; // watch out for problem with this when spawning wolves in packs.
        lock = new ReentrantLock();
        bedtime = 0;
        wakeup = 0;
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

        if (world.getCurrentTime() == 14) {
            System.out.println("bear bedtime");
        }
        if (isBedtime(world) && !in_hiding && habitat != null){
            if (distanceTo(world, world.getLocation(habitat)) > 0){
                moveCloser(world, world.getLocation(habitat));
            }
            if (distanceTo(world, world.getLocation(habitat)) < 1) {
                enterHabitat(world);
            } else return;

        } else if (!isBedtime(world) && in_hiding){
            exitHabitat(world);
        }
        // Adds hunger if it is not sleeping/hiding.
        if (!in_hiding) {
            hunger++;
        }
        // checks if the animal should die
        if (hunger >= max_hunger || age >= max_age) {
            // become carcass
            System.out.println(type + " died");
        }

        // Adds one to the counter of how many days since it gave birth to an offspring.
        steps_since_last_birth++;

        lock.lock();
        // If the animal is currently inside its habitat.
        if (in_hiding) {
            if (checkBreed(world)) {
                try {
                    breed(world);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        // If the animal is outside its habitat.
        else {
            // Make habitat if doesn't have one.
            if (habitat == null) {
                makeHabitat(world);
            } if (!findFoodOrSafety(world)) {
                nextMove(world);
            }
        }
        lock.unlock();
    }

    public void makeHabitat(World world) {
        // individual for each animal
    }

    /**
     * Method for reducing code duplication of this sequence
     * Used to allow animals to make habitats
     * @param world
     * @param location
     * @return
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
     * Enables the animal to eat, reducing its hunger based on the nutritional value of the organism consumed.
     * The consumed organism is removed from the world.
     *
     * @param world The world in which the animal and its food source exist.
     */
    public void eat(World world, Organism organism) {
        // Deducts the animal's hunger with the nutritional value of the eaten organism.
        if (organism instanceof Grass grass) {
            hunger -= 2;
            // Deletes the eaten organism from the world.
            world.delete(world.getNonBlocking(world.getLocation(this)));
            // check
            System.out.println(this.getType() + " " + this.getId() + " ate " + grass.getId());

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
    public void breed(World world) throws Exception {
        Class<? extends Animal> animalClass = this.getClass();

        // Assuming idGenerator is available in the scope and hasCordyceps is either a field or a parameter
        Constructor<? extends Animal> constructor = animalClass.getDeclaredConstructor(IDGenerator.class, boolean.class);
        Animal cub = null;
        cub = constructor.newInstance(id_generator, has_cordyceps);

        world.add(cub);
        habitat.addResident(cub);
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

    public void moveAway(World world, Location danger_location) {
        Location current_location = world.getLocation(this);
        int dx = -Integer.compare(danger_location.getX(), current_location.getX());
        int dy = -Integer.compare(danger_location.getY(), current_location.getY());
        move(world, dx, dy);
    }

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
     * Enters the habitat that the animal is standing on, if it is empty, and it's their cave.
     * @param world The world in which the current events are happening.
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
     *
     * @param world The world where the cave is located.
     */
    public void exitHabitat(World world) {
        habitat.removeResident(this);
        world.setTile(getSpawnLocation(world), this);
        in_hiding = false;
        // overridden by bear
    }

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
     * Assigns a given cave to a wolf.
     * @param habitat The cave which is to be assigned a new wolf.
     */
    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
        has_habitat = true;
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

    public boolean ifDeadReturnTrue(){
        return damage_taken >= max_damage;
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

    public int getGracePeriod() {
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
    public Animal getMate() {
        return null;
    }

    public boolean isBedtime(World world){
        if (world.getCurrentTime() > bedtime) {
            if (world.getCurrentTime() < wakeup) {
                return true;
            }
        }
        return false;
    }
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
                    if (!friends.contains(animal)) {
                        if (animal.getTrophicLevel() > trophic_level) {
                            moveAway(world, location);
                            being_hunted = true;
                            return true;
                            // If the organism has a higher trophic level than itself.
                        }
                        if (animal.getTrophicLevel() < trophic_level && consumable_foods.contains(animal.getType())) {
                            eat(world, animal);
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

}


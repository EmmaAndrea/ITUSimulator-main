package ourcode.Organism.OrganismChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Obstacles.Habitat;
import ourcode.Organism.DinosaurEgg;
import ourcode.Organism.Gender;
import ourcode.Organism.Organism;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.*;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Organism.OrganismChildren.PlantChildren.Bush;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.Entity;
import ourcode.Setup.EntityFactory;
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
    protected int hunger; // Current hunger level, determines the need for food.
    protected int max_hunger; // Maximum hunger level before the animal succumbs to starvation.
    protected int steps_since_last_birth; // Steps elapsed since the animal last reproduced.
    protected boolean is_hiding; // Indicates if the animal is currently in hiding.
    protected Gender gender; // Gender of the animal, crucial for reproduction processes.
    protected ArrayList<String> consumable_foods; // Types of food that the animal can consume.
    protected int damage_taken; // Accumulated damage sustained by the animal.
    protected int power; // Determines the animal's strength in conflicts or predation.
    protected int max_damage; // Threshold of damage beyond which the animal cannot survive.
    protected boolean has_cordyceps; // Indicates infection with the cordyceps fungus.
    protected ArrayList<Animal> friends; // List of friendly animals, usually excluded from predation.
    protected Habitat habitat; // The animal's designated living or nesting area.
    protected boolean has_habitat; // Flag indicating whether the animal has an established habitat.
    protected int bedtime; // Simulation step at which the animal begins its resting period.
    protected int wakeup; // Simulation step at which the animal ends its resting period and becomes active.
    protected boolean pack_hunting; // Status indicating if the animal is engaged in group hunting activities.
    protected Location my_carcass_location; // Stores the location of the animal's carcass, if applicable.
    protected int carcass_count; // Counter for the number of carcasses 'associated' with the animal.

    /**
     * Constructor for the Animal class, initializing basic attributes and setting up the animal in the simulation.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the animal.
     * @param has_cordyceps Indicates whether the animal is infected with the cordyceps fungus.
     */
    public Animal(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator);
        hunger = 1; // Initial hunger level
        max_hunger = 1; // Max hunger level, set in subclasses
        steps_since_last_birth = 0; // Counter for breeding interval
        is_hiding = false; // Initial hiding state
        gender = new Random().nextBoolean() ? Male : Female; // Random gender assignment
        damage_taken = 0; // Initial damage state
        grace_period = 0; // Initial grace period
        this.has_cordyceps = has_cordyceps; // Cordyceps infection state
        friends = new ArrayList<>(); // List of friend animals
        habitat = null; // Initial habitat state
        has_habitat = false; // Habitat possession state
        bedtime = 0; // Initial bedtime
        wakeup = 0; // Initial wakeup time
        pack_hunting = false; // Pack hunting state
        carcass_count = 0; // Carcass interaction counter
    }

    /**
     * Executes the animal's behavior during each simulation step, including movement, feeding, and other species-specific actions.
     *
     * @param world The simulation world in which the animal exists.
     */
    @Override
    public void act(World world) {
        super.act(world);

        if (!is_hiding) grace_period = 0;

        steps_since_last_birth++;
        //if (damage_taken > 0) damage_taken -= 1;

        if (!is_hiding) {
            hunger++;
        }

        if (hasBeenKilled || (age >= max_age) || (hunger >= max_hunger) || isDead()) {

                grace_period = 1;
                dieAndBecomeCarcass(world);
                System.out.println(type + id + " died of natural causes");
                return;

        }

        if (isBedtime(world) && !is_hiding && habitat != null && world.contains(habitat)) {
            Location habitat_location = world.getLocation(habitat);
            goToHabitat(world, habitat_location);
        } else if (!isBedtime(world) && is_hiding) {
            exitHabitat(world);
            return;
        }

        // If the animal is currently inside its habitat.
        if (is_hiding) {
            if (checkBreedStats(world)) {
                try {
                    breed(world);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            return;
        }
        // If the animal is outside its habitat.
        else {
            // Make habitat if it doesn't have one.
            if (habitat == null) {
                makeHabitat(world);
            }
            if (!findFoodOrSafety(world)) {
                nextMove(world);
            }
        }
    }

    /**
     * Mpves an animal closer to its habitat if it is far away.
     * If it is nearby, it goes in.
     *
     * @param world
     * @param habitat_location
     */
    public void goToHabitat(World world, Location habitat_location) {
        if (distanceTo(world, habitat_location) > 0) {
            moveCloser(world, habitat_location);
        }
        if (distanceTo(world, habitat_location) < 2) {
            if (grace_period == 0) {
                grace_period = 1;
            }
            enterHabitat(world);
        }
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
     * Checks if there is empty spaces. Though, if there is grass, it will delete the grass and return true.
     *
     * @param world The simulation world.
     * @param location The location to check for available space.
     * @return true if the space is empty and suitable for habitat creation.
     */
    public boolean checkEmptySpace(World world, Location location) {
        if (world.containsNonBlocking(location)) {
            if (world.getNonBlocking(location) instanceof Grass grass) {
                world.delete(grass);
                return true;
            }
        } else return true;
        return false;
    }

    /**
     * Consumes a specified organism, reducing hunger and potentially affecting health.
     *
     * @param world    The simulation world where feeding occurs.
     * @param organism The organism that the animal consumes.
     */
    public void eat(World world, Organism organism) {
        int nutrition = organism.getNutritionalValue();

        if (organism instanceof Grass || organism instanceof Bush) {
            hunger -= nutrition;
            if (organism instanceof Grass grass) {
                world.delete(grass);
            }
        } else if (organism instanceof Carcass carcass) {
                    if (carcass.isRotten) {
                        damage_taken -= 4;
                    }
                    if (carcass.has_fungus) {
                        infect();
                    }
                    else {
                        int nutritionChange = pack_hunting ? 2 : 4;
                        hunger -= nutritionChange;
                        carcass.setNutrition(nutritionChange);
                        carcass.addEatenBy(this);
                        if (damage_taken >= nutritionChange) {
                            damage_taken -= nutritionChange;
                        }
                    }
                    carcass.setGracePeriod(0);

        }
    }

    /**
     * Initiates the breeding process if conditions are met, resulting in the creation of offspring.
     * Uses similar switch case to program runner.
     * @param world The simulation world where breeding occurs.
     */
    public void breed(World world) throws Exception {
        int max_cubs = 12; // Maximum number of cubs that can be born.
        int family_size = 0;

        for (Object o : world.getEntities().keySet()) {
            if (this.getClass().isInstance(o)) {
                family_size++;
            }
        }
        // Calculate amount of cubs, ensures amount_of_cubs doesn't go below 0.
        int amount_of_cubs = Math.max(max_cubs - family_size, 1);

        for (int i = 0; i < amount_of_cubs; i++) {
            if (!spawnEntity(world, type, 1, has_cordyceps)){
                break;
            }
        }
        steps_since_last_birth = 0;
    }

    /**
     * Spawns entities of a given type in the simulation world. The method determines the entity type,
     * applies the special condition cordyceps, and uses the appropriate factory method
     * to create entities.
     *
     * @param world       The simulation world where the entity will be spawned.
     * @param entityType  The type of entity to spawn (e.g., "rabbit", "grass", "burrow").
     * @param amount      The number of entities of the specified type to spawn.
     */
    public boolean spawnEntity(World world, String entityType, int amount, boolean hasCordyceps) {
        switch (entityType) {
            case "rabbit":
                if (!spawnEntities(world, () -> new Rabbit(id_generator, hasCordyceps))){
                    return false;
                }
                return true;

            case "wolf":
                if (!spawnEntities(world, () -> new Wolf(id_generator, hasCordyceps))){
                    return false;
                }
                return true;

            case "bear":
                if (!spawnEntities(world, () -> new Bear(id_generator, hasCordyceps))){
                    return false;
                }
                return true;

            // Include other cases as needed based on your entity types
            default:
                System.out.println("Unknown entity type: " + entityType);
        }
        return true;
    }

    /**
     * Spawns an entity in the world as per the given factory method.
     * Puts the created cub in the world
     *
     * @param world The simulation world where entities are to be spawned.
     * @param factory A factory method to create instances of the entity.
     */
    public boolean spawnEntities(World world, EntityFactory factory) {
        Entity entity = factory.create();
        Animal cub = (Animal) entity;

        if (!putCubInWorld(world, cub)){
            return false;
        }
        return true;
    }

    /**
     * This method is used to put the cubs in the world
     * It makes sure the parents don't eat the cubs, as well as making sure it stays in its habitat until wakeup time.
     * @param world
     * @param cub
     * @return
     */
    public boolean putCubInWorld(World world, Animal cub){
        world.add(cub);
        habitat.addResident(cub);
        cub.setGracePeriod(1);
        cub.setHabitat(habitat);
        cub.setInHiding();
        System.out.println(type + " made cub");

        if (cub instanceof SocialPredator s_cub) {
            if (this instanceof SocialPredator this_s){
                this_s.getMyAlpha().addToPack(s_cub);
            }
        }

        return true;
    }

    /**
     * Checks whether the conditions are favorable for breeding, such as presence of a mate and suitable age.
     *
     * @param world The simulation world where breeding might occur.
     * @return True if the animal can breed, false otherwise.
     */
    public boolean checkBreedStats(World world) {
        // Only females can give birth.
        if (gender == Female) {

            // If animal is in breeding age.
            if (age >= max_age * 0.15 && age <= max_age * 0.85) {

                // If it's not too much time since they last gave birthed.
                if (steps_since_last_birth >= 10) {

                    return checkHasBreedMate(world);

                }
            }
        }
        // If any of these are false, return false.
        return false;
    }

    /**
     * This method checks the female has a mate to breed with. This method is different for each type of animal.
     * @param world
     * @return
     */
    public boolean checkHasBreedMate(World world){
        if (has_habitat && habitat.getResidents().size() > 1) {
            for (Animal animal : habitat.getResidents()) {
                if (animal.getGender() == Male) {
                    return true;
                }
            }
        } return false;
    }

    /**
     * Determines the most suitable next action for the animal, such as moving towards food or away from danger.
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
     * Moves the animal towards a specified target location, considering obstacles and other factors.
     *
     * @param world           The simulation world where the movement occurs.
     * @param target_location The destination towards which the animal is moving.
     */
    public void moveCloser(World world, Location target_location) {
        Location current_location = world.getLocation(this);
        int dx = Integer.compare(target_location.getX(), current_location.getX());
        int dy = Integer.compare(target_location.getY(), current_location.getY());
        move(world, dx, dy);

    }

    /**
     * Moves the animal away from a specified danger location, such as predators or harmful environments.
     *
     * @param world           The simulation world where the movement occurs.
     * @param danger_location The location from which the animal should distance itself.
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
                if (world.getLocation(this) != current_location){
                    return;
                }
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
        if (location != null && !is_hiding && world.contains(this)) {
            Location currentLocation = world.getLocation(this);

            // Calculate the absolute difference in x and y coordinates
            int step_x = Math.abs(location.getX() - currentLocation.getX());
            int step_y = Math.abs(location.getY() - currentLocation.getY());

            // Return the sum of x and y steps
            return step_x + step_y;
        } return 0;
    }

    /**
     * Enters the animal's habitat, such as a burrow or nest, making it hidden in the simulation.
     *
     * @param world The simulation world where the habitat is located.
     */
    public void enterHabitat(World world) {

        // add animal to list of residents of habitat
        habitat.addResident(this);

        // remove animal from map
        world.remove(this);

        // goes into hiding
        is_hiding = true;

        // overridden by bear
    }


    /**
     * Exits the animal's habitat, making it visible and active again in the simulation world.
     *
     * @param world The simulation world where the habitat exit occurs.
     */
    public void exitHabitat(World world) {
        habitat.removeResident(this);
        Location spawn_location = getSpawnLocation(world);
        if (spawn_location == null){
            System.out.println(type + id + " could not leave habitat");
            return;
        }
        try {
            world.setTile(spawn_location, this);
        } catch (Exception e) {
            return;
        }
        is_hiding = false;
        System.out.println(type + id + "left habitat");
        // overridden by bear
    }

    /**
     * Determines the best location for the animal to spawn when exiting its habitat.
     *
     * @param world The simulation world where the habitat is located.
     * @return The location where the animal should spawn upon exiting its habitat.
     */
    public Location getSpawnLocation(World world) {
        if (!world.contains(habitat)) return null;
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
     * Assigns a specific habitat to the animal.
     *
     * @param habitat The habitat to be assigned to the animal.
     */
    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
        has_habitat = true;
    }

    /**
     * Retrieves the animal's gender, important for breeding and other behaviors.
     *
     * @return The gender of the animal.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Initiates an attack on another animal in the simulation.
     *
     * @param world  The simulation world where the attack occurs.
     * @param animal The target animal of the attack.
     */
    public void attack(World world, Animal animal) {
    }

    /**
     * Inflicts damage on the animal, affecting its health and survival.
     *
     * @param power The amount of damage to inflict on the animal.
     */
    public void damage(int power) {
        damage_taken += power;
        System.out.println(type + id + " was hit for " + power + " damage");
    }

    /**
     * Checks if the animal has died due to excessive damage.
     *
     * @return True if the animal is dead, false otherwise.
     */
    public boolean isDead() {
        return damage_taken >= max_damage;
    }

    /**
     * Retrieves the grace period of the animal, used in specific scenarios.
     *
     * @return The grace period of the animal.
     */
    public int getGracePeriod() {
        return grace_period;
    }

    /**
     * Manually sets the gender of the animal, typically for testing purposes.
     *
     * @param gender The new gender to assign to the animal.
     */
    public void setGender(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            this.gender = Gender.Male;
        } else {
            this.gender = Gender.Female;
        }
    }

    /**
     * Checks if it is currently the animal's bedtime based on the world's time.
     *
     * @param world The simulation world where the time check occurs.
     * @return True if it's bedtime for the animal, false otherwise.
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
        if (!world.contains(this) || is_hiding) {
            System.out.println("Warning: Animal not on the map");
            // returns true to stop act
            return true;
        }

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
                        if (animal.getGracePeriod() == 0) {
                            if (!friends.contains(animal)) {
                                if (animal.getType().equals(type)) {
                                    if (sameTypeInteraction(world, animal)) {
                                        return true;
                                    } else continue;
                                } else if (differentTypeInteraction(world, animal)) {
                                    return true;
                                } else continue;
                            } continue;
                        }
                    }
                    // if the object is a carcass this will eat part of it.
                    if (object instanceof Carcass carcass) {
                        if (this instanceof Predator) {
                            if (hunger >= 4) {
                                if (carcass.isRotten) {
                                    damage_taken -= 4;
                                    moveAway(world, world.getLocation(carcass));
                                }
                                if (carcass.has_fungus) {
                                    infect();
                                    moveAway(world, world.getLocation(carcass));
                                }
                                else {
                                    if (carcass.getGracePeriod() == 0) {
                                        carcass.setGracePeriod(1);
                                        eat(world, carcass);
                                    }
                                }
                                return true;
                            }
                        }
                    }
                }
            }

            if (world.containsNonBlocking(world.getLocation(this))) {
                Object object = world.getNonBlocking(world.getLocation(this));
                if (object instanceof Grass grass) {
                    if (consumable_foods.contains(grass.getType())) {
                        if (hunger >= grass.getNutritionalValue()) {
                            eat(world, grass);
                            return true;
                        }
                    }
                }
            }
            // Next, check for plants.
            for (Location location : surrounding_tiles) {
                if (!world.isTileEmpty(location)) {
                    // Declares a new object at given location.
                    Object object = world.getTile(location);

                    // Casts object to Organism class and checks if the object is an Organism.
                    if (object instanceof Bush bush) {
                        if (consumable_foods.contains("bush")) {
                            if (hunger > 2 && bush.getBerriesAmount() > 2) {
                                bush.eatBerries();
                                eat(world, bush);
                            }
                            System.out.println(type + " ate berries");
                            return true;
                        }
                    } else if (world.containsNonBlocking(location)) {
                        object = world.getNonBlocking(location);
                        if (object instanceof Grass grass) {
                            if (consumable_foods.contains(grass.getType())) {
                                if (hunger >= grass.getNutritionalValue()) {
                                    moveCloser(world, location);
                                    eat(world, grass);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

            // If no food or danger is found, return false.
            return false;
        } catch (IllegalArgumentException iae) {
            System.out.println(iae + "this has been eaten");
            // returns true to stop act
            return true;
        }
    }

    /**
     * Retrieves the amount of damage this animal has taken.
     *
     * @return The damage taken.
     */
    public int getDamageTaken() {
        return damage_taken;
    }

    /**
     * Defines interaction with another animal of the same type.
     * This method is a placeholder and should be overridden in subclasses for specific interactions.
     *
     * @param world The simulation world.
     * @param animal The animal of the same type to interact with.
     * @return False by default. Should return true if an interaction occurs in an overridden method.
     */
    public boolean sameTypeInteraction(World world, Animal animal) {
        return false;
    }

    /**
     * Defines interaction with another animal of a different type.
     * This method handles interactions based on the trophic level and hunger of the animal.
     *
     * @param world The simulation world.
     * @param animal The animal of a different type to interact with.
     * @return True if an interaction occurs (either moving away or attacking), false otherwise.
     */
    public boolean differentTypeInteraction(World world, Animal animal){
        // If the organism has a higher trophic level than itself, this will move away.
        if (animal.getTrophicLevel() > trophic_level) {
            moveAway(world, world.getLocation(animal));
            return true;

        }
        // Otherwise this will attack.
        if (animal.getTrophicLevel() <= trophic_level && consumable_foods.contains(animal.getType())) {
            if (hunger >= animal.getNutritionalValue()) {
                if (animal.getGracePeriod() == 0) {
                    attack(world, animal);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Transforms the organism into a carcass upon its death. The organism is replaced by a carcass
     * at its current location in the simulation world. The new carcass retains the nutritional value,
     * type, and cordyceps status of the original organism.
     *
     * @param world The simulation world where the transformation occurs.
     */
    public void dieAndBecomeCarcass(World world) {
        grace_period = 1;
        if (is_hiding) {
            world.delete(this);
        } else {
            if (my_carcass_location == null) {
                my_carcass_location = world.getLocation(this);
            }
            Carcass carcass = new Carcass(id_generator, nutritional_value, type, has_cordyceps);
            carcass.setGracePeriod(1);
            world.delete(this);
            if (world.isTileEmpty(my_carcass_location)) world.setTile(my_carcass_location, carcass);
        }
    }

    /**
     * Sets the animal's state to 'in hiding'.
     * This method is used to mark the animal as hidden within its habitat or other shelter,
     * making it invisible and inactive in the simulation world until it exits the hiding state.
     */
    public void setInHiding(){
        is_hiding = true;
    }

    /**
     * Determines if the animal is infected with cordyceps, affecting its health and behavior.
     *
     * @return True if the animal is infected with the cordyceps fungus, false otherwise.
     */
    public boolean hasCordyceps() {
        return has_cordyceps;
    }

    /**
     * Infects the animal with the cordyceps fungus, altering its lifespan and behavior.
     */
    public void infect() {
        has_cordyceps = true;
        max_age = max_age - 18;
    }

    /**
     * Checks if there is a habitat of an enemy animal nearby.
     * If an enemy habitat is found, the animal moves away to avoid danger.
     *
     * @param world The simulation world to check for enemy habitats.
     * @return True if an enemy habitat is nearby, false otherwise.
     */
    public boolean enemyHabitatNearby(World world) {
        Location current;
        try {
            current = world.getLocation(this);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage() + " animal has been eaten");
            return true;
        }

        // checks standing on location for enemy habitat
        if (world.containsNonBlocking(current)) {
            if (world.getNonBlocking(current) instanceof Habitat other_habitat) {
                if (habitat != other_habitat) {
                    moveAway(world, current);
                    moveAway(world, current);
                    moveAway(world, current);
                    return true;
                }
            }
        }

        // check surrounding locations for enemy habitat
        for (Location potential_enemy_location: world.getSurroundingTiles(current,  2)) {
            if (world.containsNonBlocking(potential_enemy_location)) {
                if (world.getNonBlocking(potential_enemy_location) instanceof Habitat other_habitat) {
                    if (habitat != other_habitat) {
                        moveAway(world, potential_enemy_location);
                        moveAway(world, potential_enemy_location);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the animal is currently in hiding.
     * Being in hiding affects the animal's visibility and interactions in the simulation.
     *
     * @return True if the animal is hiding, false otherwise.
     */
    public boolean isHiding() {
        return is_hiding;
    }

    /**
     * Retrieves the current hunger level of the animal.
     * Hunger level influences the animal's need to find food.
     *
     * @return The current hunger level of the animal.
     */
    public int getHunger() {
        return hunger;
    }

    /**
     * Sets the hunger level of the animal to a specified value.
     * This method can be used to simulate feeding or starvation effects.
     *
     * @param i The new hunger level to be set.
     */
    public void setHunger(int i) {
        hunger = i;
    }

    /**
     * Determines if the animal is close to death due to damage taken.
     * This method is useful for making decisions related to health and survival.
     *
     * @return True if the animal is close to death, false otherwise.
     */
    public boolean isCloseToDeath(){
        return max_damage - damage_taken < 4;
    }

    /**
     * Marks the animal as killed, changing its state and interactions in the simulation.
     */
    public void setHasBeenKilled(){
        hasBeenKilled = true;
    }

    /**
     * Sets the location of the carcass for the animal when it dies.
     * This method is used to manage the placement of carcasses in the simulation.
     *
     * @param location The location where the carcass will be placed.
     */
    public void setCarcassLocation(Location location) {
        if (my_carcass_location == null) {
            my_carcass_location = location;
        }
    }

    /**
     * Retrieves the location of the animal's carcass.
     * This method is used to find the carcass of the animal after its death.
     *
     * @return The location of the carcass, or null if it's not available.
     */
    public Location getCarcassLocation() {
        if (carcass_count == 0) {
            carcass_count++;
            return my_carcass_location;
        } else return null;
    }
}
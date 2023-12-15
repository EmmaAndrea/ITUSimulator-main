package ourcode.Organism.OrganismChildren;

import itumulator.world.World;
import itumulator.world.Location;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

/**
 * The Animal class gives the abstraction of an Animal. An animal inherits from the Organism
 *  class and has the field hunger.
 */
public abstract class Animal extends Organism {
    public int hunger;
    public int max_hunger;

    /**
     * The constructor of an Animal.
     */
    public Animal(IDGenerator original_id_generator, String type) {
        super(original_id_generator, type); // life_counter = 1;
        hunger = 1;
        max_hunger = 1;
    }

    /**
     * Spawns an Animal at a random location.
     */
    public void eat(Organism organism) {
        // Eats another organism and deducts hunger by the nutritional value of the eaten organism.
        hunger -= organism.getNutritionalValue();

        // delete eaten organism from world
        // our code here
    }

    /**
     * Calls the act method from Actor and overrides the method. Increases hunger by 1.
     */
    public void act(World world) {
        super.act(world);

        // Animal gains hunger by 1.
        hunger++;

<<<<<<< Updated upstream
        // An animal can die of hunger if it is hungrier than its max hunger.
        if (hunger > max_hunger) {
=======
        steps_since_last_birth++;
        //if (damage_taken > 0) damage_taken -= 1;

        if (!is_hiding) {
            hunger++;
        }

        if (hasBeenKilled || age >= max_age || hunger >= max_hunger) {
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

        lock.lock();

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
            } if (!findFoodOrSafety(world)) {
                nextMove(world);
            }
        }
        lock.unlock();
    }

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
     * @param world    The simulation world in which breeding occurs.
     * @param organism The organism to be eaten.
     */
    public void eat(World world, Organism organism) {
        int nutrition = organism.getNutritionalValue();

        if (organism instanceof Grass || organism instanceof Bush) {
            hunger -= nutrition;
            if (organism.getType().equals("grass")) {
                world.delete(world.getNonBlocking(world.getLocation(this)));
            }
        } else if (organism instanceof Carcass carcass) {
            synchronized (carcass) {
                //if (carcass.getGracePeriod() == 0) {
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
                //}
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

        Constructor<? extends Animal> constructor = animalClass.getDeclaredConstructor(IDGenerator.class, boolean.class);

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
            Animal cub = constructor.newInstance(id_generator, has_cordyceps);
            putCubInWorld(world, cub);
        }

        steps_since_last_birth = 0;
    }

    public void putCubInWorld(World world, Animal cub){
        world.add(cub);
        habitat.addResident(cub);
        cub.setGracePeriod(1);
        cub.setHabitat(habitat);
        cub.setInHiding();
        System.out.println(type + " made cub");

        if (cub instanceof Wolf wolf_cub) {
            if (this instanceof Wolf this_wolf){
                this_wolf.getMyAlpha().addWolfToPack(wolf_cub);
            }
        }
    }

    /**
     * Checks whether the conditions for breeding are met, such as gender compatibility, age, and proximity to a potential mate.
     *
     * @param world The simulation world where breeding might occur.
     * @return true if the animal can breed, false otherwise.
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
        is_hiding = true;

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
        Location spawn_location = getSpawnLocation(world);
        if (spawn_location == null){
            System.out.println(type + id + " could not leave habitat");
            return;
        }
        try {
            world.setTile(spawn_location, this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        is_hiding = false;
        System.out.println(type + id + "left habitat");
        // overridden by bear
    }

    /**
     * Determines the spawn location for the animal when exiting its habitat.
     * This location is chosen from available spots surrounding the habitat.
     *
     * @param world The simulation world where the habitat is located.
     * @return The chosen location for the animal to spawn.
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
    public void attack(World world, Animal animal) {
    }

    /**
     * Inflicts damage on the animal.
     * This method increases the damage taken by the animal, affecting its health and survival.
     *
     * @param power The amount of damage to be inflicted on the animal.
     */
    public void damage(int power) {
        damage_taken += power;
        System.out.println(type + id + " was hit for " + power + " damage");
    }

    /**
     * Checks if the animal is dead due to sustained damage.
     * Returns true if the damage taken exceeds the maximum damage threshold.
     *
     * @return True if the animal is dead, false otherwise.
     */
    public boolean isDead() {
        return damage_taken >= max_damage;
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
        if (!world.contains(this) || is_hiding) {
            System.out.println("Warning: Animal not on the map");
            // returns true to stop act
            return true;
        }

        lock.lock();
        try {
            // Get surrounding tiles to iterate through them.
            Set<Location> surrounding_tiles = world.getSurroundingTiles(world.getLocation(this), 1);

            // First, check for blocking organisms.
            // Though, if there is an animal of higher trophic level, move away from this animal.
            for (Location location : surrounding_tiles) {

                // If the tile at given location is empty.
                if (world.isTileEmpty(location)) {
                    return false;
                }

                // Declares a new object at given location.
                Object object = world.getTile(location);

                // Casts object to Organism class and checks if the object is an Organism.
                if (object instanceof Animal animal) {
                    if (animal.getGracePeriod() == 0) {
                        return false;
                    }

                    if (friends.contains(animal)) {
                        return true;
                    }

                    // wolves act differently than other animals when interacting with each other.
                    if (animal.getType().equals(type)) {
                        sameTypeInteraction(world, animal);
                        return true;
                    }

                    // If the organism has a higher trophic level than itself, this will move away.
                    if (animal.getTrophicLevel() > trophic_level) {
                        moveAway(world, location);
                        lock.unlock();
                        return true;
                    }
                    if (hunger >= animal.getNutritionalValue()) {
                        if (animal.getGracePeriod() == 0) {
                            animal.setGracePeriod(1);
                            attack(world, animal);
                            lock.unlock();
                            return true;
                        } else continue;
                    }

            }

            // if the object is a carcass this will eat part of it.
            if (object instanceof Carcass carcass) {
                if (this instanceof Predator) {
                    if (hunger >= 4) {
                        eat(world, carcass);
                        lock.unlock();
                        return true;
                    }
                }
            }
        }

            // Next, check for non-blocking organisms like grass.
            for (Location location : surrounding_tiles) {
                if (!world.containsNonBlocking(location)) {
                    return false;
                }
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
                } else if (!world.isTileEmpty(location)) {
                    // Declares a new object at given location.
                    Object ob = world.getTile(location);

                    // Casts object to Organism class and checks if the object is an Organism.
                    if (ob instanceof Bush bush) {
                        if (consumable_foods.contains("bush")) {
                            if (hunger > 2 && bush.getBerriesAmount() > 2) {
                                bush.eatBerries();
                                eat(world, bush);
                            }
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
        } catch (IllegalArgumentException ex) {
            System.out.println(ex + "this has been eaten");
            lock.unlock();
            // returns true to stop act
            return true;
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

    public void sameTypeInteraction(World world, Animal animal) {
        attack(world, animal);
    }

    /**
     * Transforms the organism into a carcass upon its death. The organism is replaced by a carcass
     * at its current location in the simulation world. The new carcass retains the nutritional value,
     * type, and cordyceps status of the original organism.
     *
     * @param world The simulation world where the transformation occurs.
     */
    public Carcass dieAndBecomeCarcass(World world) {
        grace_period = 1;
        if (is_hiding) {
>>>>>>> Stashed changes
            world.delete(this);
        }

        // Sets the new location of the animal to be the same, if there is nowhere to go
        Location new_location = world.getCurrentLocation();

        // Finds a random free tile.
        for (Location location : world.getEmptySurroundingTiles(world.getCurrentLocation())) {
            new_location = location;
        }
        // Moves animal to this new tile
        world.move(this, new_location);
    }

        /**
         * Gives the ability to breed with a partner, if there exists another one of its kind.
         */
    public void breed(World world) {
        // only if there exists 2 or more rabbits - check entities list?
        // only if the rabbits are 20 steps old (modulo 20, only every 20 steps)
        // only if there is space for one more rabbit
        // check these factors in the act method

        // calls spawn() method from Organism class
        super.spawn(world); // should make new rabbit with new id, but doesn't

    }

    /**
     * A method for creating a new burrow from the Burrow class.
     */
    public void makeBurrow(){

    }

    public int getHunger() {
        return hunger;
    }

    public int getMaxHunger() {
        return max_hunger;
    }

    public void deductHunger(int nutritional_value) {
        hunger -= nutritional_value;
    }
}

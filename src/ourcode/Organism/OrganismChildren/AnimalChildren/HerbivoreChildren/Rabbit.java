package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.OrganismChildren.AnimalChildren.Prey;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a Rabbit entity in the simulated world.
 * Rabbits are a type of Herbivore, characterized by their interactions with burrows.
 * They have unique behaviors like seeking burrows for shelter and foraging for food.
 */
public class Rabbit extends Prey implements DynamicDisplayInformationProvider {

    ArrayList <Burrow> my_burrows;
    boolean has_burrow; // Indicates whether the rabbit has a burrow.
    /**
     * Constructs a Rabbit with a unique identifier, initializes its basic characteristics and
     * sets up its relationship with burrows.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the rabbit.
     * @param has_cordyceps If the animal is being born in an infected state (with cordyceps).
     */
    public Rabbit(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator, has_cordyceps);
        type = "rabbit";
        max_hunger = 18;
        nutritional_value = 4;
        max_age = 100;
        trophic_level = 2;
        power = 2;
        // Specifics for rabbit.
        has_burrow = false;
        max_damage = 8;
        consumable_foods = new ArrayList<>(List.of("grass")); // Can only eat grass.
        this.has_cordyceps = has_cordyceps;
    }

    @Override
    public void act(World world) {
        super.act(world);

        if (grace_period == 1 && !in_hiding) {
            grace_period = 0;
        }

        boolean isCloseToBurrow = false;

        boolean isNight = timeToNight(world) == 0;

        // if it is in the burrow
        if (!isNight && has_burrow && in_hiding) {
            exitBurrow(world);
            return;
        }

        // get a burrow
        if (!has_burrow) {
            if (linkBurrow(world)) {
                return;
            }
        }
        // if it is not in its burrow
        if (!in_hiding && has_burrow && !being_eaten) {
            if (distanceTo(world, world.getLocation(my_burrows.get(0))) <= 1) {
                isCloseToBurrow = true;
            }
            if (isNight) {

                if (isCloseToBurrow && !being_eaten) {
                    enterBurrow(world);

                } else {
                    moveCloser(world, world.getLocation(my_burrows.get(0)));
                }

            } else if (timeToNight(world) < 5 && !being_eaten) {
                moveCloser(world, world.getLocation(my_burrows.get(0)));
                return;
            }

            else if (!in_hiding && !being_eaten) {
                nextMove(world);
            }
        }
    }

    /**
     * Spawns a rabbit.
     */
    public void spawn(World world) {
        super.spawn(world);
    }

    /**
     * Links the animal to an existing burrow. This method updates the animal's burrow list and sets
     * its 'has_burrow' flag to true.
     */
    private boolean linkBurrow(World world) {
        Location location = world.getLocation(this);
        if (world.containsNonBlocking(location)){
            if(world.getNonBlocking(location) instanceof Burrow burrow){
                my_burrows = new ArrayList<>();
                my_burrows.add(burrow);
                has_burrow = true;
                return true;
            }
        } return false;
    }

    /**
     * Creates a new burrow at the specified location and updates the animal's state accordingly.
     * This method also updates the ID generator maps with the new burrow's location and ID.
     * The animal is then prompted to make its next move in the world.
     *
     * @param world The simulation world where the new burrow is created.
     */
    @Override
    public void makeHabitat(World world) {
        if (age < 5) return;
        Location location = world.getLocation(this);
        Burrow newBurrow = new Burrow(id_generator);
        world.setTile(location, newBurrow);

        my_burrows = new ArrayList<>();
        my_burrows.add(newBurrow);
        has_burrow = true;

        id_generator.addBurrowToLocationMap(world.getLocation(this), newBurrow);
        id_generator.addLocationToIdMap(world.getLocation(this), newBurrow.getId());

        nextMove(world);
    }

    /**
     * The rabbit enters a burrow, removing itself from the visible world and adding itself to the burrow's list of residents.
     *
     * @param world The simulation world where the burrow is located.
     */
    public void enterBurrow(World world) {
        // If it has not been assigned a burrow.
        grace_period = 1;

        if (my_burrows.isEmpty()) {

            // Sets its personal burrow to be the burrow it enters.
            my_burrows.add(id_generator.getBurrow(world.getLocation(this)));

            has_burrow = true;
        }
        // add burrow to list of residents of burrow
        my_burrows.get(0).addResident(this);

        // remove rabbit from map
        world.remove(this);

        // goes into hiding
        in_hiding = true;
    }

    /**
     * The rabbit exits its burrow and reappears in the simulation world at a location near the burrow.
     *
     * @param world The simulation world where the burrow is located.
     */
    public void exitBurrow(World world) {
        // Retrieve current location

        Location burrow_location = world.getLocation(my_burrows.get(0));

        // Makes list of possible spawn locations (locations with no blocking elements).
        ArrayList<Location> possible_spawn_locations = new ArrayList<>();
        for (Location surroundingTile : world.getSurroundingTiles(burrow_location, 1)) {
            if (world.isTileEmpty(surroundingTile)) {
                possible_spawn_locations.add(surroundingTile);
            }
        }

        // Removes itself from possible locations to spawn.
        possible_spawn_locations.remove(burrow_location);

        // Return null value if there is no empty location (to be used in if statement in larger method to check).
        if (possible_spawn_locations.isEmpty()) {
            return;
        }
        // Finds a random index in this list of locations.
        Random random = new Random();
        int random_index = random.nextInt(0, possible_spawn_locations.size());

        world.setTile(possible_spawn_locations.get(random_index), this);
        // Rabbit is now outside of burrow.
        in_hiding = false;
    }

    /**
     * Retrieves the current hunger level of the rabbit. This can be used to determine the rabbit's need for food
     * and possibly influence its behavior in the simulation, such as seeking food sources.
     *
     * @return The current hunger level of the rabbit.
     */
    public int getHunger() {
        return hunger;
    }

    /**
     * Acquires a reference to the rabbit's second burrow, if it exists. This method assumes that the rabbit
     * might have more than one burrow and returns the second one in its list. It's used in scenarios where
     * the rabbit might need to choose between multiple burrows.
     *
     * @return The second Burrow in the rabbit's list of burrows, if it exists; otherwise, it may throw an exception
     * or return null, depending on how the list is managed.
     */
    public Burrow acquireBurrow() {
        return my_burrows.get(1);
    }

    /**
     * Provides the visual representation of the rabbit in the simulation. Changes appearance based on the rabbit's age.
     *
     * @return DisplayInformation containing the color and icon representation of the rabbit.
     */
    @Override
    public DisplayInformation getInformation() {
        if (age >= 15) {
            return new DisplayInformation(Color.black, "rabbit-large");
        } else {
            return new DisplayInformation(Color.black, "rabbit-small");
        }
    }

    public int getDamageTaken() {
        return damage_taken;
    }
    public int getPower(){
        return power;
    }
    @Override
    public int getGracePeriod(){
        return grace_period;
    }

}
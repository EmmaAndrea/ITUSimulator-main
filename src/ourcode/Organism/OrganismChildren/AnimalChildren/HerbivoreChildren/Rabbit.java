package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.OrganismChildren.AnimalChildren.Herbivore;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a Rabbit entity in the simulated world.
 * Rabbits are a type of Herbivore.
 * Each Rabbit has a unique identifier and a maximum hunger level as well as a life_counter.
 * The behavior of a Rabbit in the simulated world can be defined in the {@code act} method.
 */

public class Rabbit extends Herbivore implements DynamicDisplayInformationProvider {

    ArrayList <Burrow> my_burrows;
    boolean has_burrow;

    /**
     * Has max_hunger, that dictates how hungry it can be
     */
    public Rabbit(IDGenerator original_id_generator) {
        super(original_id_generator);
        type = "rabbit";
        max_hunger = 18;
        nutritional_value = 4;
        max_age = 100;

        // Specifics for rabbit.
        has_burrow = false;
        my_burrows = new ArrayList<>();
    }

    /**
     * Spawns a rabbit.
     */
    public void spawn(World world) {
        super.spawn(world);
    }

    /**
     * Calls herbivoreAct.
     * If it doesn't have a burrow, make a burrow.
     * Moves closer to burrow
     */
    public void herbivoreAct(World world) {
        // Gets older and hungrier and dies if too old or hungry.
        super.herbivoreAct(world);

        boolean isCloseToBurrow = false;

        boolean isNight = timeToNight(world) == 0;

        // get a burrow
        if (my_burrows.isEmpty()){
            acquireBurrow(world);
            if (my_burrows.isEmpty()){
                nextMove(world);
                return;
            }
        }

        // if it is not in its burrow
        if (!in_hiding) {
            if (distanceTo(world, world.getLocation(my_burrows.get(0))) <= 1) {
                isCloseToBurrow = true;

            } if (isNight) {

                if (isCloseToBurrow) {
                    enterBurrow(world);

                } else moveCloser(world, world.getLocation(my_burrows.get(0))); return;

            } else if (timeToNight(world) < 5) moveCloser(world, world.getLocation(my_burrows.get(0))); return;

            // if it is in the burrow
        } else if (!isNight) exitBurrow(world);

        if (!in_hiding) nextMove(world);

    }

    /**
     * Make burrow from location where the rabbit currently is.
     */
    public void acquireBurrow(World world) {
        // Removes whatever nonblocking it’s standing on if there is one.
        if (age > 5 ){
            if (world.containsNonBlocking(world.getLocation(this))) {
                // Remove the nonblocking tile from id_generators lists
                if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {
                    world.delete(world.getNonBlocking(world.getLocation(this)));
                    // Instantiates new burrow and sets the tile with current location.
                } else {
                my_burrows.add(0, id_generator.getBurrow(world.getLocation(this)));
                has_burrow = true;
                return;
                }
            }
            Burrow newburrow = new Burrow(id_generator);
            world.setTile(world.getLocation(this), newburrow);

            // Rabbit now has a personal burrow.
            my_burrows.add(newburrow);

            // Set rabbit’s boolean has_burrow to be true.
            has_burrow = true;

            // Add to maps to keep track of where things are.
            id_generator.addBurrowToLocationMap(world.getLocation(this), my_burrows.get(0));
            id_generator.addLocationToIdMap(world.getLocation(my_burrows.get(0)), my_burrows.get(0).getId());

            nextMove(world);

        } else {
            if (world.containsNonBlocking(world.getLocation(this))) {
                if (world.getNonBlocking(world.getLocation(this)) instanceof Burrow) {
                    my_burrows.add(0, id_generator.getBurrow(world.getLocation(this)));
                    has_burrow = true;
                }
            }
        }
    }

    /**
     * Puts a rabbit inside a burrow.
     * 'Removes' them from the world.
     * Adds them to the lists of residents of the particular burrow.
     * If the rabbit didn't beforehand have a personal burrow, it sets this burrow as the personal burrow.
     */
    public void enterBurrow(World world) {
        // If it has not been assigned a burrow.
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
     * Puts a rabbit at the location where the burrow is located
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

        // rabbits jump up at same time in same place
    }

    public int getHunger() {
        return hunger;
    }

    public Burrow acquireBurrow() {
        return my_burrows.get(1);
    }

    /**
     * Graphics of the rabbit.
     * Becomes an old rabbit after 20 steps.
     */
    @Override
    public DisplayInformation getInformation() {
        if (age >= 15) {
            return new DisplayInformation(Color.black, "rabbit-large");
        } else {
            return new DisplayInformation(Color.black, "rabbit-small");
        }
    }
}
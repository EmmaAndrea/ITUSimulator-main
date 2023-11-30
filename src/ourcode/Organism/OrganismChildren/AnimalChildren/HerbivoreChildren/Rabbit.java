package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.OrganismChildren.AnimalChildren.Herbivore;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a Rabbit entity in the simulated world.
 * Rabbits are a type of Herbivore.
 * Each Rabbit has a unique identifier and a maximum hunger level as well as a life_counter.
 * The behavior of a Rabbit in the simulated world can be defined in the {@code act} method.
 */

public class Rabbit extends Herbivore {

    Burrow burrow;
    boolean has_burrow;

    /**
     * Has max_hunger, that dictates how hungry it can be
     */
    public Rabbit(IDGenerator original_id_generator) {
        super(original_id_generator);
        type = "rabbit";
        max_hunger = 10;
        nutritional_value = 3;
        max_age = 100;
        has_burrow = false;
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

        // If the rabbit has died, then stop method.
        if (!world.contains(this)) {
            return;
        }

        boolean isNight = timeToNight(world) == 0;
        boolean isCloseToBurrow = burrow != null && distanceTo(world, burrow.getLocation()) <= 1;

        // Prioritize entering the burrow at night if close to it.
        if (isNight && isCloseToBurrow) {
            enterBurrow(world, burrow);
            return;
        }

        // Handle daytime burrow exit.
        if (burrow != null && burrow.getResidents().contains(this) && !isNight) {
                exitBurrow(world);
                return;
        }

        // Create a burrow if old enough and doesn't have one.
        if (age > 5 && !has_burrow) {
            makeBurrow(world);
            return;
        }

            // Move closer to the burrow if it's later than midday and far from the burrow.
            if (burrow != null && timeToNight(world) > 4 && !isCloseToBurrow) {
                moveCloser(world, burrow.getLocation());
                return;
            }

            // If the rabbit doesn't have a burrow, try finding and entering another burrow.
            if (burrow == null && isNight) {
                Location currentLocation = world.getLocation(this);

                // If there is a nonblocking location.
                if (world.containsNonBlocking(currentLocation)) {

                    // If the nonblocking at the current location is a burrow.
                    if (world.getNonBlocking(currentLocation) instanceof Burrow) {

                        // Rabbit enters the burrow.
                        enterBurrow(world, id_generator.getBurrow(currentLocation));
                    }
                }
            }
        }
    }

    /**
     * Make burrow from location where the rabbit currently is.
     *
     */
    public void makeBurrow(World world) {
        // Removes whatever nonblocking it’s standing on if there is one.
        if (world.containsNonBlocking(world.getLocation(this))) {
            // Remove the nonblocking tile from id_generators lists
            world.delete(world.getNonBlocking(world.getLocation(this)));
        }

        // Instantiates new burrow and sets the tile with current location.
        Burrow burrow = new Burrow(id_generator.getID(), world, world.getLocation(this));

        // Set rabbit’s boolean has_burrow to be true
        has_burrow = true;

        // Add to maps to keep track of where things are.
        id_generator.addBurrowToIdMap(burrow.getId(), burrow);
        id_generator.addLocationToIdMap(burrow.getLocation(), burrow.getId());
    }

    /**
     * Puts a rabbit inside a burrow.
     * 'Removes' them from the world.
     * Adds them to the lists of residents of the particular burrow.
     * If the rabbit didn't beforehand have a personal burrow, it sets this burrow as the personal burrow.
     */
    public void enterBurrow(World world, Burrow burrow) {
        // If it has not been assigned a burrow.
        if (this.burrow == null) {
            has_burrow = true;

            // Sets its personal burrow to be the burrow it enters.
            this.burrow = burrow;
        }

        // "Enters" burrow; removes - not deletes - from world.
        world.remove(this);

        // Adds rabbit to the list of residents of the particular burrow.
        burrow.addResident(this);
        in_hiding = true;
    }

    /**
     * Puts a rabbit at the location where the burrow is located
     */
    public void exitBurrow(World world) {
        // Adds all empty surrounding tiles to a list intended.
        List<Location> empty_surrounding_location = new ArrayList<>(world.getEmptySurroundingTiles(burrow.getLocation()));

        // put rabbits from burrow on empty tiles
        for (int i = 0; i < empty_surrounding_location.size() ; i++) {
            world.setTile(empty_surrounding_location.get(i), burrow.getResidents().get(i));
        }
        // Finds a random index in this list of locations.
        Random random = new Random();
        int random_index = random.nextInt(0, possible_spawn_locations.size());

        world.setTile(possible_spawn_locations.get(random_index), this);
        // Rabbit is now outside of burrow.
        in_hiding = false;

        // rabbits jump up at same time in same place
    }
}

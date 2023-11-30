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
        max_hunger = 100; // changed from 10 due to testing
        nutritional_value = 4;
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
        boolean isCloseToBurrow = !in_hiding && burrow != null && distanceTo(world, burrow.getLocation()) <= 1;

        // Handle daytime burrow exit.
        if (burrow != null && in_hiding && !isNight) {
            exitBurrow(world);
            return;
        }

        if (!in_hiding) {
            // Prioritize entering the burrow at night if close to it.
            if (isNight && isCloseToBurrow) {
                enterBurrow(world, burrow);
                return;
            }
            /*
            // Create a burrow if old enough and doesn't have one.
            if (age > 5 && !has_burrow) {
                makeBurrow(world);
                return;
            }

             */

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
     */

    public void makeBurrow(World world) {
        // Removes whatever nonblocking it’s standing on if there is one.
        if (world.containsNonBlocking(world.getLocation(this))) {
            // Remove the nonblocking tile from id_generators lists
            world.delete(world.getNonBlocking(world.getLocation(this)));
        }

        // Instantiates new burrow and sets the tile with current location.
        Burrow burrow = new Burrow(id_generator);

        // Rabbit now has a personal burrow.
        this.burrow = burrow;

        // Set rabbit’s boolean has_burrow to be true.
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

        // Rabbit is now inside of burrow.
        in_hiding = true;
    }

    /**
     * Puts a rabbit at the location where the burrow is located
     */
    public void exitBurrow(World world) {
        // Retrieve current location
        Location burrow_location = burrow.getLocation();

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

    public Burrow getBurrow() {
        return burrow;
    }
}
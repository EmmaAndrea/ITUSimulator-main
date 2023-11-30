package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.OrganismChildren.AnimalChildren.Herbivore;
import ourcode.Setup.IDGenerator;

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

        boolean isNight = timeToNight(world) == 0;
        boolean isCloseToBurrow = burrow != null && distanceTo(world, burrow.getLocation()) <= 1;

        // Prioritize entering the burrow at night if close to it.
        if (isNight && isCloseToBurrow) {
            enterBurrow(world, burrow);
            return;
        }

        // Handle daytime burrow exit.
        if (burrow != null && burrow.getResidents().contains(this) && !isNight) {
            if (world.getCurrentTime() % 20 == 0) {
                exitBurrow(world);
                return;
            }
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
            if (world.containsNonBlocking(currentLocation) && world.getNonBlocking(currentLocation) instanceof Burrow) {
                enterBurrow(world, id_generator.getBurrow(currentLocation));
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

            // "Enters" burrow; removes - not deletes - from world.
            world.remove(this);
        } else {
            world.remove(this);
        }

        // Adds rabbit to the list of residents of the particular burrow.
        burrow.addResident(this);
    }

    /**
     * Currently magically transports rabbit to its burrow.
     * We should fix it so that it is incremental by step.
     */
    public void goToBed(World world) {
            world.move(this, burrow.getLocation()); // give statements for locating threats
            world.remove(this);
    }

    /**
     * Puts a rabbit at the location where the burrow is located
     */
    public void exitBurrow(World world) {
        world.setTile(burrow.getLocation(), this);
    }

    public int timeToNight(World world){
        if (world.getCurrentTime()%20 > 10){
            return 0;
        } else {
            return 10 - world.getCurrentTime()%20;
        }
    }
}

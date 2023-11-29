package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

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
     */
    public void herbivoreAct(World world) {
        // Gets older and hungrier and dies if too old or hungry.
        super.herbivoreAct(world);

        // Makes a burrow if rabbit does not already have a burrow.
        if (age > 5 && !has_burrow) {
            makeBurrow(world);
        }

        // If it has a burrow and if it is past midday
        // and if it takes longer or same time to get to burrow than steps there is to night,
        // Move closer to burrow.
        if (burrow != null && timeToNight(world)>4 && distanceTo(world, burrow.getLocation()) >= timeToNight(world)) {
            // go to burrow
            moveCloser(world, burrow.getLocation());
        }

        // If it's night, then the rabbit enters burrow.
        if (burrow != null && timeToNight(world) == 0) {
            if (distanceTo(world, burrow.getLocation()) == 1 ) { // not certain if rabbit is 'close' to burrow
                this.enterBurrow(world, burrow);
            }
        }
        // If a burrow contains given resident and if it is daytime, leave the burrow
        if (burrow != null) {
            if (burrow.getResidents().contains(this)) {
                if (world.getCurrentTime() % 20 == 0) {
                    exitBurrow(world);
                }
            }
        }

        // Enters a burrow:
        // If rabbit does not have a burrow.
        if (burrow == null) {
            // If there is a non-blocking at location.
            if (world.containsNonBlocking(world.getLocation(this))) {

                // If said non-blocking is a burrow.
                if (world.getNonBlocking(world.getLocation(this)) instanceof Burrow) {

                    // If it is night.
                    if (timeToNight(world) == 0) {

                        // Gets burrow
                        enterBurrow(world, id_generator.getBurrow(world.getLocation(world.getNonBlocking(world.getLocation(this)))));
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

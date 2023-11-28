package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.OrganismChildren.AnimalChildren.Herbivore;
import ourcode.Setup.IDGenerator;

// yooooo! haha :D

/**
 * Represents a Rabbit entity in the simulated world.
 * Rabbits are a type of Herbivore.
 * Each Rabbit has a unique identifier and a maximum hunger level as well as a life_counter.
 * The behavior of a Rabbit in the simulated world can be defined in the {@code act} method.
 */

public class Rabbit extends Herbivore {
    Burrow burrow;
    boolean has_burrow;
    public Rabbit(IDGenerator original_id_generator) {
        super(original_id_generator);
        type = "rabbit";
        max_hunger = 10;
        nutritional_value = 3;
        max_age = 100;
        has_burrow = false;
    }

    public void spawn(World world) {
        super.spawn(world);
    }

    public void herbivoreAct(World world) {
        // Gets older and hungrier, dies if too old or hungry
        super.herbivoreAct(world);

        // Makes a burrow rabbit does not already have a burrow.
        if (!has_burrow) makeBurrow();

        // if (world.isNight()), find nearby burrow and enter
        if (world.isNight()) {
            // go to burrow
        }
    }

    /**
     * Make burrow from location where the rabbit currently is.
     * Adds rabbit to list of residents
     */
    public void makeBurrow(World world) {
        // Creates new burrow at the location of the burrow creator.
        burrow = new Burrow(id_generator.getID(), world, world.getLocation(this));

        // Adds rabbit to the list of residents of the burrow, and then removes rabbit from world.
        burrow.addResident(this);

        // It is now true that the rabbit has a burrow.
        has_burrow = true;
    }

    /**
     * sets 'has_burrow' boolean to 'True'. This is helpful for adding a rabbit to a resident,
     * making sure the rabbit won't go anywhere else
     */
    public void setBurrow() {
        has_burrow = true;
    }
    /**
     * Puts a rabbit inside a burrow, 'removing' them from the world and adding them to the lists of residents
     *  if the specified rabbit doesn't exist in the burrow
     */
    public void enterBurrow(World world) {
        if (!this.has_burrow) {
            burrow.addResident(this);
            this.setBurrow();
            world.remove(this);
        } else {
            world.remove(this);
        }
    }

    /**
     * Currently magically transports rabbit to its burrow.
     * We should fix it so that it is incremental by step.
     */
    public void goToBed(World world) {
        if (world.isNight()) {
            world.move(this, burrow.getBurrowLocation()); // give statements for
            world.remove(this);
        }
    }

    /**
     * Puts a rabbit at the location where the burrow is located
     */
    public void exitBurrow(World world) {
        world.setTile(burrow.getBurrowLocation(), this);
    }
}

package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

import itumulator.world.World;
import itumulator.world.Location;

import ourcode.Organism.OrganismChildren.AnimalChildren.Herbivore;
import ourcode.Setup.IDGenerator;
import ourcode.Obstacles.Burrow;

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

        // if (isNight()), find nearby burrow and enter

        //
    }

    /**
     * Make burrow from location where the rabbit currently is.
     * Adds rabbit to list of residents
     */
    public void makeBurrow(int id, World world, Rabbit rabbit) {
        Location rabbitLocation = world.getLocation(rabbit);
        Burrow burrow = new Burrow(id, world, rabbitLocation);
        burrow.addRabbit(this);
    }

    /**
     * sets 'has_burrow' boolean to 'True'. This is helpful for adding a rabbit to a resident,
     * making sure the rabbit won't go anywhere else
     */
    public void setBurrow() {
        has_burrow = true;
    }
    /**
     *
     */
    public void enterBurrow(int burrow_id, World world) {
        world.remove(this);
    }

    /**
     *
     */
    public void joinBurrow(int burrow_id, World world) {

    }

    /**
     * Puts a rabbit at the location where the burrow is located
     */
    public void exitBurrow(World world) {
        world.setTile(burrow.getBurrowLocation(), this);
    }
}

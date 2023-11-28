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
 * The behavior of a Rabbit in the simulated world can be defined in the {@code act} method
 * .
 */

public class Rabbit extends Herbivore {
    public Rabbit(IDGenerator original_id_generator) {
        super(original_id_generator, "Rabbit");
        max_hunger = 10;
        nutritional_value = 3;
        max_age = 100;
    }

    public void spawn(World world) {
        super.spawn(world);
    }

    /**
     * Gives the behaviour of a Rabbit; how a rabbit should 'act'.
     */

    public void herbivoreAct(World world) {
        // Gets older and hungrier, dies if too old or hungry
        super.herbivoreAct(world);

        // if (condition), breed

        // if (condition), make burrow

        // if (isNight()), find nearby burrow and enter

        //
    }

    /**
     * Make burrow
     * add rabbit to list of residents
     */
    public void makeBurrow(int id, World world, Location location) {
        Burrow burrow = new Burrow(id, world, location);
        burrow.addRabbit(this);
    }

    public void enterBurrow(int burrow_id, World world) {
        world.remove(this);
    }

    public void joinBurrow(int burrow_id, World world) {

    }

    public void exitBurrow(int burrow_id, World world) {
        //world.setTile(burrow_id.getBurrowLocation);
    }
}

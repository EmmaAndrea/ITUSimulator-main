package ourcode.Obstacles;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;

import java.util.ArrayList;
import java.util.List;

/**
 * The Burrow class represents holes and are also the home of rabbits. The Burrow class can add rabbits to live inside
 *  or
 */

public class Burrow implements NonBlocking {
    // list of rabbits which live there
    List<Rabbit> residents;
    int burrow_id;
    Location burrow_location;

    /**
     * the constructor for a Burrow
     */
    public Burrow(int burrow_id, World world, Location location) {
        residents = new ArrayList<>();
        this.burrow_id = burrow_id;
        world.setTile(location, this);
        burrow_location = location;
    }

    /**
     * method for adding rabbit to list of residents
     */
    public void addRabbit(Rabbit rabbit){
        residents.add(rabbit);
    }

    /**
     * A method for retrieving the ID of a burrow. Returns an integer.
     */
    public int getBurrowId() {
        return burrow_id;
    }

    public Location getBurrowLocation() {
        return burrow_location;
    }

    //hej fra emma
}

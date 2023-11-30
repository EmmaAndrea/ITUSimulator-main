package ourcode.Obstacles;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * The Burrow class represents holes and are also the home of rabbits. The Burrow class can add rabbits to live inside
 *  or remove
 */

public class Burrow extends Obstacle {
    // List of rabbits which are currently inside the burrow.
    List<Rabbit> residents;

    // A location for the burrow such that a rabbit can find a way to it.
    Location burrow_location;

    /**
     * The constructor for a Burrow.
     * Makes and ID.
     * Sets the tile in the world to burrow.
     * A burrow has its own location.
     */
    public Burrow(IDGenerator id) {
        super(id);
        type = "burrow";
        residents = new ArrayList<>();
    }

    /**
     * calls
     */
    public void spawn(World world) {
        super.spawn(world);
    }

    /**
     * Adds a given rabbit to the list of residents of the burrow.
     */
    public void addResident(Rabbit rabbit){
        residents.add(rabbit);
    }

    /**
     * Returns the ID of a burrow.
     */
    /*
    public int getId() {
        return burrow_id;
    }

     */

    /**
     * Returns the location of the burrow.
     */
    public Location getLocation() {
        return burrow_location;
    }

    /**
     * Returns the list of residents of the burrow.
     */
    public List<Rabbit> getResidents() {
        return residents;
    }
}

package ourcode.Obstacles;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import ourcode.Setup.IDGenerator;

import java.util.Random;

/**
 * The Obstacle class provides the abstraction of obstacles in the world. Obstacles inherits from the 'NonBlocking' class
 * from the 'ITUmulator' library.
 */
public abstract class Obstacle implements NonBlocking {
    // the 'type' of obstacle used to distinguish different obstacles in the world
    protected String type;
    // the age of an obstacle useful for dating the creation of a specific obstacle
    protected int age;

    // the identification number of an obstacle used to distinguish between obstacles of same type
    protected int id;

    // Represents the generated ID.
    protected IDGenerator id_generator;


    public Obstacle(IDGenerator originID) {
        age = 1;
        id_generator = originID;

    }

    /**
     * Creates an obstacle at a randomised location.
     */
    public void spawn(World world) {
        // Gives an instance of a randomised location
        Location location = getRandomLocation(world);

        // Makes sure the given location is empty
        while (!world.isTileEmpty(location)) {
            location = getRandomLocation(world); // Creates new randomised location
        }

        world.setTile(location, world); // puts an obstacle at provided location

        id_generator.addLocationToIdMap(location, id);
    }

    /**
     * Create an obstacle at a specific location.
     */
    public void spawn(World world, Location location) {
        if (world.isTileEmpty(location)) {
            world.setTile(location, world);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns a randomised location
     */
    protected Location getRandomLocation(World world) {
        Random random = new Random();

        // generates random x and y coordinates
        int randomX = random.nextInt(world.getSize());
        int randomY = random.nextInt(world.getSize());

        return new Location(randomX, randomY);
    }
}

package ourcode.Test.Theme1;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.*;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BurrowTest {
    public final ProgramRunner programRunner;
    public World world;

    public BurrowTest() {
        programRunner = new ProgramRunner();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for burrows");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test Started");
    }

    /**
     * This test demonstrates the usage of the constructor
     */
    @Test
    public void testConstructor() throws Exception {
        // creates a world with size of 1
        World world = new World(5);
        // creates a new id to create a burrow
        IDGenerator id = new IDGenerator();

        Burrow burrow0 = new Burrow(id);
        Burrow burrow1 = new Burrow(id);
        Burrow burrow2 = new Burrow(id);

        world.add(burrow0);
        world.add(burrow1);
        world.add(burrow2);

        assertEquals(world.getEntities().size(), 3);
    }

    /**
     * the 'testRabbitStandingOnBurrow()' test demonstrates if a rabbit can stand directly on top of a burrow
     */
    @Test
    public void testRabbitStandingOnBurrow() throws RuntimeException {
        // creates a world with size of 1
        World world = new World(1);
        // creates a new id to give to a rabbit and a burrow
        IDGenerator id = new IDGenerator();

        // creates a rabbit
        Rabbit rabbit = new Rabbit(id);
        // creates a burrow
        Burrow burrow = new Burrow(id);
        // creates a location for the rabbit and burrow to be spawned at
        Location location = new Location(0,0);

        // adds rabbit to the world
        world.add(rabbit);
        // adds burrow to the world
        world.add(burrow);
        // places rabbit at location
        world.setTile(location, rabbit);
        // places burrow at location
        world.setTile(location, burrow);

        // checks if rabbits location is equal to burrows location
        assertEquals(world.getLocation(rabbit), world.getLocation(burrow));
    }

    /**
     * Tests both addResident() method and getResident() method.
     */
    @Test
    public void testResidents() {
        IDGenerator id = new IDGenerator();
        Burrow burrow = new Burrow(id);
        Rabbit rabbit = new Rabbit(id);
        Rabbit rabbit1 = new Rabbit(id);

        burrow.addResident(rabbit);
        burrow.addResident(rabbit1);

        assertEquals(burrow.getResidents().size(), 2);

    }

    /**
     * Tests the spawn() method for a burrow.
     */
    @Test
    public void testBurrowSpawn() {
        Program p = new Program(2, 800, 200);
        World world = p.getWorld();
        IDGenerator id = new IDGenerator();
        Burrow burrow = new Burrow(id);

        burrow.spawn(world);
        assertEquals(world.getEntities().size(),  1);
    }

    @AfterEach
    public void endTest() {
        System.out.println("Test Ended");
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("All tests complete");
    }
}
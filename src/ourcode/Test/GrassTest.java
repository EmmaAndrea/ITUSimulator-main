package ourcode.Test;

import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A class which tests all the requirements of theme 1
 */
public class GrassTest {
    public ProgramRunner programRunner;
    public World world;

    /**
     * Creates one program-runner for all the tests to use
     */
    public GrassTest(){
        programRunner = new ProgramRunner();
    }
    @BeforeAll
    public static void setUp(){
        System.out.println("Testing for theme 1: ");
    }

    @BeforeEach
    public void startTest(){
        System.out.println("Test Started");
    }

    /**
     * Creates a world based on file "t1-1c", such that 1 grass is spawned.
     * Checks that one grass is in entities
     */
    @Test
    public void testGrass1() throws Exception{
        programRunner.create("./data/t1-1c.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);
        assertEquals(world.getEntities().size(), 1);
        assertTrue(world.getEntities().containsKey(programRunner.getGrass()));
    }

    /**
     * Creates a world based on file "t1-1c", such that 1 grass is spawned.
     * Creates one new grass in world and spawns it twice, catching the locations in variables
     * Checks the locations are different, and thereby random
     */
    @Test
    public void testGrass2() throws Exception{
        programRunner.create("./data/t1-1c.txt");
        world = programRunner.getWorld();
        Grass grass1 = new Grass(programRunner.getOriginal_id_generator());
        Grass grass2 = new Grass(programRunner.getOriginal_id_generator());
        grass1.spawn(world);
        Location location1 = world.getLocation(grass1);
        grass2.spawn(world);
        Location location2 = (world.getLocation(grass2));
        assertNotEquals(location1, location2);
    }

    /**
     * Creates a world based on file "t1-1c", such that 1 grass is spawned.
     * Creates one new grass in world and spawns it, then runs the simulation for 31 steps.
     * Checks entities does not contain grass1, showing that it is deleted and thereby dead.
     */
    @Test
    public void testGrass3() throws Exception{
        programRunner.create("./data/t1-1c.txt");
        world = programRunner.getWorld();
        Grass grass1 = new Grass(programRunner.getOriginal_id_generator());
        grass1.spawn(world);
        programRunner.runSimulation(31);
        assertFalse(world.getEntities().containsKey(grass1));
    }
    /**
     * Creates a world based on file "t1-1c", such that 1 grass is spawned.
     * Creates one new grass and one new rabbit, and adds them to the same location
     * Checks the rabbit has the location we wanted
     * Checks grass location is the same as before adding rabbit
     * and didn't crash the program
     */
    @Test
    public void testGrass4() throws Exception{
        programRunner.create("./data/t1-1c.txt");
        world = programRunner.getWorld();

        Location baseLocation = new Location(1, 1);

        Grass grass1 = new Grass(programRunner.getOriginal_id_generator());
        world.setTile(baseLocation, grass1);
        Location grassBeforeRabbit = world.getEntities().get(grass1);

        Rabbit rabbit1 = new Rabbit(programRunner.getOriginal_id_generator());
        world.setTile(baseLocation, rabbit1);

        Location grassAfterRabbit = world.getLocation(grass1);

        assertEquals(world.getEntities().get(rabbit1), baseLocation);
        assertEquals(grassBeforeRabbit, grassAfterRabbit);
    }

    /**
     * Creates a world based on file "t1-2fg", such that 4 rabbits are spawned.
     * Checks that entities contains exactly four entities,
     * Checks a rabbit is in entities
     */
    @Test
    public void testRabbit1() throws Exception{
        programRunner.create("./data/t1-2fg.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);
        assertEquals(world.getEntities().size(), 4);
        assertTrue(world.getEntities().containsKey(programRunner.getRabbit()));
    }

    /**
     * Creates a world based on file "t1-2fg", such that some rabbits and grass are spawned.
     * Spawns one specific rabbit which we track
     * Before the program runs, we check the rabbit is in entities
     * After the program runs a
     */
    @Test
    public void testRabbit2() throws Exception{
        programRunner.create("./data/t1-2cde.txt");
        world = programRunner.getWorld();
        Rabbit rabbit1 = new Rabbit(programRunner.getOriginal_id_generator());
        rabbit1.spawn(world);
        assertTrue(world.getEntities().containsKey(rabbit1));
        programRunner.runSimulation(101);
        assertFalse(world.getEntities().containsKey(rabbit1));
    }

    /**
     * Creates a world based on file "t1-2fg", such that 4 rabbits are spawned.
     * Checks that they each make burrows at age 6,
     */
    @Test
    public void testRabbit3() throws Exception{
        programRunner.create("./data/t1-2fg.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(6);
        int burrowCount = 0;
        for (Object entity : world.getEntities().keySet()) {
            if (entity instanceof Burrow) {
                burrowCount++;
            }
        }
        assertEquals(4, burrowCount);
    }


    @AfterEach
    public void endTest(){
        System.out.println("Test Ended");
    }

    @AfterAll
    public static void tearDown(){
        System.out.println("All tests complete");
    }
}

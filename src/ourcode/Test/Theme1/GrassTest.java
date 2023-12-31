package ourcode.Test.Theme1;

import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.*;

import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rodent;
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
        System.out.println("Testing for grass: ");
    }

    @BeforeEach
    public void startTest(){
        System.out.println("*** Test Started ***");
    }

    /**
     * Requirement a for grass.
     * Creates a world based on file "t1-1c", such that 1 grass is spawned.
     * Checks that one grass is in entities
     */
    @Test
    public void testGrassSpawn() throws Exception {
        programRunner.create("./data/t1-1c.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);
        assertEquals(world.getEntities().size(), 1);
    }

    /**
     * Creates a world based on file "t1-1c", such that 1 grass is spawned.
     * Creates one new grass in world and spawns it twice, catching the locations in variables
     * Checks the locations are different, and thereby random
     */
    @Test
    public void testGrassRandomSpawn() throws Exception {
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
     * Requirement b for grass
     * Creates a world based on file "t1-1c", such that 1 grass is spawned.
     * Creates one new grass in world and spawns it, then runs the simulation for 31 steps.
     * Visually possible to see grass develop from young grass to fully grown grass
     * Checks entities does not contain grass1, showing that it is deleted and thereby dead.
     */
    @Test
    public void testGrassDies() throws Exception {
        programRunner.create("./data/t1-1c.txt");
        world = programRunner.getWorld();
        Grass grass1 = new Grass(programRunner.getOriginal_id_generator());
        grass1.spawn(world);
        programRunner.runSimulation(31);
        assertFalse(world.getEntities().containsKey(grass1));
    }

    /**
     * Requirement c for grass
     * Creates a world based on file "t1-1c", such that 1 grass is spawned.
     * Checks there is more grass after 10 steps without manually spawning extra.
     */
    @Test
    public void testGrassSpreads() throws Exception {
        programRunner.create("./data/t1-1c.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(10);

        int grass_counter = 0;

        for(Object object: world.getEntities().keySet()){
            if (object instanceof Grass){
                grass_counter++;
            }

        }
        assertTrue(grass_counter > 1);
    }

    /**
     * Requirement d for grass
     * Creates a world based on file "t1-1c", such that 1 grass is spawned.
     * Creates one new grass and one new rabbit, and adds them to the same location
     * Checks the rabbit has the location we wanted
     * Checks grass location is the same as before adding rabbit and thereby still exists in the world
     * Checks the rabbit is still in the world
     */
    @Test
    public void testRabbitStandingOnGrassTile() throws Exception {
        programRunner.create("./data/t1-1c.txt");
        world = programRunner.getWorld();

        Location baseLocation = new Location(1, 1);

        Grass grass1 = new Grass(programRunner.getOriginal_id_generator());
        world.setTile(baseLocation, grass1);
        Location grassBeforeRabbit = world.getEntities().get(grass1);

        Rodent rabbit1 = new Rabbit(programRunner.getOriginal_id_generator(), false);
        world.setTile(baseLocation, rabbit1);

        Location grassAfterRabbit = world.getLocation(grass1);

        assertEquals(world.getEntities().get(rabbit1), baseLocation);
        assertEquals(grassBeforeRabbit, grassAfterRabbit);
        assertTrue(world.getEntities().containsKey(rabbit1));
    }


    @AfterEach
    public void endTest(){
        System.out.println("*** Test Ended ***");
    }

    @AfterAll
    public static void tearDown(){
        System.out.println("All tests complete");
    }
}

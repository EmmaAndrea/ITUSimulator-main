package ourcode.Test;

import itumulator.executable.Program;
import itumulator.world.World;
import org.junit.jupiter.api.*;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A class which tests the rabbit-related requirements.
 */
public class RabbitTest {
    private final ProgramRunner programRunner;
    private World world;

    public RabbitTest() {
        programRunner = new ProgramRunner();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing rabbits: ");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test Started");
    }

    /**
     * Test if the correct amount of rabbits spawn.
     */
    @Test
    public void testRabbitPlacement() throws Exception {
        programRunner.create("./data/t1-2cde.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1); // Run the simulation for one step to spawn entities

        int rabbitCount = 0;
        for (Object entity : world.getEntities().keySet()) {
            if (entity instanceof Rabbit) {
                rabbitCount++;
            }
        }

        assertTrue(rabbitCount >= 2 && rabbitCount <= 4, "Rabbits spawned should be between 2 and 4, but was " + rabbitCount);
    }

    /**
     * Test to see if all rabbits die after expected amount of days without eating.
     */
    @Test
    public void testRabbitDeath() throws Exception {
        programRunner.create("./data/t1-2cde.txt");
        world = programRunner.getWorld();
        Rabbit rabbit = new Rabbit(programRunner.getOriginal_id_generator());
        rabbit.spawn(world);

        // Run the simulation for a certain number of steps that would lead to the rabbit's death.
        programRunner.runSimulation(9); // Adjust the number of steps based on your simulation's logic

        // After running the simulation, check if the rabbit is removed from the world.
        assertFalse(world.getEntities().containsKey(rabbit), "Rabbit should be removed from the world after death");
    }

    @Test
    public void testHungerAfterEat() {
        // Create a 1x1 world
        Program p = new Program(1, 800, 2000);
        World world = p.getWorld();

        // Create a rabbit and grass
        IDGenerator idGenerator = new IDGenerator();
        Rabbit rabbit = new Rabbit(idGenerator);
        Grass grass = new Grass(idGenerator);

        // Spawn both rabbit and grass at the same location
        grass.spawn(world);
        rabbit.spawn(world);

        // Check initial hunger level of rabbit
        int initialHungerLevel = rabbit.getHunger();

        // Simulate rabbit eating grass
        rabbit.eat(world);

        // Check hunger level after eating
        int postFeedingHungerLevel = rabbit.getHunger();

        // Assert that hunger level has decreased
        assertTrue(postFeedingHungerLevel < initialHungerLevel, "Rabbit's hunger should decrease after eating grass");
    }

    @Test
    public void testDeleteAfterEat() {
        // Create a 1x1 world
        Program p = new Program(1, 800, 2000);
        World world = p.getWorld();

        // Create a rabbit and grass
        IDGenerator idGenerator = new IDGenerator();
        Rabbit rabbit = new Rabbit(idGenerator);
        Grass grass = new Grass(idGenerator);

        // Spawn both rabbit and grass at the same location
        grass.spawn(world);
        rabbit.spawn(world);

        // Simulate rabbit eating grass
        rabbit.eat(world);

        // Assert that hunger level has decreased
        assertThrows(Exception.class, () -> {
            assertNull(world.getNonBlocking(world.getLocation(rabbit)), "Grass should be deleted after getting eaten.");
        });
    }

    // More tests for Age-Related Energy, Reproduction, and Burrow Interactions

    @AfterEach
    public void endTest() {
        System.out.println("Test Ended");
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("All tests complete");
    }
}

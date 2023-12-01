package ourcode.Test.Theme1;

import itumulator.executable.Program;
import itumulator.world.World;
import org.junit.jupiter.api.*;
import ourcode.Obstacles.Burrow;
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
        assertThrows(Exception.class, () -> assertNull(world.getNonBlocking(world.getLocation(rabbit)), "Grass should be deleted after getting eaten."));
    }

    @Test
    public void testRabbitBreeding() {
        // Create a world
        Program p = new Program(5, 800, 100); // Assuming a world size that allows for breeding
        World world = p.getWorld();

        IDGenerator idGenerator = new IDGenerator();

        // Spawn rabbits in the world
        Rabbit rabbit1 = new Rabbit(idGenerator);
        Rabbit rabbit2 = new Rabbit(idGenerator);

        // Set up the rabbits in suitable locations and conditions for breeding
        rabbit1.spawn(world);
        rabbit2.spawn(world);

        // Get the initial count of rabbits
        int initialRabbitCount = countRabbits(world);

        // Run the simulation for a number of steps to allow breeding
        for (int i = 0; i < 10; i++) {
            p.simulate();

            // Deletes burrows if they make them, we don't want them to go in yet due to bugs.
            for (Object object : world.getEntities().keySet()) {
                if (object instanceof Burrow) {
                    world.delete(object);
                }
            }
        }

        // Get the count of rabbits after running the simulation
        int postSimulationRabbitCount = countRabbits(world);
        System.out.println(postSimulationRabbitCount);

        // Assert that the number of rabbits has increased
        assertTrue(postSimulationRabbitCount > initialRabbitCount, "Rabbits should breed and increase in number");
    }

    private int countRabbits(World world) {
        int counter = 0;
        for (Object object : world.getEntities().keySet()) {
            if (object instanceof Rabbit) {
                counter++;
            }
        }
        return counter;
    }

    @Test
    public void testRabbitsDontBreedThemselves() {
        // Create a world
        Program p = new Program(5, 800, 100); // Assuming a world size that allows for breeding
        World world = p.getWorld();

        IDGenerator idGenerator = new IDGenerator();

        // Spawn rabbits in the world
        Rabbit rabbit1 = new Rabbit(idGenerator);

        // Set up the rabbits in suitable locations and conditions for breeding
        rabbit1.spawn(world);

        // Get the initial count of rabbits
        int initialRabbitCount = countRabbits(world);

        // Run the simulation for a number of steps to allow breeding
        for (int i = 0; i < 99; i++) {
            p.simulate();

            // Deletes burrows if they make them, we don't want them to go in yet due to bugs.
            for (Object object : world.getEntities().keySet()) {
                if (object instanceof Burrow) {
                    world.delete(object);

                }
                if (object instanceof Rabbit) {
                    Rabbit rabbit = (Rabbit) object;
                    //rabbit.getBurrow()
                }
            }
        }

        // Get the count of rabbits after running the simulation
        int postSimulationRabbitCount = countRabbits(world);
        System.out.println(postSimulationRabbitCount);

        // Assert that the number of rabbits has increased
        assertTrue(postSimulationRabbitCount == initialRabbitCount, "Rabbits should not breed themselves.");
    }

    /**
     * Creates a world based on file "t1-2fg", such that 4 rabbits are spawned.
     * Checks that entities contains exactly four entities,
     * Checks a rabbit is in entities
     */
    @org.testng.annotations.Test
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
    @org.testng.annotations.Test
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
    @org.testng.annotations.Test
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

    @org.testng.annotations.Test
    public void testRabbit4() throws Exception{
        programRunner.create("./data/t1-2fg.txt");
        world = programRunner.getWorld();

        for (int i = 0 ; i < 40 ; i ++){
            Grass grass = new Grass (programRunner.getOriginal_id_generator());
            grass.spawn(world);
        }
        programRunner.runSimulation(11);
        int rabbits_in_burrows = 0;
        for (Object entity : world.getEntities().keySet()) {
            if (world.getEntities().get(entity) == null) {
                rabbits_in_burrows++;
            }
        }
        assertEquals(4, rabbits_in_burrows);
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

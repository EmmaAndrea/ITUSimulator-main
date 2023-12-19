package ourcode.Test.Theme1;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.DinosaurEgg;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rodent;
import ourcode.Organism.OrganismChildren.Carcass;
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
     * Creates a world based on file "t1-2fg", such that 4 rabbits are spawned.
     * Checks that entities contains exactly four entities,
     * Checks a rabbit is in entities
     */
    @Test
    public void testRabbitSpawn1() throws Exception{
        programRunner.create("./data/t1-2fg.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);
        assertEquals(world.getEntities().size(), 4, "4 rabbits should be spawned");
    }

    /**
     * Test if the correct amount of rabbits spawn, when file uses a range.
     */
    @org.testng.annotations.Test
    public void testRabbitPlacement() throws Exception {
        programRunner.create("./data/t1-2cde.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1); // Run the simulation for one step to spawn entities

        int rabbitCount = countRabbits(world);

        assertTrue(rabbitCount >= 2 && rabbitCount <= 4, "between 2 and 4 rabbits should be spawned");
    }

    /**
     * Test to see if all rabbits die after expected amount of steps without eating.
     * Rabbits don't lose hunger during the time when they are in their burrows
     * File spawns 4 rabbits and no grass
     * Spawns one rabbit to track
     */
    @Test
    public void testRabbitDeath() throws Exception {
        programRunner.create("./data/t1-2fg.txt");
        world = programRunner.getWorld();
        Rodent rabbit0 = new Rabbit(programRunner.getOriginal_id_generator(), false);
        rabbit0.spawn(world);

        // Run the simulation for a certain number of steps that would lead to the rabbit's death.
        programRunner.runSimulation(30); // Adjust the number of steps based on your simulation's logic

        // After running the simulation, check if the rabbit is removed from the world.
        assertFalse(world.getEntities().containsKey(rabbit0), "Rabbit should be removed from the world after death");
    }

    @org.testng.annotations.Test
    public void testHungerAfterEat() {
        // Create a 1x1 world
        Program p = new Program(1, 800, 2000);
        World world = p.getWorld();

        // Create a rabbit and grass
        IDGenerator idGenerator = new IDGenerator();
        Rodent rabbit = new Rabbit(idGenerator, false);
        Grass grass = new Grass(idGenerator);

        // Spawn both rabbit and grass at the same location
        grass.spawn(world);
        rabbit.spawn(world);

        // Check initial hunger level of rabbit
        int initialHungerLevel = rabbit.getHunger();

        // Simulate rabbit eating grass
        rabbit.eat(world, grass);

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
        Rodent rabbit = new Rabbit(idGenerator, false);
        Grass grass = new Grass(idGenerator);

        // Spawn both rabbit and grass at the same location
        grass.spawn(world);
        rabbit.spawn(world);

        // Simulate rabbit eating grass
        rabbit.eat(world, grass);

        // Assert that hunger level has decreased
        assertFalse(world.containsNonBlocking(world.getLocation(rabbit)), "Grass should be deleted after getting eaten.");
    }

    @org.testng.annotations.Test
    public void testRabbitBreeding() throws Exception {
        // Create a world
        programRunner.create("./data/t1-2b.txt");
        world = programRunner.getWorld();

        IDGenerator idGenerator = programRunner.getOriginal_id_generator();
        for (int i = 0; i < 6; i++) {
            Grass grass = new Grass(programRunner.getOriginal_id_generator());
            grass.spawn(world);
        }

        Rabbit rabbit = new Rabbit(idGenerator, false);
        rabbit.setGender("Female");
        rabbit.spawn(world);

        Rabbit rabbit2 = new Rabbit(idGenerator, false);
        rabbit2.setGender("Male");
        rabbit2.spawn(world);

        // Get the initial count of rabbits
        int initialRabbitCount = countRabbits(world);

        // Run the simulation for a number of steps to allow breeding
        programRunner.runSimulation(10);
        programRunner.runSimulation(10);

        // Get the count of rabbits after running the simulation
        int postSimulationRabbitCount = countRabbits(world);

        // Assert that the number of rabbits has increased
        assertTrue(postSimulationRabbitCount > initialRabbitCount, "Rabbits should breed and increase in number");
    }

    private int countRabbits(World world) {
        int counter = 0;
        for (Object object : world.getEntities().keySet()) {
            if (object instanceof Rodent) {
                counter++;
            }
        }
        return counter;
    }

    @Test
    public void testRabbitsDontBreedThemselves() throws Exception{
        // Create a world
        programRunner.create("./data/t1-2a.txt");
        world = programRunner.getWorld();

        // Get the initial count of rabbits
        int initialRabbitCount = countRabbits(world);

        // Run the simulation for a number of steps to allow breeding
        programRunner.runSimulation(18);

        // Get the count of rabbits after running the simulation
        int postSimulationRabbitCount = countRabbits(world);

        // Assert that the number of rabbits has increased
        assertEquals(postSimulationRabbitCount, initialRabbitCount, "Rabbits should not breed themselves.");
    }



    /**
     * Creates a world based on file "t1-2fg", such that some rabbits and grass are spawned.
     * Spawns one specific rabbit which we track
     * Before the program runs, we check the rabbit is in entities
     * After the program runs 100 times, check rabbit is dead
     */
    @org.testng.annotations.Test
    public void testRabbitAgeDeath() throws Exception{
        programRunner.create("./data/t1-2a.txt");
        world = programRunner.getWorld();

        for (int i =0 ; i<25 ; i++) {
            Grass grass = new Grass(programRunner.getOriginal_id_generator());
            grass.spawn(world);
        }

        programRunner.runSimulation(115);

        int post_sim = countRabbits(world);

        assertEquals(0, post_sim, "checks rabbit dies of old age");
    }

    /**
     * Creates a world based on file "t1-2fg", such that 4 rabbits are spawned.
     * Checks that they each make burrows at age 6,
     */
    @org.testng.annotations.Test
    public void testRabbitsMakeBurrows() throws Exception{
        programRunner.create("./data/t1-2fg.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(6);
        int burrowCount = 0;
        for (Object entity : world.getEntities().keySet()) {
            if (entity instanceof Burrow) {
                burrowCount++;
            }
        }
        assertEquals(4, burrowCount, "checks each rabbit creates their own burrow");
    }

    @org.testng.annotations.Test
    public void testRabbitGoesIntoBurrowAtNight() throws Exception {
        programRunner.create("./data/t1-2fg.txt");
        world = programRunner.getWorld();

        for (int i = 0; i < 40; i++) {
            Grass grass = new Grass(programRunner.getOriginal_id_generator());
            grass.spawn(world);
        }

        programRunner.runSimulation(19);
        int rabbits_in_burrows = 0;

        for (Object o : world.getEntities().keySet()) {
            if (world.getLocation(o) == null) {
                rabbits_in_burrows++;
            }
        }
        assertEquals(4, rabbits_in_burrows, "Rabbits should be in their burrows at night.");
    }

    /**
     * Rabbit moves away from wolves
     * @throws Exception l
     */
    @org.testng.annotations.Test
    public void RabbitMovesAwayFromFromWolves() throws Exception {
        programRunner.create("./data/rabbit-test1.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(50);


    }

    @Test
    public void rabbitInfinity() {
        Program p = new Program(3, 500, 800);
        world = p.getWorld();
        IDGenerator idGenerator = new IDGenerator();

        Location location = new Location(0,0);
        Location location1 = new Location(0,1);
        Location location2 = new Location(1,0);
        Location location3 = new Location(0,1);
        Rodent rabbit = new Rabbit(idGenerator, false);
        Carcass carcass = new Carcass(idGenerator, 4, "bear", false);
        DinosaurEgg egg = new DinosaurEgg(idGenerator, false);
        Burrow burrow = new Burrow(idGenerator);

        world.setTile(location, rabbit);
        world.setTile(location1, carcass);
        world.setTile(location2, egg);
        world.setTile(location3, burrow);

        p.show();
        for (int i = 0; i < 40; i++) {
            p.simulate();
        }

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

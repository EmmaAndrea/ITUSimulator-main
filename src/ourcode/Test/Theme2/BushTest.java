package ourcode.Test.Theme2;

import itumulator.world.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.PlantChildren.Bush;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the functionalities of the Bush class.
 * It includes tests for spawning, growing berries, eating berries, and the bush's act method.
 */
public class BushTest {
    public ProgramRunner programRunner;
    public World world;

    /**
     * Initializes a new instance of the BushTest class.
     * It creates a new ProgramRunner for the test.
     */
    public BushTest() {
        programRunner = new ProgramRunner();
    }

    /**
     * Set up method that runs once before all the tests.
     * It prints a message indicating the start of the test suite for the Bush class.
     */
    @BeforeAll
    public static void setUp() {
        System.out.println("Starting test for Cave");
    }

    /**
     * Set up method that runs before each test.
     * It initializes the world and spawns a bush in it.
     */
    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
        world = new World(3);
        IDGenerator id_generator = new IDGenerator();
        Bush bush1 = new Bush(id_generator);
        bush1.spawn(world);
    }

    /**
     * Tests the spawn functionality of a Bush.
     * Verifies that a Bush is correctly added to the world.
     */
    @Test
    public void testSpawn() {
        assertEquals(world.getEntities().size(), 1,
                "the amount of bush in the world should be 1, but it is: " + world.getEntities().size());
    }

    /**
     * Tests the grow method of Bush.
     * Checks if the number of berries increases after the grow method is called.
     */
    @Test
    public void testGrowBerries() {
        Bush bush = null;
        for (Object object : world.getEntities().keySet()) {
            bush = (Bush) object;
            bush.grow();
        }
        Assertions.assertTrue(bush.getBerriesAmount() > 0, "the bush should have more berries after growing");
    }

    /**
     * Tests the eatBerries method of Bush.
     * Ensures that the number of berries decreases after the eatBerries method is called.
     */
    @Test
    public void testEatBerries() {
        Bush bush = null;
        for (Object object : world.getEntities().keySet()) {
            bush = (Bush) object;
            bush.grow();
        }
        bush.eatBerries();
        Assertions.assertTrue(bush.getBerriesAmount() < 3, "the bush should have less berries after eating");
    }

    /**
     * Tests the act method of Bush.
     * Verifies that the act method affects the bush's state correctly.
     */
    @Test
    public void testBushAct() {
        Bush bush = null;
        for (Object object : world.getEntities().keySet()) {
            bush = (Bush) object;
            bush.act(world);
            System.out.println("the amount of berries are: " + bush.getBerriesAmount());
        }
        Assertions.assertTrue(bush.getBerriesAmount() == 3, "bushAct should grow the amount of berries");
    }
}

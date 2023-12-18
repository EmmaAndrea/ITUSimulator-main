package ourcode.Test.Theme2;

import itumulator.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Obstacles.Cave;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.SocialPredator;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the functionalities of the Cave class.
 * It includes tests for the creation and addition of caves in the world.
 */
public class CaveTest {
    ProgramRunner program_runner = new ProgramRunner();
    World world;

    /**
     * Initializes a new instance of the CaveTest class.
     * It creates a new ProgramRunner for the test.
     */
    public CaveTest() {
    }

    /**
     * Set up method that runs once before all the tests.
     * It prints a message indicating the start of the test suite for the Cave class.
     */
    @BeforeAll
    public static void setUp() {
        System.out.println("Starting test for Cave");
    }

    /**
     * Set up method that runs before each test.
     * It prints a message indicating the start of an individual test.
     */
    @BeforeEach
    public void startTest() {
        System.out.println("*** Test started! ***");
    }

    /**
     * Set up method that runs before each test.
     * It prints a message indicating the start of an individual test.
     */
    @AfterEach
    public void finishTest() {
        System.out.println("*** Test completed! ***");
    }

    /**
     * Tests the creation and addition of Cave instances to the world.
     * Verifies that the correct number of Cave instances are added to the world.
     */
    @Test
    public void testCaveDeclaration() {
        World world = new World(3);
        IDGenerator id_generator = new IDGenerator();

        Cave cave1 = new Cave(id_generator);
        Cave cave2 = new Cave(id_generator);

        world.add(cave1);
        world.add(cave2);

        assertEquals(world.getEntities().size(), 2,
                "The amount of entities in the world should be 2, but it is: " + world.getEntities().size());
    }

    /**
     * Tests that there is the correct amount of residents after wolves enter the cave.
     */
    @Test
    public void testResidentAmount() throws Exception {
        program_runner.create("data/test-cave-1.txt");
        world = program_runner.getWorld();
        program_runner.runSimulation(6);

        int amount = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof SocialPredator wolf) {
                amount = wolf.getMyHabitat().getResidents().size();
            }
        }

        assertEquals(1, amount, "At step 6, the wolf should have made a cave, entered it, " +
                                                  "and the amount of cave residents herein should be 1.");
    }


}
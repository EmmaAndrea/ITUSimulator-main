package ourcode.Test;

import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Obstacles.Burrow;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestBurrow {
    public ProgramRunner programRunner;
    public World world;

    public TestBurrow() {
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
     * This test will provide an instance of a burrow based off of the requirements in 'K1-3a' from 'Tema 1'
     */
    @Test
    public void testConstructor() throws Exception {
        programRunner.create("./data/t1-3a.txt");
        World world = programRunner.getWorld();
        programRunner.runSimulation(1);

        int burrowCount = 0;
        for (Object entity : world.getEntities().keySet()) {
            if (entity instanceof Burrow) {
                burrowCount++;
            }
        }

        assertTrue(burrowCount >= 1 && burrowCount <= 5, "amount of burrow should be between 1 and 5, but was" + burrowCount);
    }
    /*
    @Test
    public void testRabbitStandingOnBurrow() throws Exception {
        World world = new World(2);
        IDGenerator id = new IDGenerator();

        Rabbit rabbit = new Rabbit(id);
        Burrow burrow = new Burrow(id);
        Burrow burrow1 = new Burrow(id);
        Burrow burrow2 = new Burrow(id);
        Burrow burrow3 = new Burrow(id);


        rabbit.spawn(world);
        burrow.spawn(world);

        assertEquals(world.getLocation(rabbit), world.getLocation(burrow));
    }

     */
}
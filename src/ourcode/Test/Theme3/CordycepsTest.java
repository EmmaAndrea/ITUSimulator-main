package ourcode.Test.Theme3;

import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CordycepsTest {
    public ProgramRunner programRunner;
    public World world;

    public CordycepsTest() {
        programRunner = new ProgramRunner();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Fungus");
    }

    @BeforeEach
    public void startTest() throws Exception{
        System.out.println("Test started");
    }

    /**
     * Testing that cordyceps spreads when eaten.
     * @throws Exception skibob
     */
    @Test
    public void testCordycepsSpreadsWhenEaten() throws Exception {
        programRunner.create("data/cordyceps-test-2.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(11);
        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Animal animal) {
                if (animal.hasCordyceps()) {
                    counter++;
                }
            }
        }

        assertEquals(1, counter, "Wolf should've eaten the carcass and contracted cordyceps.");
    }
}
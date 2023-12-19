package ourcode.Test.Theme3;

import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * Optional cordyceps requirement: can spread and live on animals
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

    /**
     * Optional Requirement
     * When an infected animal dies, fungus appears on the carcass
     */

    @Test
    public void testCordycepsOnCarcass() throws Exception{
        programRunner.create("data/cordyceps-test-2.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(11);
        Animal animal = null;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Animal animal1) {
                animal = animal1;
            }
        }
        animal.dieAndBecomeCarcass(world);

        programRunner.runSimulation(1);

        boolean has_fungus = false;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass carcass) {
                if (carcass.hasFungus()){
                    has_fungus = true;
                } else {
                    has_fungus = false;
                    break;
                }
            }
        }
        assertTrue(has_fungus);
    }
}
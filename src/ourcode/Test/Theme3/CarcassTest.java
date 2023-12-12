package ourcode.Test.Theme3;

import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Organism.OrganismChildren.Fungus;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarcassTest {
    public ProgramRunner programRunner;
    public World world;

    public CarcassTest() {
        programRunner = new ProgramRunner();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Fungus");
    }

    @BeforeEach
    public void startTest() throws Exception{
        System.out.println("****Test started****");
        programRunner.create("./data/carcasstest.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);
    }

    /**
     * Testing that fungus is spawned correctly into thw world.
     * The file should spawn five to eight fungi
     */
    @Test
    public void testCarcassSpawnsCorrectlyIntoWorld() {

        int carcass_with_fungi_counter = 0;
        int carcass_without_fungi_counter = 0;

        for (Object object : world.getEntities().keySet()){
            if (object instanceof Carcass carcass){
                if (carcass.hasFungus()) {
                    carcass_with_fungi_counter++;
                } else {
                    carcass_without_fungi_counter++;
                }
            }
        }

        assertEquals(5, carcass_without_fungi_counter, "The amount of fungus should be 5, but is: " + carcass_without_fungi_counter);
        assertEquals(6, carcass_with_fungi_counter, "The amount of fungus should be 6, but is: " + carcass_with_fungi_counter);
    }

    @Test
    public void testFungusReplacesCarcass() {

        programRunner.runSimulation(20);

        int fungi_counter = 0;

        for (Object object : world.getEntities().keySet()) {
            if (object instanceof Fungus) {
                fungi_counter++;
            }
        }

        assertTrue(fungi_counter >= 6, "6 carcass have fungus, and should each leave behind 1 fungus when they die");
    }

    /**
     * Testing that carcass with fungus is spawned correctly into thw world.
     * @throws Exception skibob
     */
    @Test
    public void testCarcassSpawnsWithFungus() throws Exception {
        programRunner.create("data/carcasstest2.txt");
        world = programRunner.getWorld();
        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass carcass && carcass.hasFungus()) {
                counter++;
            }
        }

        assertEquals(1, counter, "There should be exactly one carcass with fungus");
    }

    /**
     * Testing that carcass gets eaten by bear.
     * @throws Exception skibob
     */
    @Test
    public void testCarcassGetsEatenByBear() throws Exception {
        programRunner.create("data/carcasstest3.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(    15);
        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass) {
                counter++;
            }
        }

        assertEquals(0, counter, "The bear should have eaten the carcass after 15 steps.");
    }

    /**
     * Testing that carcass gets eaten by wolf.
     * @throws Exception skibob
     */
    @Test
    public void testCarcassGetsEatenByWolf() throws Exception {
        programRunner.create("data/carcasstest4.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(15);
        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass) {
                counter++;
            }
        }

        assertEquals(0, counter, "The wolf should have eaten the carcass after 15 steps.");
    }

    /**
     * Testing that carcass gets eaten by dinosaur.
     * @throws Exception skibob
     */
    @Test
    public void testCarcassGetsEatenDinosaur() throws Exception {
        programRunner.create("data/carcasstest5.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(15);
        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass) {
                counter++;
            }
        }

        assertEquals(0, counter, "The dinosaur should have eaten the carcass after 15 steps.");
    }

    /**
     * Testing that an animal who dies becomes a carcass.
     * @throws Exception skibob
     */
    @Test
    public void testAnimalBecomesCarcass() throws Exception {
        programRunner.create("data/t1-2a.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(30);
        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass) {
                counter++;
            }
        }

        assertEquals(1, counter, "The rabbit should have died and become carcass without food.");
    }

    /**
     * Testing that carcass rots.
     * @throws Exception skibob
     */
    @Test
    public void testCarcassRots() throws Exception {
        programRunner.create("data/carcasstest2.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(15);
        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass carcass && carcass.hasFungus()) {
                if (carcass.isRotten()) {
                    counter++;
                }
            }
        }

        assertEquals(1, counter, "The carcass should rot");
    }
}
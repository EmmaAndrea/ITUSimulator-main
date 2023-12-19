package ourcode.Test.Theme3;

import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Wolf;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Organism.OrganismChildren.Fungus;
import ourcode.Setup.IDGenerator;
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
        System.out.println("Testing for Carcass");
    }

    @BeforeEach
    public void startTest() throws Exception{
        System.out.println("Test started");
    }

    /**
     * Requirement a for carcass.
     * Testing that fungus is spawned correctly into thw world.
     * The file should spawn five to eight fungi
     */
    @Test
    public void testCarcassSpawnsCorrectlyIntoWorld() throws Exception {

        programRunner.create("./data/carcasstest.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);

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

    /**
     * testing that carcass is replaced by fungus after deteriorating
     * @throws Exception
     */
    @Test
    public void testFungusReplacesCarcass() throws Exception {

        programRunner.create("./data/carcasstest.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);

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
     * Requirement a for fungus: carcass spawn with fungus.
     * Testing that carcass with fungus is spawned correctly into thw world.
     * @throws Exception skibob
     */
    @Test
    public void testCarcassSpawnsWithFungus() throws Exception {
        programRunner.create("./data/carcasstest.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);

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
     * Requirement b for carcass: being eaten.
     * Following tests assure this is true for all animals
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
     * Requirement b for carcass: animal dies and becomes carcass.
     * Testing that a dead animal becomes a carcass.
     * @throws Exception skibob
     */
    @Test
    public void testAnimalBecomesCarcass() throws Exception {
        programRunner.create("data/carcass-test6.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(101);
        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass) {
                counter++;
            }
        }

        assertEquals(1, counter, "Wolf should have died of natural causes and become a carcass.");
    }

    /**
     * Requirement b for carcass: size of carcass depends on animal size
     * @throws Exception skibob
     */
    @Test
    public void testSizeOfCarcass() throws Exception {
        programRunner.create("data/wolf-test3.txt");
        world = programRunner.getWorld();
        IDGenerator idGenerator = programRunner.getOriginal_id_generator();

        programRunner.runSimulation(1);

        Rabbit rabbit = new Rabbit(idGenerator, false);
        rabbit.spawn(world);
        int rabbit_nutrition = rabbit.getNutritionalValue();

        rabbit.dieAndBecomeCarcass(world);
        int carcass_nutrition = 0;

        for (Object object: world.getEntities().keySet()){
            if (object instanceof Carcass carcass){
                carcass_nutrition = carcass.getNutritionalValue();
                world.delete(carcass);
            }
        }

        assertEquals(rabbit_nutrition, carcass_nutrition);

        Wolf wolf = new Wolf(idGenerator, false);
        wolf.spawn(world);
        int wolf_nutrition = wolf.getNutritionalValue();

        wolf.dieAndBecomeCarcass(world);

        for (Object object: world.getEntities().keySet()){
            if (object instanceof Carcass carcass){
                carcass_nutrition = carcass.getNutritionalValue();
            }
        }
        assertEquals(wolf_nutrition, carcass_nutrition);
    }
}
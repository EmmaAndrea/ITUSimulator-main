package ourcode.Test.Theme2;

import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Wolf;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class WolfTest {
    public ProgramRunner programRunner;
    public World world;

    public WolfTest() { programRunner = new ProgramRunner(); }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Wolves");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
    }

    /**
     * this test will check if a wolf can be declared through the input of a file. Also checks if the
     * constructor of a Wolf functions
     */
    @Test
    public void testWolfDeclaration() throws Exception {
        programRunner.create("./data/t2-1ab.txt");

        programRunner.runSimulation(1);

        world = programRunner.getWorld();
        assertEquals(world.getEntities().size(), 1,
                "the amount of entities in the world should be 1 but is " + world.getEntities().size());
    }

    /**
     * This test will check the list 'pack' in the Wolf class along with some of the methods for retrieving
     * and adding a wolf to the list
     */
    @Test
    public void testWolfPackDeclaration() {
        world = new World(4);
        IDGenerator id = new IDGenerator();

        Wolf wolf1 = new Wolf(id);
        Wolf wolf2 = new Wolf(id);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        assertEquals(wolf1.getPack().size(), 2,
                "The size of the wolf pack list should be 2, but it is: " + wolf1.getPack().size());
    }

    /**
     * This test will check the 'removeWolfFromPack()' method from the Wolf class
     * This will ensure
     */
    @Test
    public void testRemovingWolfFromPack() {
        world = new World(3);
        IDGenerator idGenerator = new IDGenerator();

        Wolf wolf1 = new Wolf(idGenerator);
        Wolf wolf2 = new Wolf(idGenerator);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf1);

        wolf1.removeWolfFromPack(wolf2);

        assertEquals(wolf1.getPack().size(), 1,
                "The size of the pack after removing a wolf should be 1, but is: " + wolf1.getPack().size());


    }

    /**
     * This test will check the 'OvertakePack()' method from the Wolf class to see if the correct wolf is the new 'alpha'
     * By checking this method the test will also use 'getMy_alpha()' method
     * to check if the correct information is displayed
     */
    @Test
    public void testOvertakePack() {
        world = new World(3);
        IDGenerator idGenerator = new IDGenerator();

        Wolf wolf1 = new Wolf(idGenerator);
        Wolf wolf2 = new Wolf(idGenerator);
        Wolf wolf3 = new Wolf(idGenerator);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        wolf3.overtakePack(wolf1);

        assertSame(wolf1.getMy_alpha(), wolf3,
                "wolf2's alpha should be wolf3, but it is: " + wolf1.getMy_alpha());
    }

}

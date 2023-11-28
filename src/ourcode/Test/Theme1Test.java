package ourcode.Test;
import itumulator.world.World;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;
import ourcode.Setup.ProgramRunner;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * A class which tests all the requirements of theme 1
 */
public class Theme1Test {
    public ProgramRunner programRunner;
    public World world;

    /**
     * Creates one programrunner for all the tests to use
     */
    public Theme1Test(){
        programRunner = new ProgramRunner();
    }
    @BeforeAll
    public void setUp(){
        System.out.println("Testing for theme 1: ");

    }

    @BeforeEach
    public void startTest(){
        System.out.println("Test Started");
    }

    /**
     * Creates a world based on file "t1-1c", such that 1 grass is spawned.
     */
    @Test
    public void testGrass1() throws Exception{
        programRunner.create("./data/t1-1c.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);
        assertEquals(world.getEntities().size(), 1);
        Grass grass = new Grass(programRunner.getOriginal_id_generator());
        assertEquals(programRunner.getOrganism(), grass);
    }



    @AfterEach
    public void endTest(){
        System.out.println("Test Ended");
    }

    @AfterAll
    public void tearDown(){
        System.out.println("All tests complete");
    }
}

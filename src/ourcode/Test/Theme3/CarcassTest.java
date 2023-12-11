package ourcode.Test.Theme3;

import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Organism.OrganismChildren.Fungus;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;
import static org.junit.jupiter.api.Assertions.*;

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
        System.out.println("Test started");
        programRunner.create("./data/carcasstest.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);
    }

    /**
     * Testing that fungus is spawned correctly into thw world.
     * The file should spawn five to eight fungi
     * @throws Exception
     */
    @Test
    public void testCarcassSpawnsCorrectlyIntoWorld() throws Exception {

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

        assertTrue(carcass_without_fungi_counter == 5, "The amount of fungus should be 5, but is: " + carcass_without_fungi_counter);
        assertTrue(carcass_with_fungi_counter == 6, "The amount of fungus should be 6, but is: " + carcass_with_fungi_counter);
    }

    @Test
    public void testFungusReplacesCarcass(){

        programRunner.runSimulation(21);

        int fungi_counter = 0;

        for (Object object : world.getEntities().keySet()) {
            if (object instanceof Fungus fungus) {
                fungi_counter++;
            }
        }

        assertEquals(fungi_counter, 6, "6 carcass have fungus, and should leave behind 1 fungus when they die");
    }

    public void testFungusBeingEaten(){
        // create code first
    }
}
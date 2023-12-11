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

public class FungusTest {
    public ProgramRunner programRunner;
    public World world;

    public FungusTest() {
        programRunner = new ProgramRunner();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Fungus");
    }

    @BeforeEach
    public void startTest() throws Exception{
        System.out.println("Test started");
        programRunner.create("./data/fungitest.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1); // Run the simulation for one step to spawn entities

    }

    /**
     * Testing that fungus is spawned correctly into thw world.
     * The file should spawn five to eight fungi
     * @throws Exception
     */
    @Test
    public void testFungusDeclaration() throws Exception {
        int fungus_counter = 0;

        for (Object object : world.getEntities().keySet()){
            if (object instanceof Fungus){
                fungus_counter++;
            }
        }

        assertTrue(fungus_counter >= 5 && fungus_counter <= 8, "The amount of fungus should be between 5 and 8, but is: " + fungus_counter);
    }

    /**
     * Tests that fungus can spread into a carcass
     * @throws Exception
     */
    @Test
    public void testFungusSpread() throws Exception {

        Location base_location = new Location(0,0);
        Carcass new_carcass = new Carcass(programRunner.getOriginal_id_generator(), 3, "rabbit", false);

        for (Location location: world.getSurroundingTiles(base_location, world.getSize())){
            if (world.isTileEmpty(location)){
                world.setTile(location, new_carcass);
                break;
            }
        }
        programRunner.runSimulation(1);

        assertTrue(new_carcass.hasFungus());
    }



    @Test
    public void testFungusDiesIfNotSpread() {


    }
}
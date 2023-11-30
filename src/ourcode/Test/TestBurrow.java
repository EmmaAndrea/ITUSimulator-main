package ourcode.Test;

import itumulator.executable.Program;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Setup.InputReader;
import ourcode.Setup.ProgramRunner;

public class TestBurrow {
    public ProgramRunner programRunner;
    public World world;

    @BeforeAll
    public static void setUp(){
        System.out.println("Testing for burrows");
    }

    @BeforeEach
    public void startTest(){
        System.out.println("Test Started");
    }

    /**
     * This test will provide an instance of a burrow based off of the requirements in 'K1-3a' from 'Tema 1'
     */
    @Test
    public void testConstructor() throws Exception {
        InputReader input = new InputReader("./data/t1-3a.txt");

        Program p = new Program(input.readWorldSize(), 800, 2000 );


        /*
        programRunner = new ProgramRunner();
        programRunner.create("./data/t1-3a.txt");
        world = programRunner.getWorld();
        assertTrue(world.getEntities().containsKey(programRunner.getBurrow()));
        assertEquals(world.getEntities().size(), 6);
         */
    }
}
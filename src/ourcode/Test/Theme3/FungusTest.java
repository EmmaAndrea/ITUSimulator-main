package ourcode.Test.Theme3;

import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Setup.ProgramRunner;

public class FungusTest {
    public ProgramRunner programRunner;
    public World world;

    public FungusTest() {
        programRunner = new ProgramRunner();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Wolves");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
    }

    @Test
    public void testFungus() {

    }
}
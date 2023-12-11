package ourcode.Test.Theme4;


import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Setup.ProgramRunner;

public class DinosaurTest {
    public ProgramRunner programRunner;
    public World world;

    public DinosaurTest() {
        programRunner = new ProgramRunner();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Fungus");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
    }

    @Test
    public void testDinosaurDeclaration() {
    }
}
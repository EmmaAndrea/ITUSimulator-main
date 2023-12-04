package ourcode.Test.Theme2;


import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ourcode.Setup.ProgramRunner;

public class CaveTest {
    public ProgramRunner programRunner;
    public World world;

    public CaveTest() {
        programRunner = new ProgramRunner();
    }

    /**
     *
     */
    @BeforeAll
    public void setUp() {
        System.out.println("Starting test for Cave");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
    }
}

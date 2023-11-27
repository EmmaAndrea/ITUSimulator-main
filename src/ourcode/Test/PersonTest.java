/*

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;

public class PersonTest {

    @Test
    public void testActWithNoEmptyTiles() {
        int size = 1;
        int delay = 1000;
        int display_size = 800;
        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();
        Animal person = new Animal(); Location place = new Location(0,0);
        world.setTile(place, person);
        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Animal.class, di);

        // Call act method
        person.act(world);

        assertThrows(Exception.class, () -> person.act(world));
    }
}
 */
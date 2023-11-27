package ourcode.Setup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * The IDGenerator class generates a unique ID and adds it to the list of IDs.
 * Useful for distinguishing objects in the World.
 */
public class IDGenerator {
    private HashSet<Integer> IDs;

    /**
     * Instantiates the list of IDs
     */
    public IDGenerator() {
        IDs = new HashSet<>();
    }

    /**
     * Generates the ID.
     * Adds it to the list of IDs.
     * Returns a string of the generated ID.
     * Throws exception if capacity of IDs is reached (>9999).
     */
    public int getID() {
        Random random = new Random();
        int ID = random.nextInt(9999);
        while(IDs.contains(ID)){
            ID = random.nextInt(9999);
        }
        return ID;
    }

    /**
     * Checks if the given ID is already in the list of IDs.
     */
    public boolean isIDTaken(String ID) {
        return IDs.contains(ID);
    }
}


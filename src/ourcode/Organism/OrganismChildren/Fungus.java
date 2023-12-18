package ourcode.Organism.OrganismChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a fungus in the simulation environment. This class extends 'Organism' and
 * implements 'DynamicDisplayInformationProvider' for graphical representation.
 * A fungus can grow inside a carcass, increasing its growth level over time. It can also spread to nearby carcasses,
 * and its appearance changes based on its size and growth state.
 */
public class Fungus extends Organism implements DynamicDisplayInformationProvider {
    protected Carcass carcass; // Reference to the carcass the fungus is associated with, if any.
    protected Location fungus_location; // Current location of the fungus in the simulation world.
    protected boolean in_carcass; // Flag to check if the fungus is inside a carcass.
    protected int growth; // Tracks the growth level of the fungus, especially when inside a carcass.

    /**
     * Constructs a Fungus entity with a unique identifier.
     * Initializes properties such as maximum age and nutritional value.
     * @param idGenerator The IDGenerator instance that provides the unique identifier.
     */
    public Fungus(IDGenerator idGenerator) {
        super(idGenerator);
        max_age = 5;
        nutritional_value = 2;
        in_carcass = false;
        growth = 0;
    }

    /**
     * Defines the behavior of the fungus in each simulation step.
     * This includes growing inside a carcass, spreading to nearby carcasses, and lifespan management.
     * @param world The simulation world where the fungus exists.
     */
    public void act(World world) {
        super.act(world);
        if (in_carcass){
            growth++;
            return;
        }

        if (growth > 0){
            max_age = max_age + growth;
            growth = 0;
        }

        if (age >= max_age){
            world.delete(this);
            return;
        }

        if (checkSurroundingCarcass(world) != null) {
            spread(checkSurroundingCarcass(world));
            max_age++;
        }
    }

    /**
     * Records the location of the fungus.
     * @param location The location of the fungus in the simulation world.
     */
    public void giveLocation(Location location) {
        fungus_location = location;
    }

    /**
     * Spreads the fungus to a nearby carcass if it doesn't already have a fungus.
     * @param carcass The target carcass for fungus spreading.
     */
    public void spread(Carcass carcass) {
        if (!carcass.has_fungus) {
            Fungus fungus = new Fungus(id_generator);
            carcass.setHasFungus();
            carcass.addFungus(fungus);
            fungus.setInCarcass();
        }
    }

    /**
     * Checks for a carcass in the vicinity of the fungus that does not already have a fungus.
     * @param world The simulation world to search for a suitable carcass.
     * @return The found carcass, or null if none is available.
     */
    public Carcass checkSurroundingCarcass(World world) {
        Location current = world.getLocation(this);
        for (Location location : world.getSurroundingTiles(current, 2)) {
            if (!world.isTileEmpty(location)){
                if (world.getTile(location) instanceof Carcass carcass) {
                    return carcass;
                }
            }
        } return null;
    }

    /**
     * Sets the status of the fungus to indicate it is inside a carcass.
     */
    public void setInCarcass() {
        in_carcass = true;
    }

    /**
     * Gets the growth level of the fungus.
     *
     * @return The current growth level.
     */
    public int getGrowth() {
        return growth;
    }

    /**
     * Removes the fungus from a carcass and places it back into the world.
     *
     * @param world The simulation world where the fungus will be placed.
     */
    public void leaveCarcass(World world) {
        world.setTile(fungus_location, this);
        in_carcass = false;
        age = 0;
    }

    /**
     * Provides the display information for the fungus.
     * The appearance changes based on the fungus's size.
     *
     * @return DisplayInformation object containing color and image data for the fungus's representation.
     */
    @Override
    public DisplayInformation getInformation() {
        if (nutritional_value <= 4) {
            return new DisplayInformation(Color.PINK, "fungi-small");
        } return new DisplayInformation(Color.PINK, "fungi-large");
    }
}
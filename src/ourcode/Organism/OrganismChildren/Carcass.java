package ourcode.Organism.OrganismChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a carcass in the simulation environment, providing a source of food for carnivores.
 * When an animal dies, it is replaced by a carcass of the same type, carrying nutritional value.
 * Carcasses can also host fungi, affecting their state and appearance over time.
 */
public class Carcass extends Organism implements DynamicDisplayInformationProvider {
    protected boolean isRotten; // Indicates whether the carcass has rotted.
    protected boolean has_fungus; // Flag indicating the presence of fungus on the carcass.
    protected Fungus fungus; // The fungus growing on the carcass, if present.
    protected Location carcass_location; // The location of the carcass.
    protected int size; // The size of the carcass, representing its nutritional value.
    protected boolean fungus_added;

    protected ArrayList<Animal> eaten_by;

    /**
     * Constructs a Carcass with a unique identifier, nutritional value, type, and fungus presence.
     * Initializes the carcass properties including maximum age and rotting state.
     *
     * @param id_generator      The IDGenerator instance for generating the unique identifier.
     * @param nutritional_value The nutritional value of the carcass.
     * @param type              The type of the organism the carcass represents.
     * @param has_fungus        Boolean indicating if the carcass hosts fungus.
     */
    public Carcass(IDGenerator id_generator, int nutritional_value, String type, boolean has_fungus) {
        super(id_generator);
        this.nutritional_value = nutritional_value;
        this.type = type;
        max_age = 20;
        isRotten = false;
        this.has_fungus = has_fungus;
        size = nutritional_value;
        if (has_fungus) {
            fungus = new Fungus(id_generator);
            fungus_added = false;
        }
        eaten_by = new ArrayList<>();
    }

    /**
     * Defines the behavior of the carcass in each simulation step. This includes aging,
     * becoming rotten, interacting with fungus, and potentially spawning fungus after decomposition.
     *
     * @param world The simulation world where the carcass resides.
     */
    @Override
    public void act(World world) {
        super.act(world);

        if (age >= 15) {
            isRotten = true;
        }

        if (grace_period == 1) {
            grace_period = 0;
        }

        // sets the carcass' fungus to be inside the carcass, then makes the fungus act.
        if (has_fungus && !fungus_added) {
            fungus.setInCarcass();
            world.add(fungus);
            fungus.giveLocation(world.getLocation(this));
            fungus_added = true;
        }

        if (nutritional_value <= 0) {
            world.delete(this);
            return;
        }
        // If the carcass is too old and has a fungus inside that has 'grown'
        // Will spawn the fungus at the carcass' location
        if (age >= max_age) {
            world.delete(this);
            if (has_fungus && fungus.getGrowth() >= 4) { // the 'growth' is set to '4' can be changed for 'balance'
                fungus.leaveCarcass(world);
            }
        }
    }

    /**
     * Adds a fungus to the carcass.
     *
     * @param fungus The fungus to be added to the carcass.
     */
    public void addFungus(Fungus fungus) {
        this.fungus = fungus;
    }

    /**
     * Gets the fungus growing on the carcass.
     *
     * @return The fungus instance.
     */
    public Fungus getFungus() {
        return fungus;
    }

    /**
     * Adjust the nutrition after being eaten.
     */
    public void setNutrition(int eaten) {
        nutritional_value -= eaten;
    }

    /**
     * Gets the size of the carcass.
     *
     * @return The size of the carcass.
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the carcass to have fungus.
     */
    public void setHasFungus() {
        has_fungus = true;
    }

    /**
     * Checks if the carcass has fungus.
     *
     * @return True if the carcass has fungus, false otherwise.
     */
    public boolean hasFungus() {
        return has_fungus;
    }

    public boolean isRotten() {
        return isRotten;
    }

    /**
     * Spawns the fungus at the carcass' location in the world.
     *
     * @param world The simulation world where the fungus is spawned.
     */
    public void leaveFungus(World world) {
        world.setTile(carcass_location, fungus);
    }

    /**
     * Sets the location of the carcass in the world.
     *
     * @param world The world where the carcass is located.
     */
    public void setLocation(World world) {
        carcass_location = world.getLocation(this);
    }

    public void addEatenBy(Animal animal){
        eaten_by.add(animal);
    }

    public String getEatenBy(){
        String list= "";
        for (Animal animal: eaten_by){
            list = list + animal.getId() + " " + animal.getType() + ", ";
        }
        return list;
    }

    /**
     * Provides the display information for the carcass. The appearance changes based on the carcass' state,
     * such as the presence of fungus and whether it is rotten, to illustrate the process of decomposition.
     *
     * @return DisplayInformation object containing color and image data for the carcass's representation.
     */
    @Override
    public DisplayInformation getInformation() {
        if (has_fungus) {
            if (isRotten) {
                return new DisplayInformation(Color.ORANGE, "carcass-rotten-fungi");
            } else {
                return new DisplayInformation(Color.ORANGE, "carcass-fungi");
            }
        } else {
            if (isRotten) {
                return new DisplayInformation(Color.ORANGE, "carcass-rotten");
            } else {
                return new DisplayInformation(Color.ORANGE, "carcass");
            }
        }
    }
}

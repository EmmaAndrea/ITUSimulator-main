package ourcode.Organism.OrganismChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * The Carcass class gives other carnivores in the world something to eat from. When an animal dies a carcass will
 * 'replace' it with a carcass of same type. This means that the carcass' nutritional value
 */
public class Carcass extends Organism implements DynamicDisplayInformationProvider {
    protected boolean is_rotten;
    protected boolean has_fungus;
    protected Fungus fungus;

    protected Location carcass_location; // will store the location of the carcass

    protected int size;

    private boolean fungus_added;
    public Carcass(IDGenerator idGenerator, int nutritionalValue, String type, boolean has_fungus) {
        super(idGenerator);
        this.nutritional_value = nutritionalValue;
        this.type = type;
        max_age = 20;
        is_rotten = false;
        this.has_fungus = has_fungus;
        size = nutritionalValue;
        if (has_fungus) {
            fungus = new Fungus(idGenerator);
            fungus_added = false;
        }
    }

    @Override
    public void act(World world) {
        super.act(world);

        if (age >= 15) {
            is_rotten = true;
        }

        // sets the carcass' fungus to be inside the carcass, then makes the fungus act.
        if (has_fungus && !fungus_added) {
            fungus.setInCarcass();
            world.add(fungus);
            fungus.giveLocation(world.getLocation(this));
            fungus_added = true;
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

    public void addFungus(Fungus fungus) {
        this.fungus = fungus;
    }

    public Fungus getFungus() {
        return fungus;
    }

    public void setSize(Animal animal) {
        size = animal.getTrophicLevel();
    }

    public int getSize() {
        return size;
    }

    public void setHasFungus() {
        has_fungus = true;
    }

    public boolean hasFungus() {
        return has_fungus;
    }

    public void leaveFungus(World world) {
        world.setTile(carcass_location, fungus);
    }

    public void setLocation(World world) {
        carcass_location = world.getLocation(this);
    }

    /**
     * the carcass display information will have several 'stages' of graphic to illustrate the process of
     * 'decomposition'.
     * @return
     */
    @Override
    public DisplayInformation getInformation() {
        if (has_fungus) {
            if (is_rotten) {
                return new DisplayInformation(Color.ORANGE, "carcass-rotten-fungi");
            } else {
                return new DisplayInformation(Color.ORANGE, "carcass-fungi");
            }
        } else {
            if (is_rotten) {
                return new DisplayInformation(Color.ORANGE, "carcass-rotten");
            } else {
                return new DisplayInformation(Color.ORANGE, "carcass");
            }
        }
    }
}

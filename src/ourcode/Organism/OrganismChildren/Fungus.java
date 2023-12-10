package ourcode.Organism.OrganismChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class Fungus extends Organism implements DynamicDisplayInformationProvider {
    protected Carcass carcass; //
    protected boolean in_Carcass; // a check to see if the fungus is inside a carcass
    protected int growth; // fungus will grow if it is inside of carcass
    public Fungus(IDGenerator idGenerator) {
        super(idGenerator);
        max_age = 5;
        nutritional_value = 2;
        in_Carcass = false;
        growth = 0;
    }

    @Override
    public void act(World world) {
        if (in_Carcass) {
            growth++;
        } else {
            super.act(world);
            if (checkSurroundingCarcass(world)) {
                spread(carcass, id_generator);
            }
        }
    }

    /**
     * when a fungus 'spreads' it will add a new fungus to the found carcass, which will then
     * @param carcass
     * @param idGenerator
     */
    public void spread(Carcass carcass, IDGenerator idGenerator) {
        Fungus fungus = new Fungus(idGenerator);
        carcass.setHasFungus();
        carcass.addFungus(fungus);
        fungus.setAge(carcass);
        fungus.setInCarcass();
        fungus.setCarcass(carcass);
    }

    /**
     * Checks if a fungus has a carcass in its vicinity and that carcass doesn't already have a fungus inside.
     * @param world
     * @return hasCarcass
     */
    public boolean checkSurroundingCarcass(World world) {
        boolean hasCarcass = false;
        Location current = world.getLocation(this);
        for (Location location : world.getSurroundingTiles(current, 2)) {
            if (!world.isTileEmpty(location) && world.getTile(location) instanceof Carcass carcass && !carcass.hasFungus()) {
                hasCarcass = true;
                this.setCarcass(carcass);
            }
        }
        return hasCarcass;
    }

    /**
     * adds the carcass' size to 'max_age'
     * @param carcass
     */
    public void setAge(Carcass carcass) {
        max_age += carcass.getSize();
    }

    /**
     * sets 'inCarcass' to 'true'
     */
    public void setInCarcass() {
        in_Carcass = true;
    }

    /**
     * returns 'inCarcass' boolean
     * @return inCarcass
     */
    public boolean inCarcass() {
        return in_Carcass;
    }

    /**
     *
     * @param carcass
     */
    public void setCarcass(Carcass carcass) {
        this.carcass = carcass;
    }

    public int getGrowth() {
        return growth;
    }

    @Override
    public DisplayInformation getInformation() {
        if (age <= 2) {
            return new DisplayInformation(Color.PINK, "fungi-small");
        } return new DisplayInformation(Color.PINK, "fungi-large");
    }
}
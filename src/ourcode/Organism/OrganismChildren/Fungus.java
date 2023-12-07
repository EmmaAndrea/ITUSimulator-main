package ourcode.Organism.OrganismChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

public class Fungus extends Organism implements DynamicDisplayInformationProvider {
    protected Carcass myCarcass; //
    protected boolean inCarcass; // a check to see if the fungus is inside a carcass
    protected int grow; // fungus will grow if it is inside of carcass
    public Fungus(IDGenerator idGenerator) {
        super(idGenerator);
        max_age = 5;
        nutritional_value = 2;
        inCarcass = false;
        grow = 0;
    }

    @Override
    public void act(World world) {
        if (inCarcass) {
            grow++;
        } else {
            super.act(world);
            if (checkCarcass(world)) {
                spread(myCarcass, id_generator);
            } else {

            }
        }

    }

    /**
     *
     * @param carcass
     * @param idGenerator
     */
    public void spread(Carcass carcass, IDGenerator idGenerator) {
        Fungus fungus = new Fungus(idGenerator);
        carcass.setHasFungus();
        fungus.setAge(carcass);
        fungus.setInCarcass();
        fungus.setMyCarcass(carcass);
    }

    /**
     * Checks if a fungus has a carcass in its vicinity and that carcass doesn't already have a fungus inside.
     * @param world
     * @return hasCarcass
     */
    public boolean checkCarcass(World world) {
        boolean hasCarcass = false;
        Location current = world.getLocation(this);
        for (Location location : world.getSurroundingTiles(current, 2)) {
            if (!world.isTileEmpty(location) && world.getTile(location) instanceof Carcass carcass && !carcass.isHasFungus()) {
                hasCarcass = true;
                this.setMyCarcass(carcass);
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
        inCarcass = true;
    }

    /**
     * returns 'inCarcass' boolean
     * @return inCarcass
     */
    public boolean isInCarcass() {
        return inCarcass;
    }

    /**
     *
     * @param carcass
     */
    public void setMyCarcass(Carcass carcass) {
        myCarcass = carcass;
    }

    public int getGrow() {
        return grow;
    }

    @Override
    public DisplayInformation getInformation() {
        return null;
    }
}

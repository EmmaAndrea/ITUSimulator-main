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

    protected Location fungus_location;
    protected boolean in_carcass; // a check to see if the fungus is inside a carcass
    protected int growth; // fungus will grow if it is inside of carcass

    public Fungus(IDGenerator idGenerator) {
        super(idGenerator);
        max_age = 5;
        nutritional_value = 2;
        in_carcass = false;
        growth = 0;
    }

    @Override
    public void act(World world) {
        super.act(world);
        if (in_carcass) {
            growth++;
        }
        else if (checkSurroundingCarcass(world) != null){
            spread(checkSurroundingCarcass(world));
        }

    }

    public void giveLocation(Location location) {
        fungus_location = location;
    }
    /**
     * when a fungus 'spreads' it will add a new fungus to the found carcass, which will then
     * @param carcass
     */
    public void spread(Carcass carcass) {
        Fungus fungus = new Fungus(id_generator);
        carcass.setHasFungus();
        carcass.addFungus(fungus);
        fungus.setAge(carcass);
        fungus.setInCarcass();
    }

    /**
     * Checks if a fungus has a carcass in its vicinity and that carcass doesn't already have a fungus inside.
     * @param world
     * @return hasCarcass
     */


    public Carcass checkSurroundingCarcass(World world) {
        Location current = world.getLocation(this);
        for (Location location : world.getSurroundingTiles(current, 2)) {
            if (!world.isTileEmpty(location)){
                if (world.getTile(location) instanceof Carcass carcass) {
                    if (!carcass.hasFungus()) {
                        return carcass;
                    }
                }
            }
        } return null;
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
        in_carcass = true;
    }

    /**
     * returns 'inCarcass' boolean
     * @return inCarcass
     */
    public boolean inCarcass() {
        return in_carcass;
    }

    public int getGrowth() {
        return growth;
    }

    public void leaveCarcass(World world) {
        world.setTile(fungus_location, this);
        in_carcass = false;
    }

    @Override
    public DisplayInformation getInformation() {
        if (age <= 2) {
            return new DisplayInformation(Color.PINK, "fungi-small");
        } return new DisplayInformation(Color.PINK, "fungi-large");
    }
}
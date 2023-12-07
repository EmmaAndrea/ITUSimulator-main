package ourcode.Organism.OrganismChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

/**
 * The Carcass class gives other carnivores in the world something to eat from. When an animal dies a carcass will
 * 'replace' it with a carcass of same type. This means that the carcass' nutritional value
 */
public class Carcass extends Organism implements DynamicDisplayInformationProvider {
    protected boolean rotten;
    protected boolean hasFungus;

    protected int size;
    public Carcass(IDGenerator idGenerator, int nutritionalValue, String type) {
        super(idGenerator);
        this.nutritional_value = nutritionalValue;
        this.type = type;
        max_age = 20;
        rotten = false;
        hasFungus = false;
        size = nutritionalValue;
    }

    @Override
    public void act(World world) {
        super.act(world);

        if (age >= 10) {
            setRotten();
        } else if (age >= max_age) {
            world.delete(this);
        }
    }

    public void setSize(Animal animal) {
        size = animal.getTrophicLevel();
    }
    public int getSize() {
        return size;
    }

    public void setRotten() {
        rotten = true;
    }
    public void setHasFungus() {
        hasFungus = true;
    }
    public boolean isHasFungus() {
        return hasFungus;
    }
    public boolean isRotten() {
        return rotten;
    }

    /**
     * the carcass display information will have several 'stages' of graphic to illustrate the process of
     * 'decomposition' for each fifth 'act'.
     * @return
     */
    @Override
    public DisplayInformation getInformation() {
        return null;
    }
}

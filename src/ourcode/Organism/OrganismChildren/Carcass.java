package ourcode.Organism.OrganismChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import ourcode.Organism.Organism;
import ourcode.Setup.IDGenerator;

/**
 * The Carcass class gives other carnivores in the world something to eat from. When an animal dies a carcass will
 * 'replace' it with a carcass of same type. This means that the carcass' nutritional value
 */
public class Carcass extends Organism implements DynamicDisplayInformationProvider {
    public Carcass(IDGenerator idGenerator, int nutritionalValue, String type) {
        super(idGenerator);
        this.nutritional_value = nutritionalValue;
        this.type = type;
        max_age = 20;
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

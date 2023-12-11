package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.List;

public class Dinosaur extends Predator implements DynamicDisplayInformationProvider {

    /**
     * Constructs a dinosaur instance. Calls the constructor of the superclass Animal
     * and initializes specific attributes for a predator.
     *
     * @param original_id_generator The IDGenerator instance for generating the unique ID of the predator.
     */
    public Dinosaur(IDGenerator original_id_generator, boolean has_cordyceps) {
        super(original_id_generator, has_cordyceps);
        trophic_level = 6;
        type = "dinosaur";
        max_age = 250;
        max_hunger = 50;
        power = 7;
        max_damage = 30;
        consumable_foods = new ArrayList<>(List.of("wolf", "bear", "rabbit"));
        bedtime = 12;
        wakeup = 18;
    }

    @Override
    public DisplayInformation getInformation() {
        return null;
    }
}

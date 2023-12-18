package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

import itumulator.executable.DisplayInformation;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class Rabbit extends Rodent{

    public Rabbit(IDGenerator original_id_generator, Boolean has_cordyceps){
        super(original_id_generator, has_cordyceps);
        type = "rabbit";
        max_age = 100;
        bedtime = 9;
        wakeup = 19;
    }

    /**
     * Provides the visual representation of the rabbit in the simulation. Changes appearance based on the rabbit's age.
     *
     * @return DisplayInformation containing the color and icon representation of the rabbit.
     */
    @Override
    public DisplayInformation getInformation() {
        if (!has_cordyceps) {
            if (age >= 20) {
                return new DisplayInformation(Color.black, "rabbit-large");
            } else {
                return new DisplayInformation(Color.black, "rabbit-small");
            }
        } else {
            if (age >= 20) {
                return new DisplayInformation(Color.black, "rabbit-large-cordyceps");
            } else {
                return new DisplayInformation(Color.black, "rabbit-small-cordyceps");
            }
        }
    }
}

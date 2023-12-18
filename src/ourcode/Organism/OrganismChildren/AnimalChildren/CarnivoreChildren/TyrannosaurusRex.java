package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class TyrannosaurusRex extends Dinosaur{

    public TyrannosaurusRex(IDGenerator original_id_generator, Boolean has_cordyceps){
        super(original_id_generator, has_cordyceps);
        type = "tyrannosaurus";
        max_age = 200;
        bedtime = 12;
        wakeup = 18;
    }

    /**
     * Determines the graphic of the dinosaur based on its current condition and age.
     * @return Returns the graphics information for the dinosaur.
     */
    @Override
    public DisplayInformation getInformation() {
        if (!has_cordyceps) {
            if (age >= 14) {
                if (is_sleeping) {
                    return new DisplayInformation(Color.black, "dinosaur-adult-sleeping");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.black, "dinosaur-adult-wounded");
                } else {
                    return new DisplayInformation(Color.black, "dinosaur-adult");
                }
            } else {
                if (is_sleeping) {
                    return new DisplayInformation(Color.black, "dinosaur-baby-sleeping");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.black, "dinosaur-baby-wounded");
                } else {
                    return new DisplayInformation(Color.black, "dinosaur-baby");
                }
            }
        } else {
            if (age >= 14) {
                if (is_sleeping) {
                    return new DisplayInformation(Color.black, "dinosaur-adult-sleeping-cordyceps");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.black, "dinosaur-adult-wounded-cordyceps");
                } else {
                    return new DisplayInformation(Color.black, "dinosaur-adult-cordyceps");
                }
            } else {
                if (is_sleeping) {
                    return new DisplayInformation(Color.black, "dinosaur-baby-sleeping-cordyceps");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.black, "dinosaur-baby-wounded-cordyceps");
                } else {
                    return new DisplayInformation(Color.black, "dinosaur-baby-cordyceps");
                }
            }
        }
    }
}

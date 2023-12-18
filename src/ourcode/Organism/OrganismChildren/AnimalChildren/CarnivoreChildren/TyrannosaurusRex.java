package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a Tyrannosaurus Rex in a simulation environment, extending the Dinosaur class.
 * This class characterizes the Tyrannosaurus Rex with specific behaviors and attributes, such as its lifespan,
 * sleeping patterns, and visual representation based on its condition and age.
 */
public class TyrannosaurusRex extends Dinosaur {

    /**
     * Constructs a Tyrannosaurus Rex with a unique identifier and potential cordyceps infection.
     * Initializes specific properties of the Tyrannosaurus Rex, such as type, maximum age, and sleep schedule.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the Tyrannosaurus Rex.
     * @param has_cordyceps Indicates whether the Tyrannosaurus Rex is initially infected with cordyceps.
     */
    public TyrannosaurusRex(IDGenerator original_id_generator, Boolean has_cordyceps) {
        super(original_id_generator, has_cordyceps);
        type = "tyrannosaurus";
        max_age = 200;
        bedtime = 12;
        wakeup = 18;
    }

    /**
     * Provides the display information for the Tyrannosaurus Rex.
     * The appearance changes based on its age, whether it is sleeping, and if it has sustained any damage.
     * It also varies if the Tyrannosaurus Rex is infected with cordyceps.
     *
     * @return DisplayInformation object containing color and image data for the Tyrannosaurus Rex representation.
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

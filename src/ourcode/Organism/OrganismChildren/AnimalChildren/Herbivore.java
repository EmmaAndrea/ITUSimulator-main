package ourcode.Organism.OrganismChildren.AnimalChildren;

import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.IDGenerator;

/**
 * The Herbivore class gives the abstraction of the types of animals who are non-meat eaters.
 */

public abstract class Herbivore extends Animal {

    public Herbivore(IDGenerator original_id_generator) {
        super(original_id_generator);
    }

    public void spawn(World world) {
        super.spawn(world);
    }


    /**
     * checks if a given Herbivore is hungry enough and will use the 'eat()' method.
     */
    @Override
    public void herbivoreAct(World world) {
        if (world.containsNonBlocking(world.getLocation(this))) {
            if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {
                if (hunger >= getStandingOnNutritionalValue(world)) {
                    eat(world);
                }
            }
        }
    }

    /**
     * method to move animals if they only eat grass
     * overrides method for other animals
     * @param world dependent on world
     */
    @Override
    public void nextMove(World world){
        // Moves to grass, if there is grass nearby.
        if (getGrassLocation(world) != null) {
            world.move(this, getGrassLocation(world));

            // if there is no grass, move randomly
        } else if (getRandomMoveLocation(world) != null){
            world.move(this, getRandomMoveLocation(world));
        }
    }
}

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

        // If the herbivore is not currently hiding in e.g. a burrow.
        if (!in_hiding) {

            // If the herbivore is standing on a nonblocking tile
            if (world.containsNonBlocking(world.getLocation(this))) {

                // If the non-blocking at this location is grass.
                if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {

                    // If the herbivore is hungrier than how full the grass will make it.
                    if (hunger >= getStandingOnNutritionalValue(world)) {
                        eat(world);

                    }
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
            if (getGrassLocation(world) != world.getLocation(this)) {
                world.move(this, getGrassLocation(world));
            } else if (getRandomMoveLocation(world) != null) {
                world.move(this, getRandomMoveLocation(world));
                return;
            }
            // if there is no grass, move randomly
        }  else if (getRandomMoveLocation(world) != null){
            world.move(this, getRandomMoveLocation(world));
            return;
        }
    }
}

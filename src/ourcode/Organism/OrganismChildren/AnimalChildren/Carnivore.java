package ourcode.Organism.OrganismChildren.AnimalChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.IDGenerator;

import java.util.Set;

/**
 * the Carnivore class gives the abstraction of all meat eaters. Animals of this type cannot eat any
 * other food than meat. Carnivores will typically hunt their prey and be fed through the prey's
 * sustenance.
 */
public abstract class Carnivore extends Animal {
    protected boolean is_sleeping;

    /**
     * the constructor of a Carnivore, will call 'super()' from the Animal class.
     * @param original_id_generator idGenerator that creates the ID for the entity.
     */
    public Carnivore(IDGenerator original_id_generator) {
        super(original_id_generator);
        is_sleeping = false;
    }

    /**
     * will spawn a Carnivore at given world
     */
    public void spawn(World world) {
        super.spawn(world);
    }

    /**
     *
     * @param world The simulation world in which the carnivore exists.
     */
    @Override
    public void carnivoreAct(World world) {
    }

    @Override
    public void attack(World world, Animal animal){
        if(animal.getTrophicLevel() > 2) {
            if (!animal.checkWounded()) animal.becomeWounded();
            else if (hunger >= animal.getNutritionalValue()) eat(world, animal);
            return;
        }

        if (hunger >= animal.getNutritionalValue()) eat(world, animal);
    }

    public void hunt(World world){
        Set<Location> surrounding_tiles = world.getSurroundingTiles(world.getLocation(this), 5);

        // First, check for blocking organisms.
        // Though, if there is an animal of higher trophic level, move away from this animal.
        for (Location location : surrounding_tiles) {

            // If the tile at given location isn't empty.
            if (!world.isTileEmpty(location)) {

                // Declares a new object at given location.
                Object object = world.getTile(location);

                // Casts object to Organism class and checks if the object is an Organism.
                if (object instanceof Animal animal) {
                    if (consumable_foods.contains(animal.getType())) {
                        for (int i = 2; i <= distanceTo(world, location); i++) {
                            moveCloser(world, location);
                        }
                    }
                }
            }
        }
    }
}
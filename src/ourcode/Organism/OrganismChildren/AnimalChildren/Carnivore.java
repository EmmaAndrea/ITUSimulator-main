package ourcode.Organism.OrganismChildren.AnimalChildren;

import itumulator.world.World;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Setup.IDGenerator;

/**
 * the Carnivore class gives the abstraction of all meat eaters. Animals of this type cannot eat any
 * other food than meat. Carnivores will typically hunt their prey and be fed through the prey's
 * sustenance.
 */
public abstract class Carnivore extends Animal {
    /**
     * the constructor of a Carnivore, will call 'super()' from the Animal class.
     * @param original_id_generator
     */
    public Carnivore(IDGenerator original_id_generator) {
        super(original_id_generator);
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

    protected void attack(World world, Animal animal){
        if(wounded == false) animal.becomeWounded();
        else world.delete(animal);
    }
}
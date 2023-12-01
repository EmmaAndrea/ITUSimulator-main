package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Carnivore;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;

public class Wolf extends Carnivore {

    protected ArrayList<Wolf> pack;
    public Wolf(IDGenerator idGenerator) {
        super(idGenerator);
        this.trophic_level = 3;
    }

    public void attack(Animal animal){
        animal.becomeWounded();
    }

    public void createPack(){
        pack = new ArrayList<>();
    }

    public ArrayList<Wolf> getPack(){
        return pack;
    }

    public void addWolfToPack(Wolf wolf){
        pack.add(wolf);
    }

    public void removeWolfFromPack(Wolf wolf){
        pack.remove(wolf);
    }

    public void overtakePack(Wolf oldwolf){
        pack = new ArrayList<>();
        pack.addAll(oldwolf.getPack());
    }
}

package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import ourcode.Organism.OrganismChildren.AnimalChildren.Carnivore;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;

public class Wolf extends Carnivore implements DynamicDisplayInformationProvider {

    protected ArrayList<Wolf> pack;
    public boolean has_pack;
    public Wolf(IDGenerator idGenerator) {
        super(idGenerator);
        type = "wolf";
        trophic_level = 3;
        max_age = 130;
        max_hunger = 21;
        has_pack = false;
    }

    public void createPack() {
        pack = new ArrayList<>();
        pack.add(this);
        has_pack = true;
    }

    public ArrayList<Wolf> getPack() {
        return pack;
    }

    public void addWolfToPack(Wolf thewolf) {
        if (!thewolf.isHas_pack()) {
            pack.add(thewolf);
            thewolf.setHas_pack();
            if (pack.size() == 5) {
                for (Wolf wolf : pack) {
                    wolf.setTrophicLevel(5);
                }
            }
        }
    }

    public Wolf getWolfFromPack(Wolf thisWolf) {
        Wolf getwolf = null;
        for (Wolf wolf : pack) {
            if (wolf.equals(thisWolf)) {
                getwolf = wolf;
                break;
            }
        }
        return getwolf;
    }

    public boolean isHas_pack() {
        return has_pack;
    }

    public void setHas_pack() {
        has_pack = true;
    }

    public void removeWolfFromPack(Wolf thewolf) {
        pack.remove(thewolf);
        if(pack.size() == 4){
            for (Wolf wolf : pack){
                wolf.setTrophicLevel(3);
            }
        }
    }

    public void overtakePack(Wolf oldwolf) {
        pack = new ArrayList<>();
        pack.addAll(oldwolf.getPack());
    }

    public void setTrophicLevel(int i){
        trophic_level=i;
    }

    @Override
    public DisplayInformation getInformation() {
        if (age >= 12) {
            return new DisplayInformation(Color.cyan, "wolf-large");
        } else {
            return new DisplayInformation(Color.cyan, "wolf-small");
        }
    }
}

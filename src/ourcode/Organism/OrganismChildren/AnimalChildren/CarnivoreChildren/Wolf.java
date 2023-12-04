package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.AnimalChildren.Carnivore;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Wolf extends Carnivore implements DynamicDisplayInformationProvider {

    protected ArrayList<Wolf> pack;
    public boolean has_pack;

    private Wolf my_alpha;

    private boolean alpha;
    public Wolf(IDGenerator idGenerator) {
        super(idGenerator);
        type = "wolf";
        trophic_level = 3;
        max_age = 130;
        max_hunger = 21;
        has_pack = false;
        consumable_foods = new ArrayList<>(List.of("rabbit", "bear", "wolf"));
        alpha = false;
    }

    @Override
    public void carnivoreAct(World world) {
        super.carnivoreAct(world);
        if (pack != null) System.out.println("My pack: " + pack.size());

        if(timeToNight(world) > 7 && timeToNight(world) < 3) {
            in_hiding = true;
            sleeping = true;
        } else {
            sleeping = false;
            if (timeToNight(world) == 1) System.out.println("hooooooooowwwwwwwlllll");
            nextMove(world);
        }
    }

    public void createPack() {
        pack = new ArrayList<>();
        pack.add(this);
        has_pack = true;
        alpha = true;
        my_alpha = this;
    }

    public ArrayList<Wolf> getPack() {
        return pack;
    }

    // can only be used by the alpha wolf
    public void addWolfToPack(Wolf thewolf) {
        if (!thewolf.getHasPack()) {
            pack.add(thewolf);
            thewolf.setAlpha(this);
            thewolf.setHasPack();
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

    public boolean getHasPack() {
        return has_pack;
    }

    public void setHasPack() {
        has_pack = true;
    }

    public void setHasNotPack() {
        has_pack = false;
        alpha = false;
        trophic_level = 3;
    }

    public void removeWolfFromPack(Wolf thewolf) {
        if(pack.size() == 4){
            for (Wolf wolf : pack){
                wolf.setTrophicLevel(3);
            }
        }
        pack.remove(thewolf);
        thewolf.setHasNotPack();
    }

    public void overtakePack(Wolf oldwolf) {
        if (oldwolf.getPack() != null) {
            pack = new ArrayList<>();
            pack.addAll(oldwolf.getPack());
            has_pack = true;
            alpha = true;
            removeWolfFromPack(oldwolf);
            oldwolf.setHasNotPack();
        }
    }

    public void setTrophicLevel(int i) {
        trophic_level=i;
    }

    public void setAlpha(Wolf alpha) {
        my_alpha = alpha;
    }
    /**
     * Graphics for old, young and wounded wolf.
     * @return the display information for the wolf in its current state.
     */
    @Override
    public DisplayInformation getInformation() {
        if (age >= 12) {
            if (wounded) {
                return new DisplayInformation(Color.cyan, "wolf-large-wounded");
            } else {
                return new DisplayInformation(Color.cyan, "wolf-large");
            }
        } else {
            if (wounded) {
                return new DisplayInformation(Color.cyan, "wolf-small-wounded");
            } else {
                return new DisplayInformation(Color.cyan, "wolf-small");
            }
        }
    }

    public void deleteMe(World world){
        if (my_alpha == this) {
            pack.removeAll(pack);
            has_pack = false;
        } if (has_pack) {
            my_alpha.removeWolfFromPack(this);
        }
        world.delete(this);
    }

    @Override
    public int getTrophicLevel(){
        return trophic_level;
    }
}

package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.AnimalChildren.Carnivore;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bear extends Carnivore implements DynamicDisplayInformationProvider {

    protected Location territory;

    protected Bear mate;

    public Bear(IDGenerator idGenerator) {
        super(idGenerator);
        trophic_level = 4;
        territory = null;
        type = "bear";
        max_age = 190;
        max_hunger = 30;
        consumable_foods = new ArrayList<>(List.of("grass", "wolf", "bear", "rabbit", "bush"));
    }

    /**
     * Graphics for old, young and wounded bear.
     * @return the display information for the bear in its current state.
     */
    @Override
    public DisplayInformation getInformation() {
        if (age >= 11) {
            if (wounded) {
                return new DisplayInformation(Color.cyan, "bear-large-wounded");
            } else {
                return new DisplayInformation(Color.cyan, "bear-large");
            }
        } else {
            if (wounded) {
                return new DisplayInformation(Color.cyan, "bear-small-wounded");
            } else {
                return new DisplayInformation(Color.cyan, "bear-small");
            }
        }
    }

    @Override
    public void omnivoreAct(World world){
        //trophic_level = 4;
        if (time(world) == 12){
            is_sleeping = true;
            System.out.println("bear asleep");
            in_hiding = true;
        } else if (time(world) == 18) {
            is_sleeping = false;
            in_hiding = false;
        }

        if (gender == Gender.Male && age > 19 && mate == null) {
            findMate(world);
        }

        if (!is_sleeping) {
            if (territory == null) {
                territory = world.getLocation(this);
            }

            if (!findFoodOrSafety(world)) {
                if (world.getLocation(this).equals(territory)) {
                    if (getRandomMoveLocation(world) != null) {
                        world.move(this, getRandomMoveLocation(world));
                    }
                }
                if (hunger >= 20) {
                    hunt(world);
                    if (!findFoodOrSafety(world)) {
                        if (getRandomMoveLocation(world) != null) {
                            world.move(this, getRandomMoveLocation(world));
                        }
                    }
                } else if (distanceTo(world, territory) > 3) {
                    moveCloser(world, territory);
                } else if (getRandomMoveLocation(world) != null) {
                    world.move(this, getRandomMoveLocation(world));
                }
            }
        }

    }
    public void setTerritory(Location territory) {
        this.territory = territory;
    }

    @Override
    public int getTrophicLevel() {
        return trophic_level;
    }

    public void findMate(World world){

        for(Location location: world.getSurroundingTiles(world.getLocation(this), 7)){
            if (world.getTile(location) instanceof Bear potential_mate){
                if (potential_mate.getGender() == Gender.Female){
                    for(int i = 0 ; i < distanceTo(world, location) ; i++) {
                        moveCloser(world, location);
                    }
                    setMate(potential_mate);
                }
            }
        }
    }

    @Override
    public boolean checkBreed(World world){
        if (gender == Gender.Female) {
            if (mate != null) {
                System.out.println("tries to breed");
                return distanceTo(world, world.getLocation(mate)) < 2;
            }
        }
        return false;
    }

    public void setMate(Bear bear){
        mate = bear;
        territory = bear.getTerritory();
    }

    public Location getTerritory(){
        return territory;
    }
}

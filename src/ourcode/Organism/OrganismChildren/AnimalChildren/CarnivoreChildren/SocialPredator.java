package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Cave;
import ourcode.Obstacles.Habitat;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Represents a Wolf entity in the simulated world, extending the Carnivore class.
 * Wolves have unique behaviors such as forming packs, having an alpha wolf,
 * and different interactions based on their pack status and trophic level.
 */
public abstract class SocialPredator extends Predator implements DynamicDisplayInformationProvider {

    protected ArrayList<SocialPredator> pack; // The pack of wolves to which this wolf belongs.
    public boolean has_pack; // Indicates whether this wolf is part of a pack.

    private SocialPredator my_alpha; // The alpha wolf of the pack.
    private boolean alpha; // Indicates whether this wolf is the alpha of a pack.
    protected boolean has_cave;

    protected Location killed_animal_location;

    protected boolean pack_is_done_eating;

    /**
     * Constructs a Wolf with specific characteristics and initializes its pack-related properties.
     *
     * @param idGenerator  The IDGenerator instance providing the unique identifier for the wolf.
     * @param has_cordyceps Boolean if it has cordyceps at birth or not.
     */
    public SocialPredator(IDGenerator idGenerator, boolean has_cordyceps) {
        super(idGenerator, has_cordyceps);
        trophic_level = 3;
        max_hunger = 71;
        has_pack = false;
        alpha = false;
        has_cave = false;
        power = 2;
        max_damage = 30;
        this.has_cordyceps = has_cordyceps;
        nutritional_value = 8;
        pack_is_done_eating = true;
    }

    /**
     * Defines the behavior of a wolf in each simulation step. This includes pack behavior,
     * nighttime activities such as sleeping, and movement during the day.
     *
     * @param world The simulation world in which the wolf exists.
     */

    @Override
    public void act(World world) {
        super.act(world);
        // if wolf has an alpha and the pack does not exist, delete pack
        if (my_alpha != null && my_alpha.getPack() == null) {
            deletePack();
        }
        else if (my_alpha != null && my_alpha.getPack().size() < 2) {
            deletePack();
            System.out.println("pack deleted");
        }
        else if (my_alpha != null) {
            if (my_alpha.getPack().size() > 4) {
                setTrophicLevel(5);
            } else setTrophicLevel(3);
        }


        if (is_hiding) return;
        if (isBedtime(world)) return;

        // Sets grace_period to zero for lonely wolves
        //if (pack == null || pack.size() == 1) {
          //  grace_period = 0;
        //}


        if (pack_is_done_eating && pack_hunting) {
            if (my_alpha != null) {
                for (SocialPredator s : my_alpha.getPack()) {
                    s.setPackHuntingFalse();
                }
            } else pack_hunting = false;
        }
        // if the wolf is the alpha and its hungry enough, set pack_hunting to false for all wolves in pack
        if (alpha) {
            if (hunger < 5) {
                for (SocialPredator s : pack) {
                    s.setPackHuntingFalse();
                }
            }
        }

        // pack hunting method
        if (pack_hunting && my_alpha != null){
            if (killed_animal_location != null) {
                eatWithPack(world);
                grace_period = 0;
            } else huntWithPack(world);
        }

        // sequence for alphas
        else if (alpha) {
            if (hunger > 14) {
                for (SocialPredator s : pack) {
                    s.setPackHuntingTrue();
                }
            } else if (!enemyHabitatNearby(world)){
                nextMove(world);
            }
        }

        // sequence for pack wolves
        else if (my_alpha != null && world.getEntities().get(my_alpha) != null) {
            if (hunger > 15) {
                for (SocialPredator s : my_alpha.getPack()) {
                    s.setPackHuntingTrue();
                }
            } else if (distanceTo(world, world.getLocation(my_alpha)) > 3) {
                moveCloser(world, world.getLocation(my_alpha));

            } else if (!enemyHabitatNearby(world)) {
                nextMove(world);
            }

            // sequence for lone wolves
        } else if (hunger > 15) {
            if (!hunt(world)){
                nextMove(world);
            }
        } else if (!enemyHabitatNearby(world)) {
            nextMove(world);
        }
    }

    /**
     * When two wolves meet, they assess each other
     * @param world
     * @param animal
     */
    @Override
    public boolean sameTypeInteraction(World world, Animal animal){
        if(animal instanceof SocialPredator s){
            if (!friends.contains(s)) {
                if (s.getMyAlpha() != null && s.getMyAlpha().getPack() != null) {
                    if (s.getMyAlpha().getPack().size() < 2) {
                        attack(world, animal);
                        return true;
                    } else {
                        moveAway(world, world.getLocation(animal));
                        moveAway(world, world.getLocation(animal));
                        System.out.println("moved away");
                        return true;
                    }
                }
            }
        } return false;
    }

    /**
     * Method to ensure all wolves in the pack have a chance to eat the carcass of an animal,
     * which a wolf from the pack has killed.
     * @param world
     */
    public void eatWithPack(World world){
        grace_period = 1;
        SocialPredator hungriest;
        hungriest = getHungriestWolf();

        if (!world.isTileEmpty(killed_animal_location)) {
            if (world.getTile(killed_animal_location) instanceof Carcass carcass) {
                if (getHungriestWolf().getHunger() < 2) {
                    pack_is_done_eating = true;
                    killed_animal_location = null;
                    carcass.setGracePeriod(0);
                    return;
                }

                    while (!world.isTileEmpty(killed_animal_location)) {
                        if (carcass.getNutritionalValue()<2) return;
                        hungriest.eat(world, carcass);
                        hungriest = getHungriestWolf();
                    }

            } else killed_animal_location = null;

        }
    }

    /**
     * Finds the hungriest wolf at each stage
     * @return
     */
    public SocialPredator getHungriestWolf(){
        SocialPredator hungriest = null;
        for (SocialPredator member : my_alpha.getPack()) {
            synchronized (member) {
                if (member.getHunger() > hungriest.getHunger()) {
                    hungriest = member;
                }
            }
        }
        return hungriest;
    }
    /**
     * pack huts together
     * @param world
     */

    public void huntWithPack(World world){
        if (my_alpha != null && world.getEntities().get(my_alpha) != null) {
            if (distanceTo(world, world.getLocation(my_alpha)) > 3) {
                moveCloser(world, world.getLocation(my_alpha));
            }
            if (!hunt(world)){
                nextMove(world);
            }
        }
    }

    /**
     * Attacks a specified animal in the world. If the target is a wolf with significant damage,
     * and this wolf is the alpha, it overtakes the target wolf's pack. Otherwise, performs
     * a standard attack.
     *
     * @param world  The simulation world where the attack happens.
     * @param animal The animal to be attacked.
     */
    @Override
    public void attack(World world, Animal animal) {
        if (world.getEntities().containsKey(animal) && world.getEntities().get(animal) != null) {
            if (friends.contains(animal)) return;
            if (animal.getGracePeriod() == 0) {
                animal.setGracePeriod(1);
                animal.damage(power);
                if (animal instanceof SocialPredator animal_to_take) {
                    if (animal_to_take.getDamageTaken() > 7) {
                        if (alpha && animal_to_take.isAlpha()) overtakePack(animal_to_take);
                        else if (my_alpha != null) my_alpha.addToPack(animal_to_take);
                    }
                }
                if (animal.isDead()) {
                    animal.setCarcassLocation(world.getLocation(animal));
                    if (animal.getCarcassLocation() != null) {
                        killed_animal_location = animal.getCarcassLocation();
                    }
                }
                animal.setGracePeriod(0);
            }
        }

    }

    public boolean shareCarcass(){
        return my_alpha != null;
    }

    /**
     * Creates a new pack with this wolf as the alpha. Initializes the pack and sets pack-related properties.
     */
    public void createPack() {
        pack = new ArrayList<>();
        pack.add(this);
        has_pack = true;
        alpha = true;
        my_alpha = this;
    }

    /**
     * Creates a cave in the world at the current location if certain conditions are met.
     * If the object has a pack (has_pack is true) and doesn't already have a cave (has_cave is false),
     * this method will attempt to create a new Cave. If the current location in the world contains
     * Grass, the grass will be removed.
     * A new cave is then created and set at this location.
     * Finally, if a cave is created, all wolves in the pack are assigned this cave.
     *
     * @param world         The world in which the cave is to be created.
     */
    @Override
    public void makeHabitat(World world) {
        if (has_pack && !alpha) return;

        Location location = world.getLocation(this);

        if (checkEmptySpace(world, location)) {
            habitat = new Cave(id_generator);
            world.setTile(location, habitat);
            has_cave = true;

            if(alpha) {
                for (SocialPredator s : pack) {
                    s.setHabitat(habitat);
                }
            }
        }
    }

    /**
     * Retrieves the pack to which this wolf belongs.
     *
     * @return The pack of wolves, or null if this wolf is not part of any pack.
     */
    public ArrayList<SocialPredator> getPack() {
        return pack;
    }

    /**
     * Retrieves the cave associated with this wolf.
     *
     * @return The cave where this wolf resides, or null if no cave is associated.
     */
    public Habitat getMyCave() {
        return habitat;
    }

    /**
     * Adds a wolf to this wolf's pack. Only the alpha wolf can add members to the pack.
     *
     * @param new_member The wolf to be added to the pack.
     */
    public void addToPack(SocialPredator new_member) {
        if (new_member.getHasPack() && new_member.getMyAlpha().getPack() != null) {
            new_member.getMyAlpha().removeFromPack(new_member);
        }
        if (pack == null){
            createPack();
        }
        pack.add(new_member);
        new_member.setAlpha(this);
        new_member.setHasPack();
        new_member.setHabitat(habitat);
        for (SocialPredator s : pack) {
            s.setFriends(new_member);
            new_member.setFriends(s);
        }
    }

    /**
     * Checks whether this wolf is part of a pack.
     *
     * @return true if the wolf is part of a pack, false otherwise.
     */
    public boolean getHasPack() {
        return has_pack;
    }

    /**
     * Sets the wolf's status to being part of a pack.
     */
    public void setHasPack() {
        has_pack = true;
    }

    /**
     * Sets the wolf's status to not being part of a pack and adjusts its trophic level.
     */
    public void setHasNotPack() {
        has_pack = false;
        alpha = false;
        trophic_level = 3;
        pack = null;
    }

    /**
     * Removes a wolf from this wolf's pack. Adjusts the trophic level of the pack if necessary.
     *
     * @param s The wolf to be removed from the pack.
     */
    public void removeFromPack(SocialPredator s) {
        if (pack.size() == 4) {
            for (SocialPredator s_predator : pack) {
                s_predator.setTrophicLevel(3);
                s_predator.setPackHuntingFalse();
            }
        }
        pack.remove(s);
        s.setHasNotPack();
        s.setAlpha(null);
    }

    /**
     * Overtakes the pack of another wolf, becoming the new alpha. The existing pack members
     * are transferred to this wolf's pack, and the former alpha is removed from the pack.
     *
     * @param old The alpha wolf of the pack being overtaken.
     */
    public void overtakePack(SocialPredator old) {
        // if the old wolf has a pack
        if (old.getPack() != null) {
            // if this pack is null and this doesn't have an alpha
            // create a new pack with this as alpha
            if (pack == null && my_alpha == null){
                createPack();
            }

            // if this has a pack and there is no alpha
            // make this alpha of this pack
            if (pack != null && my_alpha == null){
                for (SocialPredator wolf: pack){
                    wolf.setAlpha(this);
                }
            }

            // if the oldwolf had a pack with only itself
            // delete pack and add to new pack
            if (old.getPack().size() == 1) {
                old.deletePack();
                my_alpha.addToPack(old);
                return;
            }

            // if the old wolf has a big pack
            // add all the wolves to this pack
            if (old.getPack().size() > 1) {

                try {
                    for (SocialPredator s : old.getPack()) {
                        my_alpha.addToPack(s);
                    }
                } catch (ConcurrentModificationException e) {
                    System.out.println("member has been eaten");

                }
            }

        } else addToPack(old);
    }

    /**
     * Retrieves the alpha wolf of the pack to which this wolf belongs.
     *
     * @return The alpha wolf of the pack, or null if this wolf has no pack.
     */
    public SocialPredator getMyAlpha() { return my_alpha; }

    /**
     * Removes this wolf from the simulation world. If this wolf is the alpha, pack dynamics
     * are adjusted accordingly.
     *
     * @param world The simulation world from which the wolf is removed.
     */
    public void deleteMe(World world) {

        if (alpha) {
            if (pack.size() > 1) {
                int i = 0;
                while (pack.get(i) == this) {
                    i++;
                    if (i >= pack.size() - 1) break;
                }
                SocialPredator next_alpha = pack.get(i);

                next_alpha.setGracePeriod(1);

                    removeFromPack(next_alpha);
                    next_alpha.createPack();


                    for (int j = 1 ; j < pack.size() ; j++) {
                        if (pack.get(j).getGracePeriod() == 0){
                            next_alpha.addToPack(pack.get(j));
                        }
                    }

                    next_alpha.removeFromPack(this);

                next_alpha.setGracePeriod(0);
            }
        } else if (has_pack && pack != null) {
            my_alpha.removeFromPack(this);
        }
        world.delete(this);
    }

    /**
     * Deletes the pack associated with this wolf, resetting pack-related properties.
     */
    public void deletePack(){
        if (pack != null) {
            for (SocialPredator wolf : pack) {
                if (wolf != this) {
                    removeFromPack(wolf);
                }
            }
        }
        pack = null;
        has_pack = false;
        alpha = false;
        my_alpha = null;
    }

    @Override
    public void dieAndBecomeCarcass(World world) {
        if (is_hiding) {
                try {
                    deleteMe(world);
                } catch (ConcurrentModificationException e) {
                    System.out.println(e.getMessage() + "concurrent");
            }
            return;
        }

        if (world.contains(this) && !is_hiding) {
            grace_period = 1;
            Location current_location = world.getLocation(this);
            try {
                deleteMe(world);
            } catch (ConcurrentModificationException e) {
                System.out.println(e.getMessage());
                return;
            }
            Carcass carcass = new Carcass(id_generator, nutritional_value, type, has_cordyceps);
            carcass.setGracePeriod(1);
            world.setTile(current_location, carcass);
        }
    }

    /**
     * Sets the alpha wolf for this wolf.
     *
     * @param wolf The wolf to be set as alpha.
     */
    public void setAlpha(SocialPredator wolf){
        my_alpha = wolf;
    }

    /**
     * Sets the pack hunting status of this wolf to true, indicating it is part of a hunting pack.
     */
    public void setPackHuntingTrue(){
        pack_is_done_eating = false;
        pack_hunting = true;
    }

    /**
     * Sets the pack hunting status of this wolf to false, indicating it is not part of a hunting pack.
     */
    public void setPackHuntingFalse(){
        pack_is_done_eating = true;
        pack_hunting = false;
    }

    /**
     * Checks if this wolf is the alpha of its pack.
     *
     * @return true if this wolf is the alpha, false otherwise.
     */
    public boolean isAlpha(){
        return alpha;
    }
}
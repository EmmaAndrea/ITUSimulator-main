package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Cave;
import ourcode.Obstacles.Habitat;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Represents a social predator entity in the simulated world, extending the Carnivore class.
 * Social predators have unique behaviors such as forming packs, having an alpha individual,
 * and different interactions based on their pack status and trophic level.
 */
public abstract class SocialPredator extends Predator {

    protected ArrayList<SocialPredator> pack; // The pack of wolves to which this wolf belongs.
    public boolean has_pack; // Indicates whether this wolf is part of a pack.

    private SocialPredator my_alpha; // The alpha wolf of the pack.
    private boolean alpha; // Indicates whether this wolf is the alpha of a pack.
    protected boolean has_cave;

    protected Location killed_animal_location;

    protected boolean pack_is_done_eating;

    /**
     * Constructs a social predator with specific characteristics and initializes its pack-related properties.
     *
     * @param idGenerator  The IDGenerator instance providing the unique identifier for the social predator.
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
     * Defines the behavior of a social predator in each simulation step. This includes pack behavior,
     * nighttime activities such as sleeping, and movement during the day.
     *
     * @param world The simulation world in which the social predator exists.
     */

    @Override
    public void act(World world) {
        super.act(world);
        // if the social predator has an alpha and the pack does not exist, delete pack
        if (my_alpha != null && my_alpha.getPack() == null) {
            deletePack();
        }
        else if (my_alpha != null && my_alpha.getPack().size() < 2) {
            deletePack();
        }
        else if (my_alpha != null) {
            if (my_alpha.getPack().size() > 4) {
                setTrophicLevel(5);
            } else setTrophicLevel(3);
        }

        if (is_hiding) return;

        if (isBedtime(world)) return;

        if (pack_is_done_eating && pack_hunting) {
            if (my_alpha != null) {
                for (SocialPredator s : my_alpha.getPack()) {
                    s.setPackHuntingFalse();
                }
            } else pack_hunting = false;
        }
        // If the social predator is the alpha and its hungry enough, set pack_hunting to false for all wolves in pack.
        if (alpha) {
            if (hunger < 5) {
                for (SocialPredator s : pack) {
                    s.setPackHuntingFalse();
                }
            }
        }

        // Pack hunting methodology.
        if (pack_hunting && my_alpha != null){
            if (killed_animal_location != null) {
                eatWithPack(world);
                grace_period = 0;
            } else huntWithPack(world);
        }

        // Sequence for alphas.
        else if (alpha) {
            if (hunger > 14) {
                for (SocialPredator s : pack) {
                    s.setPackHuntingTrue();
                }
            } else if (!enemyHabitatNearby(world)){
                nextMove(world);
            }
        }

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

            // Sequence for lone social predators.
        } else if (hunger > 15) {
            if (!hunt(world)){
                nextMove(world);
            }
        } else if (!enemyHabitatNearby(world)) {
            nextMove(world);
        }
    }

    /**
     * When two social predators of the same type meet, they assess each other.
     * If the encountered social predator is part of a pack with less than two members, this predator attacks.
     * Otherwise, it moves away to avoid conflict.
     *
     * @param world The simulation world.
     * @param animal The animal encountered by this predator.
     * @return true if an interaction occurs, false otherwise.
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
                        return true;
                    }
                }
            }
        } return false;
    }

    /**
     * Coordinates pack eating behavior. Ensures all wolves in the pack have a chance to eat the carcass of an animal,
     * which a wolf from the pack has killed. The method determines the hungriest wolf at each stage to eat next.
     *
     * @param world The simulation world.
     */
    public void eatWithPack(World world){
        grace_period = 1;
        SocialPredator hungriest;
        hungriest = getHungriestSocialPredator();

        if (!world.isTileEmpty(killed_animal_location)) {
            if (world.getTile(killed_animal_location) instanceof Carcass carcass) {
                if (getHungriestSocialPredator().getHunger() < 2) {
                    pack_is_done_eating = true;
                    killed_animal_location = null;
                    carcass.setGracePeriod(0);
                    return;
                }

                    while (!world.isTileEmpty(killed_animal_location)) {
                        if (carcass.getNutritionalValue()<2) return;
                        hungriest.eat(world, carcass);
                        hungriest = getHungriestSocialPredator();
                    }

            } else killed_animal_location = null;

        }
    }

    /**
     * Finds the hungriest predator in the pack at each stage.
     *
     * @return The hungriest SocialPredator in the pack.
     */
    public SocialPredator getHungriestSocialPredator(){
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
     * Coordinates the pack during hunting. If the alpha is present and the pack is in hunting mode, it follows the alpha's lead.
     *
     * @param world The simulation world.
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
     * Attacks a specified animal in the world. If the target is a significant threat
     * and this wolf is the alpha, it may overtake the target wolf's pack.
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

    /**
     * Creates a new pack with this social predator as the alpha. Initializes the pack and sets pack-related properties.
     * Adds this predator to the pack as the first member and sets its status as the alpha of the pack.
     */
    public void createPack() {
        pack = new ArrayList<>();
        pack.add(this);
        has_pack = true;
        alpha = true;
        my_alpha = this;
    }

    /**
     * Initiates the creation of a habitat for the social predator, typically a cave, if certain conditions are met.
     * If the predator is the alpha of a pack and does not already have a habitat, this method attempts to create one.
     *
     * @param world The simulation world in which the habitat is to be created.
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
     * Retrieves the pack to which this social predator belongs.
     *
     * @return An ArrayList of SocialPredators representing the pack, or null if this predator is not part of any pack.
     */
    public ArrayList<SocialPredator> getPack() {
        return pack;
    }

    /**
     * Retrieves the habitat associated with this social predator, typically a cave.
     *
     * @return The Habitat instance where this predator resides, or null if no habitat is associated.
     */
    public Habitat getMyHabitat() {
        return habitat;
    }

    /**
     * Adds a new member to the pack of this social predator. Only the alpha predator can add members to the pack.
     * Sets the new member's alpha to this predator and updates their pack status.
     *
     * @param new_member The SocialPredator to be added to the pack.
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
     * Checks whether this social predator is part of a pack.
     *
     * @return true if this predator is part of a pack, false otherwise.
     */
    public boolean getHasPack() {
        return has_pack;
    }

    /**
     * Sets the status of this social predator as being part of a pack.
     */
    public void setHasPack() {
        has_pack = true;
    }

    /**
     * Sets the status of this social predator as not being part of a pack and adjusts its trophic level accordingly.
     */
    public void setHasNotPack() {
        has_pack = false;
        alpha = false;
        trophic_level = 3;
        pack = null;
    }

    /**
     * Removes a specific social predator from this predator's pack.
     * Adjusts the trophic level of the remaining pack members if necessary.
     *
     * @param s The SocialPredator to be removed from the pack.
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
     * Overtakes the pack of another social predator, becoming the new alpha.
     * Transfers the existing pack members to this predator's pack and removes the former alpha from the pack.
     *
     * @param old The former alpha social predator whose pack is being overtaken.
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
                    System.out.println(e.getMessage() + "member has been eaten");

                }
            }

        } else addToPack(old);
    }

    /**
     * Retrieves the alpha predator of the pack to which this social predator belongs.
     *
     * @return The alpha SocialPredator of the pack, or null if this predator has no pack.
     */
    public SocialPredator getMyAlpha() { return my_alpha; }

    /**
     * Removes this social predator from the simulation world.
     * If this predator is the alpha, pack dynamics are adjusted accordingly.
     *
     * @param world The simulation world from which the predator is removed.
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
     * Deletes the pack associated with this social predator, resetting all pack-related properties.
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

    /**
     * Transforms this social predator into a carcass upon death. Removes the predator from the world if hiding,
     * otherwise replaces it with a carcass. Handles exceptions during the deletion process.
     *
     * @param world The simulation world where the transformation occurs.
     */
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
     * Assigns a new alpha wolf to this social predator. This is important for maintaining pack hierarchy.
     *
     * @param wolf The social predator to be set as the new alpha.
     */
    public void setAlpha(SocialPredator wolf){
        my_alpha = wolf;
    }

    /**
     * Engages this social predator in pack hunting, setting the corresponding status to true.
     * Indicates that the predator is actively participating in a pack hunt.
     */
    public void setPackHuntingTrue(){
        pack_is_done_eating = false;
        pack_hunting = true;
    }

    /**
     * Disengages this social predator from pack hunting, setting the status to false.
     * Indicates that the predator is no longer participating in a pack hunt.
     */
    public void setPackHuntingFalse(){
        pack_is_done_eating = true;
        pack_hunting = false;
    }

    /**
     * Determines if this social predator is the alpha of its pack, indicating its leadership status.
     *
     * @return true if this predator is the alpha, false otherwise.
     */
    public boolean isAlpha(){
        return alpha;
    }
}
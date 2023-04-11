//import java.io.*;
/*
 * The CalculateDamage class has three major methods the user interacts with:
 *  calcAvgDamage finds the average amount of damage inflicted by an attack given the dice probability
 *  simAttackDamage simulates an actual attack by using Math.random() to simulate dice rolls
 *  calcModelsKilled finds the number of models killed by an attack.
 * 
 * To use these methods first an Attacker, Weapon, Defender, and Modifiers object must be created. This
 * will most likely be done automatically by a class that converts input information from the front end
 * into this program. Defaults are provided if this is not the case.
 */
public class CalculateDamage {
    public static void main (String[] args) {
        // These are placeholder stats intended to fill the methods until we can create
        // functionality for pulling stats from MySQL.
        // TODO: Write code to import data from, and export data to the front end
        // IMPORTANT //
        // The new modifiers object contains the invulSave boolean that used to be in the Defender object.

        Attacker attacker = new Attacker(3, 0);
        Weapon weapon = new Weapon(20, true, 2, 4, 1, 1);
        Defender defender = new Defender(10, 4, 4, 1, 6);
        boolean[] mods = {false, false, false, false, false, false, false, false, false, false, false, false,
            false, false, false};
        Modifiers modifiers = new Modifiers(mods);
        
        double avgDamage = calcAvgDamage(attacker, weapon, defender, modifiers);
        int avgModelsKilled = calcModelsKilled(avgDamage, weapon, defender, modifiers);
        int simDamage = simAttackDamage(attacker, defender, weapon, modifiers);
        int simModelsKilled = calcModelsKilled(simDamage, weapon, defender, modifiers);

        System.out.println("Average Total Damage: " + avgDamage);
        System.out.println("Average Models Killed: " + avgModelsKilled);
        System.out.println("***********************");
        System.out.println("Simulated Total Damage: " + simDamage);
        System.out.println("Simulated Models Killed: " + simModelsKilled);
    }

    /**
     * Calculates the average total damage of an attack between two units using one weapon type. Invoke multiple
     * times, once per weapon type in the attacking unit. 
     * 
     * @param attacker Is an object which holds all the attacker data input by the user
     * @param weapon Is an object which holds all the weapon data input by the user
     * @param defender Is an object which holds all the defender data input by the user
     * 
     * @return a double which shows the average total damage
     */
    public static double calcAvgDamage(Attacker attacker, Weapon weapon, Defender defender, Modifiers modifiers) {
        // Calculates the number of hits
        double hits;
        int toHit = 0;
        int hitBonus = 0;
        int attacks = weapon.getNum() * weapon.getAttacks();
        if (modifiers.getHitPlusOne()) {hitBonus++;}
        if (modifiers.getHitMinusOne()) {hitBonus--;}

        // Finds the toHit value that will be subtracted from 7
        if (weapon.getIsRanged()) {
            toHit = attacker.getBalSkill() + hitBonus;    
        } else {
            toHit = attacker.getWepSkill() + hitBonus;   
        }

        // If the toHit value is too high or too low, set it to 6 or 1 which are the maximum and minimum
        // values even with modifiers.
        if (toHit >= 7) {
            toHit = 6;
        } else if (toHit <= 0) {
            toHit = 1;
        }
        hits = attacks * ((7.0-toHit)/6.0);

        // If rerollOnes then reroll Ones, otherwise reroll all hits if that modifier is true
        if (modifiers.getRerollHitsOne() && !modifiers.getRerollHits()) {
            hits += (attacks / 6) * ((7.0-toHit)/6.0);
        } else if (modifiers.getRerollHits()) {
            double misses = attacks - hits;
            hits += misses * ((7.0-toHit)/6.0);
        }
        // Exploding hits
        if (modifiers.getExplodingHits()) {
            hits += (attacks / 6);
        }
        // Mortal wounds on hit rolls of 6
        double mortalWounds = 0;
        if (modifiers.getMortalWoundHits()) {
            mortalWounds = (attacks / 6);
        }
        
        // Then all the hits have to wound the opponent, the higher the strength the more likely it is
        double woundsDealt = hits * ((7.0 - toWound(weapon, defender))/6); 
        // Reroll wounds
        if (modifiers.getRerollWounds()) {
            double fails = hits - woundsDealt;
            woundsDealt += fails * ((7.0 - toWound(weapon, defender))/6); 
        }
        // Mortal wounds on wound rolls of 6
        if (modifiers.getMortalWoundWounds()) {
            mortalWounds += (hits / 6);
        }
        // Set aside wounds that have an extra point of armor penetration
        double extraAPWounds = 0;
        double otherSave = effSave(weapon, defender, modifiers) - 1;
        if (modifiers.getExtraAPWound()) {
            extraAPWounds = (hits / 6);
            woundsDealt -= extraAPWounds;
            if (otherSave < 1) {
                otherSave = 1;
            }
        }

        // Then the defender has a chance to save against woundsDealt and feelNoPain unsaved damage
        double unsavedWounds = woundsDealt * (1 - ((7.0 - effSave(weapon, defender, modifiers))/6.0));
        double unsavedExtraAP = extraAPWounds * (1 - ((7.0 - otherSave)/6.0));
        // Reroll saves of one, also contains rerolls for the separate extra AP wound track
        if (modifiers.getRerollSaveOne() && !modifiers.getRerollSave()) {
            unsavedWounds -= (woundsDealt / 6) * ((7.0 - effSave(weapon, defender, modifiers))/6.0);
            if (modifiers.getExtraAPWound()) {
                unsavedExtraAP -= (extraAPWounds / 6) * ((7.0 - otherSave)/6.0);
            }
        } else if (modifiers.getRerollSave()) {
            unsavedWounds -= unsavedWounds * ((7.0 - effSave(weapon, defender, modifiers))/6.0);
            if (modifiers.getExtraAPWound()) {
                unsavedExtraAP -= unsavedExtraAP * ((7.0 - otherSave)/6.0);
            }
        }

        // Damage minus one
        int damage = weapon.getDamage();
        if (modifiers.getDamageMinusOne()) {
            damage--;
            if (damage <= 0) {
                damage = 1;
            }
        }
        // Damage is totalled, mortal wounds skip the save process but can still be blocked by feel no pain
        double avgDamage = ((unsavedWounds + unsavedExtraAP) * damage) + mortalWounds; 
        avgDamage -= avgDamage * ((7.0 - defender.getFeelNoPain())/6.0);
        return avgDamage;
    }

    /**
     * Simulates an attack from an attacker against a defender using a single weapon, invoke once per
     * attack weapon. The simulation will use the rollD6() helper method to roll dice instead of using
     * the base probability to determine the numbers of hits, wounds, etc.
     * 
     * TODO: Implement modifiers and re-rolls
     * 
     * @param attacker Is an object which holds all the attacker data input by the user
     * @param weapon Is an object which holds all the weapon data input by the user
     * @param defender Is an object which holds all the defender data input by the user
     * 
     * @return An int value showing the simulated number of damage produced by the attack
     */
    public static int simAttackDamage(Attacker attacker, Defender defender, Weapon weapon, Modifiers modifiers) {
        // Rolls all the dice in the attack and totals all the hits
        int hits = 0;
        for (int i = 0; i < (weapon.getNum() * weapon.getAttacks()); i++) {
            if (weapon.getIsRanged()) {
                if (rollD6() >= attacker.getBalSkill()) {
                    hits++;
                }
            } else {
                if (rollD6() >= attacker.getWepSkill()) {
                    hits++;
                }
            }
        }
        // TODO: hitPlus/MinusOne, rerollHits/rerollHitsOne, explodingHits, mortalWoundHits
        
        // Finds the number of hits that manage to wound
        int woundsDealt = 0;
        int toWound = toWound(weapon, defender);
        for (int i = 0; i < hits; i++) {
            if (rollD6() >= toWound) {
                woundsDealt++;
            }
        }
        // TODO: rerollWounds, mortalWoundWounds, extraAPWound

        int damageDealt = 0;
        int effSave = effSave(weapon, defender, modifiers);
        // Finds the total unsaved damage
        for (int i = 0; i < woundsDealt; i++) {
            if (rollD6() < effSave) {
                damageDealt += weapon.getDamage();
            }
        }
        // Subtracts any damage blocked by feel no pain
        for (int i = 0; i < damageDealt; i++) {
            if (rollD6() >= defender.getFeelNoPain()) {
                damageDealt--;
            }
        }
        // TODO: savePlus/MinusOne, rerollSave/One, damageMinusOne
        return damageDealt;
    }

    /**
     * Calculates the average number of defending models killed from an attack. Must be invoked after
     * calcAverageDamage
     * 
     * @param avgDamage the average total damage from an attack, calculated by the calcAvgDamage method
     * @param weapon Is an object which holds all the weapon data input by the user. The damage stat is the
     * only one used in this method.
     * @param defender Is an object which holds all the defender data input by the user. The wounds stat is the
     * only one used in this method.
     * 
     * @return and int that shows the average number of defending models killed by the attack
     */
    public static int calcModelsKilled(double avgDamage, Weapon weapon, Defender defender, Modifiers modifiers) {
        int modelsKilled = 0;
        int modelWounds = defender.getWounds();
        int fxdAvgDamage = (int)Math.floor(avgDamage); // Turns avgDamage into an int for this method
        int damage = weapon.getDamage();
        if (modifiers.getDamageMinusOne()) {
            damage--;
            if (damage <= 0) {
                damage = 1;
            }
        }
        // Applies damage on a per-model basis (as explained in the damage parameter) until there is no more 
        // damage to apply
        while (fxdAvgDamage > 0) {
            // Edge case where there is more damage in the damage statistic than the damage total, so
            // the damage total is subtracted as subtracted the damage statistic would subtract more
            // damage than existed in the total.
            if (damage > fxdAvgDamage) {
                modelWounds -= fxdAvgDamage;
            } else {
                modelWounds -= damage;
            }
            if (modelWounds <= 0) {
                modelsKilled++;
                modelWounds = defender.getWounds();
            }
            fxdAvgDamage -= damage;
        }
        if (modelsKilled > defender.getSize()) {
            modelsKilled = defender.getSize();
        }
        return modelsKilled;
    }

    /**
     * Helper method that finds the target value an attacker needs to wound a defender using a specified
     * weapon.
     * 
     * @param weapon Is an object which holds all the weapon data for the attacker
     * @param defender Is an object which holds all the defender data input by the user
     * 
     * @return An int value between 1 and 6 which is the target value to wound
     */
    private static int toWound(Weapon weapon, Defender defender) {
        int toWound;
        if (weapon.getStrength() == defender.getToughness()) {
            toWound = 4;
        } else if (weapon.getStrength() > defender.getToughness()) {
            toWound = 3;
            if (weapon.getStrength() >= (2 * defender.getToughness())) {
                toWound = 2;
            }
        } else {
            toWound = 5;
            if (weapon.getStrength() <= (Math.floor((double)defender.getToughness()/2))) {
                toWound = 6;
            }
        }
        return toWound;
    }

    /**
     * Helper method which finds the effective save of the defender. This save being the defender's
     * base armor save statistic modified by any of the attacking weapon's armor penetration statistic
     * unless the defender has an invulnerable save.
     * 
     * @param weapon
     * @param defender
     * 
     * @return The int value of the defender's save
     */
    private static int effSave(Weapon weapon, Defender defender, Modifiers modifiers) {
         // Effective save, saves the value of a save minus armorPen
        int effSave = defender.getSave();
        if(modifiers.getSavePlusOne()) {
            effSave += 1;
        }
        if (modifiers.getSaveMinusOne()) {
            effSave -= 1;
        }
        if (!modifiers.getInvulSave()) {
            effSave = effSave + weapon.getArmorPen();
        }
        if (effSave >= 7) {
            effSave = 6;
        } else if (effSave <= 0) {
            effSave = 1;
        }
        return effSave;
    }

    /**
     * Generates a random number between 1 and 6, simulating the roll of a single six-sided die.
     * 
     * @return A random number between 1 and 6
     */
    private static int rollD6() {
        return (int) ((Math.random() * 6) + 1);
    }
}
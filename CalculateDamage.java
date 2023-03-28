//import java.io.*;

public class CalculateDamage {
    public static void main (String[] args) {
        // These are placeholder stats intended to fill the methods until we can create
        // functionality for pulling stats from MySQL.
        // TODO: Write code to import data from, and export data to the front end

        Attacker attacker = new Attacker(3, 0);
        Weapon weapon = new Weapon(20, true, 2, 4, 1, 1);
        Defender defender = new Defender(10, 4, 4, 1, 6, false);
        
        double avgDamage = calcAvgDamage(attacker, weapon, defender);
        int avgModelsKilled = calcModelsKilled(avgDamage, weapon, defender);
        int simDamage = simAttackDamage(attacker, defender, weapon);
        int simModelsKilled = calcModelsKilled(simDamage, weapon, defender);

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
     * TODO: Implement modifiers and re-rolls
     * 
     * @param attacker Is an object which holds all the attacker data input by the user
     * @param weapon Is an object which holds all the weapon data input by the user
     * @param defender Is an object which holds all the defender data input by the user
     * 
     * @return a double which shows the average total damage
     */
    public static double calcAvgDamage(Attacker attacker, Weapon weapon, Defender defender) {

        // All attacks that hit are totalled
        double hits;
        if (weapon.getIsRanged()) {
            hits = weapon.getNum() * weapon.getAttacks() * ((7.0-attacker.getBalSkill())/6.0);
        } else {
            hits = weapon.getNum() * weapon.getAttacks() * ((7.0-attacker.getWepSkill())/6.0);
        }
        
        // Then all the hits have to wound the opponent, the higher the strength the more likely it is
        double woundsDealt = hits * ((7.0 - toWound(weapon, defender))/6); 

        // Then the defender has a chance to save against woundsDealt and feelNoPain unsaved damage
        // TODO: Break up this equation into more local variables
        double avgDamage = (woundsDealt * (1 - ((7.0 - effSave(weapon, defender))/6.0)) * 
            weapon.getDamage()) * (1 - ((7.0 - defender.getFeelNoPain())/6.0));
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
    public static int simAttackDamage(Attacker attacker, Defender defender, Weapon weapon) {
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
        
        // Finds the number of hits that manage to wound
        int woundsDealt = 0;
        int toWound = toWound(weapon, defender);
        for (int i = 0; i < hits; i++) {
            if (rollD6() >= toWound) {
                woundsDealt++;
            }
        }

        int damageDealt = 0;
        int effSave = effSave(weapon, defender);
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
        return damageDealt;
    }

    /**
     * Calculates the average number of defending models killed from an attack. Must be invoked after
     * calcAverageDamage
     * 
     * TODO: Implement modifiers and re-rolls
     * 
     * @param avgDamage the average total damage from an attack, calculated by the calcAvgDamage method
     * @param weapon Is an object which holds all the weapon data input by the user. The damage stat is the
     * only one used in this method.
     * @param defender Is an object which holds all the defender data input by the user. The wounds stat is the
     * only one used in this method.
     * 
     * @return and int that shows the average number of defending models killed by the attack
     */
    public static int calcModelsKilled(double avgDamage, Weapon weapon, Defender defender) {
        int modelsKilled = 0;
        int modelWounds = defender.getWounds();
        int fxdAvgDamage = (int)Math.floor(avgDamage); // Turns avgDamage into an int for this method
        // Applies damage on a per-model basis (as explained in the damage parameter) until there is no more 
        // damage to apply
        while (fxdAvgDamage > 0) {
            // Edge case where there is more damage in the damage statistic than the damage total, so
            // the damage total is subtracted as subtracted the damage statistic would subtract more
            // damage than existed in the total.
            if (weapon.getDamage() > fxdAvgDamage) {
                modelWounds -= fxdAvgDamage;
            } else {
                modelWounds -= weapon.getDamage();
            }
            if (modelWounds <= 0) {
                modelsKilled++;
                modelWounds = defender.getWounds();
            }
            fxdAvgDamage -= weapon.getDamage();
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
    public static int toWound(Weapon weapon, Defender defender) {
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
    public static int effSave(Weapon weapon, Defender defender) {
         // Effective save, saves the value of a save minus armorPen
        int effSave = defender.getSave();
        if (!defender.getInvulSave()) {
            effSave = effSave + weapon.getArmorPen();
        }
        return effSave;
    }

    /**
     * Generates a random number between 1 and 6, simulating the roll of a single six-sided die.
     * 
     * @return A random number between 1 and 6
     */
    public static int rollD6() {
        return (int) ((Math.random() * 6) + 1);
    }
}
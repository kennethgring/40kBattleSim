//import java.io.*;

public class CalculateDamage {
    public static void main (String[] args) {
        // These are placeholder stats intended to fill the methods until we can create
        // functionality for pulling stats from MySQL.
        // TODO: Write code to import stats for a MySQL table and then export the results of calculations.

        Attacker attacker = new Attacker(3, 0);
        Weapon weapon = new Weapon(10, true, 2, 4, 1, 1);
        Defender defender = new Defender(10, 4, 4, 1, 6, false);
        
        double avgDamage = calcAvgDamage(attacker, weapon, defender);
        int avgModelsKilled = calcAvgModelsKilled(avgDamage, weapon, defender);

        System.out.println("Average Total Damage: " + avgDamage);
        System.out.println("Average Models Killed: " + avgModelsKilled);
    }

    /**
     * Calculates the average total damage of an attack between two units using one weapon type. Invoke multiple
     * times, once per weapon type in the attacking unit. 
     * 
     * TODO: Implement modifiers and re-rolls
     * 
     * @param attacker Is an object which holds all the attacker data input by the user
     * 
     * @param weapon Is an object which holds all the weapon data input by the user
     * 
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
        double woundsDealt = hits * ((7.0 - toWound)/6); 

        // Then the defender has a chance to save against woundsDealt and feelNoPain unsaved damage
        int effSave = defender.getSave(); // Effective save, saves the value of a save minus armorPen
        if (!defender.getInvulSave()) {
            effSave = effSave + weapon.getArmorPen();
        }
        double avgDamage = (woundsDealt * (1 - ((7.0 - effSave)/6.0)) * weapon.getDamage()) * 
            (1 - ((7.0 - defender.getFeelNoPain())/6.0));
        return avgDamage;
    }

    /**
     * Calculates the average number of defending models killed from an attack. Must be invoked after
     * 
     * TODO: Implement modifiers and re-rolls
     * 
     * @param avgDamage the average total damage from an attack, calculated by the calcAvgDamage method
     * 
     * @param weapon Is an object which holds all the weapon data input by the user. The damage stat is the
     * only one used in this method.
     * 
     * @param defender Is an object which holds all the defender data input by the user. The wounds stat is the
     * only one used in this method.
     * 
     * @return and int that shows the average number of defending models killed by the attack
     */
    public static int calcAvgModelsKilled(double avgDamage, Weapon weapon, Defender defender) {
        int modelsKilled = 0;
        int modelWounds = defender.getWounds();
        int fxdAvgDamage = (int)Math.floor(avgDamage); // Turns avgDamage into an int for this method
        // Applies damage on a per-model basis (as explained in the damage parameter) until there is no more 
        // damage to apply
        while (fxdAvgDamage > 0) {
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

}
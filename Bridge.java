import java.util.*;
import java.sql.*;

public interface Bridge {
    // TODO: Also need methods to look up units by primary key
    boolean userExists(UserId userId);
    void addUser(UserId userId);
    Entry<Attacker> saveAttacker(UserId userId, Attacker attacker);
    Entry<Weapon> saveWeapon(UserId userId, Weapon weapon);
    Entry<Defender> saveDefender(UserId userId, Defender defender);
    boolean saveSimulation(UserId userId, Pk attackerPk, Pk weaponPk, Pk defenderPk, Modifiers modifiers, Simulation results);
    List<Entry<Attacker>> loadAttackers(UserId userId);
    List<Entry<Weapon>> loadWeapons(UserId userId);
    List<Entry<Defender>> loadDefenders(UserId userId);
    List<Simulation> loadSimulations(UserId userId);
}



class UserId {
    // Fields and methods for UserId class
    private int id;

    public UserId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}

class Pk {
    // Fields and methods for Pk class
    private int pk;

    public Pk(int pk) {
        this.pk = pk;
    }

    public int getPk() {
        return this.pk;
    }
}

// Contains all the inputs and outputs for a simulation. Provides access to static average values and
// the ability to re-run simulations.
class Simulation {
    // Fields and methods for Simulation class
    private Attacker attacker;
    private Weapon weapon;
    private Defender defender;
    private Modifiers modifiers;

    private double avgDamage;
    private int avgModelsKilled;

    private int simDamage;
    private int simModelsKilled;

    public Simulation(Attacker attacker, Weapon weapon, Defender defender, Modifiers modifiers) {
        this.attacker = attacker;
        this.weapon = weapon;
        this.defender = defender;
        this.modifiers = modifiers;

        this.avgDamage = CalculateDamage.calcAvgDamage(this.attacker, this.weapon, this.defender, this.modifiers);
        this.avgModelsKilled = CalculateDamage.calcModelsKilled(this.avgDamage, this.weapon, this.defender, this.modifiers);

        this.simDamage = CalculateDamage.simAttackDamage(this.attacker, this.defender, this.weapon, this.modifiers);
        this.simModelsKilled = CalculateDamage.calcModelsKilled(this.simDamage, this.weapon, this.defender, this.modifiers);
    }

    // Both values are calculated once and never touched again by the calculation
    public double getAvgDamage() {
        return this.avgDamage;
    }
    public int getAvgModelsKilled() {
        return this.avgModelsKilled;
    }

    // Getters for simulated attack outputs
    public int getSimDamage() {
        return this.simDamage;
    }
    public int getSimModelsKilled() {
        return this.simModelsKilled;
    }
    // Simulates another attack, replacing the previous values
    public void reSimulate() {
        this.simDamage = CalculateDamage.simAttackDamage(this.attacker, this.defender, this.weapon, this.modifiers);
        this.simModelsKilled = CalculateDamage.calcModelsKilled(this.simDamage, this.weapon, this.defender, this.modifiers);
    }
}

/* Merged this class with the simulation class
class FixedSimResults {
    // Fields and methods for FixedSimResults class
    private double avgDamage;
    private double avgModelsKilled;

    public FixedSimResults(Attacker attacker, Weapon weapon, Defender defender) {

    }
}
*/

class Entry<UnitType> {
    // Fields and methods for Entry class
    
}



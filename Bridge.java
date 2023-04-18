import java.util.*;

public interface Bridge {
    Entry<Attacker> saveAttacker(UserId userId, Attacker attacker);
    Entry<Weapon> saveWeapon(UserId userId, Weapon weapon);
    Entry<Defender> saveDefender(UserId userId, Defender defender);
    boolean saveSimulation(UserId userId, Pk attackerPk, Pk weaponPk, Pk defenderPk, Modifiers modifiers, FixedSimResults results);
    List<Entry<Attacker>> loadAttackers(UserId userId);
    List<Entry<Weapon>> loadWeapons(UserId userId);
    List<Entry<Defender>> loadDefenders(UserId userId);
    List<Simulation> loadSimulations(UserId userId);
}

class UserId {
    // Fields and methods for UserId class
}

class Pk {
    // Fields and methods for Pk class
}

class Simulation {
    // Fields and methods for Simulation class
}

class FixedSimResults {
    // Fields and methods for FixedSimResults class
}

class Entry<UnitType> {
    // Fields and methods for Entry class
}

class UnitType {
    // Fields and methods for UnitType class
    // This class can be extended by Attacker, Weapon, and Defender classes
}



import java.util.*;
import java.sql.*;

public class Bridge {
    // TODO: Also need methods to look up units by primary key
    private final String url = "jdbc:mysql://localhost:3306/40kBattleSim";
    private final String username = "your_username";
    private final String password = "your_password";

    boolean userExists(UserId userId);
    void addUser(UserId userId);
    /*
     * Saves an attacker into the table
     */
    public Entry<Attacker> saveAttacker(UserId userId, Attacker attacker) {
        Entry<Attacker> entry = null;
        String unitName = attacker.getName();
        int ballisticSkill = attacker.getBalSkill();
        int weaponSkill = attacker.getWepSkill();

        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "INSERT INTO Attacker (user_id, unit_name, ballistic_skill, weapon_skill) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set the parameter values for the SQL query
            statement.setInt(1, userId.getId());
            statement.setString(2, unitName);
            statement.setInt(3, ballisticSkill);
            statement.setInt(4, weaponSkill);

            // Execute the SQL query
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the generated attacker_id
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int attackerId = generatedKeys.getInt(1);
                    // Create the Entry object with the saved data
                    entry = new Entry<Attacker>(attacker, userId, attackerId);
                }
            }

            // Close the database connection and statement
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entry;
    }

    Entry<Weapon> saveWeapon(UserId userId, Weapon weapon);
    Entry<Defender> saveDefender(UserId userId, Defender defender);
    boolean saveSimulation(UserId userId, int attackerPk, int weaponPk, int defenderPk, Modifiers modifiers, Simulation results);
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

class Entry<UnitType> {
    // Fields and methods for Entry class
    private UnitType unitType;
    private UserId userId;
    private int pk; //primary key

    public Entry(UnitType unitType, UserId userId, int pk) {
        this.unitType = unitType;
        this.userId = userId;
        this.pk = pk;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

    public UserId getUserId() {
        return this.userId;
    }

    public int getPk() {
        return this.pk;
    }

}



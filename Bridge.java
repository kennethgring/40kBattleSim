import java.util.*;
import java.sql.*;

public class Bridge {
    // TODO: Also need methods to look up units by primary key. ALSO double check queries
    private final String url = "jdbc:mysql://localhost:3306/40kBattleSim";
    private final String username = "your_username";
    private final String password = "your_password";

    // TODO: Update when userId table comes out
    boolean userExists(int userId) {
        boolean exists = false;
        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "SELECT COUNT(*) FROM Attacker a " +
            "JOIN Weapon w ON a.user_id = w.user_id " +
            "JOIN Defender d ON w.user_id = d.user_id " +
            "JOIN Calculations c ON d.user_id = c.user_id " +
            "WHERE a.user_id = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            // Execute the SQL query
            ResultSet resultSet = statement.executeQuery();

            // Check if the user exists in the Attacker table
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = count > 0;
            }

            // Close the database connection and statement
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    void addUser(int userId);

    /*
     * Saves an attacker into the table
     */
    public Entry<Attacker> saveAttacker(int userId, Attacker attacker) {
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
            statement.setInt(1, userId);
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

    /*
     * Saves a weapon into the table
     * TODO: Update once num is added to table
     */
    Entry<Weapon> saveWeapon(int userId, Weapon weapon) {
        Entry<Weapon> entry = null;
        String name = weapon.getName();
        int num = weapon.getNum();
        boolean isRanged = weapon.getIsRanged();
        int attacks = weapon.getAttacks();
        int strength = weapon.getStrength();
        int armorPen = weapon.getArmorPen();
        int damage = weapon.getDamage();

        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "INSERT INTO Weapon (user_id, weapon_type, weapon_name, attacks, strength, armor_pen, damage) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set the parameter values for the SQL query
            statement.setInt(1, userId);
            statement.setBoolean(2, isRanged);
            statement.setString(3, name);
            statement.setInt(4, attacks);
            statement.setInt(5, strength);
            statement.setInt(6, armorPen);
            statement.setInt(7, damage);

            // Execute the SQL query
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the generated attacker_id
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int weaponId = generatedKeys.getInt(1);
                    // Create the Entry object with the saved data
                    entry = new Entry<Weapon>(weapon, userId, weaponId);
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

    Entry<Defender> saveDefender(int userId, Defender defender) {
        Entry<Defender> entry = null;
        String name = defender.getName();
        int size = defender.getSize();
        int toughness = defender.getToughness();
        int save = defender.getSave();
        int wounds = defender.getWounds();
        int fnp = defender.getFeelNoPain();

        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "INSERT INTO Defender (user_id, unit_name, size, toughness, save, wounds, feel_no_pain) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set the parameter values for the SQL query
            statement.setInt(1, userId);
            statement.setString(2, name);
            statement.setInt(3, size);
            statement.setInt(4, toughness);
            statement.setInt(5, save);
            statement.setInt(6, wounds);
            statement.setInt(7, fnp);
            

            // Execute the SQL query
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the generated attacker_id
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int defenderId = generatedKeys.getInt(1);
                    // Create the Entry object with the saved data
                    entry = new Entry<Defender>(defender, userId, defenderId);
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

    boolean saveSimulation(int userId, int attackerPk, int weaponPk, int defenderPk, Modifiers modifiers, Simulation results);
    List<Entry<Attacker>> loadAttackers(int userId);
    List<Entry<Weapon>> loadWeapons(int userId);
    List<Entry<Defender>> loadDefenders(int userId);
    List<Simulation> loadSimulations(int userId);
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
    private int userId;
    private int pk; //primary key

    public Entry(UnitType unitType, int userId, int pk) {
        this.unitType = unitType;
        this.userId = userId;
        this.pk = pk;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getPk() {
        return this.pk;
    }

}



package com.example.application;

import java.util.*;
import java.sql.*;
import java.util.List;
import java.util.Random;

import com.example.application.unit.*;

/*
 * The bridge class contains the following methods for the front end to interact with the back end.
 * NOTE: MUST BE ON CAMPUS VPN TO ACCESS DB PROPERLY!
 * 
 * These methods checks for an existing user and can add a user to the table:
 *      userExists - Returns ture if the user exists
 *      addUser - Generates a random, unused int for a new user
 * 
 * These methods add data to their associated tables:
 *      saveAttacker
 *      saveWeapon
 *      saveDefender
 *      saveSimulation
 * 
 * These methods search for a single row in the associated table based on a passed in primary key:
 *      findAttacker
 *      findWeapon
 *      findDefender
 *      findSimulation
 * 
 * These methods find all the rows of the associated table and return them as a list
 *      loadAttackers
 *      loadDefenders
 *      loadWeapons
 *      loadSimulations
 * 
 */
public class Bridge {
    private static String url = "jdbc:mysql://"
        + (System.getenv().getOrDefault("MYSQL_HOST",
                                        "cs506-team-09.cs.wisc.edu"))
        + ":3306/40kBattleSim";
    private static String username = "root";
    private static String password = "baktop09";
    private static Random random = new Random();

    /*
     * Checks if a user with the passed in userId exists in the User_IDs table.
     * Returns true if it does.
     */
    public static boolean userExists(int userId) {
        boolean exists = false;
        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "SELECT COUNT(*) FROM User_IDs u WHERE u.user_id = ?";

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

    /*
     * Adds a new user to the User_IDs table
     */
    public static int addUser() {
        int userId;
        for (;;) {
            userId = random.nextInt(1 << 23);
            if (!userExists(userId)) {
                break;
            }
        }
        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "INSERT INTO User_IDs (user_id) VALUES (?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            statement.executeUpdate();

            
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    /*
     * Saves an attacker object into the Attacker table
     */
    public static Entry<Attacker> saveAttacker(int userId, Attacker attacker) {
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
     * Saves a weapon object into the Weapon table
     */
    public static Entry<Weapon> saveWeapon(int userId, Weapon weapon) {
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
            String query = "INSERT INTO Weapon (user_id, isRanged, weapon_name, attacks, strength, armor_pen, damage, number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set the parameter values for the SQL query
            statement.setInt(1, userId);
            statement.setBoolean(2, isRanged);
            statement.setString(3, name);
            statement.setInt(4, attacks);
            statement.setInt(5, strength);
            statement.setInt(6, armorPen);
            statement.setInt(7, damage);
            statement.setInt(8, num);

            // Execute the SQL query
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the generated weapon_id
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

    /*
     * Saves a defender object into the Defender table
     */
    public static Entry<Defender> saveDefender(int userId, Defender defender) {
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
                // Retrieve the generated defender_id
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

    /*
     * Saves a calculation into the calculations table, returns true on success
     */
    public static boolean saveSimulation(int userId, int attackerPk, int weaponPk, int defenderPk, Modifiers modifiers) {
        boolean added = false;
        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "INSERT INTO Calculations (user_id, attacker_id, weapon_id, defender_id, `hit+1`, `hit-1`, reroll_hits, reroll_hit_1," +
                "reroll_wounds, exploding_hits, mortal_wounds_hit, mortal_wounds_wound, additional_ap_wound, `save+1`, `save-1`," +
                "invulnerable_save, reroll_save, reroll_save_1, `damage-1`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            // Set the parameter values for the SQL query
            statement.setInt(1, userId);
            statement.setInt(2, attackerPk);
            statement.setInt(3, weaponPk);
            statement.setInt(4, defenderPk);
            statement.setBoolean(5, modifiers.getHitPlusOne());
            statement.setBoolean(6, modifiers.getHitMinusOne());
            statement.setBoolean(7, modifiers.getRerollHits());
            statement.setBoolean(8, modifiers.getRerollHitsOne());
            statement.setBoolean(9, modifiers.getRerollWounds());
            statement.setBoolean(10, modifiers.getExplodingHits());
            statement.setBoolean(11, modifiers.getMortalWoundHits());
            statement.setBoolean(12, modifiers.getMortalWoundWounds());
            statement.setBoolean(13, modifiers.getExtraAPWound());
            statement.setBoolean(14, modifiers.getSavePlusOne());
            statement.setBoolean(15, modifiers.getSaveMinusOne());
            statement.setBoolean(16, modifiers.getInvulSave());
            statement.setBoolean(17, modifiers.getRerollSave());
            statement.setBoolean(18, modifiers.getRerollSaveOne());
            statement.setBoolean(19, modifiers.getDamageMinusOne());
            
            // Execute the SQL query
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                added = true;
            }

            // Close the database connection and statement
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return added;
    }

    /*
     * Finds a single row from the Attacker table using the passed in attackerId and saves it into an
     * Entry<Attacker> object which it returns
     */
    public static Entry<Attacker> findAttacker(int attackerId) {
        Entry<Attacker> entry = null;
        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "SELECT user_id, unit_name, ballistic_skill, weapon_skill FROM Attacker WHERE attacker_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            // Set the parameter values for the SQL query
            statement.setInt(1, attackerId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                // Retrieve values from the retrieved row
                int userId = rs.getInt("user_id");
                String unitName = rs.getString("unit_name");
                int ballisticSkill = rs.getInt("ballistic_skill");
                int weaponSkill = rs.getInt("weapon_skill");
    
                // Create Attacker object with retrieved values
                Attacker attacker = new Attacker(unitName, ballisticSkill, weaponSkill);
    
                // Create Entry<Attacker> object with Attacker object and retrieved userId and attackerId values
                entry = new Entry<Attacker>(attacker, userId, attackerId);
            }

            // Close the database connection and statement
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entry;
    }

    /*
     * Finds a single row from the Weapon table using the passed in weaponId and saves it into an
     * Entry<Weapon> object which it returns
     */
    public static Entry<Weapon> findWeapon(int weaponId) {
        Entry<Weapon> entry = null;
        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "SELECT user_id, isRanged, weapon_name, attacks, strength, armor_pen, damage, number FROM Weapon WHERE weapon_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            // Set the parameter values for the SQL query
            statement.setInt(1, weaponId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                // Retrieve values from the retrieved row
                int userId = rs.getInt("user_id");
                Boolean isRanged = rs.getBoolean("isRanged");
                String weaponName = rs.getString("weapon_name");
                int attacks = rs.getInt("attacks");
                int strength = rs.getInt("strength");
                int armorPen = rs.getInt("armor_pen");
                int damage = rs.getInt("damage");
                int number = rs.getInt("number");
    
                // Create Weapon object with retrieved values
                Weapon weapon = new Weapon(weaponName, number, isRanged, attacks, strength, armorPen, damage);
    
                // Create Entry<Weapon> object with Weapon object and retrieved userId and weaponId values
                entry = new Entry<Weapon>(weapon, userId, weaponId);
            }

            // Close the database connection and statement
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entry;
    }

    /*
     * Finds a single row from the Defender table using the passed in defenderId and saves it into an
     * Entry<Defender> object which it returns
     */
    public static Entry<Defender> findDefender(int defenderId) {
        Entry<Defender> entry = null;
        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "SELECT user_id, unit_name, size, toughness, save, wounds, feel_no_pain FROM Defender WHERE defender_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            // Set the parameter values for the SQL query
            statement.setInt(1, defenderId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                // Retrieve values from the retrieved row
                int userId = rs.getInt("user_id");
                String unitName = rs.getString("unit_name");
                int size = rs.getInt("size");
                int toughness = rs.getInt("toughness");
                int save = rs.getInt("save");
                int wounds = rs.getInt("wounds");
                int fnp = rs.getInt("feel_no_pain");
    
                // Create Defender object with retrieved values
                Defender defender = new Defender(unitName, size, toughness, save, wounds, fnp);
    
                // Create Entry<Defender> object with Defender object and retrieved userId and defenderId values
                entry = new Entry<Defender>(defender, userId, defenderId);
            }

            // Close the database connection and statement
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entry;
    }

    /*
     * Finds a single row from the Calculation table using the passed in calcId and saves it into a
     * Simulation object which it returns
     */
    public static Simulation findSimulation(int calcId) {
        Simulation simulation = null;
        try {
            // Connect to the MySQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL query
            String query = "SELECT attacker_id, weapon_id, defender_id, `hit+1`, `hit-1`, reroll_hits, reroll_hit_1," +
            "reroll_wounds, exploding_hits, mortal_wounds_hit, mortal_wounds_wound, additional_ap_wound, `save+1`, `save-1`," +
            "invulnerable_save, reroll_save, reroll_save_1, `damage-1` FROM Calculations WHERE calc_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            // Set the parameter values for the SQL query
            statement.setInt(1, calcId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                // Retrieve values from the retrieved row
                int attackerId = rs.getInt("attacker_id");
                int weaponId = rs.getInt("weapon_id");
                int defenderId = rs.getInt("defender_id");
                
                boolean hitsPlusOne = rs.getBoolean("hit+1");
                boolean hitMinusOne = rs.getBoolean("hit-1");
                boolean rerollHits = rs.getBoolean("reroll_hits");
                boolean rerollHitsOne = rs.getBoolean("reroll_hit_1");
                boolean rerollWounds = rs.getBoolean("reroll_wounds");
                boolean explodingHits = rs.getBoolean("exploding_hits");
                boolean mortalWoundsHit = rs.getBoolean("mortal_wounds_hit");
                boolean mortalWoundsWounds = rs.getBoolean("mortal_wounds_wound");
                boolean additionalAp = rs.getBoolean("additional_ap_wound");
                boolean savePlusOne = rs.getBoolean("save+1");
                boolean saveMinusOne = rs.getBoolean("save-1");
                boolean invulnerableSave = rs.getBoolean("invulnerable_save");
                boolean rerollSave = rs.getBoolean("reroll_save");
                boolean rerollSaveOne = rs.getBoolean("reroll_save_1");
                boolean damageMinusOne = rs.getBoolean("damage-1");
    
                // Create Modifiers and Simulation objects with retrieved values
                boolean[] mods = {hitsPlusOne, hitMinusOne, rerollHits, rerollHitsOne, rerollWounds, explodingHits,
                    mortalWoundsHit, mortalWoundsWounds, additionalAp, savePlusOne, saveMinusOne, invulnerableSave,
                    rerollSave, rerollSaveOne, damageMinusOne};
                Modifiers modifiers = new Modifiers(mods);
                simulation = new Simulation(attackerId, weaponId, defenderId, modifiers);
            }

            // Close the database connection and statement
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return simulation;
    }

    /**
     * Populates and returns a List of type Entry<Attacker> for frontend to use.
     * @param userId all Entry<Attackers> that this userId can access
     * @return 
     */
    public static List<Entry<Attacker>> loadAttackers(int userId) {

        List<Entry<Attacker>> attacker_list = new ArrayList<Entry<Attacker>>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM Attacker WHERE user_id = " + userId + " OR user_id = 0;");
            
            while (result.next()) {
                String unit_name = result.getString("unit_name");
                int ballistic_skill = result.getInt("ballistic_skill");
                int weapon_skill = result.getInt("weapon_skill");
                int pk = result.getInt("attacker_id");

                Attacker attacker = new Attacker(unit_name, ballistic_skill, weapon_skill);
                Entry<Attacker> entry = new Entry<Attacker>(attacker, userId, pk);
                attacker_list.add(entry);
            }
            conn.close();
            return attacker_list;

        } catch(Exception e){
            System.out.println("Exception: " + e);
            return null;
        }

    }

    /**
     * Method to populate List with all of userId's Weapon records.
     * @param userId foreign key associated with Weapon table
     * @return list with all available Weapon records for given userId
     */
    public static List<Entry<Weapon>> loadWeapons(int userId) {

        List<Entry<Weapon>> weapon_list = new ArrayList<Entry<Weapon>>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM Weapon WHERE user_id = " + userId + " OR user_id = 0;");
            
            while (result.next()) {
                String weapon_name = result.getString("weapon_name");
                int num = result.getInt("number");
                boolean isRanged = result.getBoolean("isRanged");
                int attacks = result.getInt("attacks");
                int strength = result.getInt("strength");
                int armor_pen = result.getInt("armor_pen");
                int damage = result.getInt("damage");
                int pk = result.getInt("weapon_id");

                Weapon weapon = new Weapon(weapon_name, num, isRanged, attacks, strength, armor_pen, damage);
                Entry<Weapon> entry = new Entry<Weapon>(weapon, userId, pk); 
                weapon_list.add(entry);
            }
            conn.close();
            return weapon_list;

        } catch(Exception e){
            System.out.println("Exception: " + e);
            return null;
        }
    }

    /**
     * Method to populate List with all of userId's Defender records.
     * @param userId foreign key associated with Defender table
     * @return list with all available Defender records for given userId
     */
    public static List<Entry<Defender>> loadDefenders(int userId) {

        List<Entry<Defender>> defender_list = new ArrayList<Entry<Defender>>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM Defender WHERE user_id = " + userId + " OR user_id = 0;");
            
            while (result.next()) {
                String unit_name = result.getString("unit_name");
                int size = result.getInt("size");
                int toughness = result.getInt("toughness");
                int save = result.getInt("save");
                int wounds = result.getInt("wounds");
                int feel_no_pain = result.getInt("feel_no_pain");
                int pk = result.getInt("defender_id");

                Defender defender = new Defender(unit_name, size, toughness, save, wounds, feel_no_pain);
                Entry<Defender> entry = new Entry<Defender>(defender, userId, pk);
                defender_list.add(entry);
            }
            conn.close();
            return defender_list;

        } catch(Exception e){
            System.out.println("Exception: " + e);
            return null;
        }
    }
    
    /**
     * Method to populate List with all of userId's Calculations records.
     * @param userId foreign key associated with Calculations table
     * @return list with all available Calculations records for given userId
     */
    public static List<Entry<Simulation>> loadSimulations(int userId) {

        List<Entry<Simulation>> calc_list = new ArrayList<Entry<Simulation>>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM Calculations WHERE user_id = " + userId + " OR user_id = 0;");
            
            while (result.next()) {
                int attacker_id = result.getInt("attacker_id");
                int defender_id = result.getInt("defender_id");
                int weapon_id = result.getInt("weapon_id");
                
                boolean hitsPlusOne = result.getBoolean("hit+1");
                boolean hitMinusOne = result.getBoolean("hit-1");
                boolean rerollHits = result.getBoolean("reroll_hits");
                boolean rerollHitsOne = result.getBoolean("reroll_hit_1");
                boolean rerollWounds = result.getBoolean("reroll_wounds");
                boolean explodingHits = result.getBoolean("exploding_hits");
                boolean mortalWoundsHit = result.getBoolean("mortal_wounds_hit");
                boolean mortalWoundsWounds = result.getBoolean("mortal_wounds_wound");
                boolean additionalAp = result.getBoolean("additional_ap_wound");
                boolean savePlusOne = result.getBoolean("save+1");
                boolean saveMinusOne = result.getBoolean("save-1");
                boolean invulnerableSave = result.getBoolean("invulnerable_save");
                boolean rerollSave = result.getBoolean("reroll_save");
                boolean rerollSaveOne = result.getBoolean("reroll_save_1");
                boolean damageMinusOne = result.getBoolean("damage-1");
    
                // Create Modifiers and Simulation objects with retrieved values
                boolean[] mods = {hitsPlusOne, hitMinusOne, rerollHits, rerollHitsOne, rerollWounds, explodingHits,
                    mortalWoundsHit, mortalWoundsWounds, additionalAp, savePlusOne, saveMinusOne, invulnerableSave,
                    rerollSave, rerollSaveOne, damageMinusOne};
                Modifiers modifiers = new Modifiers(mods);

                int pk = result.getInt("calc_id");

                Simulation calc = new Simulation(attacker_id, weapon_id, defender_id, modifiers);
                Entry<Simulation> entry = new Entry<Simulation>(calc, userId, pk);
                calc_list.add(entry);
            }
            conn.close();
            return calc_list;

        } catch(Exception e){
            System.out.println("Exception: " + e);
            return null;
        }
    }

    /**
     * Main method to test changes locally.
     * @param args
     */
    public static void main(String[] args) {
        Bridge test = new Bridge();

        // List tests //
        List<Entry<Attacker>> attacker_list = test.loadAttackers(0);
        System.out.println(attacker_list);

        for (int i = 0; i < attacker_list.size(); i++) {
            System.out.println("attacker_list[" + i + "] pk: " + attacker_list.get(i).getPk());
        }

        List<Entry<Weapon>> weap_list = test.loadWeapons(0);
        System.out.println(weap_list);

        for (int i = 0; i < weap_list.size(); i++) {
            System.out.println("weap_list[" + i + "] pk: " + weap_list.get(i).getPk());
        }

        List<Entry<Defender>> def_list = test.loadDefenders(0);
        System.out.println(def_list);
        
        for (int i = 0; i < def_list.size(); i++) {
            System.out.println("def_list[" + i + "] pk: " + def_list.get(i).getPk());
        }

        // User Tests //
        if (userExists(0)) {
            System.out.println("Test Passed: User 0 exists");
        } else {
            System.out.println("Test Failed: User 0 does not exist");
        }

        //addUser(69);
        if (userExists(69)) {
            System.out.println("Test Passed: User 69 was added");
        } else {
            System.out.println("Test Failed: User 69 was not added");
        } 

        // Find Method Tests //
        Entry<Attacker> atkrFindTest = findAttacker(1);
        System.out.print(atkrFindTest.toString());

        Entry<Defender> dfndrFindTest = findDefender(1);
        System.out.print(dfndrFindTest.toString());

        Entry<Weapon> wpnFindTest = findWeapon(1);
        System.out.print(wpnFindTest.toString());

        // Save Method Tests //
        /* In many of the below tests there is code that is commented out. All of these blocks of code add entries to the DB
         * and the following uncommented blocks retrieve those entries as testing. If the entries are ever deleted, uncomment those
         * blocks and then run main. Make sure to recomment them afterwards or else the DB will fill with duplicate values after every
         * test.
         */
        /* This code was executed once, the code below shows it succeeded
        Attacker atkrSave = new Attacker("save_test_atkr", 4, 4);
        Entry<Attacker> atkrSaveEntry = saveAttacker(69, atkrSave);
        */
        List<Entry<Attacker>> attacker_list_69 = loadAttackers(69);
        System.out.println(attacker_list_69);

        /* This code was executed once, the code below shows it succeeded
        Defender dfndrSave = new Defender ("save_test_dfndr", 10, 4, 4, 2, 4);
        Entry<Defender> dfndrSaveEntry = saveDefender(69, dfndrSave);
        */
        List<Entry<Defender>> defender_list_69 = loadDefenders(69);
        System.out.println(defender_list_69);

        /* This code was executed once, the code below shows it succeeded
        Weapon wpnSave = new Weapon ("save_test_wpn", 10, true, 10, 4, -2, 1);
        Entry<Weapon> wpnSaveEntry = saveWeapon(69, wpnSave);
        */
        List<Entry<Weapon>> weapon_list_69 = loadWeapons(69);
        System.out.println(weapon_list_69);

        // Calculation Tests // 
        /* The below code was executed successfully but commented out to avoid filling the table with duplicates
        Modifiers modTest;
        boolean[] mods = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        modTest = new Modifiers(mods);
        if (saveSimulation(69, 3, 3, 3, modTest)) {
            System.out.println("Test passed: Calculation was added");
        } else {
            System.out.println("Test failed: Calculation was not saved");
        }
        */
        List<Entry<Simulation>> simList = loadSimulations(69);
        System.out.println(simList);

        Simulation simFindTest = findSimulation(1);
        System.out.println("AttackerId: " + simFindTest.getAttackerId() + 
            ", WeaponId: " + simFindTest.getWeaponId() + ", DefenderId: " + simFindTest.getDefenderId());
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

    private int attackerId;
    private int weaponId;
    private int defenderId;

    private double avgDamage;
    private int avgModelsKilled;

    private int simDamage;
    private int simModelsKilled;

    // Constructor that uses objects. This will not have the object primary keys!
    public Simulation(Attacker attacker, Weapon weapon, Defender defender, Modifiers modifiers) {
        this.attacker = attacker;
        this.defender = defender;
        this.weapon = weapon;

        this.modifiers = modifiers;

        this.avgDamage = CalculateDamage.calcAvgDamage(this.attacker, this.weapon, this.defender, this.modifiers);
        this.avgModelsKilled = CalculateDamage.calcModelsKilled(this.avgDamage, this.weapon, this.defender, this.modifiers);

        this.simDamage = CalculateDamage.simAttackDamage(this.attacker, this.defender, this.weapon, this.modifiers);
        this.simModelsKilled = CalculateDamage.calcModelsKilled(this.simDamage, this.weapon, this.defender, this.modifiers);
    }

    // Constructor that uses object Ids
    public Simulation(int attackerId, int weaponId, int defenderId, Modifiers modifiers) {
        this.attackerId = attackerId;
        this.weaponId = weaponId;
        this.defenderId = defenderId;
        Entry<Attacker> atkr = Bridge.findAttacker(attackerId); 
        Entry<Defender> dfnr = Bridge.findDefender(defenderId);
        Entry<Weapon> wpn = Bridge.findWeapon(weaponId);

        this.attacker = atkr.getUnitType();
        this.defender = dfnr.getUnitType();
        this.weapon = wpn.getUnitType();

        this.modifiers = modifiers;

        this.avgDamage = CalculateDamage.calcAvgDamage(this.attacker, this.weapon, this.defender, this.modifiers);
        this.avgModelsKilled = CalculateDamage.calcModelsKilled(this.avgDamage, this.weapon, this.defender, this.modifiers);

        this.simDamage = CalculateDamage.simAttackDamage(this.attacker, this.defender, this.weapon, this.modifiers);
        this.simModelsKilled = CalculateDamage.calcModelsKilled(this.simDamage, this.weapon, this.defender, this.modifiers);
    }

    public Attacker getAttacker() {
        return attacker;
    }
    public Weapon getWeapon() {
        return weapon;
    }
    public Defender getDefender() {
        return defender;
    }
    public Modifiers getModifiers() {
        return modifiers;
    }

    public int getAttackerId() {
        return attackerId;
    }
    public int getWeaponId() {
        return weaponId;
    }
    public int getDefenderId() {
        return defenderId;
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

/*
 * Generic class that can hold either an Attacker, Weapon, or Defender object as well as the associated
 * primary key and userId
 */
class Entry<UnitType> {
    // Fields and methods for Entry class
    private UnitType unitType;
    private int userId;
    private int pk; //primary key

    public Entry(UnitType unitType, int userId, int pk) {
        this.unitType = (UnitType) unitType;
        this.userId = userId;
        this.pk = pk;
    }

    // Overrides the default toString() method, returns a different string depending on object type of
    // UnitType
    public String toString() {

        if (unitType instanceof Attacker) {
            Attacker attacker = (Attacker) unitType;
            return "Attacker: [Name: " + attacker.getName() + ", Ballistic Skill: " 
                + attacker.getBalSkill() + ", Weapon Skill: " + attacker.getWepSkill() + "]\n";

        } else if (unitType instanceof Defender) {
            Defender defender = (Defender) unitType;
            return "Defender: [Name: " + defender.getName() + ", Size: " + defender.getSize() + 
                ", Toughness: " + defender.getToughness() + ", Save: " + defender.getSave() +
                ", Wounds: " + defender.getFeelNoPain() + ", Feel No Pain: " + defender.getFeelNoPain()
                + "]\n";

        } else if (unitType instanceof Weapon) {
            Weapon weapon = (Weapon) unitType;
            return "Weapon: [Name: " + weapon.getName() + ", Number: " + weapon.getNum() + ", isRanged: " +
                weapon.getIsRanged() + ", Attacks: " + weapon.getAttacks() + ", Strength: " + weapon.getStrength()
                + ", Armor Penetration: " + weapon.getArmorPen() + ", Damage: " + weapon.getDamage() + "]\n";

        } else if (unitType instanceof Simulation) {
            Simulation sim = (Simulation) unitType;
            return "Simulation: [Attacker_Id: " + sim.getAttackerId() + ", Weapon_Id: " + sim.getWeaponId() +
                ", DefenderId: " + sim.getDefenderId() + "]\n";
        }

        return "Error: UnitType not Attacker, Defender, Weapon or Simulation.\n";
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
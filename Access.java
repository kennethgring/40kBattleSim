import java.sql.*;

public class Access{
    // Fields to for Connection
    private final String url;
    private final String user;
    private final String password;

    // Construtor that sets field values
    public Access(String url, String user, String password){
        this.url = url;
        this.user = user;
        this.password = password;
    }

    // Gets the calculations data for a specific calc_id
    public Calc getCalcData(int calc_id){
        try(Connection connection = DriverManager.getConnection(url, user, password)){
            // Executes query for the calculations data
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT *, a.unit_name AS attacker_unit_name, d.unit_name AS defender_unit_name " +
                            "FROM Attacker a, Weapon w, Defender d, Calculations c " +
                            "WHERE c.attacker_id = a.attacker_id " +
                            "AND c.weapon_id = w.weapon_id " +
                            "AND c.defender_id = d.defender_id " +
                            "AND c.calc_id = " + calc_id + ";");

            // If the calculation exists, return a Calc object that contains the data
            if(result.next()){
                String attacker_unit_name = result.getString("attacker_unit_name");
                int ballistic_skill = result.getInt("ballistic_skill");
                int weapon_skill = result.getInt("weapon_skill");

                boolean weapon_type = result.getBoolean("weapon_type");
                String weapon_name = result.getString("weapon_name");
                int attacks = result.getInt("attacks");
                int strength = result.getInt("strength");
                int armor_pen = result.getInt("armor_pen");
                double damage = result.getDouble("damage");

                String defender_unit_name = result.getString("defender_unit_name");
                int size = result.getInt("size");
                int toughness = result.getInt("toughness");
                int save = result.getInt("save");
                int wounds = result.getInt("wounds");
                int feel_no_pain = result.getInt("feel_no_pain");

                String modifiersString = result.getString("modifiers");

                String[] modifiers = modifiersString.split(",");

                Calc data = new Calc(attacker_unit_name, ballistic_skill, weapon_skill, weapon_type, weapon_name, attacks, strength, armor_pen, damage, defender_unit_name, size, toughness, save, wounds, feel_no_pain, modifiers);

                return data;
            }

            return null;
        } catch(SQLException e){
            System.out.println("Error while accessing database - Access:getCalcData");
            return null;
        }
    }
}

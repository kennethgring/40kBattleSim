import java.sql.*;

public class JDBC {

    private final String url; //"jdbc:mysql://localhost:3306/JDBCTest"
    private final String user;
    private final String password;

    /**
     * Constructor for JDBC connection. Requires URL, user, and password.
     * @param url mysql URL, will always be "jdbc:mysql://localhost:3306/JDBCTest"
     * @param user MySQL user, should always be root
     * @param password password, according to .yml file for the MySQL server should be "baktop09"
     */
    public JDBC(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * CAUTION: REQUIRES PORT FORWARD COMMAND! ssh -f -N -L localhost:12205:localhost:12205 cslogin@cs506-team-09.cs.wisc.edu
     * Attempt to connect to the database and run a query on the Attacker table, printing out every available unit.
     * @return String formatted as "column_name: column_value" separated by newlines.
     */
    public String try_connection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM Attacker;");
            
            String res = "";
            if(result.next()){
                res += "user_id: " + result.getInt("user_id") + "\n";
                res += "attacker_id: " + result.getInt("attacker_id") + "\n";
                res += "unit_name: " + result.getString("unit_name") + "\n";
                res += "ballistic_skill: " + result.getInt("ballistic_skill") + "\n";
                res += "weapon_skill: " + result.getInt("weapon_skill") + "\n";
            }
            conn.close();
            return res;

        } catch(Exception e){
            System.out.println("Exception: " + e);
            return null;
        }
    }

    /**
     * Testing main method that calls the try_connection method from above.
     * We must also call the constructor and create a new JDBC object.
     * @param args
     */
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:12205/40kBattleSim";
        String user = "root";
        String password = "baktop09";

        JDBC test = new JDBC(url, user, password);

        System.out.println(test.try_connection());
    }
}
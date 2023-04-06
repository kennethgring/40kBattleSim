package com.example.application.views.main;

import java.sql.*;

public class JDBC {
    static String url; //"jdbc:mysql://localhost:3306/JDBCTest"

    public JDBC (String url) {
        this.url = url;
    }

    public static String Connect(String query) {
        String username = "root";
        String password = "password";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query); //"select * from students"
            String out = "";
            while (resultSet.next()) {
                out = out.concat(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getInt(3) + "\n");
            }
            connection.close();
            return out;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Failed";
    }
}

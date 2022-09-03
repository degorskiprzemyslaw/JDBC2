package jdbc_day1;

import com.mysql.cj.xdevapi.Result;

import java.sql.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        final String DB_URL = "jdbc:mysql://localhost:3306/jdbc_schema?useSSL=false"; //tutaj nazwa bazy
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "degorski");
        connectionProps.put("serverTimezone", "CET");

        Connection conn = null;
        Statement stmt = null;
        try{
            conn = DriverManager.getConnection(DB_URL, connectionProps);

            stmt = conn.createStatement();
            //zad1
            //int rowsChanged = stmt.executeUpdate("INSERT INTO User (nazwa_uzytkownika, haslo, jezyk) VALUES ( 'Barbara Kowalska', 'baekol123', 'polski');");
           // stmt.executeUpdate("INSERT INTO User (nazwa_uzytkownika, haslo, jezyk) VALUES ( 'Dziubek', 'dziubus6783', '" + Language.niemiecki + "');");
            //stmt.executeUpdate("INSERT INTO User (nazwa_uzytkownika, haslo, jezyk) VALUES ( 'matula', 'matus74328', '" + Language.polski + "');");

            stmt.executeUpdate("INSERT INTO User (nazwa_uzytkownika, haslo, jezyk) VALUES ( 'tuptus', 'tuptusl123', 'francuski');");



            //System.out.println("Rows changed: " + rowsChanged);

            //zad2
            //stmt.executeUpdate("UPDATE User SET haslo = 'tomkol567' WHERE Id = 1;");

            //zad3
            // stmt.executeUpdate("DELETE FROM User WHERE Id = 2");
            //ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE jezyk = '" + Language.polski + "'");
            ResultSet rs = stmt.executeQuery("SELECT * FROM User ");

            //zad4
            while(rs.next()){
                System.out.println("Id: " + rs.getInt("Id"));
                System.out.println(", nazwa_uzytkownika: " + rs.getString("nazwa_uzytkownika"));
                System.out.println(", hasło: " + rs.getString("haslo"));
                System.out.println(", język: " + rs.getString("jezyk"));
            }


        }catch(SQLException se){
            se.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}

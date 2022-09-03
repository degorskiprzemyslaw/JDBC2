package jdbc_day1;

import java.sql.*;
import java.util.Properties;

public class MainPreparedStatement {
    public static void main(String[] args) {
        final String DB_URL = "jdbc:mysql://localhost:3306/jdbc_schema?"; //tutaj nazwa bazy
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "degorski");
        connectionProps.put("serverTimezone", "CET");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = DriverManager.getConnection(DB_URL, connectionProps);

            //pstmt = conn.prepareStatement("INSERT INTO User (nazwa_uzytkownika, haslo, jezyk) VALUES ( 'tuptus', 'tuptusl123', ?);");

            /*pstmt = conn.prepareStatement("SELECT * FROM User WHERE jezyk = ?");
            pstmt.setString(1, "polski");*/

            pstmt = conn.prepareStatement("INSERT INTO User (nazwa_uzytkownika, haslo, jezyk) VALUES ( 'ciuddbus', 'ciuddbusl123', ?);");
            pstmt.setString(1, "'japonski'");
            pstmt.executeUpdate();

            Statement stmt = conn.createStatement();
            ResultSet rs = pstmt.executeQuery("SELECT * FROM User ");





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
                if(pstmt!=null)
                    pstmt.close();
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

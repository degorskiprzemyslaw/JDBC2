package jdbc_day2;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.text.DateFormatter;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Repository {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/jdbc_schema";
    private static final String SAVE_USER_SQL = "INSERT INTO User (nazwa_uzytkownika, haslo, jezyk) VALUES ( ?, ?, ?);";
    private static final String UPDATE_USER_SQL = "UPDATE User SET haslo = ? WHERE Id = ?;";
    private static final String UPDATE_ALL_USER_SQL = "UPDATE User SET nazwa_uzytkownika = ?, haslo = ?, jezyk = ? WHERE Id = ?;";
    private static final String DELETE_USER_SQL = "DELETE FROM User WHERE Id = ?;";
    private static final String SELECT_BY_LANGUAGE = "SELECT * FROM User WHERE jezyk = ?";

    private static final String SAVE_POST_SQL = "INSERT INTO Post (tresc, data_utworzenia, UserId) VALUES ( ?, ?, ?);";

    private static final String SELECT_POSTS_BY_LANGUAGE = "SELECT * FROM Post INNER JOIN User ON Post.UserId = User.Id WHERE User.jezyk = ?;";
    private static final String SELECT_POSTS_BY_USERNAME = "SELECT Post.Id, Post.tresc, Post.data_utworzenia, Post.UserId, User.nazwa_uzytkownika FROM Post INNER JOIN User ON Post.UserId = User.Id WHERE User.nazwa_uzytkownika = ?;";
    private static final String SELECT_POSTS_BY_DATE = "SELECT * FROM Post INNER JOIN User ON Post.UserId = User.Id WHERE DATE(data_utworzenia) > ? ;";
    private static final String SELECT_POSTS_BY_ALL = "SELECT * FROM Post INNER JOIN User ON Post.UserId = User.Id WHERE User.jezyk = ? AND User.nazwa_uzytkownika = ? AND DATE(data_utworzenia) > ? ;";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Connection connection = null;


    public Repository() { //mozemy zainicjaliowac polaczenie w konstrukotrze
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "degorski");
        connectionProps.put("serverTimezone", "CET");
        connectionProps.setProperty("useSSL", "false");

        try {
            connection = DriverManager.getConnection(DB_URL, connectionProps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void createUser(User user) {
        try {

            PreparedStatement pstmt = connection.prepareStatement(SAVE_USER_SQL);
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getLanguage().name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserPassword(int id, String password) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_USER_SQL);
            pstmt.setString(1, password);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_ALL_USER_SQL);
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getLanguage().name());
            pstmt.setInt(4, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(DELETE_USER_SQL);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<User> findUserByLanguage(Language language) {
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT_BY_LANGUAGE);
            pstmt.setString(1, language.name());
            ResultSet rs = pstmt.executeQuery();


            while (rs.next()) {

                users.add(new User(rs.getInt("Id"),
                        rs.getString("haslo"),
                        rs.getString("jezyk"),
                        Language.valueOf(rs.getString("jezyk"))));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    // ------ metody dla postów -------


    public void createPost(Post post) {
        try {

            PreparedStatement pstmt = connection.prepareStatement(SAVE_POST_SQL);
            pstmt.setString(1, post.getText());
            pstmt.setString(2, post.getDate().toString());
            pstmt.setInt(3, post.getUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Post> findPostsByLanguage(Language language) {
        List<Post> posts = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT_POSTS_BY_LANGUAGE);
            pstmt.setString(1, language.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                posts.add(new Post(rs.getInt("Id"),
                        rs.getString("tresc"),
                        LocalDate.parse(rs.getString("data_utworzenia"), dtf), //zamiana string na datę DataTimeFormatter
                        rs.getInt("UserId")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public List<Post> findPostsByUserName(String userName) {
        List<Post> posts = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT_POSTS_BY_USERNAME);
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                posts.add(new Post(rs.getInt("Id"),
                        rs.getString("tresc"),
                        LocalDate.parse(rs.getString("data_utworzenia"), dtf), //zamiana string na datę DataTimeFormatter
                        rs.getInt("UserId")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public List<Post> findPostsAfterDate(String date) {
        List<Post> posts = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT_POSTS_BY_DATE);
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (LocalDate.parse(rs.getString("data_utworzenia")).isAfter(LocalDate.parse(date))) { //jezeli sprawdzenie byloby w javie

                    posts.add(new Post(rs.getInt("Id"),
                            rs.getString("tresc"),
                            LocalDate.parse(rs.getString("data_utworzenia"), dtf), //zamiana string na datę DataTimeFormatter
                            rs.getInt("UserId")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public List<Post> findPostsByLanguageUserNameDate(Language language, String userName, LocalDate date) {
        List<Post> posts = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT_POSTS_BY_ALL);
            pstmt.setString(1, language.name());
            pstmt.setString(2, userName);
            pstmt.setString(3, String.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                posts.add(new Post(rs.getInt("Id"),
                        rs.getString("tresc"),
                        LocalDate.parse(rs.getString("data_utworzenia"), dtf), //zamiana string na datę DataTimeFormatter
                        rs.getInt("UserId")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public void createUserWithHashPassword(User user) {
        String hash = DigestUtils.md5Hex(user.getPassword());
        try {

            PreparedStatement pstmt = connection.prepareStatement(SAVE_USER_SQL);
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, hash);
            pstmt.setString(3, user.getLanguage().name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hashAllPasswords(){
        Map<Integer, String> passwords = new HashMap<>();
        try{
            PreparedStatement pstmt = connection.prepareStatement("SELECT Id, haslo FROM User;");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                passwords.put(rs.getInt("Id"), DigestUtils.md5Hex(rs.getString("haslo")));
            }
            for (Integer key : passwords.keySet()) {
                pstmt = connection.prepareStatement("UPDATE User SET haslo = ? WHERE Id = ?");
                pstmt.setString(1, passwords.get(key));
                pstmt.setInt(2, key);
                pstmt.executeUpdate();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }


    }
}



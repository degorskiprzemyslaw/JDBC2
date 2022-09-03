package jdbc_day2.zad5logowanie;

import jdbc_day2.Language;
import jdbc_day2.Post;
import jdbc_day2.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class UserService { //todo obsługa błędów scannera przy wpisywaniu nondigits

    private User user = new User();
    private Set<Integer> userPostIds = new HashSet<>();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jdbc_schema";
    private Connection connection = null;
    private boolean loggedIn;
    private boolean running = true;
    Scanner scanner = new Scanner(System.in);
    private final String MENU_TEXT = """
            Co chcesz zrobić?
            1. Dodaj post
            2. Usuń post
            3. Zmodyfikuj post
            4. Przeglądaj swoje posty
            5. Przeglądaj posty innych użytkowników
            0. Wyloguj się""";

    private final String HOMEPAGE_TEXT = """
            Co chcesz zrobić?
            1. Zaloguj się
            2. Zarejestruj
            0. Wyjdź z programu""";

    public UserService() {
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

    public void userMenu() {
        do {
            System.out.println("Witaj w programie zarządzającym treścią");
            int input = chooseFromHomePage();
            executeHomePageSelection(input);
        }while(running);


        /*System.out.println("Zaloguj się");
        logIn();
        while (loggedIn) {
            input = chooseFromMenu();
            executeMenuSelection(input);

        }*/

    }

    private void executeHomePageSelection(int input) {
        switch (input) {
            case 1:
                System.out.println("Zaloguj się");
                logIn();
                while (loggedIn) {
                    input = chooseFromMenu();
                    executeMenuSelection(input);
                }
                break;
            case 2:
                createUser();
                break;

            case 0:
                running = false;

            default:
                return;

        }
    }

    private void createUser() {
        System.out.print("Zarejestruj nazwę użytkownika: ");
        String userName = scanner.nextLine();
        System.out.print("Zarejestruj hasło: ");
        String password = scanner.nextLine();

        try{
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO User (nazwa_uzytkownika, haslo, jezyk) VALUES (?, ?, 'polski'); ");
            pstmt.setString(1, userName);
            pstmt.setString(2, password);
        pstmt.executeUpdate();

        pstmt = connection.prepareStatement("SELECT * FROM User WHERE nazwa_uzytkownika = ? AND haslo = ?");
        pstmt.setString(1, userName);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();


        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    private int chooseFromHomePage() {
        System.out.println(HOMEPAGE_TEXT);
        int selection = scanner.nextInt();
        scanner.nextLine();
        return selection;
    }


    private void executeMenuSelection(int input) {
        switch (input) {
            case 1:
                createPost();
                break;
            case 2:
                deletePost();
                break;
            case 3:
                modifyPost();
                break;
            case 4:
                displayAllUserPosts();
                break;
            case 5:
                displayAllNonUserPosts();
                break;

            case 0:
                System.out.println("Wylogowałeś się");
                loggedIn = false;
                break;

            default:
                System.out.println("Nie rozpoznano decyzji");
        }
    }

    private void displayAllUserPosts() {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Post INNER JOIN User ON Post.UserId = User.Id WHERE User.nazwa_uzytkownika = ?");
            pstmt.setString(1, user.getUserName());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Id: " + rs.getInt("Id"));
                System.out.println(", treść: " + rs.getString("tresc"));
                System.out.println(", data utworzenia: " + rs.getString("data_utworzenia"));
                System.out.println();
                userPostIds.add(rs.getInt("Id"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayAllNonUserPosts() {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Post INNER JOIN User ON Post.UserId = User.Id WHERE User.nazwa_uzytkownika <> ?");
            pstmt.setString(1, user.getUserName());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Id: " + rs.getInt("Id"));
                System.out.println("nazwa użytkownika: " + rs.getString("nazwa_uzytkownika"));
                System.out.println(", treść: " + rs.getString("tresc"));
                System.out.println(", data utworzenia: " + rs.getString("data_utworzenia"));

                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePost() {
        displayAllUserPosts();
        int postId;
        do{
            System.out.println("Podaj numer Id posta do usunięcia: ");
            postId = scanner.nextInt();
            scanner.nextLine();
            if(!userPostIds.contains(postId)){
                System.out.println("Nie znaleziono posta o podanym Id");
            }
        }while(!userPostIds.contains(postId));



        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Post WHERE Id = ? AND UserId IN (SELECT Id FROM User WHERE nazwa_uzytkownika = ?);");
            pstmt.setInt(1, postId);
            pstmt.setString(2, user.getUserName());
            pstmt.executeUpdate();
            System.out.println("Post " + postId + " został usunięty");
            userPostIds.remove(postId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyPost() {

        displayAllUserPosts();
        int postId;

        do {
            System.out.println("Podaj numer Id posta do edycji: ");
            postId = scanner.nextInt();
            scanner.nextLine();
            if(!userPostIds.contains(postId)){
                System.out.println("Nie znaleziono posta o podanym Id");
            }
        } while(!userPostIds.contains(postId));
        System.out.println("Podaj nową treść: ");
        String newText = scanner.nextLine();
        try{
            PreparedStatement pstmt = connection.prepareStatement("UPDATE Post SET tresc = ?, data_utworzenia = ? WHERE Id = ? AND UserId IN (SELECT Id FROM User WHERE nazwa_uzytkownika = ?)"); //

            pstmt.setString(1, newText);
            pstmt.setString(2, LocalDate.now().toString());
            pstmt.setInt(3, postId);
            pstmt.setString(4, user.getUserName());
            pstmt.executeUpdate();
            System.out.println("Post o numerze " + postId + " zostal zmieniony");
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    private void createPost() {
        System.out.println("Podaj treść posta: ");
        String text = scanner.nextLine();
        Post post = new Post(text, LocalDate.now(), user.getId());
        try {

            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Post (tresc, data_utworzenia, UserId) VALUES(?, ?, ?)");
            pstmt.setString(1, post.getText());
            pstmt.setString(2, post.getDate().toString());
            pstmt.setInt(3, post.getUserId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private int chooseFromMenu() {
        System.out.println(MENU_TEXT);
        int selection = scanner.nextInt();
        scanner.nextLine();
        return selection;
    }

    private void logIn() {


        System.out.print("Podaj nazwę użytkownika: ");
        String userName = scanner.nextLine();
        System.out.print("Podaj hasło: ");
        String password = scanner.nextLine();

        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM User WHERE nazwa_uzytkownika = ? AND haslo = ?");
            pstmt.setString(1, userName);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                user.setId(rs.getInt("Id"));
                user.setUserName(rs.getString("nazwa_uzytkownika"));
                user.setPassword(rs.getString("haslo"));
                user.setLanguage(Language.valueOf(rs.getString("jezyk")));

            }
            if (user.getUserName() != null && user.getPassword() != null) {
                System.out.println("Jesteś zalogowany");
                loggedIn = true;
            } else {
                System.out.println("Nie zalogowałeś się");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

package jdbc_day2.zad5logowanie;

public class UserServiceDemo {
    public static void main(String[] args) {
        UserService us = new UserService();
        us.userMenu();
        us.closeConnection();

    }
}

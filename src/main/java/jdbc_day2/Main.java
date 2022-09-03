package jdbc_day2;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Repository ur = new Repository();
        //ur.createUser(new User(19, "basiula", "basiula736", Language.polski));
        //ur.updateUserPassword(3, "dhkashdjk9791");
        //ur.deleteUser(6);
        //System.out.println(ur.findUserByLanguage(Language.polski));
        //ur.updateUser(new User(7, "dhajk", "dl3423",Language.niemiecki));
        //List<User> usersByLanguage = ur.findUserByLanguage(Language.polski);
        //usersByLanguage.forEach(user -> System.out.println(user)); //System.out::println

        //Post p = new Post("zlapalem motylka", LocalDate.of(2004,5,1), 7);
        //ur.createPost(p);

        //List<Post> postsByDate = ur.findPostsByLanguageUserNameDate(Language.polski, "basiula",LocalDate.of(2010,01,01));
        //postsByDate.forEach(post -> System.out.println(post)); //System.out::println
        //ur.createUserWithHashPassword(new User("ciapciak", "ciapciak12345", Language.francuski));

        /*for (final Map.Entry<Integer, String> password : ur.hashAllPasswords().entrySet()) {
            System.out.println("Value for key  " + password.getKey() + " is " + password.getValue());
        }*/
        ur.hashAllPasswords();




        ur.closeConnection();


    }
}

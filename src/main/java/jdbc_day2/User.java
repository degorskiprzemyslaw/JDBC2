package jdbc_day2;

public class User {

    private int id;
    private String userName;
    private String password;
    private Language language;

    public User(int id, String userName, String password, Language language) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.language = language;
    }

    public User(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public User(String userName, String password, Language language) {
        this.userName = userName;
        this.password = password;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Language getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}

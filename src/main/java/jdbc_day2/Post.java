package jdbc_day2;

import java.time.LocalDate;

public class Post {
    private int id;
    private String text;
    private LocalDate date;
    private int userId;

    public Post(int id, String text, LocalDate date, int userId) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.userId = userId;
    }

    public Post(String text, LocalDate date, int userId) {
        this.text = text;
        this.date = date;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", userId=" + userId +
                '}';
    }
}

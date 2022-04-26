package reyne.social_app_kursach.model;

public class User {
    private int id;
    private String login;
    private String full_name;
    private String email;
    public User(String user_name)
    {
        this.login =user_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) { this.login = login; }

    public String getLogin() { return login; }

    public int getId() { return id; }
}


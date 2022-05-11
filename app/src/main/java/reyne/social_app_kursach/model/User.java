package reyne.social_app_kursach.model;

public class User {

    private int id;
    private String login;
    private String full_name;
    private String email;
    private String auth_token;
    private int role; //3=admin 2=moderator 1=regular

    public User(int id, String login, String full_name, String email, String user_token, int role)
    {
        this.login =login;
        this.full_name =full_name;
        this.id =id;
        this.email =email;
        this.auth_token =user_token;
        this.role = role;

    }
    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String user_token) {
        this.auth_token = user_token;
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


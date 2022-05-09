package reyne.social_app_kursach.model;

public class User {

    private int id;
    private String login;
    private String ful_name;
    private String email;
    private String auth_token;

    public User(int id, String login, String full_name, String email, String user_token)
    {
        this.login =login;
        this.ful_name =full_name;
        this.id =id;
        this.email =email;
        this.auth_token =user_token;

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

    public String getFul_name() {
        return ful_name;
    }

    public void setFul_name(String ful_name) {
        this.ful_name = ful_name;
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


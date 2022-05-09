package reyne.social_app_kursach.model;

public class Current_user {

    private static User instance = null;

    public static User getCurrentUser() {
        return instance;
    }
    public Current_user(int id, String login, String full_name, String email, String user_token) {
        instance = new User(id,login,full_name,email, user_token);
    }

    public String getAuth_token() { return instance.getAuth_token(); }

    public String getFull_name() {
        return instance.getFul_name();
    }

    public String getEmail() {
        return instance.getEmail();
    }

    public String getLogin() { return instance.getLogin(); }

    public int getId() { return instance.getId(); }
}
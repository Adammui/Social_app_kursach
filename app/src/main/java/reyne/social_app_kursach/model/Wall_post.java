package reyne.social_app_kursach.model;

public class Wall_post {
    private int id;
    private int user_id;
    private String text;
    private String img;
    private String created_at;
    private String updated_at;
    public Wall_post(int id ,int user_id,String text,  String image)
    {
        this.id= id;
        this.text = text;
        this.user_id = user_id;
        this.img = image;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getText() {
        return text;
    }

}

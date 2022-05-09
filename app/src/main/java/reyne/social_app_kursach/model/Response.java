package reyne.social_app_kursach.model;

public class Response {
    private String status;

    public Response(String s)
    {
        this.status =s;
    }

    public String getStatus() {
        return status;
    }
}

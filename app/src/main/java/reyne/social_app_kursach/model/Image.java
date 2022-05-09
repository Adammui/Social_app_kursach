package reyne.social_app_kursach.model;

public class Image {
    private String url;

    public Image(String s)
    {
        this.url =s;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
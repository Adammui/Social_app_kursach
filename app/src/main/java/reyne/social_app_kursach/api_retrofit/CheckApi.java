package reyne.social_app_kursach.api_retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import reyne.social_app_kursach.model.User;

public interface CheckApi {
    String BASE_URL = "https://api.stackexchange.com";

    @GET("users/1.json")
    Call<User> getAnswer();

}
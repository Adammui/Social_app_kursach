package reyne.social_app_kursach.api_retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import reyne.social_app_kursach.model.User;

public interface UserApi {
    @GET("users.json")
    Call<List<User>> getUsers();
}
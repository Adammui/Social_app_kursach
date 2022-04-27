package reyne.social_app_kursach.api_retrofit;

import retrofit2.Call;
import retrofit2.http.POST;
import reyne.social_app_kursach.model.User;

public interface LoginApi {
        @POST("/users/sign_in")
        Call<User> basicLogin();
}
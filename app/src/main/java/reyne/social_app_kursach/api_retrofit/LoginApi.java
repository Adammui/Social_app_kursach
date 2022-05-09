package reyne.social_app_kursach.api_retrofit;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;
import reyne.social_app_kursach.model.Current_user;
import reyne.social_app_kursach.model.User;

public interface LoginApi {

        @GET("token_auth")
        Call<User> GetToken(@Query("email") String email,
                                    @Query("password") String password);
}
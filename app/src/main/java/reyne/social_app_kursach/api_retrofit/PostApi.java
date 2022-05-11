package reyne.social_app_kursach.api_retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import reyne.social_app_kursach.model.Image;
import reyne.social_app_kursach.model.User;
import reyne.social_app_kursach.model.Wall_post;

public interface PostApi {

    @GET("create_post")
    Call<Wall_post> CreatePost( @Query("user_email") String email,
                                @Query("user_token") String auth_token,

                                @Query("advertisement[text]")String advertisement_text,
                                @Query("advertisement[user_id]") int user_id,
                                @Query("advertisement[image][url]") String url
    );
    @GET("update_post")
    Call<Wall_post> UpdatePost( @Query("user_email") String email,
                                @Query("user_token") String auth_token,
                                @Query("id") int id,

                                @Query("advertisement[text]")String advertisement_text,
                                @Query("advertisement[user_id]") int user_id,
                                @Query("advertisement[image][url]") String url
    );
    @GET("delete_post")
    Call<Wall_post> DeletePost( @Query("user_email") String email,
                                @Query("user_token") String auth_token,
                                @Query("id") int id
    );
}
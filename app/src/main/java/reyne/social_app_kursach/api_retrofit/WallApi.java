package reyne.social_app_kursach.api_retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import reyne.social_app_kursach.model.Wall_post;

public interface WallApi {
@GET("advertisements.json")
    Call<List<Wall_post>> getWall_posts();
}

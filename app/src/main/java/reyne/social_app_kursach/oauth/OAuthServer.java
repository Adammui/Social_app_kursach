package reyne.social_app_kursach.oauth;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import reyne.social_app_kursach.LoginActivity;

public class OAuthServer {

    private static final String siteURL = "https://www.googleapis.com/";
    private static String code = LoginActivity.Authcode;

    public static OAuthServerIntface oAuthServerIntface = null;

    public static OAuthServerIntface getoAuthServerIntface(){

        if(oAuthServerIntface == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(siteURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            oAuthServerIntface = retrofit.create(OAuthServerIntface.class);

        }
        return oAuthServerIntface;
    }

    public interface OAuthServerIntface {

        // @Headers("Accept: application/json")

        /**
         * The call to request a token
         */

        @POST("oauth2/v4/token")
        @FormUrlEncoded
        Call<OAuthToken> getAccessToken(
                @Field("code") String code,
                @Field("client_id") String client_id,
                @Field("redirect_uri") String redirect_uri,
                @Field("grant_type") String grant_type
        );

    }


}



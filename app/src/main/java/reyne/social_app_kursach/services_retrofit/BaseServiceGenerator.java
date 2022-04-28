package reyne.social_app_kursach.services_retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import reyne.social_app_kursach.api_retrofit.CheckApi;

public class BaseServiceGenerator {

    private static CheckApi api;

    public static synchronized CheckApi getStackOverflowAPI() {
        if (api == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(CheckApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();

            api = retrofit.create(CheckApi.class);
        }
        return api;
    }
}

package reyne.social_app_kursach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reyne.social_app_kursach.api_retrofit.LoginApi;
import reyne.social_app_kursach.model.User;
import reyne.social_app_kursach.services_retrofit.LoginServiceGenerator;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText mUserName = (EditText) findViewById(R.id.username);
        EditText  mPasswordView = (EditText) findViewById(R.id.password);
        Button btnLogin = (Button) findViewById(R.id.login);
        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginApi loginService =
                        LoginServiceGenerator.createService(LoginApi.class, "user", "password");
                Call<User> call = loginService.basicLogin();
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code()==200) {
                            Log.d("LOG1", "вау");
                            // user object available
                        } else {
                            Log.d("LOG1", "иди нахуй"+response.code());
                            // error response, no access to resource?
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // something went completely south (like no internet connection)
                        Log.d("Error", t.getMessage());
                    }
                });
            }});
    }
}
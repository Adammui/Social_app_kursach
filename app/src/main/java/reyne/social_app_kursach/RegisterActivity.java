package reyne.social_app_kursach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import reyne.social_app_kursach.api_retrofit.LoginApi;
import reyne.social_app_kursach.model.Current_user;
import reyne.social_app_kursach.model.User;

public class RegisterActivity extends AppCompatActivity {

    Button register, back;
    EditText login,full_name, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register= findViewById(R.id.send_register);
        back= findViewById(R.id.back_to_login);
        login= findViewById(R.id.editTextlogin);
        full_name = findViewById(R.id.editTextFullname);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextTextPassword2);
        back.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (login.getText() != null && full_name.getText() != null && email.getText() != null && password.getText() != null) {
                            register(login.getText().toString(),full_name.getText().toString(),email.getText().toString(), password.getText().toString());
                        }
                    }
                });
    }
    public void register(String login, String full_name, String email, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ruby-4-pinb.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginApi loginapi = retrofit.create(LoginApi.class);
        Call<User> call = loginapi.GetRegisteredUser(login,full_name,email, password, 1);
        call.enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, Response<User> response) {
                             try {
                                 if (response.body()!= null)
                                 {
                                     Current_user Current_user = new Current_user(
                                             response.body().getId(),
                                             response.body().getLogin(),
                                             response.body().getFull_name(),
                                             response.body().getEmail(),
                                             response.body().getAuth_token(),
                                             response.body().getRole());
                                     Toast.makeText(getApplicationContext(), "Now loggined in as " + Current_user.getEmail(), Toast.LENGTH_LONG).show();
                                     finish();
                                 }
                             } catch (Exception e){
                                 Toast.makeText(getApplicationContext(), "aaaaaaaшибка "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                             }
                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable t) {
                             Toast.makeText(getApplicationContext(), "Eror on logging. Check your internet connection", Toast.LENGTH_LONG).show();
                         }
                     }
        );
        //Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setData(Uri.parse("https://accounts.google.com/o/oauth2/auth"+ "?access_type=offline" + "&client_id=" + CLIENT_ID + "&response_type=" + CODE + "&redirect_uri=" + REDIRECT_URI + "&scope=" + OAUTH_SCOPE));
        //activityResultLauncher.launch(intent);
        //---Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //---startActivityForResult(signInIntent, 1000);
    }
}
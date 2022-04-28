package reyne.social_app_kursach;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import reyne.social_app_kursach.api_retrofit.CheckApi;
import reyne.social_app_kursach.api_retrofit.LoginApi;
import reyne.social_app_kursach.model.User;
import reyne.social_app_kursach.oauth.OAuthServer;
import reyne.social_app_kursach.oauth.OAuthToken;
import reyne.social_app_kursach.services_retrofit.BaseServiceGenerator;
import reyne.social_app_kursach.services_retrofit.LoginServiceGenerator;

public class LoginActivity extends AppCompatActivity {

    //todo: а тут убрать ненужное
    //From Google Cloud Console
    private static final String OAUTH_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    private static final String CODE = "code";
    static final String CLIENT_ID = "1089113857854-fiv9uuplintt8jm2la1986ld8hpc2kbf.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "https://ruby-4-pinb.herokuapp.com/auth/google_oauth2/callback";

    //Authorization
    static String AUTHORIZATION_CODE;
    private static final String GRANT_TYPE = "authorization_code";

    //Response
    public static String Authcode;
    static String Tokentype;
    static String Refreshtoken;
    static Long Expiresin, ExpiryTime;

    public void  saveData(){
        //todo: вписать сюда данные юзера
        SharedPreferences.Editor sharedPref = getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        sharedPref.putString("AuthCode", AUTHORIZATION_CODE);
        sharedPref.putString("secCode", Authcode);
        sharedPref.putString("refresh", Refreshtoken);
        sharedPref.putLong("expiry", ExpiryTime);
        sharedPref.apply();

    }
    public void loadData(){
        //todo: а сюда получается тоже чтобы они восстанавливались в прилаге
        //Call loadData(); in onCreate(); method.
        // Now that app is successfully Authorized,
        // we can make API calls using Authorization Code
        SharedPreferences sharedPref = getSharedPreferences("authInfo",Context.MODE_PRIVATE);
        AUTHORIZATION_CODE = sharedPref.getString("AuthCode", "");
        Authcode = sharedPref.getString("secCode", "");
        Refreshtoken = sharedPref.getString("refresh","");
        ExpiryTime = sharedPref.getLong("expiry",0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnLogin = (Button) findViewById(R.id.login);
        Button btnLogout = (Button) findViewById(R.id.logout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(CLIENT_ID)
                .requestIdToken(CLIENT_ID)
                .requestProfile()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

            }}
            );
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    //intent.setData(Uri.parse("https://accounts.google.com/o/oauth2/auth"+ "?access_type=offline" + "&client_id=" + CLIENT_ID + "&response_type=" + CODE + "&redirect_uri=" + REDIRECT_URI + "&scope=" + OAUTH_SCOPE));
                    //activityResultLauncher.launch(intent);
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, 1000);
                    }
                });
        btnLogout.setOnClickListener(
                new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                            Toast.makeText(LoginActivity.this, "You are now logged out of your account",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView textView = findViewById(R.id.textView);
        if(requestCode==1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i("ef",  account.getEmail());
                Log.i("ef", account.getDisplayName());
                Log.i("i need this session token", account.getIdToken());
                Log.i("tfyguhijok", account.getServerAuthCode());
                Toast.makeText(this, "Welcome. You logged in as: "+ account.getDisplayName()+"("+ account.getEmail()+")", Toast.LENGTH_SHORT).show();

//                OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
//                    @Override
//                    public okhttp3.Response intercept(Chain chain) throws IOException {
//                        Request originalRequest = chain.request();
//
//                        Request.Builder builder = originalRequest.newBuilder().header("Authorization",account.getIdToken());
//
//                        Request newRequest = builder.build();
//                        return chain.proceed(newRequest);
//                    }
//                }).build();
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("https://ruby-4-pinb.herokuapp.com/")
//                        .client(okHttpClient)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                CheckApi api = BaseServiceGenerator.getStackOverflowAPI();
//                Call<User> questions = api.getAnswer();
//                try {
//                    Response<User> execute = questions.execute();
//                    User body = execute.body();
//                    String item = body.getFull_name();
//                    textView.setText(item);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }



            } catch (ApiException e) {
                // refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w("ef", "TAG:failed code=" + e.getStatusCode());
                Toast.makeText(this, ":" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
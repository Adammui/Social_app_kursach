package reyne.social_app_kursach;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import reyne.social_app_kursach.api_retrofit.LoginApi;
import reyne.social_app_kursach.model.Current_user;
import reyne.social_app_kursach.model.User;

public class LoginActivity extends AppCompatActivity {

    public Current_user Cur_user;
    //todo: а тут убрать ненужное
    //From Google Cloud Console
    private static final String OAUTH_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    private static final String CODE = "code";
    static final String CLIENT_ID = "1089113857854-fiv9uuplintt8jm2la1986ld8hpc2kbf.apps.googleusercontent.com";
    static final String CLIENT_SECRET ="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";
    private static final String REDIRECT_URI = "https://ruby-4-pinb.herokuapp.com/auth/google_oauth2/callback";

    //Authorization
    static String AUTHORIZATION_CODE;
    private static final String GRANT_TYPE = "authorization_code";

    //Response
    public static String Authcode;
    static String Tokentype;
    static String Refreshtoken;
    static Long Expiresin, ExpiryTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnLogin = (Button) findViewById(R.id.login);
        Button btnLogout = (Button) findViewById(R.id.logout);
        EditText email_textbox = findViewById(R.id.editTextEmail);
        EditText password_textbox = findViewById(R.id.editTextTextPassword);
        TextView textView = findViewById(R.id.textView);
//-----
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestServerAuthCode(CLIENT_ID)
//                .requestIdToken(CLIENT_ID)
//                .requestProfile()
//                .build();
//        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//
//            }}
//            );
        //-----
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                public void onClick(View v) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://ruby-4-pinb.herokuapp.com/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        LoginApi loginapi = retrofit.create(LoginApi.class);
                        Call<User> call = loginapi.GetToken(email_textbox.getText().toString(), password_textbox.getText().toString());
                        call.enqueue(new Callback<User>()
                                     {
                                         @Override
                                         public void onResponse(Call<User> call, Response<User> response) {

                                             if(response.isSuccessful()) {
                                                 assert response.body() != null;
                                                 Current_user Current_user = new Current_user(
                                                         response.body().getId(),
                                                         response.body().getLogin(),
                                                         response.body().getFull_name(),
                                                         response.body().getEmail(),
                                                         response.body().getAuth_token(),
                                                         response.body().getRole());
                                                 Toast.makeText(getApplicationContext(), "Now loggined in as "+Current_user.getEmail(), Toast.LENGTH_LONG).show();
                                                 Intent data = new Intent();
                                                 data.setData(Uri.parse("eg"));
                                                 setResult(RESULT_OK, data);
                                                 finish();
                                                 //
                                             }
                                             else{
                                                 Toast.makeText(getApplicationContext(), "aaaaaaaшибка", Toast.LENGTH_LONG).show();
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
                });
        btnLogout.setOnClickListener(
                new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //----
//                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            finish();
//                            Toast.makeText(LoginActivity.this, "You are now logged out of your account",Toast.LENGTH_LONG).show();
//                        }
//                    });
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //---google token auth
//        if(requestCode==1000){
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.i("ef",  account.getEmail());
//                Log.i("ef", account.getDisplayName());
//                Log.i("i need this session token", account.getIdToken());
//                Log.i("tfyguhijok", account.getServerAuthCode());
//                Toast.makeText(this, "Welcome. You logged in as: "+ account.getDisplayName()+"("+ account.getEmail()+")", Toast.LENGTH_SHORT).show();
//
//
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpPost httpPost = new HttpPost("https://ruby-4-pinb.herokuapp.com/login");
//
//                try {
//                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//                    nameValuePairs.add(new BasicNameValuePair("idToken", account.getIdToken()));
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//                    int statusCode = response.getStatusLine().getStatusCode();
//                    final String responseBody = EntityUtils.toString(response.getEntity());
//                    Log.i("LOGGSS", "Signed in as: " + responseBody);
//                } catch (ClientProtocolException e) {
//                    Log.e("LOGGSS", "Error sending ID token to backend.", e);
//                } catch (IOException e) {
//                    Log.e("LOGGSS", "Error sending ID token to backend.", e);
//                }


        //----------ends here
//                OkHttpClient client = new OkHttpClient();
//                RequestBody requestBody = new FormEncodingBuilder()
//                        .add("grant_type", "authorization_code")
//                        .add("client_id", CLIENT_ID)
//                        .add("client_secret", CLIENT_SECRET)
//                        .add("redirect_uri",REDIRECT_URI)
//                        .add("code", Objects.requireNonNull(account.getServerAuthCode()))
//                        .build();
//                final Request request = new Request.Builder()
//                        .url("https://ruby-4-pinb.herokuapp.com/users/sign_in").post(requestBody).build();
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException e) {
//                        Log.e("aaaaaaaaaaaaa", e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(com.squareup.okhttp.Response response) throws IOException {
//                        String message =response.body().string();
//                        //final String message = jsonObject.toString(5);
//                        Log.i("aaaaaaaaa", message);
//                        Log.i("requ","w4");
//                        //textView.setText(message);
//                    }
//
//                });






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


            //---
//            } catch (ApiException e) {
//                // refer to the GoogleSignInStatusCodes class reference for more information.
//                Log.w("ef", "TAG:failed code=" + e.getStatusCode());
//                Toast.makeText(this, ":" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
            //-------
       // }
    }
}
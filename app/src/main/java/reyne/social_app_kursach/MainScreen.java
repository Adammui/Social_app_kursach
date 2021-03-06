package reyne.social_app_kursach;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import reyne.social_app_kursach.databinding.ActivityMainScreenBinding;
import reyne.social_app_kursach.model.Current_user;

public class MainScreen extends AppCompatActivity {

    private ActivityMainScreenBinding binding;

    public void  saveData(){
        //todo: вписать сюда данные юзера
        SharedPreferences.Editor sharedPref = getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        Current_user.getCurrentUser();
        sharedPref.putInt("id", Current_user.getCurrentUser().getId());
        sharedPref.putString("login", Current_user.getCurrentUser().getLogin());
        sharedPref.putString("full_name", Current_user.getCurrentUser().getFull_name());
        sharedPref.putString("email", Current_user.getCurrentUser().getEmail());
        sharedPref.putString("auth_token", Current_user.getCurrentUser().getAuth_token());
        sharedPref.putInt("role", Current_user.getCurrentUser().getRole());
        sharedPref.putString("profile_pic", Current_user.getCurrentUser().getAuth_token());
        sharedPref.apply();

    }
    public void loadData(){
        //todo: а сюда получается тоже чтобы они восстанавливались в прилаге
        //Call loadData(); in onCreate(); method.
        // Now that app is successfully Authorized,
        // we can make API calls using Authorization Code
        SharedPreferences sharedPref = getSharedPreferences("authInfo",Context.MODE_PRIVATE);
        if(sharedPref.getString("email",null)==null)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        else {
            new Current_user(
                    sharedPref.getInt("id", 0), sharedPref.getString("login", ""),
                    sharedPref.getString("full_name", ""), sharedPref.getString("email", ""),
                    sharedPref.getString("auth_token", ""), sharedPref.getInt("role", 0));
        }
    }
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextOfApplication = getApplicationContext();
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_screen);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
        loadData();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Current_user.getCurrentUser()!=null) saveData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(Current_user.getCurrentUser()!=null) saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Current_user.getCurrentUser()!=null) saveData();
    }

}
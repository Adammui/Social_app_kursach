package reyne.social_app_kursach.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import reyne.social_app_kursach.LoginActivity;
import reyne.social_app_kursach.R;
import reyne.social_app_kursach.model.Current_user;

public class HomeFragment extends Fragment {

  //  private FragmentHomeBinding binding;

    Context thiscontext;
    Button button;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        thiscontext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        button = view.findViewById(R.id.button);
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thiscontext, LoginActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, 1000);
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){ fill_user(); }
    }
    @Override
    public void onResume() {
        super.onResume();
        fill_user();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // binding = null;
    }
    public void fill_user(){
        if( Current_user.getCurrentUser()!=null) {
            TextView username = getView().findViewById(R.id.username);
            username.setText(Current_user.getCurrentUser().getLogin());
            TextView email = getView().findViewById(R.id.email);
            email.setText(Current_user.getCurrentUser().getEmail());
            TextView full_name = getView().findViewById(R.id.fullname);
            full_name.setText(Current_user.getCurrentUser().getFul_name());

        }
    }
}
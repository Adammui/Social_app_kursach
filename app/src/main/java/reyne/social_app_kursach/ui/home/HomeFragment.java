package reyne.social_app_kursach.ui.home;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;

import reyne.social_app_kursach.LoginActivity;
import reyne.social_app_kursach.MainScreen;
import reyne.social_app_kursach.R;
import reyne.social_app_kursach.model.Current_user;

public class HomeFragment extends Fragment {

  //  private FragmentHomeBinding binding;

    Context thiscontext;
    Button button;
    ImageView profile_pic;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        thiscontext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        button = view.findViewById(R.id.button);
        profile_pic = view.findViewById(R.id.userpic);
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thiscontext, LoginActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, 1000);
            }
        });
        Button PickImage = view.findViewById(R.id.buttonupload);
        //это загрузка картинки я хз почему кнопка называется просто баттон
        PickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1000: fill_user();
            case 1:
                if (resultCode == RESULT_OK) {
                    try {

                        //Получает URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        Log.d("ф", String.valueOf(data.getData()));
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = MainScreen.getContextOfApplication().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        profile_pic.setImageBitmap(selectedImage);
                       // (data.getData().toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
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
            EditText username = getView().findViewById(R.id.username);
            EditText email = getView().findViewById(R.id.email);
            email.setText(Current_user.getCurrentUser().getEmail());

            EditText full_name = getView().findViewById(R.id.fullname);
            full_name.setText(Current_user.getCurrentUser().getFull_name());
            int role= Current_user.getCurrentUser().getRole();
            String role_text;
            switch (role) {
                case 1:
                    username.setText(Current_user.getCurrentUser().getLogin()+"(regular)");
                    return;
                case 2:
                    username.setText(Current_user.getCurrentUser().getLogin()+"(moderator)");
                    return;
                case 3:
                    username.setText(Current_user.getCurrentUser().getLogin()+"(admin)");
                    return;
                default: return;
            }
        }
    }
}
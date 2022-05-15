package reyne.social_app_kursach.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import reyne.social_app_kursach.R;
import reyne.social_app_kursach.api_retrofit.PostApi;
import reyne.social_app_kursach.api_retrofit.UserApi;
import reyne.social_app_kursach.db.DbHelper;
import reyne.social_app_kursach.db.DbPost;
import reyne.social_app_kursach.model.Current_user;
import reyne.social_app_kursach.model.User;
import reyne.social_app_kursach.model.Wall_post;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import reyne.social_app_kursach.R;
import reyne.social_app_kursach.api_retrofit.PostApi;
import reyne.social_app_kursach.api_retrofit.UserApi;
import reyne.social_app_kursach.db.DbHelper;
import reyne.social_app_kursach.db.DbPost;
import reyne.social_app_kursach.model.Current_user;
import reyne.social_app_kursach.model.User;
import reyne.social_app_kursach.model.Wall_post;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context mContext;
    private List<User> users_list;
    private int id_of_post;

    public UserAdapter(Context mContext, List<User> users_list)
    {
        this.mContext = mContext;
        this.users_list = users_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        v = layoutInflater.inflate(R.layout.user_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username_chat.setText(users_list.get(position).getEmail());
        Log.d("","initialized user");
        holder.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initializeView();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username_chat,edit;
        ImageView image;
        Button enter;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username_chat = itemView.findViewById(R.id.username_chat);
            enter = itemView.findViewById(R.id.enter);
        }
    }


}
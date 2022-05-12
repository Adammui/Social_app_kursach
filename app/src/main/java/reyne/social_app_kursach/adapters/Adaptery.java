package reyne.social_app_kursach.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.net.URLDecoder;
import java.util.ArrayList;
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

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> {

    private Context mContext;
    private List<Wall_post> wall_posts_list;
    private int id_of_post;
    SQLiteDatabase db ;

    public Adaptery(Context mContext, List<Wall_post> wall_posts_list)
    {
        this.mContext = mContext;
        this.wall_posts_list = wall_posts_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        v = layoutInflater.inflate(R.layout.wall_post_item, parent, false);
        db = new DbHelper(mContext).getWritableDatabase();
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text.setText(wall_posts_list.get(position).getText());
        try
        {
            if(wall_posts_list.get(position).getImage().getUrl()!=null) {
                Log.d("afes", wall_posts_list.get(position).getImage().getUrl() + "");
                //phone
                Glide.with(mContext).load(wall_posts_list.get(position).getImage().getUrl()).into(holder.image);
                //pc
                Glide.with(mContext).load("https://ruby-4-pinb.herokuapp.com"
                        + wall_posts_list.get(position).getImage().getUrl() + "?user_email=" + Current_user.getCurrentUser().getEmail() + "&user_token=" + Current_user.getCurrentUser().getAuth_token()).into(holder.image);
            }
        }
        catch (Exception e)
        {
            Log.d("error loading image maybe its empty",e.getLocalizedMessage());
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ruby-4-pinb.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //users
        UserApi userapi = retrofit.create(UserApi.class);
        Call<List<User>> call1= userapi.getUsers();
        call1.enqueue(new Callback<List<User>>()
                      {
                          @Override
                          public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                              if(response.code()!=200)
                                  return; //handle error
                              List<User> users= response.body();
                              for(User u: users){
                                  if (u.getId()== wall_posts_list.get(position).getUser_id())
                                  {
                                      holder.user.setText(u.getLogin());
                                  }
                              }
                          }
                          @Override
                          public void onFailure(Call<List<User>> call, Throwable t) {
                              //Toast.makeText(mContext, "Eror on reading users", Toast.LENGTH_LONG).show();
                              Log.d("","eeror on reading users");
                          }
                      }
        );
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://ruby-4-pinb.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PostApi postapi = retrofit.create(PostApi.class);
                Call<Wall_post> call = postapi.UpdatePost(
                        Current_user.getCurrentUser().getEmail(), Current_user.getCurrentUser().getAuth_token(),
                        wall_posts_list.get(position).getId(),
                        String.valueOf(holder.edit.getText()), wall_posts_list.get(position).getUser_id(), null);
                call.enqueue(new Callback<Wall_post>() {
                                 @Override
                                 public void onResponse(Call<Wall_post> call, Response<Wall_post> response) {
                                     Log.d("", "" + response.code());
                                     if (call.isExecuted()) {
                                         holder.text.setText(holder.edit.getText());holder.edit.setVisibility(View.GONE);
                                         holder.save.setVisibility(View.INVISIBLE);holder.text.setVisibility(View.VISIBLE);
                                         Toast.makeText(mContext, "Edited  ^)", Toast.LENGTH_LONG).show();
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<Wall_post> call, Throwable t) {
                                     Toast.makeText(mContext, "Eror. Check your internet connection", Toast.LENGTH_LONG).show();
                                     Log.d("",""+t.getLocalizedMessage());
                                     call.cancel();
                                 }
                             }
                );
                try {
                    DbPost.editById(db, wall_posts_list.get(position).getId(), String.valueOf(holder.edit.getText()));
                    holder.text.setText(holder.edit.getText());holder.edit.setVisibility(View.GONE);
                    holder.save.setVisibility(View.INVISIBLE);holder.text.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, "Edited  ^)", Toast.LENGTH_LONG).show();
                }catch (Exception e){Log.d("e In save to local db",""+e.getLocalizedMessage());}


            }});
        holder.viewoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.viewoption);
                popup.inflate(R.menu.options_menu);
                //listener of clicks
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update: {
                                //banning buttons for users that cant use them
                                if(Current_user.getCurrentUser().getId()==wall_posts_list.get(position).getUser_id() ||
                                        Current_user.getCurrentUser().getRole()!=1) //admin can do everything and moder can delete and update posts
                                {
                                    holder.edit.setText(holder.text.getText());holder.edit.setVisibility(View.VISIBLE);
                                    holder.save.setVisibility(View.VISIBLE);holder.text.setVisibility(View.GONE);
                                    id_of_post=item.getItemId();
                                    Log.d("syjc ",position+"");
                                }
                                else Toast.makeText(mContext, "Only owner or moderator can update posts", Toast.LENGTH_LONG).show();

                            }
                            return true;
                            case R.id.delete:
                                if(Current_user.getCurrentUser().getId()==wall_posts_list.get(position).getUser_id() ||
                                        Current_user.getCurrentUser().getRole()!=1) //admin can do everything and moder can delete and update posts
                                {
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("https://ruby-4-pinb.herokuapp.com/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    PostApi postapi = retrofit.create(PostApi.class);
                                    Call<Wall_post> call = postapi.DeletePost(
                                            Current_user.getCurrentUser().getEmail(), Current_user.getCurrentUser().getAuth_token(),
                                            wall_posts_list.get(position).getId());
                                    call.enqueue(new Callback<Wall_post>() {
                                                     @Override
                                                     public void onResponse(Call<Wall_post> call, Response<Wall_post> response) {
                                                         Log.d("", "" + response.code());
                                                         if (call.isExecuted()) {
                                                             holder.image.setVisibility(View.GONE);holder.edit.setVisibility(View.GONE);
                                                             holder.text.setVisibility(View.GONE);holder.user.setVisibility(View.GONE);
                                                             holder.save.setVisibility(View.GONE);holder.viewoption.setVisibility(View.GONE);
                                                             Toast.makeText(mContext, "Deleted  ^)", Toast.LENGTH_LONG).show();
                                                         }
                                                     }
                                                     @Override
                                                     public void onFailure(Call<Wall_post> call, Throwable t) {
                                                         Toast.makeText(mContext, "Eror. Check your internet connection", Toast.LENGTH_LONG).show();
                                                         Log.d("",""+t.getLocalizedMessage());
                                                         call.cancel();
                                                     }
                                                 }
                                    );
                                    try
                                    {

                                        DbPost.deleteByIdOffline(db, wall_posts_list.get(position).getId());
                                        holder.image.setVisibility(View.GONE);holder.edit.setVisibility(View.GONE);
                                        holder.text.setVisibility(View.GONE);holder.user.setVisibility(View.GONE);
                                        holder.save.setVisibility(View.GONE);holder.viewoption.setVisibility(View.GONE);
                                        Toast.makeText(mContext, "Deleted  ^)", Toast.LENGTH_LONG).show();
                                    }
                                    catch (Exception e){Log.d(" e In save to local db",""+e.getLocalizedMessage());}
                                }
                                else Toast.makeText(mContext.getApplicationContext(), "Only owner or moderator can update posts", Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.save:
                                if(DbPost.add(db, wall_posts_list.get(position)) != -1) {
                                    Toast.makeText(mContext, "Saved to local", Toast.LENGTH_LONG).show();
                                }
                                else Toast.makeText(mContext, "Error saving", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return wall_posts_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text, user,edit;
        ImageView image;
        Button viewoption, save;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            edit = itemView.findViewById(R.id.editText);
            text = itemView.findViewById(R.id.text);
            user = itemView.findViewById(R.id.user);
            save = itemView.findViewById(R.id.buttonsave);
            viewoption = itemView.findViewById(R.id.view_options);
        }
    }
}

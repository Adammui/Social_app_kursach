package reyne.social_app_kursach.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import reyne.social_app_kursach.api_retrofit.UserApi;
import reyne.social_app_kursach.model.User;
import reyne.social_app_kursach.model.Wall_post;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> {

    private Context mContext;
    private List<Wall_post> wall_posts_list;

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
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text.setText(wall_posts_list.get(position).getText());
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
                                  else
                                      Log.d("","eeror "+String.valueOf(u.getId()+"   "+wall_posts_list.get(position).getUser_id()));
                              }
                          }
                          @Override
                          public void onFailure(Call<List<User>> call, Throwable t) {
                              Toast.makeText(mContext, "Eror on reading users", Toast.LENGTH_SHORT).show();
                              Log.d("","eeror on reading users");
                          }
                      }
        );
        //String temp =  post.setUser_id( users_list.get(Integer.parseInt( post.getUser_id())).getUser_name()); String.valueOf( wall_posts_list.get(position).getUser_id());
        //holder.user.setText(temp);
        //Glide.with(mContext)
            //    .load(wall_posts_list.get(position).getImg())
              //  .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return wall_posts_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text, user;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text);
            user = itemView.findViewById(R.id.user);
            //image = itemView.findViewById(R.id.image);
        }
    }
}

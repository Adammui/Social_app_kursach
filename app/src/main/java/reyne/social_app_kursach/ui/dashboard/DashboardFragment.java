package reyne.social_app_kursach.ui.dashboard;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import reyne.social_app_kursach.LoginActivity;
import reyne.social_app_kursach.R;
import reyne.social_app_kursach.adapters.Adaptery;
import reyne.social_app_kursach.api_retrofit.LoginApi;
import reyne.social_app_kursach.api_retrofit.PostApi;
import reyne.social_app_kursach.api_retrofit.UserApi;
import reyne.social_app_kursach.api_retrofit.WallApi;
import reyne.social_app_kursach.model.Current_user;
import reyne.social_app_kursach.model.User;
import reyne.social_app_kursach.model.Wall_post;

public class DashboardFragment extends Fragment {

    Context thiscontext;
    List<Wall_post> wall_posts_list;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        thiscontext = container.getContext();
        wall_posts_list= new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        SwipeRefreshLayout mySwipeRefreshLayout =  view.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.d("LOG", "onRefresh called from SwipeRefreshLayout");
                       update( mySwipeRefreshLayout);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        mySwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mySwipeRefreshLayout.setRefreshing(true);
                update(mySwipeRefreshLayout);
            }
        });

        Button open_menu_add = view.findViewById(R.id.openmenu);
        Button close_menu_add = view.findViewById(R.id.closemenu);
        EditText text_field = view.findViewById(R.id.editText);
        Button add_post = view.findViewById(R.id.add);
        open_menu_add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_field.setVisibility(View.VISIBLE);
                add_post.setVisibility(View.VISIBLE);
                close_menu_add.setVisibility(View.VISIBLE);
                open_menu_add.setVisibility(View.GONE);
            }
        });
        close_menu_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_field.setVisibility(View.GONE);
                add_post.setVisibility(View.GONE);
                close_menu_add.setVisibility(View.GONE);
                open_menu_add.setVisibility(View.VISIBLE);
            }
        });
        add_post.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://ruby-4-pinb.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PostApi postapi = retrofit.create(PostApi.class);
                Call<Wall_post> call = postapi.CreatePost(
                        Current_user.getCurrentUser().getEmail(), Current_user.getCurrentUser().getAuth_token(),
                        String.valueOf(text_field.getText()), Current_user.getCurrentUser().getId(), null);
                call.enqueue(new Callback<Wall_post>()
                             {
                                 @Override
                                 public void onResponse(Call<Wall_post> call, Response<Wall_post> response) {
                                     Log.d("",""+response.code());
                                     if (call.isExecuted()) {
                                         text_field.setVisibility(View.GONE);
                                         add_post.setVisibility(View.GONE);
                                         close_menu_add.setVisibility(View.GONE);
                                         open_menu_add.setVisibility(View.VISIBLE);
                                         update(mySwipeRefreshLayout);
//                                         Wall_post wp = new Wall_post(
//                                                 response.body().getId(),
//                                                 response.body().getUser_id(),
//                                                 response.body().getText(),
//                                                 response.body().getImg());
                                         Toast.makeText(getContext(), "Posted  ^)", Toast.LENGTH_SHORT).show();
                                    }
                                 }

                                 @Override
                                 public void onFailure(Call<Wall_post> call, Throwable t) {
                                     Toast.makeText(getContext(), "Eror on logging. Check your internet connection", Toast.LENGTH_SHORT).show();

                                 }

                             }
                );
            }
        });
        return view;
    }

    private void update( SwipeRefreshLayout mySwipeRefreshLayout) {
        wall_posts_list.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ruby-4-pinb.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        /*/users
        users_list= new ArrayList<>();
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
                                  users_list.add(u);
                              }
                          }
                          @Override
                          public void onFailure(Call<List<User>> call, Throwable t) {
                          }
                      }
        );*/
        //wall
        WallApi wallapi = retrofit.create(WallApi.class);
        Call<List<Wall_post>> call= wallapi.getWall_posts();
        call.enqueue(new Callback<List<Wall_post>>()
                     {
                         @Override
                         public void onResponse(Call<List<Wall_post>> call, Response<List<Wall_post>> response) {
                             if(!response.isSuccessful()) {
                                 Toast.makeText(getActivity(), "Eror", Toast.LENGTH_SHORT).show();
                             }
                             else{
                                 List<Wall_post> wall_posts= response.body();
                                 for(Wall_post post: wall_posts){
                                     wall_posts_list.add(post);
                                 }
                                 Collections.reverse(wall_posts_list);
                                 Adaptery adaptery = new Adaptery(thiscontext, wall_posts_list);
                                 recyclerView.setAdapter(adaptery);
                             }
                             mySwipeRefreshLayout.setRefreshing(false);
                         }
                         @Override
                         public void onFailure(Call<List<Wall_post>> call, Throwable t) {
                             Toast.makeText(getActivity(), "Eror on reading wall. Check your internet connection"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                             mySwipeRefreshLayout.setRefreshing(false);
                         }
                     }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
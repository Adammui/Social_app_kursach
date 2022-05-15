package reyne.social_app_kursach.ui.dashboard;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import reyne.social_app_kursach.R;
import reyne.social_app_kursach.adapters.PostAdapter;
import reyne.social_app_kursach.api_retrofit.PostApi;
import reyne.social_app_kursach.api_retrofit.WallApi;
import reyne.social_app_kursach.db.DbHelper;
import reyne.social_app_kursach.db.DbPost;
import reyne.social_app_kursach.model.Current_user;
import reyne.social_app_kursach.model.Wall_post;

public class DashboardFragment extends Fragment {

    Context thiscontext;
    List<Wall_post> wall_posts_list;
    RecyclerView recyclerView;
    public long lastGathered, lastSync;
    SwipeRefreshLayout mySwipeRefreshLayout;
    SQLiteDatabase db ;
    int need_local=0;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        thiscontext = container.getContext();
        wall_posts_list= new ArrayList<>();
        db = new DbHelper(thiscontext).getWritableDatabase();
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mySwipeRefreshLayout =  view.findViewById(R.id.swiperefresh);
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
        EditText image_field = view.findViewById(R.id.editImage);
        Button add_post = view.findViewById(R.id.add);
        open_menu_add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_field.setVisibility(View.VISIBLE);
                image_field.setVisibility(View.VISIBLE);
                add_post.setVisibility(View.VISIBLE);
                close_menu_add.setVisibility(View.VISIBLE);
                open_menu_add.setVisibility(View.GONE);
            }
        });
        close_menu_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_field.setVisibility(View.GONE);
                image_field.setVisibility(View.GONE);
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
                        String.valueOf(text_field.getText()), Current_user.getCurrentUser().getId(), String.valueOf(image_field.getText()));
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
                                     Toast.makeText(getContext(), "Eror on adding posts. Check your internet connection", Toast.LENGTH_SHORT).show();

                                 }

                             }
                );
            }
        });

        return view;
    }

    private void update( SwipeRefreshLayout mySwipeRefreshLayout) {
        if (Calendar.getInstance().getTimeInMillis() - lastGathered >= 6000) {
            lastGathered = Calendar.getInstance().getTimeInMillis();
            sync_data_delete();
                need_local=0;
                wall_posts_list.clear();
                Cursor cursor = DbPost.getActual(db);
                while (cursor.moveToNext()) {
                    wall_posts_list.add(new Wall_post(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("user_id")), cursor.getString(cursor.getColumnIndexOrThrow("text")) ));
                    Log.d("cursor count in local db",""+1111);
                }
                PostAdapter postAdapter = new PostAdapter(thiscontext, wall_posts_list);
                recyclerView.setAdapter(postAdapter);
                //Toast.makeText(thiscontext, "Подгружены сохраненные посты", Toast.LENGTH_LONG).show();
                mySwipeRefreshLayout.setRefreshing(false);

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
                                     Log.d("update response eror ",""+ response.code());
                                 }
                                 else{
                                     List<Wall_post> wall_posts= response.body();
                                     wall_posts_list.clear();
                                     for(Wall_post post: wall_posts){
                                         wall_posts_list.add(post);
                                     }
                                     Collections.reverse(wall_posts_list);
                                     PostAdapter postAdapter = new PostAdapter(thiscontext, wall_posts_list);
                                     recyclerView.setAdapter(postAdapter);
                                     sync_data_update(wall_posts_list);
                                 }
                                 mySwipeRefreshLayout.setRefreshing(false);
                             }
                             @Override
                             public void onFailure(Call<List<Wall_post>> call, Throwable t) {
                                 Toast.makeText(getActivity(), "Eror on reading wall. Check your internet connection"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                 mySwipeRefreshLayout.setRefreshing(false);
                                 need_local=1;
                                 call.cancel();
                             }
                         }
            );
        }
    }

    private void sync_data_update(List<Wall_post> wall_posts) {
        if (Calendar.getInstance().getTimeInMillis() - lastSync >= 10000) {
            lastSync = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Cursor cursor = DbPost.getActual(db);
            Log.d("post id",wall_posts.toString()+"");
            try {
                for (Wall_post post : wall_posts) {

                Log.d("post id",post.getId()+" ");
                cursor.moveToPosition(-1);
                cursor.moveToNext();
                while (cursor.getPosition() < cursor.getCount()) {
                    Log.d("cur position",cursor.getPosition()+" ");

                        Date local_post = sdf.parse(post.getUpdated_at());
                        Date server_post = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow("updated_at")));
                        Log.d("ASSSSS",local_post.getTime()+"   "+ server_post.getTime());
                        Log.d("ASSSSS",post.getId()+"   "+ cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                        if (post.getId() == cursor.getInt(cursor.getColumnIndexOrThrow("id")) && local_post.getTime()
                                < server_post.getTime()) {

                            Log.d("TIMES",local_post+" "+server_post);
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://ruby-4-pinb.herokuapp.com/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            PostApi postapi = retrofit.create(PostApi.class);
                            Call<Wall_post> call = postapi.UpdatePost(
                                    Current_user.getCurrentUser().getEmail(), Current_user.getCurrentUser().getAuth_token(),
                                    post.getId(),
                                    cursor.getString(cursor.getColumnIndexOrThrow("text")), cursor.getInt(cursor.getColumnIndexOrThrow("user_id")), null);
                            call.enqueue(new Callback<Wall_post>() {
                                             @Override
                                             public void onResponse(Call<Wall_post> call, Response<Wall_post> response) {
                                                 Log.d("edited posts offline sync", "true" + response.code());
                                             }

                                             @Override
                                             public void onFailure(Call<Wall_post> call, Throwable t) {
                                                 Toast.makeText(thiscontext, "Eror. Check your internet connection", Toast.LENGTH_LONG).show();
                                                // Log.d("deleted posts offline sync", "false" + t.getLocalizedMessage());
                                             }
                                         }
                            );
                        }
                        else if(post.getId() == cursor.getInt(cursor.getColumnIndexOrThrow("id")) && local_post.getTime()
                                > server_post.getTime())
                        {
                            DbPost.editById( db, post.getId(), post.getText());
                        }
                    cursor.moveToNext();
                }
            }
            update( mySwipeRefreshLayout);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private void sync_data_delete() {
        try{
            Cursor cursor = DbPost.getDeletedSync(db);
            while (cursor.moveToNext()) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://ruby-4-pinb.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PostApi postapi = retrofit.create(PostApi.class);
                Call<Wall_post> call = postapi.DeletePost(
                        Current_user.getCurrentUser().getEmail(), Current_user.getCurrentUser().getAuth_token(),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                call.enqueue(new Callback<Wall_post>() {
                                 @Override
                                 public void onResponse(Call<Wall_post> call, Response<Wall_post> response) {
                                     Log.d("deleted posts offline sync", "true" + response.code());
                                 }

                                 @Override
                                 public void onFailure(Call<Wall_post> call, Throwable t) {
                                     Toast.makeText(thiscontext, "Eror. Check your internet connection", Toast.LENGTH_LONG).show();
                                     Log.d("deleted posts offline sync", "false" + t.getLocalizedMessage());
                                 }
                             }
                );
            }}
        catch (Exception e){Log.d("data sync delete",""+e.getLocalizedMessage());}
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

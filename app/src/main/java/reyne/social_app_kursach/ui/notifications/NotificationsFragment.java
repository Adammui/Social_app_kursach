package reyne.social_app_kursach.ui.notifications;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import reyne.social_app_kursach.R;
import reyne.social_app_kursach.adapters.MessageAdapter;
import reyne.social_app_kursach.adapters.PostAdapter;
import reyne.social_app_kursach.adapters.UserAdapter;
import reyne.social_app_kursach.api_retrofit.PostApi;
import reyne.social_app_kursach.api_retrofit.UserApi;
import reyne.social_app_kursach.api_retrofit.WallApi;
import reyne.social_app_kursach.db.DbHelper;
import reyne.social_app_kursach.db.DbPost;
import reyne.social_app_kursach.model.Current_user;
import reyne.social_app_kursach.model.User;
import reyne.social_app_kursach.model.Wall_post;

//Navigation.findNavController(view).navigate(R.id.action_fromFragment_toFragment);
public class NotificationsFragment extends Fragment {
    private WebSocket webSocket;
    private String SERVER_PATH = "https://chats-socket-server.herokuapp.com/";
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;
    SwipeRefreshLayout mySwipeRefreshLayout;
    Context thiscontext;
    List<User> users_list;
    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        thiscontext = container.getContext();

        users_list = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mySwipeRefreshLayout =  view.findViewById(R.id.swiperefresh);

        showUsers();
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.d("LOG", "onRefresh called from SwipeRefreshLayout");
                        showUsers();

                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        mySwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mySwipeRefreshLayout.setRefreshing(true);
                //update(mySwipeRefreshLayout);
            }
        });

        return view;
    }

    private void showUsers(){
        users_list.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ruby-4-pinb.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi userapi = retrofit.create(UserApi.class);
        Call<List<User>> call1= userapi.getUsers();
        call1.enqueue(new Callback<List<User>>() {
                          @Override
                          public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                              if (!response.isSuccessful()) {
                                  Log.d("update response eror ", "" + response.code());
                              } else {
                                  List<User> users = response.body();
                                  Log.d("", "ищан");
                                  for (User u : users) {
                                      if (u.getId() != Current_user.getCurrentUser().getId()) {
                                          users_list.add(u);
                                          Log.d("loaded user id", "" + u.getId());
                                      }
                                  }
                                  UserAdapter userAdapter = new UserAdapter(thiscontext, users_list, getFragmentManager());
                                  recyclerView.setAdapter(userAdapter);
                              }
                              mySwipeRefreshLayout.setRefreshing(false);
                          }

                          @Override
                          public void onFailure(Call<List<User>> call, Throwable t) {
                              //Toast.makeText(mContext, "Eror on reading users", Toast.LENGTH_LONG).show();
                              Log.d("", "eeror on reading users");
                              mySwipeRefreshLayout.setRefreshing(false);
                              call.cancel();
                          }
                      }
        );

        Toast.makeText(thiscontext, "Подгружены юзеры", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
////user adapter
//
//    class UserAdapter  extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
//
//    private Context mContext;
//    private List<User> users_list;
//    private int id_of_post;
//
//    public UserAdapter(Context mContext, List<User> users_list)
//    {
//        this.mContext = mContext;
//        this.users_list = users_list;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v;
//        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
//        v = layoutInflater.inflate(R.layout.user_item, parent, false);
//        return new MyViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.username_chat.setText(users_list.get(position).getEmail());
//        Log.d("","initialized user");
//        holder.enter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //initializeView();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return users_list.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder{
//        TextView username_chat,edit;
//        ImageView image;
//        Button enter;
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            username_chat = itemView.findViewById(R.id.username_chat);
//            enter = itemView.findViewById(R.id.enter);
//        }
//    }
//}
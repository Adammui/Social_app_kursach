package reyne.social_app_kursach.ui.notifications;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import reyne.social_app_kursach.R;
import reyne.social_app_kursach.adapters.MessageAdapter;
import reyne.social_app_kursach.api_retrofit.UserApi;
import reyne.social_app_kursach.model.Current_user;
import reyne.social_app_kursach.model.User;

//todo^ тут все равно работает чат почему-то , список юзеров не выводится
public class NotificationsFragment extends Fragment implements TextWatcher {
    private WebSocket webSocket;
    private String SERVER_PATH = "https://chats-socket-server.herokuapp.com/";
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;
    Context thiscontext;
    List<User> users_list;
    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        thiscontext = container.getContext();
        initiateSocketConnection();

        users_list = new ArrayList<>();

        messageEdit = view.findViewById(R.id.messageEdit);
        sendBtn = view.findViewById(R.id.sendBtn);
        pickImgBtn = view.findViewById(R.id.pickImgBtn);

        recyclerView = view.findViewById(R.id.recyclerViewUsersMessages);
        return view;
    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString().trim();

        if (string.isEmpty()) {
            resetMessageEdit();
        } else {
            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void resetMessageEdit() {
        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);
    }

    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(),
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();

                initializeView();
            });

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            getActivity().runOnUiThread(() -> {

                try {
                    JSONObject jsonObject = new JSONObject(text);
                    if(jsonObject.getString("name").equals("Zae Games")) {

                        jsonObject.put("isSent", false);

                        messageAdapter.addItem(jsonObject);

                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }
    }

    private void showUsers(){
        Log.d("show",""+users_list.toString());
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
                          public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                              if(response.code()!=200)
                                  return; //handle error
                              List<User> users= response.body();
                              Log.d("","ищан");
                              for(User u: users){
                                  if (u.getId()!= Current_user.getCurrentUser().getId()) {
                                      users_list.add(u);
                                      Log.d("loaded user id",""+u.getId());}
                              }
                          }
                          @Override
                          public void onFailure(Call<List<User>> call, Throwable t) {
                              //Toast.makeText(mContext, "Eror on reading users", Toast.LENGTH_LONG).show();
                              Log.d("","eeror on reading users");
                          }
                      }
        );
        UserAdapter userAdapter = new UserAdapter(thiscontext, users_list);
        recyclerView.setAdapter(userAdapter);
        Toast.makeText(thiscontext, "Подгружены юзеры", Toast.LENGTH_LONG).show();
    }
    public void initializeView() {
        messageAdapter = new MessageAdapter(getLayoutInflater(),"Zae Games");
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(thiscontext));


        messageEdit.addTextChangedListener(this);

        sendBtn.setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", Current_user.getCurrentUser().getLogin());
                jsonObject.put("message", messageEdit.getText().toString());

                webSocket.send(jsonObject.toString());

                jsonObject.put("isSent", true);
                messageAdapter.addItem(jsonObject);

                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        pickImgBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            startActivityForResult(Intent.createChooser(intent, "Pick image"),
                    IMAGE_REQUEST_ID);

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = thiscontext.getApplicationContext().getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void sendImage(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", Current_user.getCurrentUser().getLogin());
            jsonObject.put("image", base64String);

            webSocket.send(jsonObject.toString());

            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
////user adapter
class UserAdapter  extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

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
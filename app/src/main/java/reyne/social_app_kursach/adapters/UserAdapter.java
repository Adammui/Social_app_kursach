package reyne.social_app_kursach.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import reyne.social_app_kursach.R;
import reyne.social_app_kursach.model.User;


import reyne.social_app_kursach.ui.notifications.ChatFragment;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mContext;
    private List<User> users_list;
    private FragmentManager fragmentManager;

    public UserAdapter(Context mContext, List<User> users_list, FragmentManager activity)
    {
        this.fragmentManager = activity;
        this.mContext = mContext;
        this.users_list = users_list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        v = layoutInflater.inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.username_chat.setText(users_list.get(position).getEmail());
        Log.d("","initialized user");
        holder.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChatFragment nextFrag= new ChatFragment(users_list.get(position).getLogin());
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main_screen, nextFrag, nextFrag.getTag())
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users_list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        TextView username_chat,edit;
        ImageView image;
        Button enter;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            username_chat = itemView.findViewById(R.id.username_chat);
            enter = itemView.findViewById(R.id.enter);
        }
    }


}
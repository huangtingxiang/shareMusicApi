package com.example.sharemusicplayer.myPlace.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.User;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.DownloadImageTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListHolder> {

    User[] users;
    List<User> selectUsers = new ArrayList<>();


    public static class UserListHolder extends RecyclerView.ViewHolder {

        public CircleImageView imageView;
        public TextView nickNameTex;
        public CheckBox checkBox;

        public UserListHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.person_image);
            nickNameTex = view.findViewById(R.id.nick_name_tex);
            checkBox = view.findViewById(R.id.checked);
        }
    }

    public UserListAdapter(User[] users) {
        this.users = users;
    }

    @Override
    public UserListAdapter.UserListHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        UserListHolder vh = new UserListHolder(v);
        return vh;
    }

    /**
     * 根据歌曲信息更新ui
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(UserListAdapter.UserListHolder holder, final int position) {
        final User user = users[position];
        holder.nickNameTex.setText(user.getNickName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectUsers.add(user);
                } else {
                    selectUsers.remove(user);
                }
            }
        });
        if (user.getImageUrl() != null && !user.getImageUrl().equals("")) {
            new DownloadImageTask(holder.imageView)
                    .execute(BaseHttpService.BASE_URL + user.getImageUrl());
        }
    }

    //    @Override
    public int getItemCount() {
        return users.length;
    }

    public void setUsers(User[] users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public List<User> getSelectUsers() {
        return selectUsers;
    }

}

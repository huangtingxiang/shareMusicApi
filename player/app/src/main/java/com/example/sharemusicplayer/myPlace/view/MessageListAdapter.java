package com.example.sharemusicplayer.myPlace.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.Message;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.entity.User;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.DownloadImageTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 消息适配器
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListHolder> {
    ArrayList<Message> messages = new ArrayList<>();    // 消息列表
    SongClickListener listener;     // 当点击歌曲的会调

    public static class MessageListHolder extends RecyclerView.ViewHolder {

        /**
         * 消息列表
         */
        CircleImageView person_image;
        TextView nick_name;
        TextView create_time;
        TextView content_tex;

        /**
         * 歌曲列表
         */
        RelativeLayout song_container;
        TextView artist;
        TextView album;
        ImageView pic_url;

        public MessageListHolder(View view) {
            super(view);
            person_image = view.findViewById(R.id.person_image);
            nick_name = view.findViewById(R.id.nick_name);
            create_time = view.findViewById(R.id.create_time);
            content_tex = view.findViewById(R.id.content_tex);
            song_container = view.findViewById(R.id.song_container);

            artist = view.findViewById(R.id.artist);
            album = view.findViewById(R.id.album);
            pic_url = view.findViewById(R.id.pic_url);
        }
    }

    @Override
    public MessageListAdapter.MessageListHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        MessageListHolder vh = new MessageListHolder(v);
        return vh;
    }

    /**
     * 根据歌曲信息更新ui
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MessageListAdapter.MessageListHolder holder, final int position) {
        final Message message = messages.get(position);
        // 设置用户和消息信息
        User user = message.getUser();
        if (user.getImageUrl() != null && !user.getImageUrl().equals("")) {
            new DownloadImageTask(holder.person_image).execute(BaseHttpService.BASE_URL + user.getImageUrl());
        }
        holder.nick_name.setText(user.getNickName());
        holder.create_time.setText(BaseHttpService.dateFormat(message.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        holder.content_tex.setText(message.getContent());

        if (message.getSong() == null) {
            holder.song_container.setVisibility(View.GONE);
        } else {
            // 设置歌曲信息
            if (listener != null) {
                holder.song_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(message.getSong(), position);
                    }
                });
            }
            Song song = message.getSong();
            holder.artist.setText(song.getName() + "--" + song.getArtist());
            holder.album.setText(song.getAlbum() + "   来源:" + song.getOrigin());
            if (song.getPic_url() != null && !song.getPic_url().equals("")) {
                new DownloadImageTask(holder.pic_url)
                        .execute(song.getPic_url());
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * 设置消息列表
     * @param messages
     */
    public void setMessage(Message[] messages) {
        this.messages = new ArrayList<>();
        for (Message message : messages) {
            this.messages.add(message);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加消息 首部
     * @param message
     */
    public void setMessage(Message message) {
        this.messages.add(0, message);
        notifyDataSetChanged();
    }

    /**
     * 设置监听歌曲点击
     * @param listener
     */
    public void setListener(SongClickListener listener) {
        this.listener = listener;
    }

    public interface SongClickListener {
        void onClick(Song song, int position);
    }
}

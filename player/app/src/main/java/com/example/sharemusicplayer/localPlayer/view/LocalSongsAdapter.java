package com.example.sharemusicplayer.localPlayer.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.httpService.DownloadImageTask;


public class LocalSongsAdapter extends RecyclerView.Adapter<LocalSongsAdapter.SongsViewHolder> {
    Song[] songList;    // 显示的歌曲列表
    LocalSongsAdapter.SongClickListener listener;
    boolean showImage = true;   // 是否显示image 默认true

    LocalSongsAdapter.ActionClickListener actionClickListener; // action点击监听


    public static class SongsViewHolder extends RecyclerView.ViewHolder {
        TextView artist;
        TextView album;
        ImageView pic_url;
        View rootView;
        TextView pic_text;
        ImageView actionBtn;


        public SongsViewHolder(View view) {
            super(view);
            rootView = view;
            artist = view.findViewById(R.id.artist);
            album = view.findViewById(R.id.album);
            pic_url = view.findViewById(R.id.pic_url);
            pic_text = view.findViewById(R.id.pic_text);
            actionBtn = view.findViewById(R.id.action);
        }
    }

    public LocalSongsAdapter(Song[] songs, LocalSongsAdapter.SongClickListener listener, LocalSongsAdapter.ActionClickListener actionClickListener) {
        this.songList = songs;
        this.listener = listener;
        this.actionClickListener = actionClickListener;
    }

    public LocalSongsAdapter(Song[] songs, LocalSongsAdapter.SongClickListener listener, boolean showImage,LocalSongsAdapter.ActionClickListener actionClickListener) {
        this.actionClickListener = actionClickListener;
        this.songList = songs;
        this.listener = listener;
        this.showImage = showImage;
    }

    /**
     * 设置歌曲列表
     *
     * @param songs
     */
    public void setSongs(Song[] songs) {
        this.songList = songs;
        notifyDataSetChanged();
    }


    @Override
    public LocalSongsAdapter.SongsViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_songs_item, parent, false);
        LocalSongsAdapter.SongsViewHolder vh = new LocalSongsAdapter.SongsViewHolder(v);
        return vh;
    }

    /**
     * 根据歌曲信息更新ui
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final LocalSongsAdapter.SongsViewHolder holder, final int position) {
        Song song = songList[position];
        holder.artist.setText(song.getName() + "--" + song.getArtist());
        holder.album.setText(song.getAlbum() + "   来源:" + song.getOrigin());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(songList[position], position);
            }
        });

        // 当点击action按钮时通知父activity
        holder.actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionClickListener.onClick(v);
            }
        });
        if (showImage) {
            new DownloadImageTask(holder.pic_url)
                    .execute(song.getPic_url());
        } else {
            holder.pic_text.setText(String.valueOf(position + 1));
        }
    }

    //    @Override
    public int getItemCount() {
        return this.songList.length;
    }

    public interface SongClickListener {
        void onClick(Song song, int position);
    }
    public interface ActionClickListener {
        void onClick(View view);
    }
}

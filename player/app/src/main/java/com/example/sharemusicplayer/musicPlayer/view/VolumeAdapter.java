package com.example.sharemusicplayer.musicPlayer.view;

import android.content.Context;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sharemusicplayer.R;

public class VolumeAdapter extends BaseAdapter {

    TextView nowVolume;
    TextView maxVolume;
    SeekBar seekBar;
    Context context;

    public VolumeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.volume, parent, false);

        // 获取各个控件
        nowVolume = v.findViewById(R.id.now_volume);
        maxVolume = v.findViewById(R.id.max_volume);
        seekBar = v.findViewById(R.id.volume_progress);
        seekBar.setMax(100);
        // 设置当前音量
        AudioManager mAudioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        setProcess(current, max);

        // 拖动时设置系统音量
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 设置系统音量
                int progress = seekBar.getProgress();
                int volume = progress * max / 100;
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);

                nowVolume.setText(String.valueOf(progress));
                notifyDataSetChanged();
            }
        });
        return v;
    }

    void setProcess(int current, int max) {
        // 计算百分比
        int process = (int) ((current / (max * 1.0)) * 100);
        seekBar.setProgress(process);
        maxVolume.setText("100");
        nowVolume.setText(String.valueOf(process));
    }
}

package com.example.sharemusicplayer.localPlayer.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharemusicplayer.R;

/**
 * 用于控制弹出框列表的显示
 */
public class ActionMenuAdapter extends BaseAdapter {
    ImageView imageView;    // 图标
    TextView textView;      // 文字描述
    @Override
    public int getCount() {
        return 11;
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
                .inflate(R.layout.action_menu_item, parent, false);
        this.imageView = v.findViewById(R.id.icon);
        this.textView = v.findViewById(R.id.text);
        // TODO 监听每个控件的点击操作并进行回调
        return v;
    }

}

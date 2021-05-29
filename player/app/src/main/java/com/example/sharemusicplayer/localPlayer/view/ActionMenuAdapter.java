package com.example.sharemusicplayer.localPlayer.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于控制弹出框列表的显示
 */
public class ActionMenuAdapter extends BaseAdapter {

    List<ActionMenu> actionMenus = new ArrayList<>();

    public ActionMenuAdapter(List<ActionMenu> actionMenus) {
        this.actionMenus = actionMenus;
    }

    @Override
    public int getCount() {
        return actionMenus.size();
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
        ImageView imageView = v.findViewById(R.id.icon);
        TextView textView = v.findViewById(R.id.text);
        ActionMenu actionMenu = this.actionMenus.get(position);
        imageView.setImageDrawable(actionMenu.getDrawable());
        textView.setText(actionMenu.getText());
        v.setOnClickListener(actionMenu.getL());
        return v;
    }

    public static class ActionMenu {
        Drawable drawable;
        String text;
        View.OnClickListener l;

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public View.OnClickListener getL() {
            return l;
        }

        public void setL(View.OnClickListener l) {
            this.l = l;
        }
    }

}

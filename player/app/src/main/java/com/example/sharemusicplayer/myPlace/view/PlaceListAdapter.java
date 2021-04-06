package com.example.sharemusicplayer.myPlace.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.Place;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.DownloadImageTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 圈子列表适配器
 */
public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceListViewHolder> {

    Place[] places = {};
    ClickListener listener;

    public static class PlaceListViewHolder extends RecyclerView.ViewHolder {

        /**
         * 圈子信息
         */
        ImageView place_pic;
        TextView place_name;
        TextView place_des;
        View view;
        /**
         * 用户信息
         */
        CircleImageView person_image;
        TextView nick_name;

        public PlaceListViewHolder(final View view) {
            super(view);
            this.view = view;
            place_pic = view.findViewById(R.id.place_pic);
            place_name = view.findViewById(R.id.place_name);
            place_des = view.findViewById(R.id.place_des);

            nick_name = view.findViewById(R.id.nick_name);
            person_image = view.findViewById(R.id.person_image);
        }
    }

    @Override
    public PlaceListAdapter.PlaceListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_item, parent, false);
        PlaceListViewHolder vh = new PlaceListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final PlaceListViewHolder holder, final int position) {
        final Place place = places[position];
        // 设置圈子信息
        holder.place_name.setText(place.getName());
        holder.place_des.setText(place.getDesName());
        if (listener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.click(place, position);
                }
            });
        }
        if (place.getPicUrl() != null && !place.getPicUrl().equals("")) {
            new DownloadImageTask(holder.place_pic).execute(BaseHttpService.BASE_URL + place.getPicUrl());
        }

        // 设置用户信息
        holder.nick_name.setText(place.getBelongUser().getNickName());
        if (place.getBelongUser().getImageUrl() != null && !place.getBelongUser().getImageUrl().equals("")) {
            new DownloadImageTask(holder.person_image).execute(BaseHttpService.BASE_URL + place.getBelongUser().getImageUrl());
        }
    }

    @Override
    public int getItemCount() {
        return places.length;
    }

    public void setPlaces(Place[] places) {
        this.places = places;
        notifyDataSetChanged();
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }


    public interface ClickListener {
        void click(Place place, int position);
    }
}

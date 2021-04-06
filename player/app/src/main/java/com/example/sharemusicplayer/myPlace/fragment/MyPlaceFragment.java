package com.example.sharemusicplayer.myPlace.fragment;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.Place;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.PlaceService;
import com.example.sharemusicplayer.musicPlayer.music.PlayerService;
import com.example.sharemusicplayer.myPlace.activities.ChooseUserActivity;
import com.example.sharemusicplayer.myPlace.activities.InitPlaceActivity;
import com.example.sharemusicplayer.myPlace.activities.MyPlaceActivity;
import com.example.sharemusicplayer.myPlace.view.PlaceListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyPlaceFragment extends Fragment {

    private MyPlaceViewModel mViewModel;

    /**
     * 圈子列表
     */
    private RecyclerView placeListView;
    private PlaceListAdapter placeListAdapter;
    private StaggeredGridLayoutManager gridLayoutManager;
    private FloatingActionButton myPlaceBtn;


    PlaceService placeService = PlaceService.getInstance();

    public static MyPlaceFragment newInstance() {
        return new MyPlaceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_place_fragment, container, false);

        // 设置圈子列表
        placeListView = view.findViewById(R.id.place_list_view);
        placeListView.setHasFixedSize(true);
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        placeListView.setLayoutManager(gridLayoutManager);
        placeListAdapter = new PlaceListAdapter();
        placeListView.setAdapter(placeListAdapter);
        placeListAdapter.setListener(new PlaceListAdapter.ClickListener() {
            @Override
            public void click(Place place, int position) {
                Intent intent = new Intent(getContext(), MyPlaceActivity.class);
                intent.putExtra(MyPlaceActivity.PLACE_ID, place.getId());
                startActivity(intent);
            }
        });
        placeService.getCurrentPlaces(new BaseHttpService.CallBack() {
            @Override
            public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                placeListAdapter.setPlaces((Place[]) result.getData());
            }
        });

        // 设置进入我的圈子
        myPlaceBtn = view.findViewById(R.id.my_place_btn);
        myPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断是否创建过圈子 如果创建过直接进入 否则进入创建圈子界面
                placeService.isCreatePlace(new BaseHttpService.CallBack() {
                    @Override
                    public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                        Boolean isCreate = (Boolean) result.getData();
                        if (isCreate) {
                            // 获取当前登陆用户的圈子 进入圈子界面
                            placeService.getCurrentPlace(new BaseHttpService.CallBack() {
                                @Override
                                public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                                    Place place = (Place) result.getData();
                                    Intent intent = new Intent(getContext(), MyPlaceActivity.class);
                                    intent.putExtra(MyPlaceActivity.PLACE_ID, place.getId());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Intent intent = new Intent(getContext(), InitPlaceActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyPlaceViewModel.class);
        // TODO: Use the ViewModel
    }

}

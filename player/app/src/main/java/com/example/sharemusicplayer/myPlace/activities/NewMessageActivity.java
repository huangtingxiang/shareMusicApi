package com.example.sharemusicplayer.myPlace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharemusicplayer.MainActivity;
import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.Message;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.DownloadImageTask;
import com.example.sharemusicplayer.httpService.MessageService;
import com.example.sharemusicplayer.musicPlayer.activities.SearchActivity;
import com.google.gson.Gson;

/**
 * 新建消息界面 根据圈子id添加到圈子中
 */
public class NewMessageActivity extends AppCompatActivity {
    public static final int SONG_RESULT = 0;    // 用于serach界面结果回调
    public static final String PLACE_ID = "place_id";
    public static final String NEW_MESSAGE = "new_message";


    Toolbar myToolbar;
    Button addSongBtn;
    Song song;

    MessageService messageService = MessageService.getInstance();

    /**
     * 歌曲信息
     */
    TextView artist;
    TextView album;
    ImageView pic_url;
    EditText content;
    Long placeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        placeId = getIntent().getLongExtra(PLACE_ID, 0L);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 显示返回按钮
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // 不显示标题

        // 进入搜索界面 获取搜索歌曲会调
        addSongBtn = findViewById(R.id.add_song);
        addSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMessageActivity.this, SearchActivity.class);
                intent.setAction(SearchActivity.GET_RESULT);
                startActivityForResult(intent, SONG_RESULT);
            }
        });

        artist = findViewById(R.id.artist);
        album = findViewById(R.id.album);
        pic_url = findViewById(R.id.pic_url);
        content = findViewById(R.id.content_tex);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.save_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.action_save);
        saveItem.setTitle("发表");
        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 保存消息
                Message message = new Message();
                message.setSong(song);
                message.setContent(content.getText().toString());
                messageService.createMessage(new BaseHttpService.CallBack() {
                    @Override
                    public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(NEW_MESSAGE, new Gson().toJson(result.getData()));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }, placeId, message);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // 当search界面结果返回时 设置歌曲
        switch (requestCode) {
            case SONG_RESULT:
                if (resultCode == Activity.RESULT_OK) {
                    Song song = new Gson().fromJson(intent.getStringExtra(SearchActivity.SONG_RESULT), Song.class);
                    // 隐藏button 并显示歌曲信息
                    addSongBtn.setVisibility(View.GONE);
                    this.song = song;
                    updateSong(song);
                }
                break;
            default:
                break;
        }
    }

    public void updateSong(Song song) {
        artist.setText(song.getName() + "--" + song.getArtist());
        album.setText(song.getAlbum() + "   来源:" + song.getOrigin());
        if (song.getPic_url() != null && !song.getPic_url().equals("")) {
            new DownloadImageTask(pic_url)
                    .execute(song.getPic_url());
        }
    }
}

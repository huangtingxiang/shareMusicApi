package com.example.sharemusicplayer.Utils;


import android.media.MediaMetadataRetriever;
import com.example.sharemusicplayer.entity.OriginType;
import com.example.sharemusicplayer.entity.Song;


public class MusicUtils {

    /**
     * 从文件路径中获取歌曲的信息
     * @param filePath
     * @return
     */
    public static Song getMusicData(String filePath) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        Song song = new Song();
        try {
            mediaMetadataRetriever.setDataSource(filePath);
            String artist = mediaMetadataRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String album = mediaMetadataRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String name = mediaMetadataRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (Boolean.valueOf(mediaMetadataRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_IMAGE))) {
                song.setPic_url(mediaMetadataRetriever
                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_PRIMARY));
            }

            song.setOrigin(OriginType.LOCAL_PLAYER.name());
            song.setArtist(artist);
            song.setAlbum(album);
            song.setName(name);
            song.setSong_url(filePath);
        } catch (IllegalArgumentException e) {
            throw e;
        } finally {
            mediaMetadataRetriever.release();
        }
        return song;
    }
}

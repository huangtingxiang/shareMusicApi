package com.jdxiang.shareMusicApi.service;

import com.jdxiang.shareMusicApi.entity.PlayList;
import com.jdxiang.shareMusicApi.entity.Song;
import com.jdxiang.shareMusicApi.repository.PlayListRepository;
import com.jdxiang.shareMusicApi.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {
    @Autowired
    SongRepository songRepository;
    @Autowired
    PlayListRepository playListRepository;


    @Override
    public List<Song> getAllByPlayList(Long playId) {
        return songRepository.findAllByPlayListIdOrderByIdDesc(playId);
    }

    @Override
    public Song createSongToPlayList(Long playId, Song song) {
        PlayList playList = playListRepository.findById(playId).get();
        playList.setPic_url(song.getPic_url());
        song.setPlayList(playList);
        return songRepository.save(song);
    }
}

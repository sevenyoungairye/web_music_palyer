package com.lovely.service;

import com.lovely.dao.MusicSongDao;
import com.lovely.entity.MusicLyrics;
import com.lovely.entity.MusicSong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author echo lovely
 * @date 2020/8/23 10:39
 */
@Service("musicSongService")
public class MusicSongService {

    @Resource(name = "musicSongDao")
    private MusicSongDao dao;

    public Map<String, Object> queryAllSong(MusicSong song, int pageNo, int pageSize) {
        List<MusicSong> songList = dao.queryAllSongPagination(song, pageNo, pageSize);
        int totalPage = dao.getTotalPage(song, pageSize);

        Map<String, Object> map = new HashMap<>();
        map.put("songList", songList);
        map.put("totalPage", totalPage);

        return map;
    }

    // upload cover image, get song LyricsId, song title(song_name)
    // give lyrics id a tran
    @Transactional
    public int addMusicSong(MusicSong song, MusicLyrics lyrics) {
        int count = -1;
        int lyricsId = dao.addLyricsToSong(lyrics);
        if (lyricsId > 0) {
            song.setLyricsId(new Long(lyricsId));
            count = dao.addMusicSong(song);
        }
        return count;
    }

    /**
     * 查询单首歌曲 和 对应的歌词id
     * @param songId
     * @return
     */
    public Map<String, Object> queryOneSong(int songId) {
        Map<String, Object> map = new HashMap<>();

        MusicSong song = dao.queryOneSong(songId);
        if (song != null) {
            map.put("song", song);
            Long lyricsId = song.getLyricsId();
            if (lyricsId != null && lyricsId > 0) {
                MusicLyrics lyrics = dao.queryOneLyrics(Integer.parseInt(lyricsId.toString()));
                map.put("lyrics", lyrics);
            }
        }

        return map;
    }

    public int updateOneSong(MusicSong song) {
        int count = -1;
        if (song != null) {
            count = dao.updateOneSong(song);
        }
        return count;
    }


    // 轮播歌曲
    public List<MusicSong> songCarousel() {

        return dao.songCarousel();
    }

    // 最新歌曲
    public List<MusicSong> queryLatestSong() {
        return dao.queryLatestSong();
    }

    // 搜索歌曲
    public List<MusicSong> searchSongInPage(String condition) {
        if (condition != null) {
            return dao.searchSongInPage(condition);
        }
        return null;
    }

}

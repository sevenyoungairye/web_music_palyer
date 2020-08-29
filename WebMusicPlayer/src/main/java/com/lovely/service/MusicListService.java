package com.lovely.service;

import com.lovely.action.upload.LyricsUpload;
import com.lovely.dao.MusicListDao;
import com.lovely.entity.MusicList;
import com.lovely.entity.MusicListRelation;
import com.lovely.entity.MusicLyrics;
import com.lovely.entity.MusicSong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author echo lovely
 * @date 2020/8/18 8:20
 */
@Service(value = "musicListService")
public class MusicListService {

    @Resource(name = "musicListDao")
    private MusicListDao dao;

    public Map<String, Object> queryAllMusicList(MusicList mList, int pageNo, int pageSize) {
        List<MusicList> musicList = dao.queryAllMusicList(mList, pageNo, pageSize);

        int totalPage = dao.getTotalPage(mList, pageSize);

        Map<String, Object> map = new HashMap<>();
        map.put("totalPage", totalPage);
        map.put("musicList", musicList);

        return map;
    }

    public int addMusicList(MusicList musicList) {
        int count = -1;

        if (musicList != null)
            count = dao.addMusicList(musicList);

        return count;
    }

    public int deleteMusicList(Long musicListId) {
        int count = -1;

        if (musicListId > 0)
            count = dao.deleteMusicList(musicListId);

        return count;
    }

    public MusicList queryOneMusicList(Long musicListId) {
        if (musicListId > 0)
            return dao.queryOneMusicList(musicListId);
        return null;
    }

    public int updateMusicList(MusicList musicList) {
        int count = -1;

        if (musicList != null)
            return  dao.updateMusicList(musicList);

        return count;
    }

    public  int removeSongInPlayList(MusicListRelation musicListRelation) {
        if (musicListRelation != null)
            return dao.removeSongInPlayList(musicListRelation);
        return -1;
    }

    @Transactional
    public Map<String, Object> querySongInPlayList(Long musicListId, int pageNo, int pageSize) {
        if (musicListId > 0) {
            List<MusicSong> playList = dao.querySongInPlayList(musicListId, pageNo, pageSize);
            int totalPage = dao.getSongTotalPage(musicListId, pageSize);

            Map<String, Object> map = new HashMap<>();
            map.put("totalPage", totalPage);
            map.put("playList", playList);

            return map;
        }
        return null;
    }

    public List<MusicSong> querySongByName(String songTitle) {
        if ("".equals(songTitle)) {
            return null;
        }
        return dao.querySongByName(songTitle);
    }

    public int addSongInPlayList(MusicListRelation musicListRelation) {
        if (musicListRelation != null)
            return dao.addSongInPlayList(musicListRelation);
        return -1;
    }

    // 首页推荐歌单
    public List<MusicList> queryRecommendPlayList() {
        return dao.queryRecommendPlayList();
    }

    // song in play list
    public List<MusicSong> getSongInPagePlayList(Long playListId) {
        return dao.getSongInPagePlayList(playListId);
    }

    /**
     * 歌曲和对应的歌词信息
     * @param playListId
     * @return
     */
    @Transactional
    public Map<String, Object> queryAllSongByListId(int playListId) {
        Map<String, Object> map = new HashMap<>();

        List<MusicSong> songList = dao.queryAllSongByListId(playListId);

        if (songList == null) {
            return null;
        }
        map.put("songList", songList);

        List<MusicLyrics> lyricsList = new ArrayList<>();

        for (int i = 0; i < songList.size(); i++) {
            Long lyricsId = songList.get(i).getLyricsId();
            if (lyricsId != null && lyricsId > 0) {
                MusicLyrics lyrics = dao.queryOneLyrics(Integer.parseInt(lyricsId.toString()));
                lyricsList.add(lyrics);
            }
        }

        map.put("lyricsList", lyricsList);

        return map;
    }

    // 查询歌单中所有歌曲id
    public List<Long> queryAllSongIdInPlayList(int playListId) {
        List<MusicSong> songList = dao.queryAllSongByListId(playListId);

        List<Long> songIdList = new ArrayList<>();
        if (songIdList != null) {
            for (int i = 0; i < songList.size(); i++) {
                songIdList.add(songList.get(i).getSongId());
            }
        }

        return songIdList;
    }

}

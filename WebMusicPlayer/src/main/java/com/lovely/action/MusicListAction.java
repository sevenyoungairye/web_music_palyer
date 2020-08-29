package com.lovely.action;

import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.lovely.entity.MusicList;
import com.lovely.entity.MusicListRelation;
import com.lovely.entity.MusicSong;
import com.lovely.listener.WebApplicationContextUtils;
import com.lovely.mvc.DispatcherAction;
import com.lovely.service.MusicListService;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author echo lovely
 * @date 2020/8/18 8:25
 */
public class MusicListAction extends DispatcherAction {

    private MusicList musicList;

    private MusicListService mListService;

    private MusicListRelation musicListRelation;
    private MusicSong song;

    @Override
    public void init(HttpServletRequest req, HttpServletResponse resp) {
        ApplicationContext app = WebApplicationContextUtils.getApp(req.getServletContext());
        mListService = (MusicListService)app.getBean("musicListService");
    }

    private int pageNo = 1;
    private int pageSize = 5;
    // multiple query
    public Object queryAllMusicList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("id"+musicList.getMusicListId());
        Map<String, Object> map = mListService.queryAllMusicList(musicList, pageNo, pageSize);
        return map;
    }

    public Object queryOneMusicList(HttpServletRequest req, HttpServletResponse resp) {
        if (musicList != null) {
            if (musicList.getMusicListId() != null) {
                return mListService.queryOneMusicList(musicList.getMusicListId());
            }
        }
        return null;
    }

    private int listPageNo = 1;
    private int listPageSize = 1;
    public Object querySongInPlayList(HttpServletRequest req, HttpServletResponse resp) {
        if (musicList != null) {
            if (musicList.getMusicListId() != null) {
                return mListService.querySongInPlayList(musicList.getMusicListId(), listPageNo, listPageSize);
            }
        }
        return null;
    }

    public Object updateMusicList(HttpServletRequest req, HttpServletResponse resp) {
        if (musicList != null)
            return mListService.updateMusicList(musicList);
        return null;
    }

    public Object addMusicList(HttpServletRequest req, HttpServletResponse resp) {
        if (musicList != null) {
            return mListService.addMusicList(musicList);
        }
        return null;
    }

    public  Object removeSongInPlayList(HttpServletRequest req, HttpServletResponse resp) {
        if (musicListRelation != null)
            return mListService.removeSongInPlayList(musicListRelation);
        return -1;
    }

    public Object querySongByName(HttpServletRequest req, HttpServletResponse resp) {

        if (song != null) {
            if (song.getTitle() != null) {
                return mListService.querySongByName(song.getTitle());
            }
        }
        return null;
    }

    public Object addSongInPlayList(HttpServletRequest req, HttpServletResponse resp) {
        if (musicListRelation != null)
            return mListService.addSongInPlayList(musicListRelation);
        return -1;
    }

    // 推荐歌单
    public Object queryRecommendPlayList(HttpServletRequest req, HttpServletResponse resp) {
        return mListService.queryRecommendPlayList();
    }

    // 每个歌单中 查四首歌曲..
    public Object getSongInPagePlayList(HttpServletRequest req, HttpServletResponse resp) {
        if (musicList != null) {
            if (musicList.getMusicListId() != null)
            return mListService.getSongInPagePlayList(musicList.getMusicListId());
        }
        return null;
    }
}

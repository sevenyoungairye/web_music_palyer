package com.lovely.action;

import com.lovely.entity.MusicLyrics;
import com.lovely.entity.MusicSong;
import com.lovely.listener.WebApplicationContextUtils;
import com.lovely.mvc.DispatcherAction;
import com.lovely.service.MusicSongService;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * song manage..
 * @author echo lovely
 * @date 2020/8/23 10:37
 */
public class MusicSongAction extends DispatcherAction {

    private MusicSongService musicSongService;

    private MusicSong song;

    private MusicLyrics lyrics;

    @Override
    public void init(HttpServletRequest req, HttpServletResponse resp) {
        ApplicationContext app = WebApplicationContextUtils.getApp(req.getServletContext());
        musicSongService = app.getBean(MusicSongService.class);
    }

    private int pageNo = 1;
    private int pageSize = 3; // one page three song..
    public Object queryAllMusicSong(HttpServletRequest req, HttpServletResponse resp) {
        return musicSongService.queryAllSong(song, pageNo, pageSize);
    }


    public Object addMusicSong(HttpServletRequest req, HttpServletResponse resp) {

        int count = -1;

        if (song != null && lyrics != null)
            count = musicSongService.addMusicSong(song, lyrics);

        return count;
    }

    public Object queryOneSong(HttpServletRequest req, HttpServletResponse resp) {
        if (song != null) {
            if (song.getSongId() > 0) {
                Integer songId = new Integer(song.getSongId().toString());
                return musicSongService.queryOneSong(songId);
            }
        }

        return null;
    }

    public Object updateMusicSong(HttpServletRequest req, HttpServletResponse resp) {

        if (song != null) {
            if (song.getSongId() > 0) {
                return musicSongService.updateOneSong(song);
            }
        }

        return null;
    }

    // 轮播歌曲
    public Object songCarousel(HttpServletRequest req, HttpServletResponse resp) {

        return musicSongService.songCarousel();
    }

    // 最新歌曲
    public Object queryLatestSong(HttpServletRequest req, HttpServletResponse resp) {
        return musicSongService.queryLatestSong();
    }

    private String condition;
    // 页面搜索歌曲
    public Object searchSongInPage(HttpServletRequest req, HttpServletResponse resp) {

        return musicSongService.searchSongInPage(condition);
    }
}

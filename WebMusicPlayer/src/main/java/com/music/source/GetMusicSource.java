package com.music.source;

import com.lovely.entity.LyricInfo;
import com.lovely.entity.MusicLyrics;
import com.lovely.entity.MusicSong;
import com.lovely.listener.WebApplicationContextUtils;
import com.lovely.mvc.DispatcherAction;
import com.lovely.service.MusicListService;
import com.lovely.service.MusicSongService;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取歌曲 资源
 * @author echo lovely
 * @date 2020/8/25 10:00
 */
public class GetMusicSource extends DispatcherAction {

    private MusicSongService songService;
    private MusicListService listService;

    @Override
    public void init(HttpServletRequest req, HttpServletResponse resp) {
        ApplicationContext app = WebApplicationContextUtils.getApp(req.getServletContext());
        songService = app.getBean(MusicSongService.class);
        listService = app.getBean(MusicListService.class);
    }

    private int songId;
    // 根据歌曲id获得歌曲
    public Object getSongBySongId(HttpServletRequest req, HttpServletResponse resp) {
        return getSingleSong(req, songId);
    }

    private int listId;
    // 根据歌单id获得 所有歌曲id
    public Object getSongIdByListId(HttpServletRequest req, HttpServletResponse resp) {
        return listService.queryAllSongIdInPlayList(listId);
    }

    /**
     * 获取歌词路径 用于歌词解析
     * @param req 请求对象
     * @param lyricFilePath 数据库中的数据名
     * @return
     */
    public String getPath(HttpServletRequest req, String lyricFilePath) {

        String lyricPath = req.getServletContext().getRealPath("/music-resource/lyrics/");

        System.out.println("lyricPath: " + lyricPath);

        File lyricsFile = new File(lyricPath);

        if (lyricsFile != null) {
            File[] files = lyricsFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (fileName.equals(lyricFilePath)) {
                    lyricPath = lyricPath + fileName;
                    System.out.println("歌词全路径：" + lyricPath);
                    break;
                }
            }
        }
        return lyricPath;
    }

    /**
     * 获取单首歌曲信息 map
     * @param lyricsPath 歌曲路径
     * @param song 歌曲对象
     * @return 歌曲歌词 + 歌曲名 图片 个堂区路径
     */
    public LinkedHashMap<String, Object> getSongInfo(String lyricsPath, MusicSong song) {
        LinkedHashMap<String, Object> lyricsContentMap = new LinkedHashMap<>();

        List<LyricInfo> lyricsContent = getLyricsContent(lyricsPath);
        String name = song.getTitle() + "-" + song.getSinger();
        String img = "music-manage/img/" + song.getCoverImg();
        String url = "music-resource/" + song.getTitle();
        // {"name":"Style-taylor swift","img":"img/list8.jpg","url":"music-resource/Style.mp3", "lrc":[]}

        lyricsContentMap.put("name", name);
        lyricsContentMap.put("img", img);
        lyricsContentMap.put("url", url);
        lyricsContentMap.put("lrc", lyricsContent);

        lyricsContentMap.put("id", song.getSongId());

        return lyricsContentMap;
    }

    /**
     * 获取歌词
     * @param lyricsPath 歌词路径
     * @return 返回歌词list
     */
    public List<LyricInfo> getLyricsContent(String lyricsPath) {

        ArrayList<LyricInfo> list = new ArrayList<>();

        /*String lyricsPath = "E:\\HRShcool\\s3\\Html5Css3\\WebMusicPlayer\\target" +
                "\\WebMusicPlayer\\music-resource\\lyrics\\a61aaf07-6f16-45ae-8ef3-7c298bacfe09.lrc";*/
        File file = new File(lyricsPath);

        System.out.println(file);

        BufferedReader in = null;
        String str = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
            while ((str = in.readLine()) != null) {
                // System.out.println(str);
                String[] split = str.split("]");

                if (split.length == 2) {
                    String time = split[0].substring(1);

                    String lrcStr = split[1];
                    //System.out.println(time + "==" + lrcStr);

                    LyricInfo lyricInfo = new LyricInfo();
                    lyricInfo.setTime(time);
                    lyricInfo.setLrcStr(lrcStr);
                    list.add(lyricInfo);

                    //lyricsContentList.add(str + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // String jsonString = JSON.toJSONString(list);
        //System.out.println(jsonString);
        return list;
    }

    // 获得当前歌单 所有歌曲
    public Object getAllSongInPlayList(HttpServletRequest req, HttpServletResponse resp) {

        List<Object> allSongList = new ArrayList<>();

        List<Long> songIdList = listService.queryAllSongIdInPlayList(listId);
        if (songIdList != null) {
            for (int i = 0; i < songIdList.size(); i++) {
                Long songId = songIdList.get(i);
                allSongList.add(getSingleSong(req, new Integer(songId.toString())));
            }
        }

        return allSongList;
    }

    // 拿到单曲信息
    public Map<String, Object> getSingleSong(HttpServletRequest req, int songId) {
        Map<String, Object> map = songService.queryOneSong(songId);
        MusicSong song = (MusicSong) map.get("song");

        MusicLyrics lyrics = (MusicLyrics) map.get("lyrics");
        String lyricPath = getPath(req, lyrics.getLyricsPath());

        LinkedHashMap<String, Object> songInfo = getSongInfo(lyricPath, song);

        return songInfo;
    }
}

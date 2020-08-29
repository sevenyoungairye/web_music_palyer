package com.lovely.dao;

import com.lovely.entity.MusicLyrics;
import com.lovely.entity.MusicSong;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * music song curd..
 * @author echo lovely
 * @date 2020/8/22 17:00
 */
@Repository("musicSongDao")
public class MusicSongDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<MusicSong> queryAllSongPagination(MusicSong song, int pageNo, int pageSize) {

        ArrayList<Object> paramList = new ArrayList<>();

        StringBuffer sql = new StringBuffer();

        sql.append("select * from music_song where 1=1 ");
        if (song != null) {
            if (song.getSongId() != null && song.getSongId() > 0) {
                sql.append("and music_song_id = ? ");
                paramList.add(song.getSongId());
            }
            if (song.getTitle() != null && !"".equals(song.getTitle())) {
                sql.append("and music_title like ? ");
                paramList.add("%" + song.getTitle() + "%");
            }
            if (song.getSinger() != null && !"".equals(song.getSinger())) {
                sql.append("and music_singer like ? ");
                paramList.add("%" + song.getSinger() + "%");
            }
        }
        sql.append(" limit ?, ?");

        paramList.add((pageNo - 1) * pageSize);
        paramList.add(pageSize);

        List<MusicSong> songList = jdbcTemplate.query(sql.toString(),
                paramList.toArray(), new MusicSongRowMapper());

        return songList;
    }

    @Test
    public void test1() {
        MusicSongDao dao = new ClassPathXmlApplicationContext("applicationContext.xml").getBean(MusicSongDao.class);
        List<MusicSong> songList = dao.queryAllSongPagination(null, 1, 3);
        System.out.println(songList);
    }

    public int getTotalPage(MusicSong song, int pageSize) {

        int totalPage = 1;

        ArrayList<Object> paramList = new ArrayList<>();

        StringBuffer sql = new StringBuffer();

        sql.append("select count(*) from music_song where 1=1 ");
        if (song != null) {
            if (song.getSongId() != null && song.getSongId() > 0) {
                sql.append("and music_song_id = ? ");
                paramList.add(song.getSongId());
            }
            if (song.getTitle() != null && !"".equals(song.getTitle())) {
                sql.append("and music_title like ? ");
                paramList.add("%" + song.getTitle() + "%");
            }
            if (song.getSinger() != null && !"".equals(song.getSinger())) {
                sql.append("and music_singer like ? ");
                paramList.add("%" + song.getSinger() + "%");
            }
        }


        Integer totalCount = jdbcTemplate.queryForObject(sql.toString(), paramList.toArray(), int.class);

        totalPage = (totalCount + pageSize - 1) / pageSize;

        return totalPage;
    }

    // give a tran
    public int addMusicSong(MusicSong song) {
        int count = -1;
        String sql = "insert into music_song values " +
                "(null, ?, ?, ?, ?, 0, ?, now())";

        ArrayList<Object> paramList = new ArrayList<>();
        paramList.add(song.getTitle());
        paramList.add(song.getCoverImg()); // upload song's cover_image..
        paramList.add(song.getSinger());
        paramList.add(song.getDetails());
        paramList.add(song.getLyricsId()); // upload lyrics file!!!

        count = jdbcTemplate.update(sql, paramList.toArray());
        return count;
    }

    // give a tran
    public int addLyricsToSong(MusicLyrics lyrics) {
        int count = -1;

        String sql = "insert into music_lyrics values (null, ?)";

        count = jdbcTemplate.update(sql, new Object[]{lyrics.getLyricsPath()});

        if (count > 0) {
            sql = "select last_insert_id() from dual"; // 当前增加的值
            count = jdbcTemplate.queryForObject(sql, int.class);
        }

        return count;
    }

    // search song and lyric..
    public MusicSong queryOneSong(int songId) {
        String sql = "select * from music_song where music_song_id = ?";
        MusicSong song = jdbcTemplate.queryForObject(sql, new Object[]{songId}, new MusicSongRowMapper());
        return song;
    }

    public MusicLyrics queryOneLyrics(int lyricsId) {

        String sql = "select * from music_lyrics where lyrics_id = ?";
        MusicLyrics musicLyrics = jdbcTemplate.queryForObject(sql, new Object[]{lyricsId},
                new BeanPropertyRowMapper<>(MusicLyrics.class));
        return musicLyrics;
    }

    // update lyrics and song
    public int updateOneLyrics(int lyricsId) {
        int count = -1;
        String sql = "update music_lyrics where lyrics_id = ?";
        count = jdbcTemplate.update(sql, new Object[]{lyricsId});
        return count;
    }

    public int updateOneSong(MusicSong song) {
        int count = -1;
        String sql = "update music_song set music_cover_img = ?, music_singer = ?, music_details = ?" +
                " where music_song_id = ?";
        Object[] paramObj = new Object[]{song.getCoverImg(), song.getSinger(), song.getDetails(), song.getSongId()};
        count = jdbcTemplate.update(sql, paramObj);
        return count;
    }


    /**
     * 歌曲轮播，查询三条推荐的歌曲 播放量最高
     * @return 三条歌曲列
     */
    public List<MusicSong> songCarousel() {

        StringBuffer sql = new StringBuffer();

        sql.append("select song.* from music_song song order by music_listening_volume desc ");
        sql.append("limit 0, 3");

        List<MusicSong> songList = jdbcTemplate.query(sql.toString(), new MusicSongRowMapper());

        return songList;
    }

    /**
     * 最新音乐
     * @return 3条最新音乐
     */
    public List<MusicSong> queryLatestSong() {

        StringBuffer sql = new StringBuffer();

        sql.append("select song.* from music_song song order by music_create_date desc ");
        sql.append("limit 0, 3");

        List<MusicSong> latestSong = jdbcTemplate.query(sql.toString(),
                new MusicSongRowMapper());

        return latestSong;
    }

    // 页面搜索歌曲
    public List<MusicSong> searchSongInPage(String condition) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from music_song ");
        sql.append("where music_title like ? or music_singer like ? or music_details like ? ");

        ArrayList<String> paramObj = new ArrayList<>();
        paramObj.add("%" + condition + "%");
        paramObj.add("%" + condition + "%");
        paramObj.add("%" + condition + "%");

        List<MusicSong> songList = jdbcTemplate.query(sql.toString(),
                paramObj.toArray(), new MusicSongRowMapper());

        return songList;
    }

}

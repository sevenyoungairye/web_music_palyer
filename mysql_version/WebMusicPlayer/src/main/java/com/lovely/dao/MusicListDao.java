package com.lovely.dao;

import com.lovely.entity.MusicList;
import com.lovely.entity.MusicListRelation;
import com.lovely.entity.MusicLyrics;
import com.lovely.entity.MusicSong;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * song list curd
 * @author echo lovely
 * @date 2020/8/17 20:52
 */
@Repository(value = "musicListDao")
public class MusicListDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    // multiple page query
    public List<MusicList> queryAllMusicList(MusicList mList, int pageNo, int pageSize) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from music_list where 1=1 ");

        ArrayList<Object> parmaList = new ArrayList<>();

        if (mList != null) {
            // search by case..
            if (mList.getMusicListId() != null && mList.getMusicListId() > 0) {
                sql.append(" and music_list_id = ?");
                parmaList.add(mList.getMusicListId());
            }

            if (mList.getMusicListTitle() != null && !"".equals(mList.getMusicListTitle())) {
                sql.append(" and music_list_title like ?");
                parmaList.add("%" + mList.getMusicListTitle() + "%");
            }

            if (mList.getMusicListTag() != null && !"".equals(mList.getMusicListTag())) {
                sql.append(" and music_list_tag like ?");
                parmaList.add("%" + mList.getMusicListTag() + "%");
            }
        }

        sql.append(" limit ?, ?");

        // select * from table limit (pageNo-1)*pageSize, pageSize;

        // rowNum ==> pageNo
        parmaList.add((pageNo - 1) * pageSize); // 查询
        parmaList.add(pageSize); // 一页 查询多少条数据

        List<MusicList> musicLists = jdbcTemplate.query(sql.toString(), parmaList.toArray(),
                new BeanPropertyRowMapper<>(MusicList.class));

        return musicLists;
    }

    // junit test multiple page
    @Test
    public void test2() {
        jdbcTemplate = new ClassPathXmlApplicationContext
                ("applicationContext.xml").getBean(JdbcTemplate.class);

        int totalPage = getTotalPage(null, 3);
        System.out.println(totalPage); // 总页数
    }

    // get music_list all page by pageSize
    public int getTotalPage(MusicList mList, int pageSize) {
        int totalPage = -1;

        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from music_list where 1=1 ");

        ArrayList<Object> paramList = new ArrayList<>();

        if (mList != null) {
            // search count
            if (mList.getMusicListId() != null && mList.getMusicListId() > 0) {
                sql.append(" and music_list_id = ?");
                paramList.add(mList.getMusicListId());
            }

            if (mList.getMusicListTitle() != null && !"".equals(mList.getMusicListTitle())) {
                sql.append(" and music_list_title like ?");
                paramList.add("%" + mList.getMusicListTitle() + "%");
            }

            if (mList.getMusicListTag() != null && !"".equals(mList.getMusicListTag())) {
                sql.append(" and music_list_tag like ?");
                paramList.add("%" + mList.getMusicListTag() + "%");
            }
        }

        int totalCount = jdbcTemplate.queryForObject(sql.toString(), paramList.toArray(), Integer.class);

        /*if (totalCount % pageSize == 0 && totalCount >= pageSize) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }*/

        totalPage = (totalCount + pageSize - 1) / pageSize;
        // int totalPageNum = (totalRecord +pageSize - 1) / pageSize; totalRecord 是总行数

        return totalPage;
    }

    public int addMusicList(MusicList musicList) {
        int count = -1;

        StringBuffer sql = new StringBuffer();
        /**
         * insert into music_list values (seq_music_list.nextval, '所念皆星河，星河不可及。', 'admin',
         * '“星河滚烫，你是人间理想。”', 300, '钢琴, 轻音乐, 清新', '5.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);
         */
        sql.append("insert into music_list values (null, ?, ?, ");
        sql.append("?, 0, ?, ?, ?, 0)");

        Object[] paramObj = new Object[] {musicList.getMusicListTitle(), musicList.getMusicListCreator(),
                musicList.getMusicListDetails(), musicList.getMusicListTag(), musicList.getMusicListImg(),
                new Date(System.currentTimeMillis())};

        count = jdbcTemplate.update(sql.toString(), paramObj);

        return count;
    }

    // add a column.. status
    public int deleteMusicList(Long musicListId) {
        int count = -1;

        String sql = "update music_list set delete_status = 1 where music_list_id = ? ";

        count = jdbcTemplate.update(sql, musicListId);
        return count;
    }

    public MusicList queryOneMusicList(Long musicListId) {
        MusicList musicList = null;

        String sql = "select * from music_list where music_list_id = ?";
        musicList = jdbcTemplate.queryForObject(sql, new Object[]{musicListId},
                new BeanPropertyRowMapper<>(MusicList.class));

        return musicList;
    }

    public int updateMusicList(MusicList musicList) {
        int count = -1;

        StringBuffer sql = new StringBuffer();
        sql.append("update music_list set music_list_title = ?, " +
                "music_list_details = ?, music_list_listening_volume = ?, music_list_tag = ?, music_list_img = ? ");
        sql.append("where music_list_id = ?");

        Object[] objArr = new Object[] {musicList.getMusicListTitle(), musicList.getMusicListDetails(),
                musicList.getMusicListListeningVolume(), musicList.getMusicListTag(),
                musicList.getMusicListImg(), musicList.getMusicListId()};

        count = jdbcTemplate.update(sql.toString(), objArr);

        return count;
    }

    /**
     * query song by list
     * @param musicListId
     * @return
     */
    public List<MusicSong> querySongInPlayList(Long musicListId, int pageNo, int pageSize) {

        StringBuffer sql = new StringBuffer();
        sql.append("select song.* from music_list list, music_song song, music_list_relation rel ");
        sql.append("where list.music_list_id = rel.music_list_id and song.music_song_id = rel.music_song_id ");
        sql.append("and list.music_list_id = ?");

        sql.append(" limit ?, ?");

        int startLine = (pageNo - 1) * pageSize;
        int endLine = pageSize;

        List<MusicSong> songList = jdbcTemplate.query(sql.toString(), new Object[]{musicListId, startLine, endLine},
                new MusicSongRowMapper());

        return songList;
    }

    public int getSongTotalPage(Long musicListId,int pageSize) {
        int totalPage = 1;

        StringBuffer sql = new StringBuffer();
        sql.append("select song.* from music_list list, music_song song, music_list_relation rel ");
        sql.append("where list.music_list_id = rel.music_list_id and song.music_song_id = rel.music_song_id ");
        sql.append("and list.music_list_id = ?");

        sql.insert(0, "select count(*) from (");
        sql.append(") as tab1"); // mysql 子查询得起别名

        int totalCount = jdbcTemplate.queryForObject(sql.toString(), new Object[]{musicListId}, int.class);

        /*if (totalCount % pageSize == 0 && totalCount >= pageSize) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }*/

        totalPage = (totalCount + pageSize - 1) / pageSize;

        return totalPage;
    }

    public int removeSongInPlayList(MusicListRelation musicListRelation) {
        int count = -1;

        String sql = "delete from music_list_relation " +
                "where music_list_id = ? and music_song_id = ?";

        count = jdbcTemplate.update(sql, new Object[]{musicListRelation.getMusicListId(),
                musicListRelation.getMusicSongId()});

        return count;
    }

    @Test
    public void test1() {

        String sql = "select * from music_song where music_title like ?";

        Object[] paramObj = new Object[] {"%"+"Spark"+"%"};

        jdbcTemplate = new ClassPathXmlApplicationContext("applicationContext.xml").getBean(JdbcTemplate.class);

        List<MusicSong> songList = jdbcTemplate.query(sql,
                paramObj, new MusicSongRowMapper());
        System.out.println(songList);
    }

    public List<MusicSong> querySongByName(String songTitle) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from music_song where music_title like ?");

        List<MusicSong> songList = jdbcTemplate.query(sql.toString(),
                new Object[]{"%"+songTitle+"%"}, new MusicSongRowMapper());

        return songList;
    }

    // 添加歌曲到歌单
    public int addSongInPlayList(MusicListRelation musicListRelation) {
        int count = -1;

        String sql = "insert into music_list_relation values (?, ?)";

        count = jdbcTemplate.update(sql, new Object[]{musicListRelation.getMusicListId(),
                musicListRelation.getMusicSongId()});

        return count;
    }

    /**
     * 主页面查询六条播放量最高的歌曲
     * @return 六条歌单
     */
    public List<MusicList> queryRecommendPlayList() {
        String sql = "select * from music_list list order by music_list_listening_volume desc limit 0, 6";

        List<MusicList> playList = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(MusicList.class));

        return playList;
    }

    // 查询四首歌曲 带上 歌单信息
    public List<MusicSong> getSongInPagePlayList(Long playListId) {
        StringBuffer sql = new StringBuffer();

        sql.append("select song.* from music_list list, music_song song, music_list_relation rel ");
        sql.append("where list.music_list_id = rel.music_list_id and song.music_song_id = rel.music_song_id ");
        sql.append("and list.music_list_id = ? ");
        sql.append("limit 0, 4 ");

        List<MusicSong> songList = jdbcTemplate.query(sql.toString(),
                new Object[]{playListId}, new MusicSongRowMapper());

        return songList;
    }

    // 查询该歌单的所有歌曲
    public List<MusicSong> queryAllSongByListId(int playListId) {
        StringBuffer sql = new StringBuffer();

        sql.append("select song.* from music_list list, music_song song, music_list_relation rel ");
        sql.append("where list.music_list_id = rel.music_list_id and song.music_song_id = rel.music_song_id ");
        sql.append("and list.music_list_id = ? ");

        List<MusicSong> songList = jdbcTemplate.query(sql.toString(),
                new Object[]{playListId}, new MusicSongRowMapper());

        return songList;
    }

    /**
     * 获取歌对应的歌词信息
     * @param lyricsId 歌词id
     * @return 歌词信息 路径..
     */
    public MusicLyrics queryOneLyrics(int lyricsId) {

        String sql = "select * from music_lyrics where lyrics_id = ?";
        MusicLyrics musicLyrics = jdbcTemplate.queryForObject(sql, new Object[]{lyricsId},
                new BeanPropertyRowMapper<>(MusicLyrics.class));
        return musicLyrics;
    }

}

/**
 * 用于查询歌曲的映射类，， 原因是字段名称设计和表名称不同。。
 */
class MusicSongRowMapper implements RowMapper<MusicSong> {

    @Override
    public MusicSong mapRow(ResultSet rs, int rowNum) throws SQLException {
        MusicSong song = new MusicSong();

        song.setSongId(rs.getLong("music_song_id"));
        song.setTitle(rs.getString("music_title"));
        song.setCoverImg(rs.getString("music_cover_img"));
        song.setSinger(rs.getString("music_singer"));
        song.setDetails(rs.getString("music_details"));
        song.setListeningVolume(rs.getLong("music_listening_volume"));
        song.setLyricsId(rs.getLong("lyrics_id"));
        song.setCreateDate(rs.getDate("music_create_date"));
        return song;
    }
}
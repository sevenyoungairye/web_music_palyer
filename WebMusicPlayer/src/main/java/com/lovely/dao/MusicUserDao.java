package com.lovely.dao;

import com.lovely.entity.MusicUser;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户 dao
 * @author echo lovely
 * @date 2020/8/16 11:13
 */
@Repository("musicUserDao")
public class MusicUserDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<MusicUser> queryAllUser(MusicUser user, int pageNo, int pageSize) {

        // 查询未注销账号
        StringBuffer sql = new StringBuffer();
        sql.append("select * from music_user where 1=1 and islogout = 0 ");

        // 储存数据多个条件
        ArrayList<Object> paramsList = new ArrayList<>();

        // append dynamic sql
        if (user != null) {
            if (user.getUserId() != null && user.getUserId() > 0) {
                sql.append("and userid = ?");
                paramsList.add(user.getUserId());
            }

            if (user.getUserName() != null && !"".equals(user.getUserName())) {
                sql.append("and username like ?");
                paramsList.add("%" + user.getUserName() + "%");
            }
        }

        // multiple pages
        sql.insert(0, "select * from (\n" +
                "       select tab1.*, rownum as rn from (");

        sql.append(")tab1\n" +
                ") where rn between ? and ?");

        // pageNo and pageSize
        // (pageNo - 1) * pageSize + 1
        // pageNo * pageSize
        paramsList.add((pageNo - 1) * pageSize + 1);
        paramsList.add(pageNo * pageSize);

        // dynamic sql
        List<MusicUser> musicUserList = jdbcTemplate.query(sql.toString(),
                paramsList.toArray(), new BeanPropertyRowMapper<MusicUser>(MusicUser.class));

        return musicUserList;
    }

    @Test
    public void test1() {
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        MusicUserDao dao = app.getBean(MusicUserDao.class);
        /*MusicUser user = new MusicUser();
        user.setUserId(2);
        user.setUserName("霉");*/
        /*List<MusicUser> list = dao.queryAllUser(null, 1, 2);
        System.out.println(list);*/

        // System.out.println(dao.getTotalPage(null, 6));

        System.out.println(dao.queryOneMusicUser(4));

    }

    public int getTotalPage(MusicUser user, int pageSize) {
        int totalPage = 1;

        StringBuffer sql = new StringBuffer("");

        sql.append("select * from music_user where 1=1 and islogout = 0 ");

        ArrayList<Object> paramsList = new ArrayList<>();

        if (user != null) {
            if (user.getUserId() != null && user.getUserId() > 0) {
                sql.append("and userid = ?");
                paramsList.add(user.getUserId());
            }

            if (user.getUserName() != null && !"".equals(user.getUserName())) {
                sql.append("and username like ?");
                paramsList.add("%" + user.getUserName() + "%");
            }
        }

        // multiple pages total count
        sql.insert(0, "select count(*) from (" +
                "       select tab1.*, rownum as rn from (");

        sql.append(")tab1)");

        /*jdbcTemplate.query*/
        int totalCount = jdbcTemplate.queryForObject(sql.toString(), paramsList.toArray(), Integer.class);

        if (totalCount % pageSize == 0 && totalCount >= pageSize) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }

        return totalPage;
    }

    public int addMusicUser(MusicUser user) {
        int count = -1;
        String sql = "insert into music_user values (seq_music_user.nextval, ?, ?, sysdate, 0)";
        count = jdbcTemplate.update(sql, new Object[]{user.getUserName(), user.getUserPwd()});
        return count;
    }

    public int updateMusicUser(MusicUser user) {
        int count = -1;
        String sql = "update music_user set userpwd = ? where userid = ?";
        count = jdbcTemplate.update(sql, user.getUserPwd(), user.getUserId());
        return count;
    }

    public int deleteMusicUser(int userId) {
        int count = -1;
        // 0表示存在 1 表示删除
        String sql = "update music_user set islogout = 1 where userid = ?";
        count = jdbcTemplate.update(sql, userId);
        return count;
    }

    public MusicUser queryOneMusicUser(int userId) {
        String sql = "select * from music_user where userid = ?";
        MusicUser musicUser = jdbcTemplate.queryForObject(sql, new Object[]{userId}, new BeanPropertyRowMapper<MusicUser>(MusicUser.class));
        System.out.println(musicUser);
        return musicUser;
    }

    public MusicUser userLogin(MusicUser user) {
        MusicUser musicUser = null;

        String sql = "select * from music_user where username = ? and userpwd = ?";

        Object[] paramObj = new Object[] {user.getUserName(), user.getUserPwd()};

        try {
            musicUser = jdbcTemplate.queryForObject(sql, paramObj,
                    new BeanPropertyRowMapper<>(MusicUser.class));
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e);
            return null;
        }

        return musicUser;
    }

}

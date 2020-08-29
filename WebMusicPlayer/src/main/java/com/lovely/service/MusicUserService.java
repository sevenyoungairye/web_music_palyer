package com.lovely.service;

import com.lovely.dao.MusicUserDao;
import com.lovely.entity.MusicUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author echo lovely
 * @date 2020/8/16 11:39
 */
@Service("musicUserService")
public class MusicUserService {

    @Resource(name = "musicUserDao")
    private MusicUserDao userDao;

    public Map<String, Object> queryAllUser(MusicUser user, int pageNo, int pageSize) {

        // 当前条件下的总页数
        int totalPage = userDao.getTotalPage(user, pageSize);
        // 当前条件下的用户
        List<MusicUser> userList = userDao.queryAllUser(user, pageNo, pageSize);

        Map<String, Object> map = new HashMap<>();
        map.put("totalPage", totalPage);
        map.put("userList", userList);

        return  map;
    }

    public int addMusicUser(MusicUser user) {
        int count = -1;

        if (user != null) {
            if (user.getUserName() != null && !"".equals(user.getUserName().trim())) {
                if (user.getUserPwd() != null && !"".equals(user.getUserPwd().trim()))
                    count = userDao.addMusicUser(user);
            }
        }

        return count;
    }

    public int updateMusicUser(MusicUser user) {
        int count = -1;

        if (user != null)
            count = userDao.updateMusicUser(user);

        return count;
    }

    public int deleteMusicUser(int userId) {
        int count = -1;

        if (userId > 0)
            count = userDao.deleteMusicUser(userId);

        return count;
    }

    public MusicUser queryOneMusicUser(int userId) {
        MusicUser musicUser = null;

        if (userId > 0) {
            musicUser = userDao.queryOneMusicUser(userId);
        }

        return musicUser;
    }

    public MusicUser userLogin(MusicUser user) {
        if (user != null)
            return userDao.userLogin(user);
        return null;
    }

}

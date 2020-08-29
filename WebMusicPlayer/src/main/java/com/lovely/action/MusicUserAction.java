package com.lovely.action;

import com.lovely.entity.MusicUser;
import com.lovely.listener.WebApplicationContextUtils;
import com.lovely.mvc.DispatcherAction;
import com.lovely.service.MusicUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author echo lovely
 * @date 2020/8/16 11:48
 */
@Controller("musicUserAction")
public class MusicUserAction extends DispatcherAction {

    // mvc 框架自动封装
    private MusicUser musicUser;

    // 通过ApplicationContext 获取
    private MusicUserService musicUserService;

    @Override
    public void init(HttpServletRequest req, HttpServletResponse resp) {
        ApplicationContext app = WebApplicationContextUtils.getApp(req.getServletContext());
        // System.out.println("applicationContext: " + app);
        // 获取spring容器中的用户bean
        musicUserService = (MusicUserService) app.getBean("musicUserService");
    }

    public Object addMusicUser(HttpServletRequest req, HttpServletResponse resp) {

        /*if (musicUser != null) {
            // System.out.println("密码：" + musicUser.getUserPwd() + "\t" + musicUser.getUserName());
        }*/

        return musicUserService.addMusicUser(musicUser);
    }

    private int pageNo = 1; // first page, just change pageNo
    private int pageSize = 3; // one page, three line data
    // query by condition
    public Object queryMusicUser(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> userList = musicUserService.queryAllUser(musicUser, pageNo, pageSize);
        return userList;
    }

    public Object updateMusicUser(HttpServletRequest req, HttpServletResponse resp) {
        return musicUserService.updateMusicUser(musicUser);
    }

    public Object deleteMusicUser(HttpServletRequest req, HttpServletResponse resp) {
        int count = -1;
        if (musicUser != null)
            count = musicUserService.deleteMusicUser(musicUser.getUserId());
        return count;
    }

    public Object queryOneMusicUser(HttpServletRequest req, HttpServletResponse resp) {
        MusicUser musicUser = null;
        if (this.musicUser != null) {
            musicUser = musicUserService.queryOneMusicUser(this.musicUser.getUserId());
        }
        return musicUser;
    }

    // login
    public Object userLogin(HttpServletRequest req, HttpServletResponse resp) {
        if (musicUser != null) {
            String userName = musicUser.getUserName();
            String userPwd = musicUser.getUserPwd();

            // 验证用户
            MusicUser user = musicUserService.userLogin(this.musicUser);

            if (user == null) {
                return -1;
            }

            // 账号 密码 正确
            if (userName.equals(user.getUserName()) && userPwd.equals(user.getUserPwd())) {
                // 1 是管理员
                if ("admin".equals(user.getUserName())) {
                    req.getSession().setAttribute("admin", user);
                    return 1;
                } else {
                    // 是普通用户
                    req.getSession().setAttribute("user", user);
                    return 2;
                }
            }

        }
        return -1;
    }
}
